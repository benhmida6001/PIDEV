@echo off
set JAVA_HOME=C:\Users\User_01\AppData\Local\Programs\Eclipse Adoptium\jdk-25.0.1.8-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

echo ========================================
echo Compilation de tous les fichiers Java
echo ========================================

echo.
echo 1. Creation des repertoires...
if not exist "target\classes" mkdir target\classes
if not exist "target\classes\fxml" mkdir target\classes\fxml

echo.
echo 2. Copie des fichiers FXML...
copy /Y "src\main\resources\fxml\*.fxml" "target\classes\fxml\" >nul 2>&1

echo.
echo 3. Compilation de tous les fichiers Java...
javac -d target\classes -cp "target\classes" ^
    src\main\java\Main.java ^
    src\main\java\api\AdvancedFeaturesAPIController.java ^
    src\main\java\controller\*.java ^
    src\main\java\interfaces\*.java ^
    src\main\java\models\*.java ^
    src\main\java\services\*.java ^
    src\main\java\tools\*.java 2>nul

if %ERRORLEVEL% NEQ 0 (
    echo Erreur de compilation - Tentative avec modules JavaFX...
    javac --module-path "%JAVA_HOME%\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml -d target\classes ^
        src\main\java\Main.java ^
        src\main\java\api\AdvancedFeaturesAPIController.java ^
        src\main\java\controller\*.java ^
        src\main\java\interfaces\*.java ^
        src\main\java\models\*.java ^
        src\main\java\services\*.java ^
        src\main\java\tools\*.java
)

echo.
echo 4. Lancement de l'application...
java -cp target\classes -Xms32m -Xmx64m Main

echo.
echo Application terminee.
pause
