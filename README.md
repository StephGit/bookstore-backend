# Enterprise Bookstore
JEE Book Store Project

## What is it?
This main goal of this project is to create a reference implementation of a state of the art enterprise application.

## Technologies
JavaEE 7, Wildfly 10.1, REST


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

This project is based on [Wildfly 10.1.0.Final](http://wildfly.org/news/2016/08/19/WildFly10-1-Released/)

- You need to generate the java artifacts from the amazon wsdl by running: `mvn generate-sources`

Setup JMS-Queue (Source from [MiddlewareMagic](http://middlewaremagic.com/jboss/?p=2739) ):
 
1. Start the WildFly 10 “full” profile (which has messaging) as following

`$ cd /PATH/TO/wildfly-10.0.0.CR3-SNAPSHOT/bin`
`$ ./standalone.sh -c standalone-full.xml`

2. Create a simple JMS user on WildFly 10 side and this user must belong to “guest” role. Please see the “messaging subsystem” configuration of “standalone-full.xml” to know more about “guest” role.
username: jmsuser
password: jmsuser@123
user role: guest
Realm: ApplicationRealm

3.  Creating a simple JMS Queue using the WildFly CLI command line utility. NOTE the JNDI name should contain “java:/jboss/exported” prefix or else the JMS queue will can not be looked up remotely

 `$ cd /PATH/TO/wildfly-10.0.0.CR3-SNAPSHOT/bin`
 `$ ./jboss-cli.sh -c`
 
`[standalone@localhost:9990 /] /subsystem=messaging-activemq/server=default/jms-queue=orderQueue:add(entries=["java:/jboss/exported/jms/queue/orderQueue"])`

