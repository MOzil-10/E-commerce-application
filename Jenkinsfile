pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "mukosi10/ecommerce-spring-boot-app"
        DOCKER_TAG = "latest"
    }

    stages {
        stage('Checkout Code') {
            steps {
                // Checkout the code from your repository
                checkout scm
            }
        }

        stage('Build Application') {
            steps {
                // Build the application using Maven
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Run Tests') {
            steps {
                // Run tests
                sh 'mvn test'
            }
        }

        stage('Build Docker Image') {
            steps {
                // Build the Docker image
                sh """
                docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                """
            }
        }

        stage('Push Docker Image') {
            steps {
                withDockerRegistry([credentialsId: 'docker-hub-credentials', url: 'https://index.docker.io/v1/']) {
                    // Push the Docker image to Docker Hub
                    sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }

        stage('Deploy Application') {
            steps {
                echo "Deploying the application..."
                // Optional: Deploy using Docker Compose or Kubernetes
                sh "docker-compose up -d"
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished.'
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Please check the logs.'
        }
    }
}
