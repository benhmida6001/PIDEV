# GREENCORE - Application de Gestion

Une application web moderne développée avec Symfony 6.4 pour la gestion complète d'utilisateurs avec un système d'authentification avancé et une interface intuitive.

## 🌟 Fonctionnalités

### 📋 Gestion des Utilisateurs
- **Inscription** : Création de compte avec validation email
- **Connexion** : Authentification sécurisée avec messages d'erreur
- **Profils** : Gestion des profils utilisateur avec photo
- **Rôles** : Système de rôles (USER, ADMIN) avec permissions
- **Sécurité** : Protection CSRF et validation côté serveur

### 🎨 Interface Utilisateur
- **Design Moderne** : Theme vert/blanc GREENCORE
- **Responsive** : Adaptation mobile/desktop
- **Animations** : Effets visuels et transitions fluides
- **Navigation** : Système de navigation intuitif

### 🛠️ Tableau de Bord
- **Recherche Avancée** : Recherche intelligente avec résultats organisés
- **Statistiques** : Vue d'ensemble des données
- **Administration** : Gestion complète des utilisateurs
- **Navigation Rapide** : Accès direct à toutes les fonctionnalités

### 🔧 Fonctionnalités Techniques
- **Upload de fichiers** : Gestion des photos de profil
- **Validation** : Contrôles de saisie côté serveur
- **Flash Messages** : Notifications utilisateur
- **Base de données** : Doctrine ORM avec SQLite

## 🚀 Installation

### Prérequis
- PHP 8.1+
- Composer
- Symfony CLI (optionnel)

### Étapes d'installation

1. **Cloner le repository**
```bash
git clone <repository-url>
cd greencore
```

2. **Installer les dépendances**
```bash
composer install
```

3. **Configurer l'environnement**
```bash
cp .env .env.local
# Configurer les variables d'environnement si nécessaire
```

4. **Créer la base de données**
```bash
php bin/console doctrine:database:create
php bin/console doctrine:migrations:migrate
```

5. **Démarrer le serveur**
```bash
symfony server:start
# ou
php -S localhost:8000 -t public/
```

## 📁 Structure du Projet

```
├── src/
│   ├── Controller/          # Contrôleurs
│   │   ├── AdminController.php
│   │   ├── DashboardController.php
│   │   ├── ProfileController.php
│   │   ├── RegistrationController.php
│   │   └── SecurityController.php
│   ├── Entity/             # Entités
│   │   └── User.php
│   ├── Form/               # Formulaires
│   │   ├── ProfileFormType.php
│   │   ├── RegistrationFormType.php
│   │   └── UserType.php
│   └── Repository/         # Repositories
│       └── UserRepository.php
├── templates/              # Templates Twig
│   ├── admin/             # Pages administration
│   ├── dashboard/         # Tableau de bord
│   ├── profile/           # Gestion profil
│   ├── registration/      # Inscription
│   └── security/          # Connexion
├── public/
│   └── uploads/           # Uploads de fichiers
└── migrations/            # Migrations Doctrine
```

## 🎯 Utilisation

### Navigation Principale
- **Accueil** : `/` - Page d'accueil avec options connexion/inscription
- **Connexion** : `/login` - Formulaire d'authentification
- **Inscription** : `/register` - Création de compte
- **Profil** : `/profile` - Vue et modification du profil
- **Dashboard** : `/dashboard` - Tableau de bord utilisateur
- **Administration** : `/admin/users` - Gestion des utilisateurs (admin)

### Fonctionnalités Admin
- **Gestion des utilisateurs** : Voir, modifier, supprimer
- **Assignation de rôles** : Gérer les permissions
- **Statistiques** : Vue d'ensemble de la plateforme

## 🔒 Sécurité

- **Authentification** : Symfony Security Component
- **Validation** : Contraintes de validation côté serveur
- **CSRF** : Protection contre les attaques CSRF
- **Permissions** : Contrôle d'accès par rôles
- **Upload sécurisé** : Validation des types de fichiers

## 🎨 Personnalisation

### Theme GREENCORE
- **Couleurs principales** : Vert (#4caf50) et blanc
- **Design** : Moderne et épuré
- **Animations** : Transitions fluides et effets visuels
- **Responsive** : Adaptation tous écrans

### Modification du theme
Les styles sont définis dans les templates Twig avec des variables CSS personnalisées.

## 📝 Développement

### Standards de Code
- **PSR-12** : Standards de codage PHP
- **Symfony Best Practices** : Conventions Symfony
- **Clean Architecture** : Séparation des responsabilités

### Tests
```bash
# Lancer les tests
php bin/phpunit

# Couverture de code
php bin/phpunit --coverage-html coverage
```

## 🔄 Git Workflow

### Branches
- **main** : Branche principale de production
- **develop** : Branche de développement
- **feature/*** : Nouvelles fonctionnalités
- **hotfix/*** : Corrections urgentes

### Commits
Les commits suivent le format Conventional Commits :
```
type(scope): description

feat(auth): add user registration
fix(dashboard): resolve search bug
docs(readme): update installation guide
```

## 📞 Support

### Équipe de Développement
- **Warriors** : Équipe de développement principale

### Technologies Utilisées
- **Backend** : Symfony 6.4, PHP 8.1+
- **Frontend** : Bootstrap 5, Twig, JavaScript
- **Database** : Doctrine ORM, SQLite
- **Tools** : Composer, Symfony CLI

## 📄 Licence

Ce projet est développé dans le cadre d'un projet éducatif par l'équipe Warriors.

---

**GREENCORE** - La technologie au cœur de la planète 🌍
