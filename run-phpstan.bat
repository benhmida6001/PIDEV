@echo off
echo Lancement de PHPStan pour l'analyse statique du code...
echo.

cd /d "c:\Users\User_01\Downloads\PIDEV-user1 (2)\PIDEV-user1"

echo Verification de PHPStan...
php phpstan.phar --version

echo.
echo Analyse du code source...
php phpstan.phar analyse src --configuration=phpstan.neon

echo.
echo Analyse terminee!
pause
