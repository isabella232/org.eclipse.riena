import hudson.tasks.test.AbstractTestResultAction
import java.time.LocalDateTime
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/********************
 * Main build script.
 ********************/

node('win10 && x86 && jdk8') {

	def numSplits = 0

	stage('Checkout') {
		cleanWs()

		parallel('Checkout of 3xTargets': {
					dir('org.eclipse.riena.3xtargets') {
						git url: params.targetsUrl, branch: params.targetsBranch
					}
				},

				'Checkout of Riena Sources': {
					dir('org.eclipse.riena') { checkout scm }
				}
				)
	}

	stage('Build') {
		dir('org.eclipse.riena') {
			// Run Maven build with pre-configured Maven installation named 'M3'.
			withMaven(maven: 'M3') {
				try {
					// Do not run tests here since we run them in parallel later.
					// Fail at end to run entire build even in case of a failure.
					bat 'mvn -fae clean package -DforceContextQualifier=HEAD_' + getBuildTimestamp()
				} catch (err) {
					String error = "${err}"

					// Send mail with the catched error
					sendErrorMail(error)

					// Mark build as failed.
					currentBuild.result = 'FAILURE'

					// Re-throw exception to notify Jenkins.
					throw err;
				}
			}
		}

		dir('org.eclipse.riena') {
			stash name: 'parentPom', includes: 'pom.xml'
			stash name: 'testProject', includes: 'org.eclipse.riena.tests/**'
			stash name: 'buildResult', includes: 'org.eclipse.riena.build.p2/target/*.zip'
		}

		dir('org.eclipse.riena.3xtargets/org.eclipse.riena.target3x') {
			stash name: '3xTargets' , includes:  '**'
		}
	}

	stage('Test') {
		numSplits = runTests()
		reportTestStatus()
	}

	stage('Archive') {
		dir('org.eclipse.riena') {
			// Archive the generated p2 repository ZIP files.
			archiveArtifacts artifacts: 'org.eclipse.riena.build.p2/target/*.zip', fingerprint: true
			archiveArtifacts artifacts: 'org.eclipse.riena.build.p2full/target/*.zip', fingerprint: true
		}

		dir('surefire-reports') {
			// Loop over all splits of the test execution and unstash the stashed surefire reports into
			// a separate sub-directory each.
			for (int i = 0; i < numSplits; i++) {
				def directory = "split-${i}"
				dir(directory) { unstash name: "surefireReports-split-${i}" }
			}
		}

		// Now archive the entire directory holding the separate surefire-reports.
		archiveArtifacts artifacts: 'surefire-reports/**', fingerprint: true
	}
}


/*******************
 * Helper functions.
 *******************/

def String getBuildTimestamp() {
	def buildTimestampMillis = currentBuild.getStartTimeInMillis()
	LocalDateTime buildTimestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(buildTimestampMillis), ZoneId.systemDefault());
	return buildTimestamp.format(DateTimeFormatter.ofPattern('yyyyMMddHHmm'))
}

def int runTests() {
	// Split tests in certain number of buckets.
	def splits = splitTests generateInclusions: true, parallelism: count(2)

	// Dictionary to hold set of parallel test executions.
	def testGroups = [:]

	// Loop over each record in splits and prepare the test group that will be run in parallel.
	// The list of inclusions is constructed based on the results gathered from the previous
	// successfully completed jobs.
	for (int i = 0; i < splits.size(); i++) {
		def split = splits[i]
		def splitLabel = "split-${i}"
		testGroups[splitLabel] = {
			node('win10 && x86 && jdk8') {
				prepareTestRun()
				executeTestRun(split)

				// Collect test results.
				junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*xml'

				// Stash surefire reports.
				// Just archiving the reports does not work since all reports have the same name and would
				// overwrite each other. For that stash the reports into a separate stash space per split
				// and unstash them into separate directories in the main node later from where they can
				// then be archived.
				stash name: "surefireReports-${splitLabel}", includes: '**/target/surefire-reports/*'
			}
		}
	}

	parallel testGroups

	return splits.size()
}

def prepareTestRun(){
	cleanWs()

	// Unstash artifacts from build step.
	parallel(
			'Unstash Test Project': {
				dir('org.eclipse.riena'){
					unstash name: 'testProject'
					unstash name: 'parentPom'
				}
			},

			'Unstash Build Result': {
				dir('tmp-copy') { unstash name: 'buildResult' }

				dir('tmp-copy/org.eclipse.riena.build.p2/target/') {
					// Unzip the all *.zip files copied from Riena job -> should be only one, but a loop is the easiest
					// way to figure out the final filename.
					def files = findFiles(glob: '*.zip')
					for(int i = 0; i < files.size(); i++) {
						def file = "${files[0].name}"
						unzip zipFile: "${file}", dir: '../../../p2.small'
					}
				}
			},

			'Unstash 3xTargets': {
				dir('org.eclipse.riena.3xtargets/org.eclipse.riena.target3x/'){ unstash name: '3xTargets' }
			}
			)
}

def executeTestRun(split) {
	def postfix = ''
	dir('org.eclipse.riena/org.eclipse.riena.tests') {
		// Of n splits, n-1 work with an inclusion list, and the n-th one works with an exclusion list
		// to ensure that tests which have not yet been seen in previous builds are also discovered.
		if (split.includes) {
			print 'Inclusion split to execute: ' + split
			writeFile file: 'includedTest.txt', text: split.list.join('\n')
			postfix = ' -DincludeTestsFile=includedTest.txt'
		} else {
			print 'Exclusion split to execute: ' + split
			writeFile file: 'excludedTest.txt', text: split.list.join('\n')
			postfix = ' -DexcludeTestsFile=excludedTest.txt'
		}
	}

	// Execute Maven build with tests.
	dir('org.eclipse.riena/org.eclipse.riena.tests') {
		try {
			bat 'mvn clean integration-test -fae -Ptest -DfailIfNoTests=false -Dmaven.test.failure.ignore=true' + postfix
		} catch (err) {
			String error = "${err}"

			// Send mail with the catched error
			sendErrorMail(error)

			// Mark build as failed.
			currentBuild.result = 'FAILURE'

			// Re-throw exception to notify Jenkins.
			throw err;
		}
	}
}

@NonCPS
def reportTestStatus() {
	def testStatus = ''
	AbstractTestResultAction testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
	if (testResultAction != null) {
		def total = testResultAction.totalCount
		def failed = testResultAction.failCount
		def skipped = testResultAction.skipCount
		def passed = total - failed - skipped

		testStatus = """<h4>Test Status:</h4></p>
                        <p><strong>Total: ${total}</strong></p>
                        <p><strong><span style="background-color: #99cc00;">Passed: ${passed}</span></strong></p>
                        <p><strong><span style="background-color: #ff0000;">Failed: ${failed}</span></strong></p>
                        <p><strong><span style="background-color: #ffff00;">Skipped: ${skipped}</span></strong></p>
                        <p><strong>Test diff: ${testResultAction.failureDiffString}</strong></p>"""

		print 'Testing result: ' + total + ' test cases, ' + passed + ' passed, ' + failed + ' failed, ' + skipped + ' skipped.'

		if (failed != 0) {
			sendTestFailedMail(testStatus)
			currentBuild.result = 'UNSTABLE'
		}
	}
}

def sendErrorMail(String error) {
	def details = """<p>Job '${env.JOB_NAME}', build ${env.BUILD_NUMBER} has encountered some problems.</p>
                     <p>We catched the following error:</p>
                     <p>""" + error + """</p>
					 <p>&nbsp;</p>
					 <p>Quick links to the details:</p>
					 <p><ul>
					   <li><a href="${env.JOB_URL}">${env.JOB_NAME} job main page</a></li>
					   <li><a href="${env.BUILD_URL}">Build ${env.BUILD_NUMBER} main page</a></li>
					   <li><a href="${env.BUILD_URL}console">Console output</a></li>
					   <li><a href="${env.BUILD_URL}changes">Git changes</a></li>
					   <li><a href="${env.BUILD_URL}testReport">Testreport</a></li>
					   <li><a href="${env.BUILD_URL}flowGraphTable">Pipeline steps</a>.
					   <li>This page will show you which step failed, and give you access to the job workspace.</li>
					 </ul></p>"""

	// Send eMail; Jenkins will automatically ignore and log this if no receiver is configured.
	emailext body: details, subject: "Job ${env.JOB_NAME}, build ${env.BUILD_NUMBER} has problems.", to: """params.emailNotificationReceivers"""
}

def sendTestFailedMail(String testStatus) {
	def body = """<p>Job '${env.JOB_NAME}', build ${env.BUILD_NUMBER} has encountered test failures.</p>
                  <p> """ + testStatus + """</p>
				  <p>Quick links to the details:</p>
				  <p><ul>
					<li><a href="${env.JOB_URL}">${env.JOB_NAME} job main page</a></li>
					<li><a href="${env.BUILD_URL}">Build ${env.BUILD_NUMBER} main page</a></li>
					<li><a href="${env.BUILD_URL}testReport">Testreport</a></li>
				  </ul></p>"""

	// Send eMail; Jenkins will automatically ignore and log this if no receiver is configured.
	emailext body: body, subject: "Job ${env.JOB_NAME}, build ${env.BUILD_NUMBER} ", to: "${params.emailNotificationReceivers}"
}
