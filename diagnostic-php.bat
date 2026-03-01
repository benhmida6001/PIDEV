# Test de diagnostic PHP
echo "Test de diagnostic PHP..."
php -v
echo ""
echo "Configuration PHP:"
php -r "echo 'max_execution_time: ' . ini_get('max_execution_time') . PHP_EOL;"
php -r "echo 'memory_limit: ' . ini_get('memory_limit') . PHP_EOL;"
php -r "echo 'max_input_time: ' . ini_get('max_input_time') . PHP_EOL;"
echo ""
echo "Test de connexion MySQL:"
php -r "
try {
    \$conn = new mysqli('localhost', 'root', '', 'mysql');
    if (\$conn->connect_error) {
        echo 'Erreur de connexion: ' . \$conn->connect_error . PHP_EOL;
    } else {
        echo 'Connexion MySQL réussie!' . PHP_EOL;
        \$conn->close();
    }
} catch (Exception \$e) {
    echo 'Exception: ' . \$e->getMessage() . PHP_EOL;
}
"
