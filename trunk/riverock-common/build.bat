set JAVA_HOME_OLD=%JAVA_HOME%
set JAVA_HOME=c:\jdk1.4.2

set CLASSPATH=..\js-lib\lib\ant-1.5.1-jdk13.jar;%JAVA_HOME%\lib\tools.jar;..\js-lib\lib\xml-commons-1.1.1.jar
%JAVA_HOME%\bin\java org.apache.tools.ant.Main %1

set JAVA_HOME=%JAVA_HOME_OLD%