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
                def myFile = new File('index.html')
                myFile.write(content)
                echo "index file created!"
            }
        }
        }
  }
}