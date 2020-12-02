# SWE645-PROJECT3 test 9.6

## [EC2 Link](http://ec2-3-235-245-12.compute-1.amazonaws.com/) 


This project is a container orchestration of 3 services. A frontend web application implemented using `Angular` and `nginx`. A backend application implemented using `JAVA EE` and `Tomcat`. And finally a persistent `MySQL` database to keep track of data. In this piece, we will explain each of these services in more detail.

## BUILD

To build and run this project you will need to have `Docker` installed on your machine. We will be using `Docker Swarm` for orchestration of services. To build and run this project simply run `docker stack deploy -c ./docker-compose.yml swe645`. This will create 3 types of services.

For this project we will be using 3 Amazon `EC2` instances to serve each services. The setup for this is exactly as we have previously constructed and can be found at [SWE645-HW2](https://github.com/popsps/SWE645-group-project).

# UNDERSTANDING PROJECT FROM SCRATCH

## JENKINS

To build and run this project we will be using `Jenkins` to automate the build. To achieve this goal we simply run this script.

```sh
docker service rm $(docker service ls -q) || true # remove all previous services 
docker service create --name registry --publish 5000:5000 registry:2 # service registry to make images available across nodes
docker-compose up -d # make images
docker-compose down --volumes # remove container created by compose
docker-compose push # make images availbe across nodes
docker stack deploy -c ./docker-compose.yml swe645 # run the container ochestration stack
```

## **`docker-compose.yml`**

Let's dive a little deeper into our setup. `docker-compose.yml` is responsible for creating 3 containerized services and linking them together. Here is the code. 

```yml
version: "3"

volumes:
  swe-data:

services:
  swe645-mysql:
    image: 127.0.0.1:5000/mysql-db
    build: ./mysql_init
    container_name: swe645-mysql
    volumes: 
      - swe-data:/var/lib/mysql
    deploy:
      placement:
        constraints:
          - node.hostname == master-172-31-68-137
    ports: 
      - 5306:3306
    environment: 
      - MYSQL_USER=javaadmin
      - MYSQL_ROOT_PASSWORD=admin
      - MYSQL_DATABASE=swedb

  swe645-backend:
    image: 127.0.0.1:5000/tomcat-backend
    build: ./backend
    container_name: swe645-backend
    environment:
      - DB_SERVER=swe645-mysql
      - MYSQL_DATABASE=swedb
      - MYSQL_USER=javaadmin
      - MYSQL_ROOT_PASSWORD=admin
    ports:
      - 8081:8080
    links:
      - swe645-mysql

  swe645-frontend:
    image: 127.0.0.1:5000/nginx-frontend
    build: ./frontend
    container_name: swe645-frontend
    ports:
      - 80:80
    links:
      - swe645-backend





```
### NETWORK

`links` is used to make these services available to each other(same network) using their corresponding service names.

### CONTAINERIZED MYSQL DATABASE BUILD

The first service, `swe645-mysql` is responsible for building a `MySQL` database. We will build our `mySQL` database from a `Dockerfile` in `mysql_init` directory. Set all environment variables that we need, namely, `user`, `password`, and the `database`. And finally we will be using `volumes` to save `MySQL` data on the host to be able to make the database **persistent**. The volume's name is `swe-data` with the orchestration(swarm) name added to it as a prefix ends up being `swe645_swe-data`. By the end of your build you can verify this by running `docker volume ls`. Here is the MySQL `Dockerfile`. 

```Dockerfile
FROM mysql:latest
ENV MYSQL_USER=javaadmin
ENV MYSQL_ROOT_PASSWORD=admin
ENV MYSQL_DATABASE=swedb
COPY ./table_init.sql /docker-entrypoint-initdb.d
EXPOSE 3306
```

We will be using `table_init.sql` to initiate a `Schema` named `student` on startup. This will create a table called `student` in `swedb` database only if the table already does not exist. Here is the schema we are using for the initiation of our table in `swedb` database.

```sql
CREATE TABLE student (
    id int not null primary key,
    firstname varchar(255) not null,
    lastname varchar(255) not null,
    street varchar(255) not null,
    zipcode int not null,
    state varchar(50) not null,
    city varchar(255) not null,
    phone varchar(50) not null,
    email varchar(50) not null,
    visited_at timestamp
);
```

### CONTAINERIZED BACKEND BUILD

We will build the backend based on a `Dockerfile` in `./backend` directory. 

```Dockerfile
FROM maven:alpine AS maven_builder
WORKDIR /workspace
ADD pom.xml ./
COPY . .
RUN mvn clean package

FROM tomcat:alpine
COPY --from=maven_builder /workspace/target/swe645_hw3-1.0.war /usr/local/tomcat/webapps
EXPOSE 8080
```

Our JAVA Backend server is built using `maven`. We pull a `apine maven` from the Docker Hub repository copy our `pom.xml` and the entire project over there and build our project using `mvn clean package`. This will generate a `war` file in `/workspace/target/swe645_hw3-1.0.war`. 

Next, we will copy this `war` file into a `alpine tomcat` `webapps` directory and start the `tomcat` server. This will create a fully functional slim backend server. 

### CONTAINERIZED FRONTEND BUILD

We will build the backend based on a `Dockerfile` in `./frontend` directory. 

```Dockerfile
FROM node:alpine AS angular_builder
WORKDIR /user/src/app
ADD package.json ./
RUN npm install
RUN npm install -g @angular/cli
COPY . .
RUN npm run build

FROM nginx:alpine
COPY default.conf /etc/nginx/conf.d/default.conf
COPY --from=angular_builder /user/src/app/dist/frontend /usr/share/nginx/html
EXPOSE 80
```

The frontend is an `Angular` application. First we build the `Angular` application by an `alpine node` image. We copy the `package.json` over there to build our project dependencies. Then we will install `Angular client` to be able run angular commands. Finally we `build` the `Angular` app using `npm run build`. This will build the entire frontend project into `/user/src/app/dist/frontend`.

Next, we want to copy this build folder (`/dist/frontend`) to a new and our final container which runs an `alpine nginx` server. We expose port 80 and serve the static files on this slim `nginx` to the end user. You may notice we have a file called `default.conf` copied over. This file changes the proxy settings so that the frontend direct the api calls to the backend instead. Here how it looks like.

```nginxconf
server {
    listen 80;
    server_name swe645-frontend;
    root /usr/share/nginx/html;
    index index.html index.html;

    location /FormServlet {
        proxy_pass http://swe645-backend:8080/swe645_hw3-1.0/FormServlet;
    }
     location /ListAllStudents {
            proxy_pass http://swe645-backend:8080/swe645_hw3-1.0/ListAllStudents;
        }

    location / {
        try_files $uri $uri/ /index.html;
    }
}
```

Calls to `FormServlet` and `ListAllStudents` will be resolved by the backend instead of `nginx` frontend. The backend URL you see above is the `tomcat` server running on port 8080. `swe645-backend` is the name of the backend service that we made in the `docker-compose.yml`. 

## BACKEND 

As we previously explained the backend is built and configured using `maven`. Here is the key aspects of the Maven `pom.xml`.

### **`pom.xml`**

```xml
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.swe645</groupId>
  <artifactId>swe645_hw3</artifactId>
  <version>1.0</version>
  <name>swe645_hw3</name>
  <packaging>war</packaging>
  <dependencies>
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>4.0.1</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>org.hibernate</groupId>
      <artifactId>hibernate-entitymanager</artifactId>
      <version>5.4.18.Final</version>
    </dependency>
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.17</version>
    </dependency>
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>2.8.6</version>
    </dependency>
  </dependencies>
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-war-plugin</artifactId>
        <version>3.3.0</version>
      </plugin>
    </plugins>
  </build>
</project>
```
we are using `javax.servlet` to handle our `api` calls. We are using `mysql-connector-java` to handle MySQL operations, and `hibernate-entitymanager` to make database operations. Additionally we are using `gson` since we only send and receive `json` files through our APIs.

### **`./backend/src/main/resources/persistence.xml`**

```xml
<persistence >
  <persistence-unit name="swe642db">
    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
    <class>com.swe645.StudentBean</class>
    <properties>
      <property name="javax.persistence.jdbc.driver"
                value="com.mysql.jdbc.Driver"/>
      <property name="javax.persistence.jdbc.url"
                value="jdbc:mysql://swe645-mysql:3306/swedb"/>
      <property name="javax.persistence.jdbc.user"
                value="root"/>
      <property name="javax.persistence.jdbc.password"
                value="admin"/>
    </properties>
  </persistence-unit>
</persistence>
```

**`jdbc:mysql://swe645-mysql:3306/swedb`** is path to database. 

## PROJECT TREE STRUCTURE

Here is the tree view of my application with some annotation if you want to quickly find out what files and components are important to look for. 

```treeview
swe645-hw3
├── README.md # Readme about setup, build, and installation
├── backend # backend service
│   ├── Dockerfile
│   ├── pom.xml # dependency and build
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── swe645
│           │           ├── FormServlet.java  # api: submit form, get student by id
│           │           ├── ListAllStudentsServlet.java # api: get all students
│           │           ├── StudentBean.java # Student bean class 
│           │           └── StudentDAO.java # Database operations
│           ├── resources
│           │   └── META-INF
│           │       └── persistence.xml # Database configuration
│           └── webapp
│               ├── 404.html # fallback on error page
│               └── WEB-INF
│                   └── web.xml
├── docker-compose.yml # orchestration of services
├── frontend
│   ├── Dockerfile
│   ├── default.conf
│   ├── package.json
│   ├── proxy.conf.json
│   ├── src
│   │   ├── Materia.css
│   │   ├── app
│   │   │   ├── app-routing.module.ts # all the routings are defined here
│   │   │   ├── app.component.html # application layout
│   │   │   ├── app.component.ts
│   │   │   ├── app.module.ts
│   │   │   └── component
│   │   │       ├── acknowledgment # acknowledgment page after submission
│   │   │       │   ├── acknowledgment.component.html
│   │   │       │   └── acknowledgment.component.ts
│   │   │       ├── home # home page
│   │   │       │   ├── home.component.html
│   │   │       │   ├── home.component.scss
│   │   │       │   ├── home.component.spec.ts
│   │   │       │   └── home.component.ts
│   │   │       ├── list-surveys # list all students. api call to ListAllStudents
│   │   │       │   ├── list-surveys.component.html
│   │   │       │   └── list-surveys.component.ts
│   │   │       ├── navigation # navbar
│   │   │       │   ├── navigation.component.html
│   │   │       │   └── navigation.component.ts
│   │   │       ├── student # api call(get) to FormServlet to get a single student form in readonly
│   │   │       │   ├── student.component.html
│   │   │       │   └── student.component.ts
│   │   │       └── survey # student survey page. api call(post) to FormServlet
│   │   │           ├── survey.component.html
│   │   │           └── survey.component.ts
│   │   └── styles.scss
└── mysql_init
    ├── Dockerfile
    └── table_init.sql
```

