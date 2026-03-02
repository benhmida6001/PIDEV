@echo off
echo Lancement de PHPStan - Configuration finale optimisee...
echo.

cd /d "c:\Users\User_01\Downloads\PIDEV-user1 (2)\PIDEV-user1"

echo Version de PHPStan:
php phpstan.phar --version

echo.
echo Analyse finale avec toutes les corrections:
php phpstan.phar analyse src --configuration=phpstan-final.neon

echo.
echo Analyse terminee!
pause
