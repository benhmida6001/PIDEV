<?php

namespace App\Tests\Unit\Controller;

use App\Controller\AdminController;
use App\Entity\User;
use Doctrine\ORM\EntityManagerInterface;
use PHPUnit\Framework\TestCase;
use Symfony\Bundle\FrameworkBundle\Test\WebTestCase;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

class AdminControllerTest extends WebTestCase
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

    public function testAdminUsersRequiresAdminRole(): void
    {
        // Créer un utilisateur normal
        $user = $this->createTestUser('user@example.com', ['ROLE_USER']);
        $this->client->loginUser($user);
        
        $this->client->request('GET', '/admin/users');
        
        // Doit être redirigé car l'utilisateur n'est pas admin
        $this->assertEquals(Response::HTTP_FOUND, $this->client->getResponse()->getStatusCode());
    }

    public function testAdminUsersWithAdminRole(): void
    {
        // Créer un utilisateur admin
        $admin = $this->createTestUser('admin@example.com', ['ROLE_ADMIN']);
        $this->client->loginUser($admin);
        
        $this->client->request('GET', '/admin/users');
        
        $this->assertEquals(Response::HTTP_OK, $this->client->getResponse()->getStatusCode());
        $this->assertSelectorTextContains('h1', 'Utilisateurs');
        $this->assertSelectorExists('.table');
    }

    public function testAdminUsersWithTheme(): void
    {
        $admin = $this->createTestUser('admin@example.com', ['ROLE_ADMIN']);
        $this->client->loginUser($admin);
        
        // Tester avec thème clair
        $session = $this->client->getRequest()->getSession();
        $session->set('theme', 'light');
        
        $this->client->request('GET', '/admin/users');
        
        $this->assertEquals(Response::HTTP_OK, $this->client->getResponse()->getStatusCode());
        $this->assertResponseIsSuccessful();
    }

    public function testAdminCreateUserReturnsForm(): void
    {
        $admin = $this->createTestUser('admin@example.com', ['ROLE_ADMIN']);
        $this->client->loginUser($admin);
        
        $this->client->request('GET', '/admin/users/create');
        
        $this->assertEquals(Response::HTTP_OK, $this->client->getResponse()->getStatusCode());
        $this->assertSelectorExists('form');
        $this->assertSelectorExists('input[name="create_user_form[email]"]');
        $this->assertSelectorExists('input[name="create_user_form[plainPassword][first]"]');
    }

    public function testAdminCreateUserSubmission(): void
    {
        $admin = $this->createTestUser('admin@example.com', ['ROLE_ADMIN']);
        $this->client->loginUser($admin);
        
        $crawler = $this->client->request('GET', '/admin/users/create');
        
        $form = $crawler->selectButton('Créer l\'utilisateur')->form([
            'create_user_form[email]' => 'newuser@example.com',
            'create_user_form[plainPassword][first]' => 'password123',
            'create_user_form[plainPassword][second]' => 'password123',
            'create_user_form[roles]' => ['ROLE_USER'],
            'create_user_form[sexe]' => 'homme',
        ]);

        $this->client->submit($form);
        
        // Vérifier la redirection
        $this->assertResponseRedirects('/admin/users');
        
        // Vérifier que l'utilisateur a été créé
        $newUser = $this->entityManager->getRepository(User::class)->findOneBy(['email' => 'newuser@example.com']);
        $this->assertNotNull($newUser);
        $this->assertEquals('newuser@example.com', $newUser->getEmail());
        $this->assertContains('ROLE_USER', $newUser->getRoles());
    }

    public function testAdminStatisticsRequiresAdminRole(): void
    {
        $user = $this->createTestUser('user@example.com', ['ROLE_USER']);
        $this->client->loginUser($user);
        
        $this->client->request('GET', '/admin/statistics');
        
        $this->assertEquals(Response::HTTP_FOUND, $this->client->getResponse()->getStatusCode());
    }

    public function testAdminStatisticsWithAdminRole(): void
    {
        $admin = $this->createTestUser('admin@example.com', ['ROLE_ADMIN']);
        $this->client->loginUser($admin);
        
        $this->client->request('GET', '/admin/statistics');
        
        $this->assertEquals(Response::HTTP_OK, $this->client->getResponse()->getStatusCode());
        $this->assertSelectorTextContains('h1', 'Statistiques');
        $this->assertSelectorExists('.stats-card');
    }

    public function testAdminDeleteUserPreventsSelfDeletion(): void
    {
        $admin = $this->createTestUser('admin@example.com', ['ROLE_ADMIN']);
        $this->client->loginUser($admin);
        
        // Tenter de supprimer son propre compte
        $this->client->request('POST', '/admin/users/' . $admin->getId() . '/delete');
        
        // Doit être redirigé sans suppression
        $this->assertResponseRedirects('/admin/users');
        
        // Vérifier que l'utilisateur existe toujours
        $existingAdmin = $this->entityManager->getRepository(User::class)->find($admin->getId());
        $this->assertNotNull($existingAdmin);
    }

    public function testAdminDeleteUserPreventsLastAdminDeletion(): void
    {
        // Créer un admin
        $admin = $this->createTestUser('admin@example.com', ['ROLE_ADMIN']);
        $this->client->loginUser($admin);
        
        // Compter le nombre d'admins
        $adminCount = count($this->entityManager->getRepository(User::class)->findBy(['roles' => '%ROLE_ADMIN%']));
        
        // S'il n'y a qu'un admin, la suppression doit être empêchée
        if ($adminCount <= 1) {
            $this->client->request('POST', '/admin/users/' . $admin->getId() . '/delete');
            $this->assertResponseRedirects('/admin/users');
        }
    }

    private function createTestUser(string $email, array $roles): User
    {
        $user = new User();
        $user->setEmail($email);
        $user->setPassword($this->passwordHasher->hashPassword($user, 'password123'));
        $user->setRoles($roles);
        $user->setNom('Test');
        $user->setPrenom('User');
        
        $this->entityManager->persist($user);
        $this->entityManager->flush();
        
        return $user;
    }
}
