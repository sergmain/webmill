set JAVA_HOME_OLD=%JAVA_HOME%
set JAVA_HOME=c:\j2sdk1.4.2_07

set CLASSPATH=..\riverock-lib\lib\ant-1.5.1-jdk13.jar;%JAVA_HOME%\lib\tools.jar;
%JAVA_HOME%\bin\java -Xms256m -Xmx384m org.apache.tools.ant.Main %1 %2 %3

set JAVA_HOME=%JAVA_HOME_OLD%