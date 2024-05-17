[![Java](https://img.shields.io/badge/Java-8%2B-orange)](https://www.oracle.com/java/)
![Lines of Code](https://img.shields.io/badge/lines_of_code-509-green)
![License](https://img.shields.io/badge/license-MIT-blue)

**Saver for Vacancies Bot** s a component of the Vacancies Bot application built on a microservices architecture. It reads vacancies imported by [Importer](https://github.com/kyljmeeski/vacancies-bot-importer), if they are new, saves them to MongoDB and sends to Notifier.
## Features
- Connects to RabbitMQ to consume imports tasks.
- Parses vacancies.
- Publish vacancies to store.

## Requirements
- Java 8 or higher
- RabbitMQ server running locally or accessible via network
- MongoDB server running locally or accessible via network
- Maven for building the project

## Installation
1. Clone the repository:
```shell
git clone https://github.com/kyljmeeksi/vacancies-bot-saver.git
```
2. Navigate to the project directory:
```shell
cd vacancies-bot-saver 
```
3. Build the project using Maven:
```shell
mvn clean install
```

## Configuration
Modify the following parameters in Main.java to match your RabbitMQ and MongoDB setup:
```java
factory.setHost("localhost");
factory.setPort(5672);
MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
```
Ensure the RabbitMQ and MongoDB servers are configured correct.

## Usage
1. Run the application:
```shell
java -jar vacancies-bot-saver.jar
```

