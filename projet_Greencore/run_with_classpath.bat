@echo off
set JAVA_HOME=C:\Users\User_01\AppData\Local\Programs\Eclipse Adoptium\jdk-25.0.1.8-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

echo ========================================
echo Lancement avec JavaFX via Maven classpath
echo ========================================

echo.
echo 1. Compilation avec Maven...
.\mvnw.cmd compile

if %ERRORLEVEL% NEQ 0 (
    echo Erreur de compilation Maven
    pause
    exit /b 1
)

echo.
echo 2. Recherche des JAR JavaFX...
set CLASSPATH=target\classes

for %%f in (%USERPROFILE%\.m2\repository\org\openjfx\javafx-controls\*.jar) do (
    set CLASSPATH=!CLASSPATH!;%%f
)

for %%f in (%USERPROFILE%\.m2\repository\org\openjfx\javafx-fxml\*.jar) do (
    set CLASSPATH=!CLASSPATH!;%%f
)

for %%f in (%USERPROFILE%\.m2\repository\org\openjfx\javafx-graphics\*.jar) do (
    set CLASSPATH=!CLASSPATH!;%%f
)

for %%f in (%USERPROFILE%\.m2\repository\org\openjfx\javafx-base\*.jar) do (
    set CLASSPATH=!CLASSPATH!;%%f
)

echo.
echo 3. Lancement de l'application...
echo Classpath: %CLASSPATH%
java -Xms16m -Xmx64m -cp "%CLASSPATH%" Main

echo.
echo Application terminee.
pause
