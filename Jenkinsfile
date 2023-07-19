pipeline {
    agent any

    triggers {
        githubPush()
    }

    stages {

        stage('build') {
            steps {
                echo "DVD - Starting gradle build"
                withGradle {
                    sh './gradlew buildFatJar'
                }
                echo "DVD - Finished gradle build"
            }
        }

        stage('deploy') {
            steps {
                sshagent(['DEPLOYER_SSH_KEY']) {
                    sh "scp -o StrictHostKeyChecking=no build/libs/* deployer@ec2-18-188-25-57.us-east-2.compute.amazonaws.com:/var/www/kotlin_server/"
                }
                sshagent(['ROOT_USER_KEY']) {
                    sh 'ssh ubuntu@ec2-18-188-25-57.us-east-2.compute.amazonaws.com "sudo supervisorctl restart kotlin_server_p"'
                }
            }
        }
    }
}