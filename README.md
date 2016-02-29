# spark-cassandra-java

Reference: http://www.datastax.com/dev/blog/accessing-cassandra-from-spark-in-java


### Build
##### gradle clean build

### Add dependencies in a libs folder
##### gradle copyLibs

### Upload files to Spark server
##### create spark-cassandra-java/ directory in spark server
mkdir spark-cassandra-java
##### upload build/libs/spark-cassandra-java-0.0.1-SNAPSHOT.jar and libs/*.jar to /sparkServer/spark-cassandra-java/

### run spark-submit
##### go to spark-cassandra-java/ in spark server
cd spark-cassandra-java
##### run spark-submit
spark-submit --class dannyk.project.JavaDemo --jars $(echo ./*.jar | tr ' ' ',') \
--master {master} spark-cassandra-1.0.jar {spark master} {cassandra end point}


spark-submit --class dannyk.project.JavaDemo --jars $(echo ./*.jar | tr ' ' ',') --master local[4] spark-cassandra-java-0.0.1-SNAPSHOT.jar local 192.168.99.100
