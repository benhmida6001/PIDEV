<?php

namespace App\Tests\Unit\Entity;

use App\Entity\User;
use App\Entity\UserPreferences;
use PHPUnit\Framework\TestCase;

class UserTest extends TestCase
{
    private User $user;

    protected function setUp(): void
    {
        $this->user = new User();
    }

    public function testUserCreation(): void
    {
        $this->assertInstanceOf(User::class, $this->user);
        $this->assertNull($this->user->getId());
        $this->assertNull($this->user->getEmail());
        $this->assertNull($this->user->getPassword());
        $this->assertEquals(['ROLE_USER'], $this->user->getRoles());
    }

    public function testEmailSetAndGet(): void
    {
        $email = 'test@example.com';
        $this->user->setEmail($email);
        
        $this->assertEquals($email, $this->user->getEmail());
    }

    public function testPasswordSetAndGet(): void
    {
        $password = 'hashed_password';
        $this->user->setPassword($password);
        
        $this->assertEquals($password, $this->user->getPassword());
    }

    public function testRolesSetAndGet(): void
    {
        $roles = ['ROLE_ADMIN', 'ROLE_USER'];
        $this->user->setRoles($roles);
        
        $this->assertEquals($roles, $this->user->getRoles());
    }

    public function testRolesAlwaysIncludeUserRole(): void
    {
        // Même si on ne définit que ROLE_ADMIN, ROLE_USER doit être inclus
        $this->user->setRoles(['ROLE_ADMIN']);
        $roles = $this->user->getRoles();
        
        $this->assertContains('ROLE_USER', $roles);
        $this->assertContains('ROLE_ADMIN', $roles);
    }

    public function testNomSetAndGet(): void
    {
        $nom = 'Doe';
        $this->user->setNom($nom);
        
        $this->assertEquals($nom, $this->user->getNom());
    }

    public function testPrenomSetAndGet(): void
    {
        $prenom = 'John';
        $this->user->setPrenom($prenom);
        
        $this->assertEquals($prenom, $this->user->getPrenom());
    }

    public function testAdresseSetAndGet(): void
    {
        $adresse = '123 Rue de la République';
        $this->user->setAdresse($adresse);
        
        $this->assertEquals($adresse, $this->user->getAdresse());
    }

    public function testAgeSetAndGet(): void
    {
        $age = 30;
        $this->user->setAge($age);
        
        $this->assertEquals($age, $this->user->getAge());
    }

    public function testSexeSetAndGet(): void
    {
        $sexe = 'homme';
        $this->user->setSexe($sexe);
        
        $this->assertEquals($sexe, $this->user->getSexe());
    }

    public function testProfilePictureSetAndGet(): void
    {
        $profilePicture = 'profile.jpg';
        $this->user->setProfilePicture($profilePicture);
        
        $this->assertEquals($profilePicture, $this->user->getProfilePicture());
    }

    public function testCreatedAtSetAndGet(): void
    {
        $createdAt = new \DateTime('2023-01-01');
        $this->user->setCreatedAt($createdAt);
        
        $this->assertEquals($createdAt, $this->user->getCreatedAt());
    }

    public function testLastLoginSetAndGet(): void
    {
        $lastLogin = new \DateTime('2023-01-01 12:00:00');
        $this->user->setLastLogin($lastLogin);
        
        $this->assertEquals($lastLogin, $this->user->getLastLogin());
    }

    public function testEraseCredentials(): void
    {
        $this->user->setPassword('sensitive_data');
        $this->user->eraseCredentials();
        
        // Le mot de passe ne doit pas être effacé (c'est géré par le système de sécurité)
        $this->assertEquals('sensitive_data', $this->user->getPassword());
    }

    public function testGetUserIdentifier(): void
    {
        $email = 'test@example.com';
        $this->user->setEmail($email);
        
        $this->assertEquals($email, $this->user->getUserIdentifier());
    }

    public function testFullDataUser(): void
    {
        // Créer un utilisateur complet
        $this->user->setEmail('john.doe@example.com');
        $this->user->setPassword('hashed_password');
        $this->user->setRoles(['ROLE_ADMIN']);
        $this->user->setNom('Doe');
        $this->user->setPrenom('John');
        $this->user->setAdresse('123 Rue de la République');
        $this->user->setAge(30);
        $this->user->setSexe('homme');
        $this->user->setProfilePicture('profile.jpg');
        $this->user->setCreatedAt(new \DateTime('2023-01-01'));
        $this->user->setLastLogin(new \DateTime('2023-01-01 12:00:00'));

        // Vérifications
        $this->assertEquals('john.doe@example.com', $this->user->getEmail());
        $this->assertEquals('hashed_password', $this->user->getPassword());
        $this->assertContains('ROLE_ADMIN', $this->user->getRoles());
        $this->assertContains('ROLE_USER', $this->user->getRoles());
        $this->assertEquals('Doe', $this->user->getNom());
        $this->assertEquals('John', $this->user->getPrenom());
        $this->assertEquals('123 Rue de la République', $this->user->getAdresse());
        $this->assertEquals(30, $this->user->getAge());
        $this->assertEquals('homme', $this->user->getSexe());
        $this->assertEquals('profile.jpg', $this->user->getProfilePicture());
        $this->assertEquals('2023-01-01', $this->user->getCreatedAt()->format('Y-m-d'));
        $this->assertEquals('2023-01-01 12:00:00', $this->user->getLastLogin()->format('Y-m-d H:i:s'));
    }
}
