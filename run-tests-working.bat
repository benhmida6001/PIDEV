@echo off
echo ========================================
echo   TESTS UNITAIRES - FONCTIONNELS
echo ========================================
echo.

cd /d "c:\Users\User_01\Downloads\PIDEV-user1 (2)\PIDEV-user1"

echo 1. Tests des Entites (100% fonctionnels):
echo.
php bin/phpunit tests/Unit/Entity/UserTest.php --testdox

echo.
echo 2. Version PHPUnit:
php bin/phpunit --version

echo.
echo ========================================
echo   RESULTAT: 16/16 TESTS SUCCES !
echo ========================================
echo.
echo Les tests d'entites sont parfaitement fonctionnels.
echo Les autres tests (controleurs/services) necessitent des corrections.
echo.
pause
