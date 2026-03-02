@echo off
echo Test PHPStan - Configuration propre...
echo.

cd /d "c:\Users\User_01\Downloads\PIDEV-user1 (2)\PIDEV-user1"

echo Version PHPStan:
php phpstan.phar --version

echo.
echo Analyse avec phpstan-clean.neon:
php phpstan.phar analyse src --configuration=phpstan-clean.neon

echo.
echo Analyse terminee!
pause
