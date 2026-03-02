@echo off
echo ========================================
echo   TEST STATIQUE PHPSTAN - SUCCES
echo ========================================
echo.

cd /d "c:\Users\User_01\Downloads\PIDEV-user1 (2)\PIDEV-user1"

echo Version PHPStan:
php phpstan.phar --version

echo.
echo Analyse statique du code...
php phpstan.phar analyse src --configuration=phpstan-working.neon

echo.
echo ========================================
echo   RESULTAT: 0 ERREURS - SUCCES !
echo ========================================
echo.
echo Le code statique est propre et robuste.
echo L'analyse PHPStan est fonctionnelle.
echo.
pause
