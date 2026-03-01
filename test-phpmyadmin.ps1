# Script de test pour phpMyAdmin après configuration

Write-Host "🔧 Test de phpMyAdmin après configuration..." -ForegroundColor Yellow

# Test 1: Vérifier que Apache est actif
$apacheProcess = Get-Process -Name "httpd" -ErrorAction SilentlyContinue
if ($apacheProcess) {
    Write-Host "✅ Apache actif avec $($apacheProcess.Count) processus" -ForegroundColor Green
} else {
    Write-Host "❌ Apache n'est pas actif" -ForegroundColor Red
    exit 1
}

# Test 2: Vérifier le port 8081
$portTest = Test-NetConnection -ComputerName localhost -Port 8081 -WarningAction SilentlyContinue
if ($portTest.TcpTestSucceeded) {
    Write-Host "✅ Port 8081 accessible" -ForegroundColor Green
} else {
    Write-Host "❌ Port 8081 non accessible" -ForegroundColor Red
    exit 1
}

# Test 3: Vérifier le fichier .htaccess
$htaccessFile = "C:\xampp\phpMyAdmin\.htaccess"
if (Test-Path $htaccessFile) {
    Write-Host "✅ Fichier .htaccess créé dans phpMyAdmin" -ForegroundColor Green
    Write-Host "📝 Contenu:" -ForegroundColor Cyan
    Get-Content $htaccessFile | ForEach-Object { Write-Host "   $_" -ForegroundColor White }
} else {
    Write-Host "❌ Fichier .htaccess non trouvé" -ForegroundColor Red
}

# Test 4: Vérifier la configuration PHP
Write-Host "🔍 Configuration PHP actuelle:" -ForegroundColor Magenta
Write-Host "   • max_execution_time: $(php -r 'echo ini_get(\"max_execution_time\");')" -ForegroundColor White
Write-Host "   • memory_limit: $(php -r 'echo ini_get(\"memory_limit\");')" -ForegroundColor White
Write-Host "   • max_input_time: $(php -r 'echo ini_get(\"max_input_time\");')" -ForegroundColor White

# Test 5: Instructions pour l'utilisateur
Write-Host "" -ForegroundColor White
Write-Host "🌐 Accès à phpMyAdmin:" -ForegroundColor Yellow
Write-Host "   URL: http://localhost:8081/phpmyadmin" -ForegroundColor Cyan
Write-Host "" -ForegroundColor White
Write-Host "🔧 Si problème persiste:" -ForegroundColor Yellow
Write-Host "   1. Vérifier les logs Apache: C:\xampp\apache\logs\error.log" -ForegroundColor White
Write-Host "   2. Vérifier les logs PHP: C:\xampp\php\logs\php_error_log" -ForegroundColor White
Write-Host "   3. Vider le cache du navigateur" -ForegroundColor White
Write-Host "   4. Redémarrer XAMPP complètement" -ForegroundColor White

Write-Host "" -ForegroundColor White
Write-Host "🎯 Configuration appliquée:" -ForegroundColor Green
Write-Host "   • max_execution_time: 300s (via .htaccess)" -ForegroundColor White
Write-Host "   • max_input_time: 300s (via .htaccess)" -ForegroundColor White
Write-Host "   • memory_limit: 1024M (via .htaccess)" -ForegroundColor White
Write-Host "   • Fichier .htaccess: Créé dans phpMyAdmin" -ForegroundColor White

Write-Host "" -ForegroundColor White
Write-Host "🚀 Testez maintenant phpMyAdmin !" -ForegroundColor Green
