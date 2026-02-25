<?php

namespace App\Service;

use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Email;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;
use Twig\Environment;

class EmailService
{
    private MailerInterface $mailer;
    private UrlGeneratorInterface $urlGenerator;
    private Environment $twig;

    public function __construct(
        MailerInterface $mailer,
        UrlGeneratorInterface $urlGenerator,
        Environment $twig
    ) {
        $this->mailer = $mailer;
        $this->urlGenerator = $urlGenerator;
        $this->twig = $twig;
    }

    public function sendPasswordResetEmail(string $to, string $resetToken): bool
    {
        $resetUrl = $this->urlGenerator->generate('app_reset_password', ['token' => $resetToken], UrlGeneratorInterface::ABSOLUTE_URL);
        
        $fromEmail = $_ENV['MAILER_FROM_EMAIL'] ?? 'admin@greencore.com';
        $fromPassword = $_ENV['MAILER_FROM_PASSWORD'] ?? '';
        
        $email = (new Email())
            ->from($fromEmail)
            ->to($to)
            ->subject('🔐 Réinitialisation de votre mot de passe - GREENCORE')
            ->html($this->twig->render('emails/password_reset.html.twig', [
                'resetUrl' => $resetUrl,
                'userEmail' => $to
            ]));

        try {
            $this->mailer->send($email);
            return true;
        } catch (\Exception $e) {
            return false;
        }
    }

    public function sendRoleChangeEmail(string $to, string $newRole): bool
    {
        $fromEmail = $_ENV['MAILER_FROM_EMAIL'] ?? 'admin@greencore.com';
        
        $email = (new Email())
            ->from($fromEmail)
            ->to($to)
            ->subject('🛡️ Changement de vos permissions - GREENCORE')
            ->html($this->twig->render('emails/role_change.html.twig', [
                'newRole' => $newRole,
                'userEmail' => $to
            ]));

        try {
            $this->mailer->send($email);
            return true;
        } catch (\Exception $e) {
            return false;
        }
    }

    public function sendWelcomeEmail(string $to, string $password = null): bool
    {
        $fromEmail = $_ENV['MAILER_FROM_EMAIL'] ?? 'admin@greencore.com';
        
        $email = (new Email())
            ->from($fromEmail)
            ->to($to)
            ->subject('🎉 Bienvenue sur GREENCORE')
            ->html($this->twig->render('emails/welcome.html.twig', [
                'userEmail' => $to,
                'tempPassword' => $password
            ]));

        try {
            $this->mailer->send($email);
            return true;
        } catch (\Exception $e) {
            return false;
        }
    }
}
