@echo off
set JAVA_HOME=C:\Users\User_01\AppData\Local\Programs\Eclipse Adoptium\jdk-25.0.1.8-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

echo Test de compilation sans JavaFX...
echo ========================================

if not exist "target\classes" mkdir target\classes

echo Compilation des classes de test...
javac -d target\classes ^
    src\main\java\MainConsole.java ^
    src\main\java\controller\LoginSimpleController.java ^
    src\main\java\interfaces\IUtilisateurService.java ^
    src\main\java\interfaces\IEvenementService.java ^
    src\main\java\models\Utilisateur.java ^
    src\main\java\models\Evenement.java ^
    src\main\java\services\UtilisateurService.java ^
    src\main\java\services\EvenementService.java ^
    src\main\java\tools\DatabaseConfig.java ^
    src\main\java\tools\MyConnection.java

if %ERRORLEVEL% EQU 0 (
    echo ✓ Compilation réussie!
    echo.
    echo Lancement du test...
    java -cp target\classes MainConsole
) else (
    echo ✗ Erreur de compilation
)

pause
