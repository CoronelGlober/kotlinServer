pipeline {
    agent any

    triggers {
        githubPush()
    }

    stages {

        stage('build') {
            steps {
                script {
                    def currentDate = new Date().format('yyyyMMddHHmmss')
                    def content = '<html><head></head><body> <h1>Hello Ktor! - ${currentDate} </h1></body></html>'
                    writeFile(file: 'index.html', text: content)
                    echo "DVD - index file created!"
                }
            }
        }

        stage('deploy') {
            steps {
                echo "DVD - Starting scp connection"
                sshagent(['DEPLOYER_SSH_KEY']) {
                    sh "scp -o StrictHostKeyChecking=no index.html deployer@ec2-18-188-25-57.us-east-2.compute.amazonaws.com:/var/www/web_server/"
                    echo "DVD - Starting scp connection"
                }
                echo "DVD - finished scp connection"
            }
        }
    }
}