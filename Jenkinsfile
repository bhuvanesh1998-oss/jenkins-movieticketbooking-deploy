pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = 'docker-bhuvanesh' // Ensure this ID matches the credentials in Jenkins
        DOCKER_IMAGE = 'bhuvaneshnexn/movie-jenkins'
        KUBECONFIG = credentials('movie-jenkins-config') // Ensure this ID matches the kubeconfig credentials in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    retry(3) {
                        git branch: 'main', url: 'https://github.com/udhaya1148/Jenkins-deploy.git'
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Define customImage in a broader scope
                    customImage = docker.build(DOCKER_IMAGE)
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS) {
                        customImage.push()
                    }
                }
            }
        }

        stage('Deploy to MicroK8s') {
            steps {
                script {
                    // Save kubeconfig content to a file
                    writeFile file: 'kubeconfig', text: KUBECONFIG
                    withEnv(["KUBECONFIG=${pwd()}/kubeconfig"]) {
                        // Apply Kubernetes deployment
                        sh 'microk8s kubectl create namespace movie-jenkins || true'
                        sh 'microk8s kubectl apply -f k8s/database-deployment.yaml --namespace=movie-jenkins'
                        sh 'microk8s kubectl apply -f k8s/app-deployment.yaml --namespace=movie-jenkins'
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
