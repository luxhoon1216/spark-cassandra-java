# spark-cassandra-java

Reference: https://gist.github.com/jacek-lewandowski/278bfc936ca990bee35a


### Build
##### gradle clean build

### Add dependencies in a lib folder
##### gradle copyLibs

### Upload files to Spark server
##### create spark-cassandra-java/ directory in spark server
mkdir spark-cassandra-java
##### upload build/libs/spark-cassandra-java-1.0.jar and lib/*.jar to /sparkServer/spark-cassandra-java/

### run spark-submit
##### go to spark-cassandra-java/ in spark server
cd spark-cassandra-java
##### run spark-submit
spark-submit --class dannyk.project.JavaDemo --jars $(echo ./*.jar | tr ' ' ',') \
--master yarn-client spark-cassandra-1.0.jar {spark master} {cassandra end point}


spark-submit --class dannyk.project.JavaDemo --jars $(echo ./*.jar | tr ' ' ',') \
--master yarn-client spark-cassandra-1.0.jar yarn-client 192.168.99.100
