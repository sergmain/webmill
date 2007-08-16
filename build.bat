call mvn clean
call mvn -f pom-short.xml install

cd riverock-portlet
call mvn clean install
cd ..
cd riverock-webmill-war
call mvn clean install
cd ..
cd riverock-forum
call mvn clean install
cd ..
cd riverock-commerce
call mvn clean install
cd ..
cd riverock-webmill-admin
call mvn clean install
cd ..

