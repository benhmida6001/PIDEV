<?php

namespace App\Tests\Unit\Service;

use App\Service\EmailService;
use PHPUnit\Framework\TestCase;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Email;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;
use Twig\Environment;

class EmailServiceTest extends TestCase
{
    private EmailService $emailService;
    private MailerInterface $mailer;
    private Environment $twig;
    private UrlGeneratorInterface $urlGenerator;

    protected function setUp(): void
    {
        $this->mailer = $this->createMock(MailerInterface::class);
        $this->twig = $this->createMock(Environment::class);
        $this->urlGenerator = $this->createMock(UrlGeneratorInterface::class);
        
        $this->emailService = new EmailService($this->mailer, $this->urlGenerator, $this->twig);
    }

    public function testSendWelcomeEmail(): void
    {
        $to = 'test@example.com';
        $password = 'temp_password_123';
        
        // Mock du template Twig
        $this->twig->expects($this->once())
            ->method('render')
            ->with('emails/welcome.html.twig', [
                'userEmail' => $to,
                'tempPassword' => $password
            ])
            ->willReturn('<html><body>Welcome email content</body></html>');
        
        // Mock du mailer
        $this->mailer->expects($this->once())
            ->method('send')
            ->with($this->callback(function (Email $email) use ($to) {
                return $email->getTo()[0]->getAddress() === $to &&
                       str_contains($email->getSubject(), 'Bienvenue') &&
                       str_contains($email->getHtmlBody(), 'Welcome email content');
            }));
        
        $result = $this->emailService->sendWelcomeEmail($to, $password);
        
        $this->assertTrue($result);
    }

    public function testSendPasswordResetEmail(): void
    {
        $to = 'test@example.com';
        $resetToken = 'reset_token_123';
        
        // Mock du template Twig
        $this->twig->expects($this->once())
            ->method('render')
            ->with('emails/password_reset.html.twig', [
                'resetUrl' => '/reset-password/' . $resetToken,
                'userEmail' => $to
            ])
            ->willReturn('<html><body>Reset password email</body></html>');
        
        // Mock du router
        $this->urlGenerator->expects($this->once())
            ->method('generate')
            ->with('app_reset_password', ['token' => $resetToken])
            ->willReturn('/reset-password/' . $resetToken);
        
        // Mock du mailer
        $this->mailer->expects($this->once())
            ->method('send')
            ->with($this->callback(function (Email $email) use ($to, $resetToken) {
                return $email->getTo()[0]->getAddress() === $to &&
                       str_contains($email->getSubject(), 'Réinitialisation') &&
                       str_contains($email->getHtmlBody(), 'Reset password email');
            }));
        
        $result = $this->emailService->sendPasswordResetEmail($to, $resetToken);
        
        $this->assertTrue($result);
    }

    public function testSendRoleChangeEmail(): void
    {
        $to = 'test@example.com';
        $newRole = 'ROLE_ADMIN';
        
        // Mock du template Twig
        $this->twig->expects($this->once())
            ->method('render')
            ->with('emails/role_change.html.twig', [
                'newRole' => $newRole,
                'userEmail' => $to
            ])
            ->willReturn('<html><body>Role change email</body></html>');
        
        // Mock du mailer
        $this->mailer->expects($this->once())
            ->method('send')
            ->with($this->callback(function (Email $email) use ($to, $newRole) {
                return $email->getTo()[0]->getAddress() === $to &&
                       str_contains($email->getSubject(), 'Changement') &&
                       str_contains($email->getHtmlBody(), 'Role change email');
            }));
        
        $result = $this->emailService->sendRoleChangeEmail($to, $newRole);
        
        $this->assertTrue($result);
    }

    public function testSendEmailWithInvalidData(): void
    {
        // Test avec un email invalide
        $result = $this->emailService->sendWelcomeEmail('', 'password123');
        $this->assertFalse($result);
        
        $result = $this->emailService->sendWelcomeEmail('invalid-email', 'password123');
        $this->assertFalse($result);
    }

    public function testSendEmailThrowsException(): void
    {
        $to = 'test@example.com';
        $password = 'password123';
        
        // Mock du template qui lance une exception
        $this->twig->expects($this->once())
            ->method('render')
            ->willThrowException(new \Exception('Template error'));
        
        $result = $this->emailService->sendWelcomeEmail($to, $password);
        
        $this->assertFalse($result);
    }

    public function testMailerSendFailure(): void
    {
        $to = 'test@example.com';
        $password = 'password123';
        
        // Mock du template Twig
        $this->twig->expects($this->once())
            ->method('render')
            ->willReturn('<html><body>Welcome email content</body></html>');
        
        // Mock du mailer qui lance une exception
        $this->mailer->expects($this->once())
            ->method('send')
            ->willThrowException(new \Exception('Mail send failed'));
        
        $result = $this->emailService->sendWelcomeEmail($to, $password);
        
        $this->assertFalse($result);
    }

    public function testEmailContentValidation(): void
    {
        $to = 'test@example.com';
        $password = 'password123';
        
        // Mock du template Twig avec contenu vide
        $this->twig->expects($this->once())
            ->method('render')
            ->willReturn('');
        
        $result = $this->emailService->sendWelcomeEmail($to, $password);
        
        $this->assertTrue($result); // Le service ne valide pas le contenu du template
    }
}
