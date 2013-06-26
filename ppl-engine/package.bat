set MAVEN_OPTS=-Xms512m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=512m -XX:+PrintGCDetails
mvn package
pause