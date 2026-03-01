<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;

class ThemeController extends AbstractController
{
    #[Route('/theme/{theme}', name: 'app_theme_switch')]
    public function switchTheme(Request $request, string $theme): Response
    {
        // Valider le thème
        $allowedThemes = ['dark', 'light', 'default'];
        if (!in_array($theme, $allowedThemes)) {
            throw $this->createNotFoundException('Thème non valide');
        }

        // Sauvegarder le thème en session
        $session = $request->getSession();
        $session->set('theme', $theme);

        // Ajouter un message flash
        
        // Rediriger vers la page précédente
        $referer = $request->headers->get('referer');
        if ($referer) {
            return $this->redirect($referer);
        }

        return $this->redirectToRoute('app_home');
    }

    #[Route('/theme', name: 'app_theme_current')]
    public function getCurrentTheme(Request $request): Response
    {
        $session = $request->getSession();
        $currentTheme = $session->get('theme', 'default');

        return $this->json(['theme' => $currentTheme]);
    }
}
