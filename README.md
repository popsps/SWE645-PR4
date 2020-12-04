# SWE645-PROJECT4

## [EC2 Link](http://ec2-3-235-245-12.compute-1.amazonaws.com/)

This project is a container orchestration of 3 services. A frontend web application implemented using `Angular` and `nginx`. A backend application implemented using `JAVA EE` and `Tomcat` to handle REST API requests. And finally a persistent `Kafka` messaging service that acts as a persistent database.

## BUILD

To build and run this project you will need to have `Docker` installed on your machine. We will be using `Docker Swarm` for orchestration of services. To build and run this project simply run `docker stack deploy -c ./docker-compose.yml swe645`. This will create a frontend, a backend, 3 brokers and 3 zookeepers.

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

There are two major changes in this project in comparison to the pervious project. One is the addition of `Kafa` and two is the modification of the backend. We first discuss our configuration for the `Kafka` services and then dive into our backend.

## KAFKA

### **`docker-compose.yml`**

```yml

```

## JAVA BACKEND



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
