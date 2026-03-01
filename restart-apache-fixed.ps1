# Script pour redémarrer Apache XAMPP
# Utilisation: powershell -ExecutionPolicy Bypass -File restart-apache-fixed.ps1

Write-Host "🔄 Redémarrage d'Apache XAMPP..." -ForegroundColor Yellow

# Arrêter Apache
Write-Host "🛑 Arrêt d'Apache..." -ForegroundColor Red
Stop-Process -Name "httpd" -Force -ErrorAction SilentlyContinue

# Attendre 3 secondes
Start-Sleep -Seconds 3

# Démarrer Apache
Write-Host "🚀 Démarrage d'Apache..." -ForegroundColor Green
Start-Process -FilePath "C:\xampp\apache\bin\httpd.exe" -WindowStyle Hidden

# Attendre 5 secondes pour que le service démarre complètement
Start-Sleep -Seconds 5

# Vérifier le statut
$apacheProcess = Get-Process -Name "httpd" -ErrorAction SilentlyContinue
if ($apacheProcess) {
    Write-Host "✅ Apache est maintenant en cours d'exécution" -ForegroundColor Green
    Write-Host "📊 Processus actifs: $($apacheProcess.Count)" -ForegroundColor Cyan
    Write-Host "🌐 Accès à phpMyAdmin: http://localhost:8081/phpmyadmin" -ForegroundColor Yellow
}
else {
    Write-Host "❌ Erreur: Apache n'a pas pu démarrer" -ForegroundColor Red
}

Write-Host "🎯 Configuration PHP mise à jour:" -ForegroundColor Magenta
Write-Host "   • max_execution_time: 120 → 300 secondes" -ForegroundColor White
Write-Host "   • max_input_time: 60 → 300 secondes" -ForegroundColor White
Write-Host "   • memory_limit: 512M → 1024M" -ForegroundColor White
Write-Host "" -ForegroundColor White
Write-Host "🔧 Testez phpMyAdmin maintenant !" -ForegroundColor Green
