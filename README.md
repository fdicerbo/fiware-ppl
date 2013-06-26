fiware-ppl
==========

This is PPL, an implementation of FI-WARE Data Handling Generic Enabler, please check for more information: http://wiki.fi-ware.eu/FIWARE.OpenSpecification.Security.Data_Handling_Generic_Enabler


PREREQUISITES

Create database "ppl", "ppl-dc" and "ppl-3p".
The default configuration assumes that database system runs on localhost on
port 3306 (e.g. default configuration for MySQL).


BUILD

Project uses Maven as a build tool.
The parent project is "ppl-engine".
Simply run:

> mvn package

in the "ppl-engine" project directory to build the full project.


RUNNING

1. Reset database: run "database-reset/reset_db" script.
2. Final build produces a WAR file:
	- "ppl-rest/target/ppl-rest.war"
	
	Drop this file into your Tomcat "webapps" directory and lunch the server.
3. The REST backend will be loaded and available.
