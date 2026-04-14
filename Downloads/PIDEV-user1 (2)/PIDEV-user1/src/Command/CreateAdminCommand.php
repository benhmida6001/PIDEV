<?php

namespace App\Command;

use App\Entity\User;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\Console\Attribute\AsCommand;
use Symfony\Component\Console\Command\Command;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;
use Symfony\Component\Console\Style\SymfonyStyle;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

#[AsCommand(
    name: 'app:create-admin',
    description: 'Crée un utilisateur administrateur',
)]
class CreateAdminCommand extends Command
{
    public function __construct(
        private EntityManagerInterface $entityManager,
        private UserPasswordHasherInterface $passwordHasher
    ) {
        parent::__construct();
    }

    protected function configure(): void
    {
        $this
            ->setHelp('Cette commande permet de créer un utilisateur administrateur par défaut');
    }

    protected function execute(InputInterface $input, OutputInterface $output): int
    {
        $io = new SymfonyStyle($input, $output);

        // Check if admin already exists
        $userRepository = $this->entityManager->getRepository(User::class);
        $existingAdmin = $userRepository->findOneBy(['email' => 'admin@greencore.com']);

        if ($existingAdmin) {
            $io->warning('Un administrateur avec l\'email admin@greencore.com existe déjà.');
            return Command::SUCCESS;
        }

        // Create admin user
        $admin = new User();
        $admin->setEmail('admin@greencore.com');
        $admin->setRoles(['ROLE_ADMIN']);
        
        // Set password
        $plainPassword = 'admin123';
        $hashedPassword = $this->passwordHasher->hashPassword($admin, $plainPassword);
        $admin->setPassword($hashedPassword);
        
        $this->entityManager->persist($admin);
        $this->entityManager->flush();
        
        $io->success('Administrateur créé avec succès!');
        $io->info('Email: admin@greencore.com');
        $io->info('Mot de passe: admin123');
        $io->warning('N\'oubliez pas de changer le mot de passe après la première connexion!');

        return Command::SUCCESS;
    }
}
