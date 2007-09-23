call mvn clean
call mvn -f pom-current.xml install


cd riverock-webmill
call mvn clean install
cd ..
cd riverock-webmill-war
call mvn clean install
cd ..