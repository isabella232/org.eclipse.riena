#!groovy

pipeline {
	agent any
	options {
		skipDefaultCheckout()
	}
	stages {
		stage('Checkout') {
			steps {
				deleteDir()
				
				dir('org.eclipse.riena') {
					checkout scm
				}
				dir('org.eclipse.riena.3xtargets') {
					git(
						url: '//vboxsrv/Git/org.eclipse.riena.3xtargets',
						branch: 'tycho-build'
					)
				}
			}
		}
		stage('Build') {
			steps {
				dir('org.eclipse.riena') {
					withMaven {
						bat "mvn clean integration-test"
					}
				}
			}
		}
	}
}