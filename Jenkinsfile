pipeline {
    agent any

    environment {
        KUBECONFIG = credentials('chinnavar-config')
        DOCKER_CREDENTIALS_ID = 'docker-bhuvanesh'
        DOCKER_IMAGE = 'bhuvaneshnexn/chinnavar-ticket-jenkins-person'
        //K8S_NAMESPACE = 'movie-jenkins'
    }
    options {
        retry(3) // Retry stages 3 times if they fail
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    git credentialsId: 'Github-udhaya1148', url: 'https://github.com/udhaya1148/Jenkins-deploy.git', branch: 'main'
                }
                // Checkout your application code and Kubernetes manifests from your repository
               // git 'https://github.com/Bhuvaneshnetcon/k8s-jenkins'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build and push Docker image
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh """
                        docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
                        docker build -t $DOCKER_IMAGE .
                        docker push $DOCKER_IMAGE
                        """
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Set the kubeconfig environment variable
                    sh 'echo $KUBECONFIG > kubeconfig'
                    sh 'export KUBECONFIG=kubeconfig'

                    // Apply Kubernetes manifests
                   // sh 'microk8s kubectl create namespace movie-jenkins'
                    sh 'kubectl apply -f k8s/'
                }
            }
        }
    }

    post {
        always {
            // Clean up
            sh 'rm -f kubeconfig'
        }
    }
}
