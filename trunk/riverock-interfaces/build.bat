set JAVA_HOME_OLD=%JAVA_HOME%

set JAVA_HOME=c:\jdk1.4.2



set CLASSPATH=lib\ant-1.5.1-jdk13.jar;%JAVA_HOME%\lib\tools.jar;lib\xml-commons-1.1.1.jar

%JAVA_HOME%\bin\java org.apache.tools.ant.Main -buildfile build-schema.xml %1 %2 %3



set JAVA_HOME=%JAVA_HOME_OLD%