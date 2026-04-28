@echo off
set JAVA_HOME=C:\Users\User_01\AppData\Local\Programs\Eclipse Adoptium\jdk-25.0.1.8-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

echo ========================================
echo Lancement avec mémoire minimale
echo ========================================

echo.
echo 1. Compilation simple...
if not exist "target\classes" mkdir target\classes

copy /Y "src\main\resources\fxml\*.fxml" "target\classes\fxml\" >nul 2>&1

javac -d target\classes -cp "target\classes" ^
    src\main\java\Main.java ^
    src\main\java\controller\*.java ^
    src\main\java\interfaces\*.java ^
    src\main\java\models\*.java ^
    src\main\java\services\*.java ^
    src\main\java\tools\*.java 2>nul

if %ERRORLEVEL% NEQ 0 (
    echo Erreur de compilation
    pause
    exit /b 1
)

echo.
echo 2. Lancement avec mémoire très réduite...
java -Xms8m -Xmx32m -XX:+UseSerialGC -XX:MaxHeapFreeRatio=10 -XX:MinHeapFreeRatio=5 -cp target\classes Main

echo.
echo Application terminee.
pause
