set OLD_CLASSPATH=%CLASSPATH%


@set CP=%CLASSPATH%
@for %%i in (lib\ANT-1.6.2\*.jar) do call cp.bat %%i
@for %%i in (lib\optional\*.jar) do call cp.bat %%i
@for %%i in (lib\runtime\*.jar) do call cp.bat %%i
@for %%i in (lib\*.jar) do call cp.bat %%i

%JAVA_HOME%\bin\java -classpath %CP% -Xms64m -Xmx128m org.apache.tools.ant.Main -buildfile me-askmore.xml %1 %2 %3

set CLASSPATH=%OLD_CLASSPATH%