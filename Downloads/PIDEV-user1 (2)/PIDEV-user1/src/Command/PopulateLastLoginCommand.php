<?php

namespace App\Command;

use Doctrine\ORM\EntityManagerInterface;
use App\Entity\User;
use Symfony\Component\Console\Attribute\AsCommand;
use Symfony\Component\Console\Command\Command;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;

#[AsCommand(
    name: 'app:populate-last-login',
    description: 'Populate last login dates for existing users'
)]
class PopulateLastLoginCommand extends Command
{
    private EntityManagerInterface $entityManager;

    public function __construct(EntityManagerInterface $entityManager)
    {
        $this->entityManager = $entityManager;
        parent::__construct();
    }

    protected function execute(InputInterface $input, OutputInterface $output): int
    {
        $userRepository = $this->entityManager->getRepository(User::class);
        $users = $userRepository->findAll();
        
        $updatedCount = 0;
        $now = new \DateTime();
        
        foreach ($users as $user) {
            if ($user->getLastLogin() === null) {
                // Générer une date de connexion aléatoire dans les 30 derniers jours
                $daysAgo = rand(1, 30);
                $lastLogin = (clone $now)->modify("-{$daysAgo} days");
                
                $user->setLastLogin($lastLogin);
                $updatedCount++;
            }
        }
        
        $this->entityManager->flush();
        
        $output->writeln("<info>✅ Updated {$updatedCount} users with last login dates</info>");
        $output->writeln("<info>📊 Users now have realistic activity data</info>");
        
        return Command::SUCCESS;
    }
}
