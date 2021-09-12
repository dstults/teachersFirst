pipeline {
	agent any

	tools { 
		maven 'maven-3.8.2'
		jdk 'jdk11'
	}

	stages {

		/*
		stage ('Debug') {
			steps {
				//sh 'printenv';
				//sh 'echo "Working Directory = ${PWD}"'
				//sh 'echo "PATH = ${PATH}"'
				//sh 'echo "M2_HOME = ${M2_HOME}"'
			}
		}
		*/

		stage ("Build") {
			steps {
				echo 'Starting application build...';
                sh 'mvn -B -DskipTests clean package';
				echo 'Build completed!';
			}
		}
	
		stage ("Test") {
			steps {
				echo 'Starting application test...';
				sh 'mvn test';
				echo 'Testing completed (although not really)!';
			}
		}
	
		stage ("Deploy") {
			steps {
				echo 'Starting application deployment...';
				sh '../deploy.sh ' + env.GIT_BRANCH;
				echo 'Deployment completed!';
			}
		}
	
	}

}
