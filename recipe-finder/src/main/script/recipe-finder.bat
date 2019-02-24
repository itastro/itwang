@echo off

::----------------------------------------------------------------------
:: Recipe Finder Startup Script
::----------------------------------------------------------------------

SET JAVA_HOME=D:\develop\Java
set classpath=.;%JAVA_HOME%\lib;%JAVA_HOME%\lib\tools.jar
SET path=D:\develop\Java\bin

f:
cd F:\itwang\recipe-finder\src\main\java\recipe

javac RecipeFinder.java
java  RecipeFinder

java -Xms128m -Xmx512m -Djava.util.logging.config.file=F:/recipe-finder/logging.properties
