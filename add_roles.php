<?php

require_once 'vendor/autoload.php';

use App\Entity\User;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\DependencyInjection\ContainerBuilder;

// Charger le conteneur Symfony
$container = new ContainerBuilder();
$loader = new Symfony\Component\DependencyInjection\Loader\YamlFileLoader($container, new Symfony\Component\Config\FileLocator(__DIR__.'/config'));
$loader->load('services.yaml');

// Obtenir l'EntityManager
$entityManager = $container->get(EntityManagerInterface::class);

// Récupérer tous les utilisateurs
$users = $entityManager->getRepository(User::class)->findAll();

foreach ($users as $user) {
    echo "Utilisateur ID: {$user->getId()}, Email: {$user->getEmail()}\n";
    echo "Rôles actuels: " . json_encode($user->getRoles()) . "\n";
    
    // Ajouter ROLE_ADMIN au premier utilisateur
    if ($user->getId() === 1) {
        $user->setRoles(['ROLE_ADMIN']);
        $entityManager->flush();
        echo "ROLE_ADMIN ajouté à l'utilisateur {$user->getEmail()}\n";
    }
    
    echo "---\n";
}

echo "Rôles mis à jour avec succès !\n";
