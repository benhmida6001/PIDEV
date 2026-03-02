<?php

namespace App\Service;

use App\Entity\User;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

class ResetPasswordService
{
    private EntityManagerInterface $entityManager;
    private UrlGeneratorInterface $urlGenerator;
    private UserPasswordHasherInterface $passwordHasher;
    private EmailService $emailService;

    public function __construct(
        EntityManagerInterface $entityManager,
        UrlGeneratorInterface $urlGenerator,
        UserPasswordHasherInterface $passwordHasher,
        EmailService $emailService
    ) {
        $this->entityManager = $entityManager;
        $this->urlGenerator = $urlGenerator;
        $this->passwordHasher = $passwordHasher;
        $this->emailService = $emailService;
    }

    public function generateResetToken(User $user): string
    {
        // Générer un token sécurisé
        $token = bin2hex(random_bytes(32));
        
        // Définir l'expiration (1 heure)
        $expiresAt = new \DateTime('+1 hour');
        
        // Mettre à jour l'utilisateur
        $user->setResetToken($token);
        $user->setResetTokenExpiresAt($expiresAt);
        
        $this->entityManager->flush();
        
        return $token;
    }

    public function sendResetEmail(User $user): bool
    {
        try {
            $token = $this->generateResetToken($user);
            error_log('Token généré: ' . $token);
            
            $result = $this->emailService->sendPasswordResetEmail($user->getEmail(), $token);
            error_log('Résultat envoi email: ' . ($result ? 'succès' : 'échec'));
            
            return $result;
        } catch (\Exception $e) {
            error_log('Exception dans sendResetEmail: ' . $e->getMessage());
            return false;
        }
    }

    public function isResetTokenValid(string $token): ?User
    {
        $userRepository = $this->entityManager->getRepository(User::class);
        
        $user = $userRepository->findOneBy(['resetToken' => $token]);
        
        if (!$user) {
            return null;
        }
        
        // Vérifier si le token n'est pas expiré
        if ($user->getResetTokenExpiresAt() < new \DateTime()) {
            // Nettoyer le token expiré
            $user->setResetToken(null);
            $user->setResetTokenExpiresAt(null);
            $this->entityManager->flush();
            return null;
        }
        
        return $user;
    }

    public function resetPassword(User $user, string $newPassword): bool
    {
        try {
            // Hasher le nouveau mot de passe
            $hashedPassword = $this->passwordHasher->hashPassword($user, $newPassword);
            $user->setPassword($hashedPassword);
            
            // Nettoyer le token
            $user->setResetToken(null);
            $user->setResetTokenExpiresAt(null);
            
            $this->entityManager->flush();
            
            return true;
        } catch (\Exception $e) {
            return false;
        }
    }

    public function cleanupExpiredTokens(): int
    {
        $userRepository = $this->entityManager->getRepository(User::class);
        
        // Trouver tous les utilisateurs avec des tokens expirés
        $expiredTokens = $userRepository->createQueryBuilder('u')
            ->where('u.resetToken IS NOT NULL')
            ->andWhere('u.resetTokenExpiresAt < :now')
            ->setParameter('now', new \DateTime())
            ->getQuery()
            ->getResult();
        
        $count = 0;
        foreach ($expiredTokens as $user) {
            $user->setResetToken(null);
            $user->setResetTokenExpiresAt(null);
            $count++;
        }
        
        if ($count > 0) {
            $this->entityManager->flush();
        }
        
        return $count;
    }
}
