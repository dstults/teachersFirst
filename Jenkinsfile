pipeline {
	agent any

	stages {
	
		stage ("Build") {
			steps {
				echo 'Starting application build...'
                		sh 'mvn -B -DskipTests clean package'
				echo 'Build completed!'
			}
		}
	
		stage ("Test") {
			steps {
				echo 'Starting application test...'
				sh 'mvn test'
				echo 'Testing completed!'
			}
			post {
				always {
					junit 'target/surefire-reports/*.xml'
				}
			}
		}
	
		stage ("Deploy") {
			steps {
				echo 'Starting application deployment...'
				sh './jenkins/scripts/deliver.sh'
				echo 'Deployment completed! -- (Not really, though.)'
			}
		}
	
	}

}
