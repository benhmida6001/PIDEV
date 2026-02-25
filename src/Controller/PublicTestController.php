<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class PublicTestController extends AbstractController
{
    #[Route('/test-public', name: 'app_test_public')]
    public function testPublic(): Response
    {
        return new Response("
            <h1>🚀 Test Public - ACCÈS RÉUSSI</h1>
            <p>Vous pouvez accéder aux pages publiques sans authentification.</p>
            <hr>
            <h3>🔗 Liens de test:</h3>
            <p><a href='/test-reset-token' target='_blank'>🔐 Test Reset Token</a></p>
            <p><a href='/reset-password' target='_blank'>📧 Demande de réinitialisation</a></p>
            <p><a href='/login' target='_blank'>🔑 Page de connexion</a></p>
            <p><a href='/register' target='_blank'>📝 Page d'inscription</a></p>
            <hr>
            <p><em>Si vous voyez cette page, l'accès public fonctionne !</em></p>
        ");
    }
}
