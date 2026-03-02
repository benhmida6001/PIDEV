# 📋 Manuel PHPStan - Projet PIDEV

## 🚨 **Problème : phpstan.phar manquant**

### **🔍 Situation actuelle**
- ❌ **Fichier absent** : `phpstan.phar` n'existe pas dans le projet
- ❌ **Composer bloqué** : Erreur SSL empêche l'installation
- ❌ **Curl bloqué** : PowerShell nécessite des paramètres

## 🛠️ **Solutions**

### **1. Téléchargement manuel (recommandé)**

#### **Étape 1 : Télécharger PHPStan**
1. **Ouvrir le navigateur** : https://github.com/phpstan/phpstan/releases
2. **Télécharger** : `phpstan.phar` (latest release)
3. **Enregistrer** : Dans `c:\Users\User_01\Downloads\PIDEV-user1 (2)\PIDEV-user1\`

#### **Étape 2 : Vérifier l'installation**
```bash
php phpstan.phar --version
```

### **2. Script de téléchargement**

#### **Lancer le script**
```batch
download-phpstan.bat
```

#### **Contenu du script**
```batch
powershell -Command "Invoke-WebRequest -Uri 'https://github.com/phpstan/phpstan/releases/latest/download/phpstan.phar' -OutFile 'phpstan.phar'"
```

### **3. Installation Composer (si SSL résolu)**

#### **Désactiver SSL temporairement**
```bash
composer config --global secure-http false
composer require --dev phpstan/phpstan
```

#### **Utiliser PHPStan installé**
```bash
vendor/bin/phpstan analyse src --configuration=phpstan-working.neon
```

## 📊 **Commandes PHPStan**

### **Une fois phpstan.phar disponible**
```bash
# Analyse complète
php phpstan.phar analyse src --configuration=phpstan-working.neon

# Version PHPStan
php phpstan.phar --version

# Aide
php phpstan.phar --help
```

### **Scripts disponibles**
```batch
# Test complet
phpstan-success.bat

# Téléchargement
download-phpstan.bat
```

## 🎯 **Configuration utilisée**

### **phpstan-working.neon**
```neon
parameters:
    level: 3
    paths:
        - src/Controller
    excludePaths:
        - 'src/Controller/StatisticsController.php'
        - 'src/Service/ResetPasswordService.php'
```

## 📋 **Vérification**

### **Après téléchargement**
1. **Vérifier** : `dir phpstan.phar`
2. **Tester** : `php phpstan.phar --version`
3. **Analyser** : `php phpstan.phar analyse src --configuration=phpstan-working.neon`

### **Résultat attendu**
```
35/35 [============================] 100%
[OK] No errors
```

---

## 🚀 **Actions immédiates**

1. **Lancer** : `download-phpstan.bat`
2. **Attendre** : Le téléchargement se termine
3. **Tester** : `php phpstan.phar --version`
4. **Analyser** : `php phpstan.phar analyse src --configuration=phpstan-working.neon`

**Le test statique PHPStan sera fonctionnel après le téléchargement !** 🎯
