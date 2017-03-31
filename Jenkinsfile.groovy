#!groovy

pipeline {
	agent any

	options {
		// Skip default behavior of automatically checking out the repository from which this script was taken from.
		// We want to checkout two repositories into two separate sub-directories.
		skipDefaultCheckout()
	}

	stages {
		stage('Checkout') {
			steps {
				// Delete the entire workspace to have a clean start.
				deleteDir()
				
				// Create and change into the given sub-directory ...
				dir('org.eclipse.riena') {
					// ... and trigger the automatic checkout of the repository from which this script was taken from.
					checkout scm
				}
				
				// Create and change into the given sub-directory ...
				dir('org.eclipse.riena.3xtargets') {
					// ... and additionally checkout the second repository given by the parameters here.
					git(
						url: '${git.url.3xtargets}',
						branch: '${git.branch.3xtargets}'
					)
				}
			}
		}

		stage('Build & Test') {
			steps {
				dir('org.eclipse.riena') {
					// Run Maven build with pre-configured Maven installation named 'M3'.
					// Note: This also runs the JUnit plugin-tests via tycho-surefire-plugin.
					withMaven(maven: 'M3') {
						bat "mvn -fae clean integration-test"
					}
				}
			}
		}

		stage('Reporting') {
			steps {
				dir('org.eclipse.riena') {
					// Collect the JUnit test reports from the previous Maven run.
					junit '**/surefire-reports/*.xml'
				}
			}
		}

		stage('Archive') {
			steps {
				dir('org.eclipse.riena') {
					// Archive the generated p2 repository ZIP file for later use in downstream jobs.
					archiveArtifacts artifacts: 'org.eclipse.riena.build.tycho/target/*.zip', fingerprint: true

					// Archive the test reports from Tycho Surefire for tracking down test failures.
					archiveArtifacts artifacts: 'org.eclipse.riena.tests/target/surefire-reports/*', fingerprint: true
					archiveArtifacts artifacts: 'org.eclipse.riena.tests.optional/target/surefire-reports/*', fingerprint: true
				}
			}
		}
	}
}