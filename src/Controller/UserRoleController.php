<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;
use Doctrine\ORM\EntityManagerInterface;
use App\Entity\User;
use App\Service\EmailService;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\RepeatedType;

class UserRoleController extends AbstractController
{
    private EmailService $emailService;

    public function __construct(EmailService $emailService)
    {
        $this->emailService = $emailService;
    }

    #[Route('/admin/user/create', name: 'app_admin_user_create')]
    public function createUser(Request $request, EntityManagerInterface $entityManager): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');

        $user = new User();
        
        $form = $this->createFormBuilder($user)
            ->add('email', EmailType::class, [
                'label' => 'Email',
                'attr' => ['class' => 'form-control']
            ])
            ->add('roles', ChoiceType::class, [
                'label' => 'Rôles',
                'choices' => [
                    'Utilisateur' => 'ROLE_USER',
                    'Administrateur' => 'ROLE_ADMIN'
                ],
                'multiple' => true,
                'expanded' => true,
                'attr' => ['class' => 'form-check']
            ])
            ->add('password', RepeatedType::class, [
                'type' => PasswordType::class,
                'first_options' => [
                    'label' => 'Mot de passe',
                    'attr' => ['class' => 'form-control']
                ],
                'second_options' => [
                    'label' => 'Confirmer le mot de passe',
                    'attr' => ['class' => 'form-control']
                ],
                'invalid_message' => 'Les mots de passe doivent correspondre.',
                'required' => true
            ])
            ->add('submit', SubmitType::class, [
                'label' => 'Créer l\'utilisateur',
                'attr' => ['class' => 'btn btn-success']
            ])
            ->getForm();

        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Vérifier que l'email est bien défini
            $email = $user->getEmail();
            if (empty($email)) {
                $this->addFlash('error', 'L\'email est obligatoire.');
                return $this->render('admin/create_user.html.twig', [
                    'form' => $form->createView(),
                    'current_theme' => $request->getSession()->get('theme', 'default'),
                    'current_theme_name' => $request->getSession()->get('theme', 'default')
                ]);
            }
            
            // Vérifier que le mot de passe est bien défini
            $password = $user->getPassword();
            if (empty($password)) {
                $this->addFlash('error', 'Le mot de passe est obligatoire.');
                return $this->render('admin/create_user.html.twig', [
                    'form' => $form->createView(),
                    'current_theme' => $request->getSession()->get('theme', 'default'),
                    'current_theme_name' => $request->getSession()->get('theme', 'default')
                ]);
            }
            
            // Hasher le mot de passe
            $user->setPassword(password_hash($password, PASSWORD_DEFAULT));
            
            // S'assurer que l'utilisateur a au moins le rôle ROLE_USER
            $roles = $user->getRoles();
            if (empty($roles)) {
                $user->setRoles(['ROLE_USER']);
            }
            
            // Sauvegarder en base de données
            $entityManager->persist($user);
            $entityManager->flush();
            
            // Ajouter un message flash
            $this->addFlash('success', 'Utilisateur créé avec succès !');
            
            // Rediriger vers la liste des utilisateurs
            return $this->redirectToRoute('app_admin_users');
        }

        return $this->render('admin/create_user.html.twig', [
            'form' => $form->createView(),
            'current_theme' => $request->getSession()->get('theme', 'default'),
            'current_theme_name' => $request->getSession()->get('theme', 'default')
        ]);
    }

    #[Route('/admin/users', name: 'app_admin_users')]
    public function users(Request $request, EntityManagerInterface $entityManager): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');

        $userRepository = $entityManager->getRepository(User::class);
        $users = $userRepository->findAll();

        return $this->render('admin/user1.html.twig', [
            'users' => $users,
            'current_theme' => $request->getSession()->get('theme', 'default'),
            'current_theme_name' => $request->getSession()->get('theme', 'default')
        ]);
    }

    #[Route('/admin/user/{id}', name: 'app_admin_user_view')]
    public function viewUser(int $id, Request $request, EntityManagerInterface $entityManager): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');

        $userRepository = $entityManager->getRepository(User::class);
        $user = $userRepository->find($id);

        if (!$user) {
            throw $this->createNotFoundException('Utilisateur non trouvé');
        }

        return $this->render('admin/view_user.html.twig', [
            'user' => $user,
            'current_theme' => $request->getSession()->get('theme', 'default'),
            'current_theme_name' => $request->getSession()->get('theme', 'default')
        ]);
    }

    #[Route('/admin/user/{id}/edit', name: 'app_admin_user_edit')]
    public function editUser(int $id, Request $request, EntityManagerInterface $entityManager): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');

        $userRepository = $entityManager->getRepository(User::class);
        $user = $userRepository->find($id);

        if (!$user) {
            throw $this->createNotFoundException('Utilisateur non trouvé');
        }

        return $this->render('admin/edit_user.html.twig', [
            'user' => $user,
            'current_theme' => $request->getSession()->get('theme', 'default'),
            'current_theme_name' => $request->getSession()->get('theme', 'default',)
        ]);
    }

    #[Route('/admin/user/{id}/delete', name: 'app_admin_user_delete', methods: ['POST'])]
    public function deleteUser(Request $request, int $id, EntityManagerInterface $entityManager): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');

        $userRepository = $entityManager->getRepository(User::class);
        $user = $userRepository->find($id);

        if (!$user) {
            throw $this->createNotFoundException('Utilisateur non trouvé');
        }

        $entityManager->remove($user);
        $entityManager->flush();

        return $this->redirectToRoute('app_admin_users');
    }

    #[Route('/admin/users/roles', name: 'app_admin_users_roles')]
    public function index(Request $request, EntityManagerInterface $entityManager): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');

        $userRepository = $entityManager->getRepository(User::class);
        $users = $userRepository->findAll();

        return $this->render('admin/user_roles.html.twig', [
            'users' => $users
        ]);
    }

    #[Route('/admin/user/{id}/promote', name: 'app_user_promote', requirements: ['id' => '\d+'])]
    public function promote(Request $request, int $id, EntityManagerInterface $entityManager): Response
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
    public function demote(Request $request, int $id, EntityManagerInterface $entityManager): Response
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
    public function sendPasswordReset(Request $request, int $id, EntityManagerInterface $entityManager): Response
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
