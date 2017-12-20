# Enterprise Bookstore
JEE Book Store Project

## What is it?
This main goal of this project is to create a reference implementation of a state of the art enterprise application.

## Technologies
JavaEE 7, Glassfish, Angular, REST

## Releasnotes

### Transaction Attibutes
Transaktionsattribute wurden bewusst überall auf dem default "REQUIRED" belassen. Hier gilt das Prinzip Convention over Configuration. Sobald wir dann auf ein Transaktions-bedingtes Problem stossen würden, würden wir entsprechende Massnahmen ergreifen und z.b Transaktionen trennen mit "REQUIRES_NEW";

Die onMessage Methode des MDB OrderProcessor ist ebenfalls auf default belassen. Dies aus dem Grund dass wir Retries möchten wenn die Message nicht verarbeitet werden konnte und etwas schief gegangen ist.



Es müsste dann die Dead Message Queue analysiert werden und schauen wo im Payload der Fehler liegt. Keinesfalls wollen wir die Message verlieren.

### Exceptions
Die PaymentFailedException wurde auf rollback=true gestellt, weil wir bewusst alles zurückrollen möchten sobald etwas beim Bezahlvorgang schief gehen sollte.
Alle anderen ApplicationsExceptions sind auf dem Default rollback=false belassen.

### Mail Service
Das versenden des Mails konnte trotz Konfiguration nach Lehrbuch nicht funktioniert. Es wurden folgende MessagingExceptions geworfen:

- 573 5.1.1 Swisscom Antispam: Authentifizierte Verbindung nicht moeglich. Bitte benutzen Sie den Port 587 oder 465 (SSL/TLS) anstelle von Port 25. Weitere Informationen: www.swisscom.ch/p25. Connexion authentifiee pas possible. Veuillez utiliser le port 587 ou 465 (SSL/TLS) a la place du port 25. Ulterieurs informations: www.swisscom.ch/p25. Collegamento autenticato non e possibile. Si prega di utilizzare la porta 587 o 465 (SSL/TLS) invece di porta 25. Altra informazione: www.swisscom.ch/p25. Authenticated connection is not possible. Please use port 587 or 465 (SSL/TLS) instead of port 25. More information: www.swisscom.ch/p25.
- 550 5.7.60 SMTP; Client does not have permissions to send as this sender

Der Grund scheint ein Security/Berechtigungsproblem zu sein.

### Interceptors
Für Logging-Zwecke wurde ein gloabler Interceptor eingebaut.

### Testing
Getestet wurde die Applikation mittels Remote aufrufen.
Weil wir aber nicht immer den ganzen, schwerfälligen Applikationsserver starten möchten, haben wir eine lokale H2 Datenbank und ein Weld CDI Container verwendet.
Dieses Setup ermöglichte und das schnelle Testen unserer Business Logik.


### Prerequisites

This project is based on a [Glassfish-Server 4.1.1.](https://javaee.github.io/glassfish/download)

Setup the GlassFish domain for the bookstore application with the following commands:

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

