# 📊 Analyse Statique du Code avec PHPStan

## 🎯 **Configuration PHPStan pour Symfony PIDEV**

### **Fichier de configuration : `phpstan.neon`**
```neon
parameters:
    level: 6
    paths:
        - src
        - tests
    excludePaths:
        - 'src/Kernel.php'
        - 'src/Entity/*'
        - 'src/Migrations/*'
        - 'src/DataFixtures/*'
    checkGenericClassInNonGenericObjectType: false
    checkMissingIterableValueType: false
    ignoreErrors:
        - '#Call to an undefined method.*#'
        - '#Access to an undefined property.*#'
        - '#Undefined variable.*#'
    bootstrapFiles:
        - vendor/autoload.php
    memoryLimit: 1G
```

## 🛠️ **Commandes d'analyse**

### **1. Analyse complète du code source**
```bash
php phpstan.phar analyse src --configuration=phpstan.neon --memory-limit=1G
```

### **2. Analyse avec rapport HTML**
```bash
php phpstan.phar analyse src --configuration=phpstan.neon --report-html=phpstan-report.html
```

### **3. Analyse avec niveau de rigueur spécifique**
```bash
php phpstan.phar analyse src --level=6 --configuration=phpstan.neon
```

### **4. Analyse des tests unitaires**
```bash
php phpstan.phar analyse tests --configuration=phpstan.neon
```

## 📋 **Niveaux d'analyse PHPStan**

| Niveau | Description | Recommandation |
|--------|-------------|----------------|
| 0 | Erreurs de syntaxe et types de base | ✅ Minimum |
| 1 | Types de retour, assignations | ✅ Recommandé |
| 2 | Types de propriétés, méthodes | ✅ Standard |
| 3 | Types d'arguments, héritage | ✅ Avancé |
| 4 | Types génériques, tableaux | ⚠️ Strict |
| 5 | Types nullables, union | ⚠️ Très strict |
| 6 | Types de tableaux, itérables | ⚠️ Expert |
| 7 | Types de clés, valeurs | ❌ Très strict |
| 8 | Types de fonctions anonymes | ❌ Maximum |
| 9 | Tous les types possibles | ❌ Paranoïaque |

## 🎯 **Points d'analyse pour Symfony**

### **Contrôleurs**
- ✅ Types de retour (Response, JsonResponse)
- ✅ Validation des paramètres (Request, EntityManagerInterface)
- ✅ Sécurité (denyAccessUnlessGranted)
- ✅ Gestion des formulaires (FormInterface)

### **Entités**
- ✅ Types de propriétés (string, int, bool, DateTime)
- ✅ Relations (OneToMany, ManyToOne, ManyToMany)
- ✅ Annotations ORM (@ORM\Column, @ORM\Entity)
- ✅ Validation (@Assert\NotBlank, @Assert\Email)

### **Services**
- ✅ Injection de dépendances
- ✅ Interfaces et implémentations
- ✅ Types de retour des méthodes
- ✅ Gestion des exceptions

### **Templates Twig**
- ✅ Variables passées aux templates
- ✅ Formulaires et champs
- ✅ Conditions et boucles
- ✅ Fonctions et filtres personnalisés

## 📈 **Rapports générés**

### **1. Rapport texte**
```bash
php phpstan.phar analyse src > phpstan-report.txt
```

### **2. Rapport HTML**
```bash
php phpstan.phar analyse src --report-html=phpstan-report.html
```

### **3. Rapport JSON**
```bash
php phpstan.phar analyse src --error-format=json > phpstan-report.json
```

### **4. Rapport JUnit**
```bash
php phpstan.phar analyse src --error-format=junit > phpstan-junit.xml
```

## 🔧 **Intégration CI/CD**

### **GitHub Actions**
```yaml
- name: PHPStan Analysis
  run: |
    php phpstan.phar analyse src --configuration=phpstan.neon --report-html=phpstan-report.html
```

### **GitLab CI**
```yaml
phpstan:
  script:
    - php phpstan.phar analyse src --configuration=phpstan.neon
```

## 📊 **Métriques de qualité**

### **Indicateurs suivis**
- 📈 **Nombre d'erreurs** : Total des problèmes détectés
- 📉 **Complexité cyclomatique** : Complexité des méthodes
- 📋 **Couverture de type** : Pourcentage de code typé
- 🎯 **Niveau de rigueur** : Niveau d'analyse appliqué

### **Objectifs qualité**
- ✅ **0 erreurs critiques** : Niveaux 0-1
- ✅ **< 10 erreurs mineures** : Niveaux 2-3
- ✅ **> 80% de typage** : Types de retour et paramètres
- ✅ **Tests couverts** : Analyse des tests unitaires

## 🚀 **Exécution rapide**

### **Script batch Windows**
```batch
@echo off
cd /d "c:\Users\User_01\Downloads\PIDEV-user1 (2)\PIDEV-user1"
php phpstan.phar analyse src --configuration=phpstan.neon --memory-limit=1G
pause
```

### **Script shell Linux**
```bash
#!/bin/bash
cd /path/to/project
php phpstan.phar analyse src --configuration=phpstan.neon --memory-limit=1G
```

---

**📋 Prêt pour l'analyse statique du code Symfony PIDEV !** 🎯
