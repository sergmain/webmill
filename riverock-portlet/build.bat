set JAVA_HOME_OLD=%JAVA_HOME%
set JAVA_HOME=C:\jdk1.5.0_05

set CLASSPATH=..\riverock-lib\lib\ant-1.6.2.jar;..\riverock-lib\lib\ant-launcher-1.6.2.jar;%JAVA_HOME%\lib\tools.jar;
%JAVA_HOME%\bin\java -Xms64m -Xmx128m org.apache.tools.ant.Main %1 %2 %3

set JAVA_HOME=%JAVA_HOME_OLD%