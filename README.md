
[![CircleCI](https://circleci.com/gh/narges-sh-maleki/mankalah-project.svg?style=svg)](https://circleci.com/gh/narges-sh-maleki/mankalah-project)

 #Mankalah Project

A Java RESTful Web Service that runs a game of 6-stone Kalah.
The application is developed by JAVA 11, the latest version of Spring Boot and Spring Data JPA.

The project has three profiles: dev, test and prod. For now, all are similar but there is possibility to change the properties. 

H2 Database is considered as a development DB, but there is possibility to change to other RDBMS data sources. 

The project includes unit tests developed by Junit 5 and Mockito, and Integration Tests.

##How to run
*1. Build the project using the below command:*

    mvn clean install 
*2. Run the application using:*

    cd mankalah
    cd target
    java -Dspring.profiles.active=prod -jar mankalah-0.0.1-SNAPSHOT.jar --server.port=$SERVER_PORT

*3. Play the game:*

Start game command:<br/>

        curl --header "Content-Type: application/json" --request POST http://<host>:<port>/games    

Make a move:<br/>

        curl --header "Content-Type: application/json" --request PUT http://<host>:<port>/games/{gameId}/pits/{pitId}

## Future Works:
- Containerization using Docker
- Adding Spring Config Server to set the application configurations
- Adding Eureka Service for monitoring and service registry
