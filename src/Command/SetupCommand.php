<?php

namespace App\Command;

use App\Entity\Category;
use App\Entity\User;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\Console\Attribute\AsCommand;
use Symfony\Component\Console\Command\Command;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;
use Symfony\Component\Console\Style\SymfonyStyle;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

#[AsCommand(
    name: 'app:setup',
    description: 'Initial setup: creates database, schema, and default data',
)]
class SetupCommand extends Command
{
    public function __construct(
        private EntityManagerInterface $entityManager,
        private UserPasswordHasherInterface $passwordHasher
    ) {
        parent::__construct();
    }

    protected function execute(InputInterface $input, OutputInterface $output): int
    {
        $io = new SymfonyStyle($input, $output);

        // Create categories
        $categories = [
            'Gardening & Nature',
            'Cleaning & Recycling',
            'Event Equipment',
            'Others'
        ];

        $io->section('Creating default categories...');
        foreach ($categories as $name) {
            $existing = $this->entityManager->getRepository(Category::class)->findOneBy(['name' => $name]);
            if (!$existing) {
                $category = new Category();
                $category->setName($name);
                $this->entityManager->persist($category);
                $io->text("✓ Created category: $name");
            } else {
                $io->text("- Category already exists: $name");
            }
        }

        // Create admin user
        $io->section('Creating admin user...');
        $adminEmail = 'admin@example.com';
        $existing = $this->entityManager->getRepository(User::class)->findOneBy(['email' => $adminEmail]);
        
        if (!$existing) {
            $admin = new User();
            $admin->setEmail($adminEmail);
            $admin->setRoles(['ROLE_ADMIN', 'ROLE_USER']);
            $admin->setPassword($this->passwordHasher->hashPassword($admin, 'admin123'));
            $this->entityManager->persist($admin);
            $io->text("✓ Created admin user: $adminEmail (password: admin123)");
        } else {
            $io->text("- Admin user already exists");
        }

        $this->entityManager->flush();

        $io->success('Setup completed successfully!');
        $io->note('You can now login with admin@example.com / admin123');

        return Command::SUCCESS;
    }
}