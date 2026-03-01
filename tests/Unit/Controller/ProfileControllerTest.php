<?php

namespace App\Tests\Unit\Controller;

use App\Controller\ProfileController;
use App\Entity\User;
use App\Entity\UserPreferences;
use Doctrine\ORM\EntityManagerInterface;
use PHPUnit\Framework\TestCase;
use Symfony\Bundle\FrameworkBundle\Test\WebTestCase;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Session\Session;
use Symfony\Component\HttpFoundation\Session\Storage\MockArraySessionStorage;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;
use Symfony\Component\Security\Core\Authentication\Token\UsernamePasswordToken;
use Symfony\Component\Security\Core\Security;

class ProfileControllerTest extends WebTestCase
{
    private $client;
    private $entityManager;
    private $passwordHasher;

    protected function setUp(): void
    {
        $this->client = static::createClient();
        $this->entityManager = static::getContainer()->get(EntityManagerInterface::class);
        $this->passwordHasher = static::getContainer()->get(UserPasswordHasherInterface::class);
    }

    public function testProfileIndexReturnsResponse(): void
    {
        // Créer un utilisateur de test
        $user = $this->createTestUser();
        
        // Simuler l'authentification
        $this->client->loginUser($user);
        
        // Faire la requête
        $this->client->request('GET', '/profile');
        
        // Vérifications
        $this->assertEquals(Response::HTTP_OK, $this->client->getResponse()->getStatusCode());
        $this->assertSelectorTextContains('h1', 'Profil');
        $this->assertSelectorExists('.card');
    }

    public function testProfileIndexWithTheme(): void
    {
        $user = $this->createTestUser();
        $this->client->loginUser($user);
        
        // Tester avec thème sombre
        $session = $this->client->getRequest()->getSession();
        $session->set('theme', 'dark');
        
        $this->client->request('GET', '/profile');
        
        $this->assertEquals(Response::HTTP_OK, $this->client->getResponse()->getStatusCode());
        $this->assertResponseIsSuccessful();
    }

    public function testProfileEditReturnsResponse(): void
    {
        $user = $this->createTestUser();
        $this->client->loginUser($user);
        
        $this->client->request('GET', '/profile/edit');
        
        $this->assertEquals(Response::HTTP_OK, $this->client->getResponse()->getStatusCode());
        $this->assertSelectorExists('form');
        $this->assertSelectorExists('input[name="profile_form[nom]"]');
    }

    public function testProfileChangePasswordReturnsResponse(): void
    {
        $user = $this->createTestUser();
        $this->client->loginUser($user);
        
        $this->client->request('GET', '/profile/change-password');
        
        $this->assertEquals(Response::HTTP_OK, $this->client->getResponse()->getStatusCode());
        $this->assertSelectorExists('form');
        $this->assertSelectorExists('input[name="change_password_form[currentPassword]"]');
    }

    public function testProfileEditFormSubmission(): void
    {
        $user = $this->createTestUser();
        $this->client->loginUser($user);
        
        $crawler = $this->client->request('GET', '/profile/edit');
        
        $form = $crawler->selectButton('Sauvegarder')->form([
            'profile_form[nom]' => 'Test Nom Modifié',
            'profile_form[prenom]' => 'Test Prénom Modifié',
            'profile_form[adresse]' => 'Adresse de test modifiée',
            'profile_form[age]' => 30,
            'profile_form[sexe]' => 'homme',
        ]);

        $this->client->submit($form);
        
        // Vérifier la redirection
        $this->assertResponseRedirects('/profile');
        
        // Vérifier que les données ont été sauvegardées
        $updatedUser = $this->entityManager->getRepository(User::class)->find($user->getId());
        $this->assertEquals('Test Nom Modifié', $updatedUser->getNom());
        $this->assertEquals('Test Prénom Modifié', $updatedUser->getPrenom());
    }

    public function testPasswordChangeWithValidData(): void
    {
        $user = $this->createTestUser();
        $this->client->loginUser($user);
        
        $crawler = $this->client->request('GET', '/profile/change-password');
        
        $form = $crawler->selectButton('Changer le mot de passe')->form([
            'change_password_form[currentPassword]' => 'password123',
            'change_password_form[plainPassword][first]' => 'newpassword123',
            'change_password_form[plainPassword][second]' => 'newpassword123',
        ]);

        $this->client->submit($form);
        
        $this->assertResponseRedirects('/profile');
        
        // Vérifier que le mot de passe a été changé
        $this->client->followRedirect();
        $this->assertSelectorTextContains('.alert-success', 'mot de passe a été changé');
    }

    public function testPasswordChangeWithInvalidCurrentPassword(): void
    {
        $user = $this->createTestUser();
        $this->client->loginUser($user);
        
        $crawler = $this->client->request('GET', '/profile/change-password');
        
        $form = $crawler->selectButton('Changer le mot de passe')->form([
            'change_password_form[currentPassword]' => 'wrongpassword',
            'change_password_form[plainPassword][first]' => 'newpassword123',
            'change_password_form[plainPassword][second]' => 'newpassword123',
        ]);

        $this->client->submit($form);
        
        $this->assertSelectorTextContains('.alert-danger', 'mot de passe actuel est incorrect');
    }

    public function testPasswordChangeWithMismatchedPasswords(): void
    {
        $user = $this->createTestUser();
        $this->client->loginUser($user);
        
        $crawler = $this->client->request('GET', '/profile/change-password');
        
        $form = $crawler->selectButton('Changer le mot de passe')->form([
            'change_password_form[currentPassword]' => 'password123',
            'change_password_form[plainPassword][first]' => 'newpassword123',
            'change_password_form[plainPassword][second]' => 'differentpassword',
        ]);

        $this->client->submit($form);
        
        $this->assertSelectorTextContains('.alert-danger', 'ne correspondent pas');
    }

    private function createTestUser(): User
    {
        $user = new User();
        $user->setEmail('test@example.com');
        $user->setPassword($this->passwordHasher->hashPassword($user, 'password123'));
        $user->setNom('Test');
        $user->setPrenom('User');
        $user->setRoles(['ROLE_USER']);
        
        $this->entityManager->persist($user);
        $this->entityManager->flush();
        
        return $user;
    }
}
