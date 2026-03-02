<?php

namespace App\Controller;

use App\Form\UserPreferencesType;
use App\Entity\UserPreferences;
use App\Entity\User;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Core\Security;

#[Route('/user/preferences')]
class UserPreferencesController extends AbstractController
{
    #[Route('/', name: 'app_user_preferences')]
    public function index(Security $security, EntityManagerInterface $entityManager): Response
    {
        /** @var User $user */
        $user = $security->getUser();
        
        // Récupérer ou créer les préférences de l'utilisateur
        $preferences = $user->getPreferences();
        if (!$preferences) {
            $preferences = new UserPreferences();
            $preferences->setUser($user);
            $entityManager->persist($preferences);
            $entityManager->flush();
            $user->setPreferences($preferences);
            $entityManager->flush();
        }

        $form = $this->createForm(UserPreferencesType::class, $preferences);
        
        return $this->render('user/preferences.html.twig', [
            'form' => $form->createView(),
            'preferences' => $preferences
        ]);
    }

    #[Route('/save', name: 'app_user_preferences_save', methods: ['POST'])]
    public function save(Request $request, Security $security, EntityManagerInterface $entityManager): Response
    {
        /** @var User $user */
        $user = $security->getUser();
        $preferences = $user->getPreferences();
        
        if (!$preferences) {
            $preferences = new UserPreferences();
            $preferences->setUser($user);
            $entityManager->persist($preferences);
        }

        $form = $this->createForm(UserPreferencesType::class, $preferences);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();
            
            // Mettre à jour la session pour le thème et la langue
            $session = $request->getSession();
            $session->set('theme', $preferences->getTheme());
            $session->set('_locale', $preferences->getLanguage());
            
            $this->addFlash('success', 'preferences.saved_success');
            return $this->redirectToRoute('app_user_preferences');
        }

        return $this->render('user/preferences.html.twig', [
            'form' => $form->createView(),
            'preferences' => $preferences
        ]);
    }

    #[Route('/reset', name: 'app_user_preferences_reset')]
    public function reset(Security $security, EntityManagerInterface $entityManager): Response
    {
        /** @var User $user */
        $user = $security->getUser();
        $preferences = $user->getPreferences();
        
        if ($preferences) {
            // Réinitialiser aux valeurs par défaut
            $preferences->setLanguage('fr');
            $preferences->setTheme('default');
            $preferences->setNotifications(true);
            $preferences->setEmailNotifications(true);
            $preferences->setSoundEffects(false);
            $preferences->setItemsPerPage(10);
            $preferences->setDateFormat('d/m/Y');
            $preferences->setTimeFormat('24h');
            
            $entityManager->flush();
            
            // Mettre à jour la session
            $session = new \Symfony\Component\HttpFoundation\Session\Session();
            $session->set('theme', 'default');
            $session->set('_locale', 'fr');
        }
        
        $this->addFlash('success', 'preferences.reset_success');
        return $this->redirectToRoute('app_user_preferences');
    }
}
