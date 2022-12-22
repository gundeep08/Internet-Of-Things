# IOT Store Authentication 

The Internet of Things (IoT) is an environment in which objects, animals, or
people are provided with unique identifiers and the ability to transfer data over network without requiring human-to-human or human-to-computer interaction. 
In this document we introduce the Authentication component of this system. Authentication component acts as a security layer and ensures that each user has an appropriate permissions or roles before invoking that action and also validates the authToken and ensures session is not invalidated.



## Compiling Instructions

•	javac com/cscie97/ledger/*.java
•	javac com/cscie97/store/command/processors/*.java
•	javac com/cscie97/store/controller/*.java
•	javac com/cscie97/store/interfaces/*.java
•	javac cscie97/store/test/*.java
•	javac cscie97/store/model/*.java
•	javac com/cscie97/store/authentication/*.java




## Execution Instructions

•	java -cp . cscie97.store.controller.test.TestAuthenticationService cscie97/store/authentication/resources/StoreAuthenticationScript.txt


