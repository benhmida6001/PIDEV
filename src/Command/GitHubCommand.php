<?php

namespace App\Command;

use Symfony\Component\Console\Attribute\AsCommand;
use Symfony\Component\Console\Command\Command;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;
use Symfony\Component\Console\Input\InputArgument;
use Symfony\Component\Console\Input\InputOption;

#[AsCommand(
    name: 'app:git',
    description: 'Commandes Git pour le projet GREENCORE'
)]
class GitHubCommand extends Command
{
    protected function configure(): void
    {
        $this
            ->addArgument('action', InputArgument::REQUIRED, 'Action à exécuter (init, status, add, commit, push)')
            ->addOption('message', 'm', InputOption::VALUE_OPTIONAL, 'Message pour le commit', 'Mise à jour du projet')
            ->setHelp('Cette commande vous aide à gérer Git et GitHub pour votre projet GREENCORE');
    }

    protected function execute(InputInterface $input, OutputInterface $output): int
    {
        $action = $input->getArgument('action');
        $message = $input->getOption('message');

        switch ($action) {
            case 'init':
                return $this->initGit($output);
            case 'status':
                return $this->gitStatus($output);
            case 'add':
                return $this->gitAdd($output);
            case 'commit':
                return $this->gitCommit($output, $message);
            case 'push':
                return $this->gitPush($output);
            default:
                $output->writeln('<error>Action non reconnue. Actions disponibles: init, status, add, commit, push</error>');
                return Command::FAILURE;
        }
    }

    private function initGit(OutputInterface $output): int
    {
        $output->writeln('<info>Initialisation du dépôt Git...</info>');
        
        $commands = [
            'git init',
            'git add .',
            'git commit -m "Initial commit - Projet GREENCORE"',
            'git branch -M main'
        ];

        foreach ($commands as $cmd) {
            $output->writeln("<comment>Exécution: $cmd</comment>");
            shell_exec($cmd);
        }

        $output->writeln('<success>Dépôt Git initialisé avec succès !</success>');
        $output->writeln('<info>Prochaines étapes:</info>');
        $output->writeln('1. Créez un dépôt sur GitHub');
        $output->writeln('2. Copiez l\'URL du dépôt');
        $output->writeln('3. Exécutez: git remote add origin <URL>');
        $output->writeln('4. Exécutez: php bin/console app:github push');

        return Command::SUCCESS;
    }

    private function gitStatus(OutputInterface $output): int
    {
        $output->writeln('<info>Statut du dépôt Git:</info>');
        $status = shell_exec('git status 2>&1');
        $output->writeln($status);

        return Command::SUCCESS;
    }

    private function gitAdd(OutputInterface $output): int
    {
        $output->writeln('<info>Ajout des fichiers au dépôt...</info>');
        $result = shell_exec('git add . 2>&1');
        if ($result) {
            $output->writeln($result);
        }
        $output->writeln('<success>Fichiers ajoutés avec succès !</success>');

        return Command::SUCCESS;
    }

    private function gitCommit(OutputInterface $output, string $message): int
    {
        $output->writeln("<info>Création du commit: $message</info>");
        $result = shell_exec("git commit -m \"$message\" 2>&1");
        $output->writeln($result);
        $output->writeln('<success>Commit créé avec succès !</success>');

        return Command::SUCCESS;
    }

    private function gitPush(OutputInterface $output): int
    {
        $output->writeln('<info>Envoi vers GitHub...</info>');
        
        // Vérifier si remote existe
        $remotes = shell_exec('git remote -v 2>&1');
        if (strpos($remotes, 'origin') === false) {
            $output->writeln('<error>Aucun remote "origin" trouvé. Veuillez d\'abord configurer le remote:</error>');
            $output->writeln('<comment>git remote add origin <URL_DU_DEPOT_GITHUB></comment>');
            return Command::FAILURE;
        }

        $result = shell_exec('git push origin main 2>&1');
        $output->writeln($result);
        $output->writeln('<success>Code envoyé vers GitHub avec succès !</success>');

        return Command::SUCCESS;
    }
}
