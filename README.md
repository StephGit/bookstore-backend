# Enterprise Bookstore
JEE Book Store Project

## What is it?
This main goal of this project is to create a reference implementation of a state of the art enterprise application.

## Technologies
JavaEE 7, Glassfish, Angular, REST


## Releasenotes


### Sprint - Goal

The goal of this sprint was to integrate the Amazon catalog into the bookstore application and to provide a RESTful interface.

### Project Architectur

Changed ejb-application to a JAVA EE-Application.

[EE-Architecture](/doc/bookstore/architecture/ee-application.png)

### Amazon Catalog Integration

Resolved Features:

- Generating the client artifacts from the Amazon WSDL-File.
- Implementation of the session bean AmazonCatalog to lookup or search books.
- Implementation of an outbound message handler which adds the required Amazon request authentication to the soap-message-header.
The credentials for the Amazon request authentication are stored in the ejb-jar.xml.
- In the catalog service the queries to the local database were replaced by invocations of the AmazonCatalog session bean.
- Modification of the order service to store books in the local database if they are not present yet.
- Replaced ngTest-framework with junit5


### RESTful Web Service

Resolved Features:

- Implemented the catalog-, customer- and order-resources according to the specification.
- Swagger-Documentation of the Interface -> [localhost:8080/bookstore/apidocs/](localhost:8080/bookstore/apidocs/)


### Testing

Testing happens mainly by remote calls. For fast tests of the business logic we use a local h2-database and a WELD CDI Containert.



## Prerequisites

This project is based on a [Glassfish-Server 4.1.1.](https://javaee.github.io/glassfish/download)

- You need to generate the java artifacts from the amazon wsdl by running: `mvn generate-sources`

- Setup the GlassFish domain for the bookstore application with the following commands:

1. create domain
```
asadmin create-domain --savelogin=true bookstore
```
2. start server
```
asadmin start-domain --verbose bookstore
```
3. create JDBC connection pool
```
asadmin create-jdbc-connection-pool ^
--datasourceclassname org.apache.derby.jdbc.ClientDataSource ^
--restype javax.sql.DataSource ^
--property ServerName=localhost:Port=1527:^
DatabaseName=bookstore:User=YourUser:Password=YourPassword:^
ConnectionAttributes='create=true' ^
ConnectionPool
```
4. create JDBC datasource
```
asadmin create-jdbc-resource ^
--connectionpoolid ConnectionPool ^
jdbc/bookstore
```
5. create JMS connection factory
```
asadmin create-jms-resource ^
--restype javax.jms.ConnectionFactory ^
jms/connectionFactory
```
6. create JMS queue
```
asadmin create-jms-resource ^
--restype javax.jms.Queue ^
--property Name=PhysicalQueue ^
jms/orderQueue
```
7. Create Java mail session
```
asadmin create-javamail-resource ^
--mailhost YourMailhost ^
--mailuser YouUser ^
--fromaddress YourMailaddress ^
--property mail.smtp.port=25:mail.smtp.auth=true:^
mail.smtp.password=password:mail.smtp.starttls.enable=true ^
mail/bookstore
```
8. Stop server
```
asadmin stop-domain bookstore
```````

