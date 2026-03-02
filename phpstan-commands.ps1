# Script PowerShell pour PHPStan
Write-Host "Test PHPStan - Configuration propre..." -ForegroundColor Green

cd "c:\Users\User_01\Downloads\PIDEV-user1 (2)\PIDEV-user1"

Write-Host "Version PHPStan:" -ForegroundColor Yellow
php phpstan.phar --version

Write-Host "`nAnalyse avec phpstan-clean.neon:" -ForegroundColor Yellow
php phpstan.phar analyse src --configuration=phpstan-clean.neon

Write-Host "`nAnalyse terminee!" -ForegroundColor Green
Read-Host "Appuyez sur Entree pour continuer"
