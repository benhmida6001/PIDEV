@echo off
echo ========================================
echo   TEST FORMULAIRE REINITIALISATION MOT DE PASSE
echo ========================================
echo.

cd /d "c:\Users\User_01\Downloads\PIDEV-user1 (2)\PIDEV-user1"

echo 1. Verification du cache Symfony:
php bin/console cache:clear

echo.
echo 2. Verification des logs Symfony:
echo Logs disponibles dans: C:\Users\User_01\.symfony5\log\

echo.
echo 3. Test du formulaire:
echo.
echo   - Acceder a: https://127.0.0.1:8000/reset-password
echo   - Saisir un email (ex: test@example.com)
echo   - Cliquer sur "Envoyer le lien"
echo   - Verifier les messages flash
echo.
echo 4. Verification des erreurs:
echo.
echo   - Logs PHP: php_errors.log
echo   - Logs Symfony: logs du serveur web
echo   - Logs application: error_log()
echo.
echo ========================================
echo   INSTRUCTIONS DE TEST
echo ========================================
echo.
echo 1. Ouvrir le navigateur sur https://127.0.0.1:8000/reset-password
echo 2. Saisir un email valide
echo 3. Cliquer sur "Envoyer le lien"
echo 4. Verifier le message flash qui s'affiche
echo 5. Si probleme, verifier les logs Symfony
echo.
pause
