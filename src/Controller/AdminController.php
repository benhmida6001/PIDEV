<?php

namespace App\Controller;

use App\Entity\User;
use App\Form\UserType;
use App\Repository\UserRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

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
            $this->addFlash('error', 'Vous ne pouvez pas modifier vos propres rôles.');
            return $this->redirectToRoute('app_admin_users');
        }

        $form = $this->createForm(UserType::class, $user);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();
            $this->addFlash('success', 'Les rôles de l\'utilisateur ont été modifiés avec succès.');
            return $this->redirectToRoute('app_admin_users');
        }

        return $this->render('admin/edit_user.html.twig', [
            'form' => $form->createView(),
            'user' => $user,
        ]);
    }
}
