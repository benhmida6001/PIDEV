<?php

namespace App\Command;

use App\Entity\User;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\Console\Attribute\AsCommand;
use Symfony\Component\Console\Command\Command;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

#[AsCommand(
    name: 'app:create-user',
    description: 'Creates a new user.'
)]
class CreateUserCommand extends Command
{
    public function __construct(
        private EntityManagerInterface $entityManager,
        private UserPasswordHasherInterface $passwordHasher
    ) {
        parent::__construct();
    }

    protected function execute(InputInterface $input, OutputInterface $output): int
    {
        // Chercher Marie Dupont
        $user = $this->entityManager->getRepository(User::class)->findOneBy(['email' => 'marie.dupont@example.com']);
        
        if (!$user) {
            $user = new User();
            $user->setEmail('marie.dupont@example.com');
            $user->setNom('DUPONT');
            $user->setPrenom('Marie');
            $user->setSexe('femme');
            $user->setAge(25);
            $user->setRoles(['ROLE_USER']);
        }

        // Hasher le mot de passe
        $hashedPassword = $this->passwordHasher->hashPassword($user, '123456');
        $user->setPassword($hashedPassword);

        // Sauvegarder
        $this->entityManager->persist($user);
        $this->entityManager->flush();

        $output->writeln('Utilisateur Marie Dupont mis à jour avec succès !');
        $output->writeln('Email: marie.dupont@example.com');
        $output->writeln('Mot de passe: 123456');

        return Command::SUCCESS;
    }
}
