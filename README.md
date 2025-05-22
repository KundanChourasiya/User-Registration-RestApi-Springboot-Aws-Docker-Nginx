# User-Registration-RestApi-Springboot-Aws-Docker-Nginx

![RestApi](https://github.com/user-attachments/assets/7ce66629-7c65-4156-bf2b-9f517db9990f)


### Set up an AWS EC2 instance and install Docker to run Nginx as a reverse proxy.

# Overview
This guide covers the steps to:
  * Launch an AWS EC2 instance.
  * install Docker and docker-compose-v2.
  * Pull project from Github.
  * Create a dockerfile file.
  * Create a docker-compose.yml file.
  * Create a docker file for nginx.
  * Create a default.conf file to Configure Nginx server is running on the default port 80.
  * Configure the AWS EC2 instance's security group to allow access from anywhere on port 80.

# Follow the steps:
1. Create instance in AWS EC2.
2. Open an SSH client and connect to your instance.
3. Update the vm (EC2 instance)
     ```
     sudo apt-get update
     ```
4. Download and install docker.io in EC2.
     ```
     sudo apt-get install docker.io
     ```
 5. Download and install docker-compose-v2 in EC2.
    ```
    sudo apt-get install docker-compose-v2
    ```
 6. Check docker engine running stage.
    ```
    sudo systemctl status docker
    ```
 7. Add current user to docker group.
    ```
    sudo usermod -aG docker $USER
    ```
 8. Refresh the docker group.
    ```
    newgrp docker
    ```
 9. List out running docker containers.
     ```
     docker ps
    ```
 10. Create a folder with name in working directory.
     ```
     mkdir project
     ```
11. Clone project from gitub inside the created folder.
    ```
    git clone https://github.com/KundanChourasiya/User-Registration-RestApi-Springboot-Aws-Docker-Nginx.git
    ```
12. Create a Dockerfile inside the project directory and write the below code.
    ```
    vim Dockerfile
    ```
    * Dockerfile
     ```yml
      # Stage 1 - Build the Jar using maven
      # pull maven and jdk-17 image
      FROM maven:3.8.3-openjdk-17 AS builder
      
      # Create a folder where the app code will be stored
      WORKDIR /project
      
      # Copy the source code from your Host machine to your container
      COPY . /project
      
      # Create Jar file
      RUN mvn clean install -DskipTests=true
      
      # Stage 2- execute JAR file from the above stage
      # pull jdk-small size
      FROM openjdk:17-alpine
      
      # Create a new folder where the app code will be stored
      WORKDIR /app
      
      # Copy the app jar file and rename the jar file name
      COPY --from=builder /project/target/*.jar /app/spring-app.jar
      
      # Run the  application when the container starts
      CMD ["java", "-jar", "spring-app.jar"]

    ```
13. Create docker-compose file to build docker services.

    *Note*: here we can use env file also for environment variable.
     ```
     docker-compose.yml
     ```
    * docker-compose
    ```yml
    version: "3.8"
    services:
    
      nginx:
        container_name: nginx_cont
        build:
          context: ./nginx
        image: nginx
        ports:
          - "80:80"
        networks:
          - api-network
        restart: always
        depends_on:
          - crud-api
    
      mysql:
        image: mysql:latest
        container_name: mysql
        environment:
          MYSQL_ROOT_PASSWORD: test
          MYSQL_DATABASE: crudapi
        ports:
          - "3306:3306"
        volumes:
          - ./api-data:/var/lib/mysql
        networks:
          - api-network
        healthcheck:
          test: ["CMD","mysqladmin","ping","-h","localhost","-uroot","-ptest"]
          interval: 10s
          timeout: 5s
          retries: 5
          start_period: 30s
        restart: always
    
      crud-api:
        build: .
        container_name: crud-api-v1
        env_file:
          - ".env"
        ports:
          - "8080:8080"
        networks:
          - api-network
        depends_on:
          - mysql
        healthcheck:
          test: ["CMD-SHELL", "curl -f http://localhost:8080 || exit 1"]
          interval: 10s
          timeout: 5s
          retries: 5
          start_period: 30s
        restart: always
    
    volumes:
      api-data:
    
    networks:
      api-network:
        name: api-network
        driver: bridge

    ```

15. check database url in project application.properties file there is “allowPublicKeyRetrieval=true&useSSL=false” public key retrieval or not, and check environment variable file the .env file like below.

    * application.properties
      ```properties
      # Server port (optional, defaults to 8080)
        server.port=${SERVER_PORT:8080}
        
        
        server.baseUrl=${BASE_URL:http://localhost:8080}
        
        # MySql Configuration
        spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
        spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/${MYSQL_DB:crudapi}?allowPublicKeyRetrieval=true&useSSL=false
        spring.datasource.username=${MYSQL_USER:root}
        spring.datasource.password=${MYSQL_PASSWORD:test}
        
        # JPA / Hibernate Configuration
        spring.jpa.hibernate.ddl-auto=update
        spring.jpa.show-sql=true
        spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
      ```

    * .env (environment variable file)
      ```properties
      SERVER_PORT=8080        # apps server port
      MYSQL_HOST=mysql        # mysql container name
      MYSQL_PORT=3306         # mysql container port
      MYSQL_DB=crudapi        # database name
      MYSQL_USER=root         # database username
      MYSQL_PASSWORD=test     # database password
      ```
16. Create a folder inside the project directory with name nginx and inside the folder create a docker file for create nginx image.
    * Dockerfile
    ```yml
    # pull nginx image
      FROM nginx:1.23.3-alpine
      
      COPY ./default.conf /etc/nginx/conf.d/default.conf
    ```
17. Create a default.conf file inside the nginx folder for Configure the nginx default port with host server ports.
    ```yml
    server {
          listen 80;          # Nginx server default port
          server_name localhost;      # DNS server name (www.abc.com)
      
          location / {
              proxy_pass http://crud-api:8080;        # running host and post no
              proxy_set_header Host $host;
              proxy_set_header X-Real-IP $remote_addr;
              proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
              proxy_set_header X-Forwarded-Proto $scheme;
          }
      }
    ```
18. Build the docker services.
    ```
    docker compose up -d --build
    ```
19. Configure the AWS EC2 instance's security group to allow access from anywhere on port 80, since the Spring Boot application is running on that port.
    ![image](https://github.com/user-attachments/assets/4ba50025-431a-4deb-9835-b656b3f6f1e0)

20. Open your browser, enter the URL with the port number, and submit some data to save it in the database.
> [!NOTE]
> Note: The Nginx server is running on the default port 80 and it forwards client requests to the backend server.

URL: http://<public_ip>/swagger-ui/index.html
![image](https://github.com/user-attachments/assets/f61a46a7-9342-4cf5-94fd-74ca52859c6b)

![image](https://github.com/user-attachments/assets/12a9bbdd-f32c-4491-bc40-4ceb04f5e8b5)

![image](https://github.com/user-attachments/assets/a5b1d20c-72c7-4b91-810d-eb1335ec9a10)

21. Open your Postman tool and test the Api, enter the URL with the port number, and submit some data to save it in the database.
![image](https://github.com/user-attachments/assets/66951e6b-f570-46d1-bf9d-2eb13e9b4471)















