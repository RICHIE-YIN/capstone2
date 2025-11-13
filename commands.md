psql foodhall -f schema.sql

mvn clean compile exec:java -Dexec.mainClass="com.richie.web.Main"

http://localhost:8080/api/health
