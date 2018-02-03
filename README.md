# Enterprise Bookstore
JEE Book Store Project

## What is it?
This main goal of this project is to create a reference implementation of a state of the art enterprise application.

## Technologies
JavaEE 7, Glassfish, Angular, REST

### Releasenotes



### TODO
- [ ]  Correct error handling (More than one book with same ISBN --> Fischlis sample?) - S
- [?]  Handle empty results -> Tests - S
- [x]  When placing an order -> save book to db if not present yet - A
- [x]  Catalog Resource - A
- [x]  Order Resource - A
- [x]  Customers Resource - S
- [x]  REST Assured sample including dependency - A
- [ ]  Make integration tests great again! - A
- [x]  Swagger - S
- [x]  Handle names with blanks (?name=von+Gruenigen)
- [x]  REMOVE TEST DEPENDENCIES - A & S

### Prerequisites

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

