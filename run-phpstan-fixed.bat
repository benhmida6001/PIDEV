@echo off
echo Lancement de PHPStan - Apres corrections...
echo.

cd /d "c:\Users\User_01\Downloads\PIDEV-user1 (2)\PIDEV-user1"

echo Version de PHPStan:
php phpstan.phar --version

echo.
echo Analyse avec corrections appliquees:
php phpstan.phar analyse src --configuration=phpstan-fixed.neon

echo.
echo Analyse terminee!
pause
