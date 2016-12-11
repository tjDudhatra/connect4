# connect4

To run Connect4 Backend you have to follow the below steps.

Prerequisites:
  Java 1.8
  Maven 3.*
  Git 1.*
  MySql 5.5 or later

How to Start the server:

1. Clone the repository: git clone https://github.com/tjDudhatra/connect4.git
2. Configure application.properties & application-test.properties.
3. Build using maven: `mvn clean compile -U package` (There should be no error. All tests should pass successfully).
4. Get jar file from the target directory and run that jar file using: `java -jar connect4-0.0.1-SNAPSHOT.jar`
    In response to this, you should be seeing something like this:
    `2016-12-11 23:38:38.578  INFO 17991 --- [main] .s.b.c.e.j.JettyEmbeddedServletContainer : Jetty started on port(s) 8080 (http/1.1)`
    `2016-12-11 23:38:38.649  INFO 17991 --- [main] c.t.games.connect4.Connect4Starter       : Started Connect4Starter in 20.632 seconds (JVM running for 21.885)`
5. Connect4 Backend is running now. Look for the required APIs and you can start using it as per your need.

