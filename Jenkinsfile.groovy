import hudson.tasks.test.AbstractTestResultAction
import java.time.LocalDateTime
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

node('windows10 && x86 && jdk8') {
	stage("Checkout") {
		// Delete the entire workspace to have a clean start.
		deleteDir()

		dir('org.eclipse.riena.3xtargets') {
			// Checkout the targets repository in sub-directory.
			git  url: params.targetsUrl, branch: params.targetsBranch
		}

		dir('org.eclipse.riena') {
			// Trigger automatic checkout of the repository from which ths script was taken in sub-directory.
			checkout scm
		}
	}

	stage("Build & Test") {
		dir('org.eclipse.riena') {
			// Run Maven build with pre-configured Maven installation named 'M3'.
			// Note: This also runs the JUnit plugin-tests via tycho-surefire-plugin.
			withMaven(maven: 'M3') {
				try {
					// fail at end so we finish the build and than fail, ignore testfailures(well collect the test failures later in the reporting pahse)
					bat "mvn -fae clean integration-test -DforceContextQualifier=HEAD_" + getBuildTimestamp() + " -Dmaven.test.failure.ignore=true"
				} catch (err) {
					String error = "${err}"
					//send mail with the catched error
					sendErrorMail(error)
					// if the build encounters an failure mark the build as failed
					currentBuild.result = 'FAILURE'
					throw err;
				}

			}

		}
	}

	stage("Reporting") {
		dir('org.eclipse.riena') {
			// Collect the JUnit test reports from the previous Maven run. 
			junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*xml'
			// Collect the Junit test results mark the build as unstable if there are test failures and send email with status informations
			testStatuses()
		}
	}

	stage('Archive') {
		dir('org.eclipse.riena') {
			// Archive the generated p2 repository ZIP file for later use in downstream jobs.
			archiveArtifacts artifacts: 'org.eclipse.riena.build.p2/target/*.zip', fingerprint: true
			archiveArtifacts artifacts: 'org.eclipse.riena.build.p2full/target/*.zip', fingerprint: true

			// Archive the test reports from Tycho Surefire for tracking down test failures.
			archiveArtifacts artifacts: 'org.eclipse.riena.tests/target/surefire-reports/*', fingerprint: true
			//archiveArtifacts artifacts: 'org.eclipse.riena.tests.optional/target/surefire-reports/*', fingerprint: true
		}
	}
}

def String getBuildTimestamp() {
	def buildTimestampMillis = currentBuild.getStartTimeInMillis()
	LocalDateTime buildTimestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(buildTimestampMillis), ZoneId.systemDefault());
	return buildTimestamp.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
}

//function which sends an email in case of build error to receivers configured in the jenkins job
def sendErrorMail(String error) {
	def details = """<p>Job '${env.JOB_NAME}', build ${env.BUILD_NUMBER}
 has encountered some problems.</p>

 <p> We catched the following error:
  <p>  """ + error + """
    <p>
  <p>Quick links to the details:
  <ul>
    <li><a href="${env.JOB_URL}">${env.JOB_NAME} job main page</a></li>
    <li><a href="${env.BUILD_URL}">Build ${env.BUILD_NUMBER} main page</a></li>
    <ul>
      <li><a href="${env.BUILD_URL}console">Console output</a></li>
      <li><a href="${env.BUILD_URL}changes">Git changes</a></li>
      <li><a href="${env.BUILD_URL}testReport">Testreport</a></li>
      <li><a href="${env.BUILD_URL}flowGraphTable">Pipeline steps</a>.
          This page will show you which step failed, and give you access
          to the job workspace.</li>
    </ul>
  </ul></p>"""

    //finally sends email 
	emailext body: details, subject: "Job ${env.JOB_NAME}, build ${env.BUILD_NUMBER} has problems.", to: """$emailNotificationReceivers"""

}
//function which sends an email in case of failed tests to receivers configured in the jenkins job
def sendTestFailedMail(String testStatus) {
	def body = """<p>Job '${env.JOB_NAME}', build ${env.BUILD_NUMBER} has encountered test failures.</p>
<p> """ + testStatus + """</p>

 <p>Quick links to the details:
 <li><a href="${env.JOB_URL}">${env.JOB_NAME} job main page</a></li>
 <li><a href="${env.BUILD_URL}">Build ${env.BUILD_NUMBER} main page</a></li>
 <li><a href="${env.BUILD_URL}testReport">Testreport</a></li>
 """
//finally sends mail
	emailext body: body, subject: "Job ${env.JOB_NAME}, build ${env.BUILD_NUMBER} ", to: """$emailNotificationReceivers"""

}

//function to collect test results
@NonCPS
def testStatuses() {
	def testStatus = ""
	AbstractTestResultAction testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
	if (testResultAction != null) {
		def total = testResultAction.totalCount
		def failed = testResultAction.failCount
		def skipped = testResultAction.skipCount
		def passed = total - failed - skipped
		testStatus = """<h4>Test Status:</h4>
        <p><strong>Total: ${total}</strong></p>
        <p><strong><span style="background-color: #99cc00;">Passed: ${passed}</span></strong></p>
        <p><strong><span style="background-color: #ff0000;">Failed: ${failed}</span></strong></p>
        <p><strong><span style="background-color: #ffff00;">Skipped: ${skipped}</span></strong></p>
        <p><strong>Test diff: ${testResultAction.failureDiffString}</strong></p>"""

		print("Testfail: " + failed)
		if (failed != 0) {
			//send mail
			sendTestFailedMail(testStatus)
			//mark build as unstable
			currentBuild.result = 'UNSTABLE'
			//sendErrorMail("""There are test failures<p>"""+testStatus)
		}
	}
}