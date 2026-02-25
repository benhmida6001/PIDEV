<?php

namespace App\Controller;

use App\Entity\User;
use App\Form\ResetPasswordRequestFormType;
use App\Form\ResetPasswordFormType;
use App\Service\ResetPasswordService;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Http\Authentication\AuthenticationUtils;

class ResetPasswordController extends AbstractController
{
    private ResetPasswordService $resetPasswordService;
    private EntityManagerInterface $entityManager;

    public function __construct(
        ResetPasswordService $resetPasswordService,
        EntityManagerInterface $entityManager
    ) {
        $this->resetPasswordService = $resetPasswordService;
        $this->entityManager = $entityManager;
    }

    #[Route('/reset-password', name: 'app_reset_password_request')]
    public function request(Request $request): Response
    {
        $form = $this->createForm(ResetPasswordRequestFormType::class);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $email = $form->get('email')->getData();
            $userRepository = $this->entityManager->getRepository(User::class);
            $user = $userRepository->findOneBy(['email' => $email]);

            if ($user) {
                $success = $this->resetPasswordService->sendResetEmail($user);
                
                if ($success) {
                    $this->addFlash('success', '📧 Un email de réinitialisation a été envoyé à votre adresse email.');
                } else {
                    $this->addFlash('error', '❌ Une erreur est survenue lors de l\'envoi de l\'email.');
                }
            } else {
                $this->addFlash('info', 'ℹ️ Si cette adresse email existe dans Notre système, vous recevrez un email de réinitialisation.');
            }

            return $this->redirectToRoute('app_reset_password_request');
        }

        return $this->render('reset_password/request.html.twig', [
            'requestForm' => $form->createView(),
        ]);
    }

    #[Route('/reset-password/{token}', name: 'app_reset_password')]
    public function reset(string $token, Request $request): Response
    {
        $user = $this->resetPasswordService->isResetTokenValid($token);
        
        if (!$user) {
            $this->addFlash('error', '❌ Lien de réinitialisation invalide ou expiré.');
            return $this->redirectToRoute('app_reset_password_request');
        }

        $form = $this->createForm(ResetPasswordFormType::class);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $newPassword = $form->get('plainPassword')->getData();
            
            if ($this->resetPasswordService->resetPassword($user, $newPassword)) {
                $this->addFlash('success', '✅ Votre mot de passe a été réinitialisé avec succès. Vous pouvez maintenant vous connecter.');
                return $this->redirectToRoute('app_login');
            } else {
                $this->addFlash('error', '❌ Une erreur est survenue lors de la réinitialisation du mot de passe.');
            }
        }

        return $this->render('reset_password/reset.html.twig', [
            'resetForm' => $form->createView(),
            'token' => $token,
            'userEmail' => $user->getEmail(),
        ]);
    }
}
