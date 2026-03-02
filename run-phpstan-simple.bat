@echo off
echo Lancement de PHPStan - Configuration simple...
echo.

cd /d "c:\Users\User_01\Downloads\PIDEV-user1 (2)\PIDEV-user1"

echo Version de PHPStan:
php phpstan.phar --version

echo.
echo Analyse simple (niveau 3):
php phpstan.phar analyse src --configuration=phpstan-minimal.neon

echo.
echo Analyse avancee (niveau 6):
php phpstan.phar analyse src --configuration=phpstan-simple.neon

echo.
echo Analyse terminee!
pause
