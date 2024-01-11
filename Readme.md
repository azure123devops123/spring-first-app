HOW TO CREATE A SPRING BOOT HELLOWORLD PROJECT spring initializer:
// Help Link:   https://www.youtube.com/watch?v=QXIxKZINJV4
=========================================================================================
STEP:-1     https://start.spring.io/        // or google "spring boot initializer"

STEP:-2     Create a file called "HelloController.java" in the "/src/main/java/com/devops/springfirstapp" directory.

STEP:-3     Contents of the "HelloController.java" are as below:

package com.devops.springfirstapp;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
@RestController  
public class HelloController   
{  
@RequestMapping("/hello")  
public String hello()   
{  
return "Greetings from Spring Boot Successful - Excellent Work";
}
}

STEP:-4     It should look like this:
muhammadjabir@Muhammads-MacBook-Air src % tree 
.
├── main
│   ├── java
│   │   └── com
│   │       └── devops
│   │           └── springfirstapp
│   │               ├── HelloController.java
│   │               └── SpringFirstAppApplication.java

STEP:-5 Create a Azure Pipeline.

STEP:- 5  Test in Browser.
https://dev-springfirstapp.azurewebsites.net/hello

============================================= LOCALLY RUN IT ============================================================
0) Go inside "spring-first-app" directory.

# Using the Maven Wrapper in Spring Boot:   https://www.baeldung.com/maven-wrapper
# ---------------------------------------
# mvn -N wrapper:wrapper                         ( Maven Wrapper plugin to make auto installation in a simple Spring Boot project )
# you can see it will create a directory .mvn
# cat .mvn/wrapper/maven-wrapper.properties      (you can see inside a specific version of maven which it downloads like in our case here is apache-maven-3.9.5)
# ./mvnw spring-boot:run                         ( When you will run it first time - it will download and install a specific version inside our project)

# ./mvnw spring-boot:run        (note:- How to run this application from local system. (Notice it will not create a JAR file inside target directory)

# Check into Browser:
# http://localhost:8080/hello

================= HOW TO PACKAGE INTO A JAR FILE SO WE CAN DEPLOY IT ON AZURE WEB APP USING JAVA 8 SE ================

3) If you want to build it to create a JAR file. Use following command
mvn -f pom.xml clean package

4) RUN THE JAR FILE FROM ROOT DIRECTORY (java -jar <jarfilename>.jar):
java -jar target/spring-first-app-0.0.1-SNAPSHOT.jar

4) Run it - STEP 4 IS NOT WORKING
java -cp target/spring-first-app-0.0.1-SNAPSHOT.jar com.devops.springfirstapp.App

Help Link: https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html

================= docker compose commands =================
docker compose version    // Docker Compose version v2.19.1
docker compose build      // Used for build OR rebuild
docker compose up -d      // Create and start container in the detached mode
docker compose down       // Stop and remove container, network