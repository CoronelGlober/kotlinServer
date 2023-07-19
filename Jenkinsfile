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
            stage('Deploying a new server build') {
                steps {
                    sshagent(['DEPLOYER_SSH_KEY']) {
                        sh(script: 'scp -o StrictHostKeyChecking=no build/libs/* deployer@ec2-18-188-25-57.us-east-2.compute.amazonaws.com:/var/www/kotlin_server/',
                                label: 'Deploying a new server build'
                        )
                    }
                }
            }

            stage('Restarting server') {
                steps {
                    sshagent(['ROOT_USER_KEY']) {
                        sh(script: '''
                            ssh ubuntu@ec2-18-188-25-57.us-east-2.compute.amazonaws.com "sudo supervisorctl restart kotlin_server_p"
                        ''', label: 'Restarting server')
                    }
                }
            }
        }
    }
}