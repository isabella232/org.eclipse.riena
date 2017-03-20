#!groovy

pipeline {
	agent any
	stages {
		stage('Build') {
			steps {
				echo '-----------'
				echo 'Building...'
				echo '-----------'
				
				withMaven {
					bat "mvn clean package"
				}
			}
		}
		stage('Test') {
			steps {
				echo '----------'
				echo 'Testing...'
				echo '----------'
			}
		}
		stage('Deploy') {
			steps {
				echo '------------'
				echo 'Deploying...'
				echo '------------'
			}
		}
	}
}