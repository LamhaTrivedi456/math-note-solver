@echo off
call mvnw.cmd -q -DskipTests compile
if not exist cp.txt call mvnw.cmd -q dependency:build-classpath -Dmdep.outputFile=cp.txt
for /f "delims=" %%i in (cp.txt) do java -cp "target\classes;%%i" com.mathnotes.mathnotesolver.MathnotesolverApplication