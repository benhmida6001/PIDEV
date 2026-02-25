<?php

namespace App\Command;

use App\Service\EmailService;
use Symfony\Component\Console\Attribute\AsCommand;
use Symfony\Component\Console\Command\Command;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;

#[AsCommand(
    name: 'app:test-email',
    description: 'Test email sending functionality'
)]
class TestEmailCommand extends Command
{
    private EmailService $emailService;

    public function __construct(EmailService $emailService)
    {
        $this->emailService = $emailService;
        parent::__construct();
    }

    protected function execute(InputInterface $input, OutputInterface $output): int
    {
        $output->writeln('<info>📧 Test d\'envoi d\'email...</info>');
        
        // Test d'envoi d'email de réinitialisation
        $testEmail = 'marie.dupont@example.com';
        $testToken = 'test-token-123';
        
        $success = $this->emailService->sendPasswordResetEmail($testEmail, $testToken);
        
        if ($success) {
            $output->writeln('<info>✅ Email envoyé avec succès à ' . $testEmail . '</info>');
            $output->writeln('<info>📧 Vérifiez votre boîte de réception</info>');
        } else {
            $output->writeln('<error>❌ Erreur lors de l\'envoi de l\'email</error>');
            $output->writeln('<error>🔧 Vérifiez la configuration MAILER_DSN dans .env</error>');
            
            // Afficher la configuration actuelle
            $output->writeln('<info>📋 Configuration actuelle:</info>');
            $output->writeln('MAILER_DSN: ' . ($_ENV['MAILER_DSN'] ?? 'Non défini'));
            $output->writeln('MAILER_FROM_EMAIL: ' . ($_ENV['MAILER_FROM_EMAIL'] ?? 'Non défini'));
        }
        
        return Command::SUCCESS;
    }
}
