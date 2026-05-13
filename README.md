# GREENCORE - PIDEV Project

## Description
Application de gestion communautaire de matériel en JavaFX 17 avec Maven, connectée à une base de données MySQL.

## Structure du Projet
```
src/main/java/com/greencore/
├── Main.java                          # Point d'entrée de l'application
├── config/
│   └── DatabaseConfig.java           # Configuration de la base de données
├── controller/
│   ├── LoginController.java          # Contrôleur de l'écran de connexion
│   └── DashboardController.java      # Contrôleur du tableau de bord
├── model/
│   ├── entity/                       # Entités JPA
│   │   ├── User.java
│   │   ├── Category.java
│   │   ├── Materiel.java
│   │   ├── Operation.java
│   │   ├── Status.java
│   │   ├── OperationType.java
│   │   └── OperationStatus.java
│   ├── repository/                   # Repository pattern avec Hibernate
│   │   ├── BaseRepository.java
│   │   ├── UserRepository.java
│   │   ├── CategoryRepository.java
│   │   ├── MaterielRepository.java
│   │   └── OperationRepository.java
│   ├── service/                      # Couche de service
│   │   ├── UserService.java
│   │   └── MaterielService.java
│   └── util/                         # Utilitaires
└── view/
    ├── fxml/                         # Fichiers FXML
    │   ├── login.fxml
    │   └── dashboard.fxml
    └── css/                          # Feuilles de style
        └── style.css

src/main/resources/
└── view/
    ├── fxml/
    └── css/
```

## Configuration de la Base de Données

1. **Créer la base de données** :
   ```sql
   Exécutez le script database_init.sql dans votre client MySQL
   ```

2. **Configuration de la connexion** :
   - Hôte : `127.0.0.1:3307`
   - Base de données : `pidev_greencore`
   - Utilisateur : `root`
   - Mot de passe : (vide par défaut)

## Installation et Exécution

### Prérequis
- Java 17 ou supérieur
- Maven 3.6+
- MySQL 8.0+
- JavaFX 17

### Étapes d'installation

1. **Compiler le projet** :
   ```bash
   mvn clean compile
   ```

2. **Exécuter l'application** :
   ```bash
   mvn javafx:run
   ```

### Utilisateurs de test
- **Admin** : `admin@greencore.com` / `password`
- **User** : `user@greencore.com` / `password`

## Fonctionnalités

### ✅ Implémentées
- Architecture MVC complète avec JavaFX
- Connexion à la base de données MySQL avec Hibernate
- Système d'authentification avec BCrypt
- Interface de connexion moderne
- Tableau de bord avec liste des matériels
- Recherche de matériels
- Validation en temps réel des formulaires

### 🔄 À compléter
- Formulaire d'ajout/modification de matériel
- Système d'emprunt et de réparation
- Gestion des catégories
- Profil utilisateur
- Notifications
- Upload d'images

## Architecture Technique

- **Framework** : JavaFX 17
- **ORM** : Hibernate 6.2.7
- **Base de données** : MySQL 8.0
- **Sécurité** : BCrypt pour les mots de passe
- **Build** : Maven
- **Pattern** : MVC avec Repository et Service layers

## Dépannage

### Problèmes courants
1. **Espace disque insuffisant** : Libérez de l'espace sur votre disque dur
2. **Connexion DB** : Vérifiez que MySQL fonctionne sur le port 3307
3. **JavaFX** : Assurez-vous d'avoir JavaFX correctement configuré

### Logs
L'application utilise Log4j2 pour la journalisation. Les logs s'affichent dans la console.

## Développement

Pour ajouter de nouvelles fonctionnalités :
1. Créer les entités JPA nécessaires
2. Implémenter les repositories correspondants
3. Ajouter les services métier
4. Créer les contrôleurs JavaFX
5. Concevoir les interfaces FXML
6. Appliquer les styles CSS

## License
Projet académique - PIDEV GREENCORE
