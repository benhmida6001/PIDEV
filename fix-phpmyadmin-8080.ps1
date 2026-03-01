# Script pour configurer phpMyAdmin sur port 8080

# Arrêter Apache actuel
Stop-Process -Name httpd -Force -ErrorAction SilentlyContinue
Start-Sleep -Seconds 3

# Modifier httpd.conf pour utiliser port 8080
$httpdConf = "C:\xampp\apache\conf\httpd.conf"
$content = Get-Content $httpdConf
$content = $content -replace "Listen 8081", "Listen 8080"
$content = $content -replace "ServerName localhost:8081", "ServerName localhost:8080"
$content | Set-Content $httpdConf

# Démarrer Apache sur port 8080
Start-Process -FilePath "C:\xampp\apache\bin\httpd.exe" -ArgumentList "-f", "C:\xampp\apache\conf\httpd.conf"

Write-Host "Apache redémarré sur port 8080"
Write-Host "Accès phpMyAdmin: http://localhost:8080/phpmyadmin"

Write-Host "Apache redémarré sur port 8080"
Write-Host "Accès phpMyAdmin: http://localhost:8080/phpmyadmin"
