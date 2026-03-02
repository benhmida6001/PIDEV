<?php

/**
 * Configuration PHPStan pour le projet Symfony PIDEV
 * 
 * Ce fichier configure l'analyse statique du code avec PHPStan
 * pour garantir la qualité et la robustesse du code.
 */

// Configuration de base
$config = [
    'level' => 6, // Niveau d'analyse (0-9)
    'paths' => [
        'src',
        'tests'
    ],
    'excludePaths' => [
        'src/Kernel.php',
        'src/Entity/*',
        'src/Migrations/*',
        'src/DataFixtures/*'
    ],
    'checkGenericClassInNonGenericObjectType' => false,
    'checkMissingIterableValueType' => false,
    'ignoreErrors' => [
        '#Call to an undefined method.*#',
        '#Access to an undefined property.*#',
        '#Undefined variable.*#'
    ],
    'bootstrapFiles' => [
        'vendor/autoload.php'
    ],
    'memoryLimit' => '1G'
];

// Export de la configuration
return $config;
