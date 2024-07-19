pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('Docker-udhaya21') // Replace 'dockerhub' with the ID of your Docker Hub credentials in Jenkins
        DOCKER_IMAGE = 'udhaya21/movie-jenkins' // Replace with your Docker Hub username and image name
        KUBECONFIG = credentials('movie-jenkins-config') // Replace 'kubeconfig' with the ID of your kubeconfig file in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the source code from the repository
                git branch: 'main', url: 'https://github.com/udhaya1148/Jenkins-deploy.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def customImage = docker.build(DOCKER_IMAGE)
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'DOCKERHUB_CREDENTIALS') {
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
                        sh 'microk8s kubectl create namespace movie-jenkins'
                        sh 'microk8s kubectl apply -f microk8s deployment/database-deployment.yaml'
                        sh 'microk8s kubectl apply -f microk8s deployment/app-deployment.yaml'
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
