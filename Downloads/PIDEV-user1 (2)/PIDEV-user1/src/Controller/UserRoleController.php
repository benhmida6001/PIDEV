<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Doctrine\ORM\EntityManagerInterface;
use App\Entity\User;
use App\Service\EmailService;

class UserRoleController extends AbstractController
{
    private EmailService $emailService;

    public function __construct(EmailService $emailService)
    {
        $this->emailService = $emailService;
    }

    #[Route('/admin/users/roles', name: 'app_admin_users_roles')]
    public function index(EntityManagerInterface $entityManager): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');

        $userRepository = $entityManager->getRepository(User::class);
        $users = $userRepository->findAll();

        return $this->render('admin/user_roles.html.twig', [
            'users' => $users
        ]);
    }

    #[Route('/admin/user/{id}/promote', name: 'app_user_promote', requirements: ['id' => '\d+'])]
    public function promote(int $id, EntityManagerInterface $entityManager): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');

        $userRepository = $entityManager->getRepository(User::class);
        $user = $userRepository->find($id);

        if (!$user) {
            $this->addFlash('error', 'Utilisateur non trouvé');
            return $this->redirectToRoute('app_admin_users_roles');
        }

        // Ajouter le rôle ROLE_ADMIN
        $roles = $user->getRoles();
        if (!in_array('ROLE_ADMIN', $roles)) {
            $roles[] = 'ROLE_ADMIN';
            $user->setRoles($roles);
            $entityManager->flush();
            
            // Envoyer email de notification
            $this->emailService->sendRoleChangeEmail($user->getEmail(), 'Admin');
            
            $this->addFlash('success', 'Utilisateur promu administrateur avec succès');
        } else {
            $this->addFlash('info', 'L\'utilisateur est déjà administrateur');
        }

        return $this->redirectToRoute('app_admin_users_roles');
    }

    #[Route('/admin/user/{id}/demote', name: 'app_user_demote', requirements: ['id' => '\d+'])]
    public function demote(int $id, EntityManagerInterface $entityManager): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');

        $userRepository = $entityManager->getRepository(User::class);
        $user = $userRepository->find($id);

        if (!$user) {
            $this->addFlash('error', 'Utilisateur non trouvé');
            return $this->redirectToRoute('app_admin_users_roles');
        }

        // Retirer le rôle ROLE_ADMIN
        $roles = $user->getRoles();
        if (in_array('ROLE_ADMIN', $roles)) {
            $roles = array_filter($roles, function($role) {
                return $role !== 'ROLE_ADMIN';
            });
            $user->setRoles(array_values($roles));
            $entityManager->flush();
            
            // Envoyer email de notification
            $this->emailService->sendRoleChangeEmail($user->getEmail(), 'User');
            
            $this->addFlash('success', 'Utilisateur rétrogradé avec succès');
        } else {
            $this->addFlash('info', 'L\'utilisateur n\'est pas administrateur');
        }

        return $this->redirectToRoute('app_admin_users_roles');
    }

    #[Route('/admin/user/{id}/send-reset', name: 'app_user_send_reset', requirements: ['id' => '\d+'])]
    public function sendPasswordReset(int $id, EntityManagerInterface $entityManager): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');

        $userRepository = $entityManager->getRepository(User::class);
        $user = $userRepository->find($id);

        if (!$user) {
            $this->addFlash('error', 'Utilisateur non trouvé');
            return $this->redirectToRoute('app_admin_users_roles');
        }

        // Générer un token de réinitialisation
        $resetToken = bin2hex(random_bytes(32));
        $user->setResetToken($resetToken);
        $user->setResetTokenExpiresAt(new \DateTime('+1 hour'));
        $entityManager->flush();

        // Envoyer email de réinitialisation
        if ($this->emailService->sendPasswordResetEmail($user->getEmail(), $resetToken)) {
            $this->addFlash('success', 'Email de réinitialisation envoyé avec succès');
        } else {
            $this->addFlash('error', 'Erreur lors de l\'envoi de l\'email');
        }

        return $this->redirectToRoute('app_admin_users_roles');
    }
}
