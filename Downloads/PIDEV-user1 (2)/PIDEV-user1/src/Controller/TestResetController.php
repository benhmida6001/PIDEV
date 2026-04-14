<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Doctrine\ORM\EntityManagerInterface;
use App\Entity\User;

class TestResetController extends AbstractController
{
    #[Route('/test-reset-token', name: 'app_test_reset_token')]
    public function testResetToken(EntityManagerInterface $entityManager): Response
    {
        $userRepository = $entityManager->getRepository(User::class);
        $user = $userRepository->findOneBy(['email' => 'marie.dupont@example.com']);
        
        if (!$user) {
            return new Response('Utilisateur marie.dupont@example.com non trouvé. Créons-le...');
        }
        
        // Test des méthodes de reset token
        try {
            $resetToken = bin2hex(random_bytes(16));
            $expiresAt = new \DateTime('+1 hour');
            
            $user->setResetToken($resetToken);
            $user->setResetTokenExpiresAt($expiresAt);
            $entityManager->flush();
            
            $token = $user->getResetToken();
            $expires = $user->getResetTokenExpiresAt();
            
            return new Response("
                <h1>✅ Test Reset Token - SUCCÈS</h1>
                <p><strong>Email:</strong> {$user->getEmail()}</p>
                <p><strong>Token:</strong> {$token}</p>
                <p><strong>Expires:</strong> {$expires->format('d/m/Y H:i')}</p>
                <p><strong>Rôles:</strong> " . implode(', ', $user->getRoles()) . "</p>
                <hr>
                <h3>🔗 Liens de test:</h3>
                <p><a href='/reset-password/{$token}' target='_blank'>🔐 Tester la réinitialisation</a></p>
                <p><a href='/reset-password' target='_blank'>📧 Page de demande de reset</a></p>
                <p><a href='/admin/users/roles' target='_blank'>👥 Gestion des rôles</a></p>
                <hr>
                <p><em>Les méthodes setResetToken() et getResetToken() fonctionnent parfaitement !</em></p>
            ");
            
        } catch (\Exception $e) {
            return new Response("❌ ERREUR: " . $e->getMessage());
        }
    }
}
