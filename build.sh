mvn compile -f "/Users/tim/_git/jmeter-redis-pugin/pom.xml"
mvn package -f "/Users/tim/_git/jmeter-redis-pugin/pom.xml"
cp target/jmeter-bzm-dummy-1.0-SNAPSHOT.jar /usr/local/opt/jmeter/libexec/lib/ext
ls -l /usr/local/opt/jmeter/libexec/lib/ext