@echo off
echo Lancement de PHPStan - Configuration propre (exclusions)...
echo.

cd /d "c:\Users\User_01\Downloads\PIDEV-user1 (2)\PIDEV-user1"

echo Version de PHPStan:
php phpstan.phar --version

echo.
echo Analyse propre avec exclusions:
php phpstan.phar analyse src --configuration=phpstan-clean.neon

echo.
echo Analyse terminee!
pause
