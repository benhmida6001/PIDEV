<?php

namespace App\Controller;

use App\Entity\User;
use App\Form\ProfileFormType;
use App\Form\ChangePasswordFormType;
use App\Repository\UserRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\String\Slugger\SluggerInterface;
use Symfony\Component\DependencyInjection\ParameterBag\ParameterBagInterface;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;
use Symfony\Component\Routing\Attribute\Route;
use Doctrine\ORM\EntityManagerInterface;

final class ProfileController extends AbstractController
{
    #[Route('/profile', name: 'app_profile')]
    public function index(): Response
    {
        $this->denyAccessUnlessGranted('IS_AUTHENTICATED_FULLY');

        // Récupérer le thème depuis la session
        $session = $this->container->get('request_stack')->getSession();
        $current_theme = $session->get('theme', 'default');

        return $this->render('profile/index.html.twig', [
            'user' => $this->getUser(),
            'current_theme' => $current_theme,
        ]);
    }

    #[Route('/profile/edit', name: 'app_profile_edit')]
    public function edit(Request $request, EntityManagerInterface $entityManager, SluggerInterface $slugger, ParameterBagInterface $params): Response
    {
        $this->denyAccessUnlessGranted('IS_AUTHENTICATED_FULLY');
        
        // Récupérer le thème depuis la session
        $session = $this->container->get('request_stack')->getSession();
        $current_theme = $session->get('theme', 'default');
        
        $user = $this->getUser();
        $form = $this->createForm(ProfileFormType::class, $user);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Vérifier si le nouvel email n'est pas déjà utilisé par un autre utilisateur
            /** @var User $user */
            $user = $this->getUser();
            $existingUser = $entityManager->getRepository(User::class)->findOneBy(['email' => $user->getEmail()]);
            
            if ($existingUser && $existingUser->getId() !== $user->getId()) {
                $this->addFlash('error', 'Cet email est déjà utilisé par un autre compte.');
                return $this->render('profile/edit.html.twig', [
                    'profileForm' => $form->createView(),
                    'current_theme' => $current_theme,
                ]);
            }

            // Gérer l'upload de la photo de profil
            $profilePictureFile = $form->get('profilePicture')->getData();
            
            if ($profilePictureFile) {
                $originalFilename = pathinfo($profilePictureFile->getClientOriginalName(), PATHINFO_FILENAME);
                $safeFilename = $slugger->slug($originalFilename);
                $newFilename = $safeFilename.'-'.uniqid().'.'.$profilePictureFile->guessExtension();
                
                try {
                    $profilePictureFile->move(
                        $params->get('kernel.project_dir').'/public/uploads/profile_pictures',
                        $newFilename
                    );
                    
                    // Supprimer l'ancienne photo si elle existe
                    if ($user->getProfilePicture()) {
                        $oldFile = $params->get('kernel.project_dir').'/public/uploads/profile_pictures/'.$user->getProfilePicture();
                        if (file_exists($oldFile)) {
                            unlink($oldFile);
                        }
                    }
                    
                    $user->setProfilePicture($newFilename);
                } catch (FileException $e) {
                    $this->addFlash('error', 'Erreur lors de l\'upload de la photo: '.$e->getMessage());
                    return $this->render('profile/edit.html.twig', [
                        'profileForm' => $form->createView(),
                        'current_theme' => $current_theme,
                    ]);
                }
            }

            $entityManager->flush();
            $this->addFlash('success', 'Votre profil a été mis à jour avec succès !');
            return $this->redirectToRoute('app_profile');
        }

        return $this->render('profile/edit.html.twig', [
            'profileForm' => $form->createView(),
            'current_theme' => $current_theme,
        ]);
    }

    #[Route('/profile/change-password', name: 'app_profile_change_password')]
    public function changePassword(Request $request, UserPasswordHasherInterface $userPasswordHasher, EntityManagerInterface $entityManager): Response
    {
        $this->denyAccessUnlessGranted('IS_AUTHENTICATED_FULLY');
        
        // Récupérer le thème depuis la session
        $session = $this->container->get('request_stack')->getSession();
        $current_theme = $session->get('theme', 'default');
        
        $user = $this->getUser();
        /** @var User $user */
        $form = $this->createForm(ChangePasswordFormType::class);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $currentPassword = $form->get('currentPassword')->getData();
            $newPassword = $form->get('newPassword')->get('first')->getData();

            // Vérifier le mot de passe actuel
            if (!$userPasswordHasher->isPasswordValid($user, $currentPassword)) {
                $this->addFlash('error', 'Le mot de passe actuel est incorrect.');
                return $this->render('profile/change_password.html.twig', [
                    'changePasswordForm' => $form->createView(),
                    'current_theme' => $current_theme,
                ]);
            }

            // Hasher et mettre à jour le nouveau mot de passe
            $hashedPassword = $userPasswordHasher->hashPassword($user, $newPassword);
            $user->setPassword($hashedPassword);

            $entityManager->flush();
            $this->addFlash('success', 'Votre mot de passe a été changé avec succès !');

            return $this->redirectToRoute('app_profile');
        }

        return $this->render('profile/change_password.html.twig', [
            'changePasswordForm' => $form->createView(),
            'current_theme' => $current_theme,
        ]);
    }
}
