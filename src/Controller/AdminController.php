<?php

namespace App\Controller;

use App\Entity\User;
use App\Form\UserType;
use App\Form\CreateUserType;
use App\Repository\UserRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

final class AdminController extends AbstractController
{
    #[Route('/admin/users', name: 'app_admin_users')]
    public function users(UserRepository $userRepository): Response
    {
        // Seuls les administrateurs peuvent accéder à cette page
        $this->denyAccessUnlessGranted('ROLE_ADMIN');

        $users = $userRepository->findAll();

        return $this->render('admin/users.html.twig', [
            'users' => $users,
        ]);
    }

    #[Route('/admin/users/{id}/edit', name: 'app_admin_user_edit')]
    public function editUser(User $user, Request $request, EntityManagerInterface $entityManager): Response
    {
        // Seuls les administrateurs peuvent accéder à cette page
        $this->denyAccessUnlessGranted('ROLE_ADMIN');

        // Empêcher un admin de modifier ses propres rôles
        if ($user->getEmail() === $this->getUser()->getEmail()) {
            return $this->redirectToRoute('app_admin_users');
        }

        $form = $this->createForm(UserType::class, $user);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();
            return $this->redirectToRoute('app_admin_users');
        }

        return $this->render('admin/edit_user.html.twig', [
            'form' => $form->createView(),
            'user' => $user,
        ]);
    }

    #[Route('/admin/users/{id}/delete', name: 'app_admin_user_delete', methods: ['POST'])]
    public function deleteUser(User $user, EntityManagerInterface $entityManager): Response
    {
        // Seuls les administrateurs peuvent accéder à cette page
        $this->denyAccessUnlessGranted('ROLE_ADMIN');

        // Empêcher un admin de supprimer son propre compte
        if ($user->getEmail() === $this->getUser()->getEmail()) {
            return $this->redirectToRoute('app_admin_users');
        }

        // Empêcher la suppression du dernier administrateur
        $adminUsers = $entityManager->getRepository(User::class)->findBy(['roles' => '%ROLE_ADMIN%']);
        $adminCount = 0;
        foreach ($adminUsers as $admin) {
            if (in_array('ROLE_ADMIN', $admin->getRoles())) {
                $adminCount++;
            }
        }

        if ($adminCount <= 1 && in_array('ROLE_ADMIN', $user->getRoles())) {
            return $this->redirectToRoute('app_admin_users');
        }

        // Supprimer la photo de profil si elle existe
        if ($user->getProfilePicture()) {
            $profilePicturePath = $this->getParameter('kernel.project_dir') . '/public/uploads/profile_pictures/' . $user->getProfilePicture();
            if (file_exists($profilePicturePath)) {
                unlink($profilePicturePath);
            }
        }

        $entityManager->remove($user);
        $entityManager->flush();
        return $this->redirectToRoute('app_admin_users');
    }

    #[Route('/admin/users/create', name: 'app_admin_user_create')]
    public function createUser(Request $request, EntityManagerInterface $entityManager, UserPasswordHasherInterface $passwordHasher): Response
    {
        // Seuls les administrateurs peuvent accéder à cette page
        $this->denyAccessUnlessGranted('ROLE_ADMIN');

        $user = new User();
        $form = $this->createForm(CreateUserType::class, $user);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Vérifier si l'email existe déjà
            $existingUser = $entityManager->getRepository(User::class)->findOneBy(['email' => $user->getEmail()]);
            
            if ($existingUser) {
                $this->addFlash('error', 'Cet email est déjà utilisé par un autre utilisateur.');
                return $this->render('admin/create_user.html.twig', [
                    'form' => $form->createView(),
                ]);
            }

            // Hasher le mot de passe
            $plainPassword = $form->get('password')->get('first')->getData();
            $hashedPassword = $passwordHasher->hashPassword($user, $plainPassword);
            $user->setPassword($hashedPassword);
            
            // Récupérer et définir les rôles
            $roles = $form->get('roles')->getData();
            $user->setRoles($roles);

            $entityManager->persist($user);
            $entityManager->flush();

            $this->addFlash('success', 'L\'utilisateur a été créé avec succès!');
            return $this->redirectToRoute('app_admin_users');
        }

        return $this->render('admin/create_user.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/admin/users/{id}/view', name: 'app_admin_user_view')]
    public function viewUser(User $user): Response
    {
        // Seuls les administrateurs peuvent accéder à cette page
        $this->denyAccessUnlessGranted('ROLE_ADMIN');

        return $this->render('admin/view_user.html.twig', [
            'user' => $user,
        ]);
    }
}
