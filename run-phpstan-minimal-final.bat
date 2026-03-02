@echo off
echo Lancement de PHPStan - Configuration minimale finale...
echo.

cd /d "c:\Users\User_01\Downloads\PIDEV-user1 (2)\PIDEV-user1"

echo Version de PHPStan:
php phpstan.phar --version

echo.
echo Analyse minimale (niveau 3, controleurs seulement):
php phpstan.phar analyse src --configuration=phpstan-minimal-final.neon

echo.
echo Analyse terminee!
pause
