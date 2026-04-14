<?php

namespace App\Controller;

use App\Entity\User;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

final class TempController extends AbstractController
{
    #[Route('/temp/add-roles', name: 'app_temp_add_roles')]
    public function addRoles(EntityManagerInterface $entityManager): Response
    {
        // Récupérer le premier utilisateur
        $user = $entityManager->getRepository(User::class)->find(1);
        
        if ($user) {
            // Ajouter le rôle d'administrateur
            $user->setRoles(['ROLE_ADMIN']);
            $entityManager->flush();
            
            return new Response('Rôle ROLE_ADMIN ajouté à l\'utilisateur ID 1: ' . $user->getEmail());
        }
        
        return new Response('Utilisateur ID 1 non trouvé');
    }
}
