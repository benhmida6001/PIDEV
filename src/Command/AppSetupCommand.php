<?php

namespace App\Command;

use Symfony\Component\Console\Attribute\AsCommand;
use Symfony\Component\Console\Command\Command;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;
use Symfony\Component\Console\Style\SymfonyStyle;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\DependencyInjection\ParameterBag\ParameterBagInterface;

#[AsCommand(
    name: 'app:setup',
    description: 'Initialise le projet (crée les dossiers, vérifie la base de données, etc.)',
)]
class AppSetupCommand extends Command
{
    public function __construct(
        private EntityManagerInterface $entityManager,
        private ParameterBagInterface $params
    ) {
        parent::__construct();
    }

    protected function execute(InputInterface $input, OutputInterface $output): int
    {
        $io = new SymfonyStyle($input, $output);

        $io->title('🚀 Initialisation du projet GREENCORE');

        // 1. Vérifier/créer les dossiers nécessaires
        $io->section('📁 Vérification des dossiers');
        
        $requiredDirs = [
            'public/uploads/profile_pictures',
            'public/uploads/documents',
            'var/log',
        ];

        foreach ($requiredDirs as $dir) {
            $fullPath = $this->params->get('kernel.project_dir') . '/' . $dir;
            if (!is_dir($fullPath)) {
                mkdir($fullPath, 0755, true);
                $io->writeln("✅ Créé: $dir");
            } else {
                $io->writeln("✅ Existe déjà: $dir");
            }
        }

        // 2. Vérifier la base de données
        $io->section('🗄️ Vérification de la base de données');
        
        try {
            $connection = $this->entityManager->getConnection();
            $connection->connect();
            $io->writeln('✅ Connexion à la base de données réussie');
        } catch (\Exception $e) {
            $io->error('❌ Erreur de connexion à la base de données: ' . $e->getMessage());
            return Command::FAILURE;
        }

        // 3. Vérifier les migrations
        $io->section('🔄 Vérification des migrations');
        
        try {
            $sql = "SELECT COUNT(*) as count FROM doctrine_migration_versions";
            $result = $connection->executeQuery($sql)->fetchAssociative();
            $migrationCount = $result['count'];
            
            if ($migrationCount > 0) {
                $io->writeln("✅ $migrationCount migration(s) appliquée(s)");
            } else {
                $io->warning('⚠️ Aucune migration appliquée. Exécutez: php bin/console doctrine:migrations:migrate');
            }
        } catch (\Exception $e) {
            $io->warning('⚠️ Impossible de vérifier les migrations: ' . $e->getMessage());
        }

        // 4. Vérifier les utilisateurs
        $io->section('👥 Vérification des utilisateurs');
        
        try {
            $userCount = $this->entityManager->getRepository('App\Entity\User')->count([]);
            
            // Compter les administrateurs correctement
            $allUsers = $this->entityManager->getRepository('App\Entity\User')->findAll();
            $adminCount = 0;
            foreach ($allUsers as $user) {
                if (in_array('ROLE_ADMIN', $user->getRoles())) {
                    $adminCount++;
                }
            }
            
            $io->writeln("✅ $userCount utilisateur(s) dans la base de données");
            $io->writeln("✅ $adminCount administrateur(s)");
            
            if ($userCount === 0) {
                $io->warning('⚠️ Aucun utilisateur. Exécutez: php bin/console app:create-admin');
            } elseif ($adminCount === 0) {
                $io->warning('⚠️ Aucun administrateur. Exécutez: php bin/console app:create-admin');
            }
        } catch (\Exception $e) {
            $io->warning('⚠️ Impossible de vérifier les utilisateurs: ' . $e->getMessage());
        }

        // 5. Résumé
        $io->section('📋 Résumé');
        $io->success([
            '✅ Initialisation terminée',
            '📁 Dossiers créés/vérifiés',
            '🗄️ Base de données connectée',
            '👥 Utilisateurs vérifiés',
            '',
            'Prochaines étapes recommandées:',
            '• php bin/console doctrine:migrations:migrate (si nécessaire)',
            '• php bin/console app:create-admin (si aucun admin)',
            '• php bin/console server:start'
        ]);

        return Command::SUCCESS;
    }
}
