<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use Symfony\Component\Routing\Annotation\Route;

class SettingsController extends AbstractController
{
    #[Route('/admin/settings', name: 'app_settings')]
    public function index(Request $request, SessionInterface $session): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');

        // Traitement du formulaire
        if ($request->isMethod('POST')) {
            $language = $request->request->get('language');
            $theme = $request->request->get('theme');
            
            // Sauvegarder la langue dans la session
            if ($language && in_array($language, ['fr', 'en', 'es', 'de', 'it', 'pt', 'nl', 'ar', 'zh', 'ja'])) {
                $session->set('_locale', $language);
                $request->setLocale($language);
            }
            
            // Sauvegarder le thème dans la session
            if ($theme && in_array($theme, ['default', 'dark', 'light'])) {
                $session->set('theme', $theme);
            }

            $this->addFlash('success', 'settings.saved_success');
            return $this->redirectToRoute('app_settings', ['_locale' => $language ?? $session->get('_locale', 'fr')]);
        }

        return $this->render('admin/settings.html.twig', [
            'current_language' => $session->get('_locale', 'fr'),
            'current_theme' => $session->get('theme', 'default'),
            'current_app_name' => $session->get('app_name', 'GREENCORE'),
            'current_admin_email' => $session->get('admin_email', 'admin@greencore.com'),
            'current_maintenance_mode' => $session->get('maintenance_mode', false),
            'current_notifications' => $session->get('notifications', true),
            'current_debug_mode' => $session->get('debug_mode', false)
        ]);
    }
}
