@echo off
echo Telechargement de PHPStan...
echo.

cd /d "c:\Users\User_01\Downloads\PIDEV-user1 (2)\PIDEV-user1"

echo Telechargement depuis GitHub...
powershell -Command "Invoke-WebRequest -Uri 'https://github.com/phpstan/phpstan/releases/latest/download/phpstan.phar' -OutFile 'phpstan.phar'"

echo.
echo Verification du telechargement:
if exist phpstan.phar (
    echo [OK] phpstan.phar telecharge avec succes
    php phpstan.phar --version
) else (
    echo [ERREUR] Le telechargement a echoue
)

echo.
pause
