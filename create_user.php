<?php

require_once 'vendor/autoload.php';

use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;
use App\Entity\User;

// Configuration Symfony
$kernel = new \App\Kernel('dev', true);
$kernel->boot();

$container = $kernel->getContainer();
$entityManager = $container->get('doctrine.orm.entity_manager');
$passwordHasher = $container->get('security.password_hasher');

// Créer ou trouver Marie Dupont
$user = $entityManager->getRepository(User::class)->findOneBy(['email' => 'marie.dupont@example.com']);

if (!$user) {
    $user = new User();
    $user->setEmail('marie.dupont@example.com');
    $user->setNom('DUPONT');
    $user->setPrenom('Marie');
    $user->setSexe('femme');
    $user->setAge(25);
    $user->setRoles(['ROLE_USER']);
}

// Hasher le mot de passe "123456"
$hashedPassword = $passwordHasher->hashPassword($user, '123456');
$user->setPassword($hashedPassword);

// Sauvegarder
$entityManager->persist($user);
$entityManager->flush();

echo "Utilisateur Marie Dupont créé/mis à jour avec succès !\n";
echo "Email: marie.dupont@example.com\n";
echo "Mot de passe: 123456\n";

?>
