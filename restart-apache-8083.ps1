# Script pour redémarrer Apache sur port 8083

# Arrêter tous les processus Apache et PHP
Stop-Process -Name httpd -Force -ErrorAction SilentlyContinue
Stop-Process -Name php -Force -ErrorAction SilentlyContinue
Start-Sleep -Seconds 3

# Modifier httpd.conf pour utiliser port 8083
$httpdConf = "C:\xampp\apache\conf\httpd.conf"
$content = Get-Content $httpdConf
$content = $content -replace "Listen 8080", "Listen 8083"
$content = $content -replace "ServerName localhost:8080", "ServerName localhost:8083"
$content | Set-Content $httpdConf

# Démarrer Apache sur port 8083
Start-Process -FilePath "C:\xampp\apache\bin\httpd.exe" -ArgumentList "-f", "C:\xampp\apache\conf\httpd.conf"

Write-Host "Apache redémarré sur port 8083"
Write-Host "Accès phpMyAdmin: http://localhost:8083/phpmyadmin"
