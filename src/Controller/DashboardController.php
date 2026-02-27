<?php

namespace App\Controller;

use App\Entity\User;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

final class DashboardController extends AbstractController
{
    #[Route('/dashboard', name: 'app_dashboard')]
    public function index(EntityManagerInterface $entityManager): Response
    {
        // L'utilisateur doit être connecté pour accéder au dashboard
        $this->denyAccessUnlessGranted('IS_AUTHENTICATED_FULLY');

        // Récupérer le thème depuis la session
        $session = $this->container->get('request_stack')->getSession();
        $current_theme = $session->get('theme', 'default');

        // Récupérer les statistiques
        $users = $entityManager->getRepository(User::class)->findAll();
        $totalUsers = count($users);
        
        // Simuler des utilisateurs actifs (pour l'exemple)
        $activeUsers = max(1, intval($totalUsers * 0.7));

        return $this->render('dashboard/index.html.twig', [
            'users_count' => $totalUsers,
            'active_users_count' => $activeUsers,
            'current_theme' => $current_theme,
        ]);
    }
}

