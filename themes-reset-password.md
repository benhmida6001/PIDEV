# 🎨 Thèmes Dynamiques - Formulaire de Réinitialisation

## 🌈 **Thèmes Disponibles**

Le formulaire de réinitialisation de mot de passe supporte maintenant 3 thèmes dynamiques :

### **1. 🌿 Thème Sombre (Dark)**
- **Arrière-plan** : Dégradé bleu foncé (#1a237e → #3949ab)
- **Carte** : Fond sombre avec transparence
- **Texte** : Blanc et gris clair
- **Bouton** : Dégradé violet (#3949ab → #5e35b1)
- **Lien** : Bleu clair (#64b5f6)

### **2. ☀️ Thème Clair (Light)**
- **Arrière-plan** : Dégradé bleu ciel (#e3f2fd → #90caf9)
- **Carte** : Fond blanc avec légère transparence
- **Texte** : Gris foncé
- **Bouton** : Dégradé bleu (#42a5f5 → #2196f3)
- **Lien** : Bleu foncé (#1976d2)

### **3. 🌿 Thème Défaut (Vert)**
- **Arrière-plan** : Dégradé vert (#1b5e20 → #43a047)
- **Carte** : Fond blanc avec transparence
- **Texte** : Gris foncé
- **Bouton** : Dégradé vert (#1b5e20 → #43a047)
- **Lien** : Vert foncé (#2e7d32)

## 🎨 **Caractéristiques du Thème**

### **📱 Responsive Design**
- ✅ **Adaptatif** : S'adapte à toutes les tailles d'écran
- ✅ **Mobile-first** : Optimisé pour les appareils mobiles
- ✅ **Centré** : Formulaire centré verticalement

### **🎯 Interface Utilisateur**
- ✅ **Carte flottante** : Effet de verre moderne
- ✅ **Ombres** : Ombres douces pour la profondeur
- ✅ **Transitions** : Animations fluides au survol
- ✅ **Focus** : États focus visibles

### **🔘 Formulaires**
- ✅ **Validation** : Messages d'erreur clairs
- **Focus** : Bordures colorées au focus
- **Placeholder** : Texte indicatif
- **Labels** : Étiquettes visibles

### **📢 Messages Flash**
- ✅ **Succès** : Vert clair avec icône
- ✅ **Erreur** : Rouge clair avec icône
- ✅ **Info** : Bleu clair avec icône
- ✅ **Positionnement** : En haut du formulaire

## 🛠️ **Implémentation Technique**

### **1. Détection du Thème**
```twig
{% set current_theme = app.session.get('theme', 'default') %}
<body class="{{ current_theme }}-theme">
```

### **2. Classes CSS Dynamiques**
```css
/* Thème Sombre */
body.dark-theme {
    background: linear-gradient(180deg, #1a237e 0%, #283593 50%, #3949ab 100%);
}

/* Thème Clair */
body.light-theme {
    background: linear-gradient(180deg, #e3f2fd 0%, #bbdefb 50%, #90caf9 100%);
}

/* Thème Défaut */
body.default-theme {
    background: linear-gradient(180deg, #1b5e20 0%, #2e7d32 50%, #43a047 100%);
}
```

### **3. Styles Spécifiques**
```css
/* Boutons adaptatifs */
body.dark-theme .btn-reset {
    background: linear-gradient(45deg, #3949ab, #5e35b1);
}

body.light-theme .btn-reset {
    background: linear-gradient(45deg, #42a5f5, #2196f3);
}

body.default-theme .btn-reset {
    background: linear-gradient(45deg, #1b5e20, #2e7d32);
}
```

## 🎯 **Expérience Utilisateur**

### **🔄 Changement de Thème**
1. **Navigation** : Menu déroulant dans la barre de navigation
2. **Session** : Thème sauvegardé en session
3. **Instantané** : Changement immédiat sans rechargement
4. **Persistant** : Thème appliqué à toutes les pages

### **📱 Accessibilité**
- ✅ **Contraste** : Ratios de contraste WCAG respectés
- ✅ **Lisibilité** : Polices de lecture adaptées
- ✅ **Navigation** : Liens et boutons clairs
- ✅ **Feedback** : États visibles et clairs

## 🎨 **Personnalisation**

### **🎨 Couleurs Principales**
- **Sombre** : Bleu nuit (#1a237e, #283593, #3949ab)
- **Clair** : Bleu ciel (#e3f2fd, #bbdefb, #90caf9)
- **Défaut** : Vert nature (#1b5e20, #2e7d32, #43a047)

### **🎨 Effets Visuels**
- **Arrière-plans** : Dégradés linéaires
- **Cartes** : Transparence et flou
- **Boutons** : Dégradés et survols
- **Textes** : Contrastes adaptés

## 📋 **Utilisation**

### **1. Accès au Formulaire**
```
https://127.0.0.1:8000/reset-password
```

### **2. Test des Thèmes**
- **Défaut** : Vert (automatique au chargement)
- **Clair** : Via le menu déroulant
- **Sombre** : Via le menu déroulant

### **3. Persistance**
- **Session** : Thème sauvegardé pendant la navigation
- **Global** : Appliqué à toutes les pages du site
- **Personnel** : Préférence utilisateur

## 🚀 **Avantages**

### **🎨 Design Moderne**
- **Esthétique** : Interface moderne et professionnelle
- **Cohérence** : Thème uniforme sur tout le site
- **Personnalisation** : Choix utilisateur respecté

### **🔧 Technique**
- **Performance** : CSS optimisé avec transitions
- **Maintenabilité** : Code organisé et commenté
- **Évolutivité** : Facile à étendre

### **👥 Expérience**
- **Confort** : Réduction de la fatigue visuelle
- **Accessibilité** : Adapté à tous les utilisateurs
- **Professionnal** : Image de marque soignée

---

## 🎯 **Conclusion**

Le formulaire de réinitialisation de mot de passe dispose maintenant de **3 thèmes dynamiques** qui s'adaptent automatiquement :

1. 🌿 **Thème Sombre** : Pour une utilisation nocturne
2. ☀️ **Thème Clair** : Pour une utilisation diurne
3. 🌿 **Thème Défaut** : Pour l'identité verte de l'application

**Le thème change dynamiquement en fonction du choix de l'utilisateur et reste persistant pendant toute la session !** 🎯
