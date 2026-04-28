# Intégration Google Maps - Documentation

## Vue d'ensemble

L'intégration Google Maps a été ajoutée pour permettre aux utilisateurs de sélectionner facilement des lieux pour les événements. Cette fonctionnalité utilise JavaFX WebView pour afficher des cartes interactives.

## Fonctionnalités

### 1. Sélection de lieu avec Google Maps
- **Bouton "📍 Ouvrir Map"** dans le formulaire d'ajout d'événement
- **Fenêtre de dialogue** avec recherche et affichage de carte
- **Recherche d'adresses** en temps réel
- **Sélection et validation** de l'adresse

### 2. Service GoogleMapsService
- Génération d'URL d'intégration Google Maps
- Support avec et sans clé API
- Génération de HTML personnalisé pour WebView
- Formatage et validation d'adresses

## Composants techniques

### 1. Dépendances Maven
```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-web</artifactId>
    <version>${javafx.version}</version>
</dependency>
```

### 2. Fichiers créés/modifiés

#### Nouveaux fichiers:
- `src/main/java/services/GoogleMapsService.java` - Service d'intégration Google Maps
- `src/main/java/controller/MapDialogController.java` - Contrôleur de la fenêtre de carte
- `src/main/resources/fxml/map_dialog.fxml` - Interface de la fenêtre de carte

#### Fichiers modifiés:
- `pom.xml` - Ajout de la dépendance JavaFX WebView
- `src/main/java/controller/AjouterEvenementController.java` - Intégration de la fonctionnalité de carte

## Configuration

### Clé API Google Maps (Optionnel)

Pour une meilleure expérience, configurez une clé API Google Maps:

1. Créez un projet dans la [Google Cloud Console](https://console.cloud.google.com/)
2. Activez l'API "Maps Embed API"
3. Obtenez une clé API
4. Modifiez la constante `GOOGLE_MAPS_API_KEY` dans `GoogleMapsService.java`:

```java
private static final String GOOGLE_MAPS_API_KEY = "VOTRE_CLÉ_API_ICI";
```

**Note:** L'application fonctionne sans clé API en utilisant la recherche Google Maps standard.

## Utilisation

### Pour les développeurs

1. **Ajout d'un événement:**
   - Cliquez sur "📍 Ouvrir Map" dans le champ "Lieu"
   - Recherchez une adresse dans la fenêtre de dialogue
   - Cliquez sur "✓ Utiliser" pour valider la sélection

2. **Personnalisation:**
   - Modifiez `GoogleMapsService.java` pour changer le comportement
   - Personnalisez `map_dialog.fxml` pour modifier l'interface
   - Adaptez `MapDialogController.java` pour ajouter des fonctionnalités

### Pour les utilisateurs

1. Dans le formulaire d'ajout d'événement, cliquez sur le bouton "📍 Ouvrir Map"
2. Entrez une adresse complète (numéro, rue, ville)
3. Cliquez sur "🔍 Rechercher" pour afficher la carte
4. Cliquez sur "✓ Utiliser" pour sélectionner l'adresse
5. L'adresse sélectionnée s'affiche automatiquement dans le champ "Lieu"

## Dépannage

### Problèmes courants

1. **Carte ne s'affiche pas:**
   - Vérifiez la connexion internet
   - Assurez-vous que JavaFX WebView fonctionne correctement
   - Consultez les logs de la console pour les erreurs

2. **Recherche ne fonctionne pas:**
   - Vérifiez que l'adresse est bien formatée
   - Essayez avec des adresses plus simples
   - Assurez-vous que JavaScript est activé dans WebView

3. **Performance:**
   - La carte peut prendre quelques secondes à charger
   - Évitez les requêtes trop fréquentes

### Limitations

- Nécessite une connexion internet active
- La performance dépend de la vitesse de connexion
- Sans clé API, les fonctionnalités sont limitées à la recherche standard

## Améliorations futures

1. **Géocodage avancé:** Conversion automatique des coordonnées GPS
2. **Sauvegarde des favoris:** Mémorisation des lieux fréquemment utilisés
3. **Autocomplétion:** Suggestions d'adresses en temps réel
4. **Mode hors ligne:** Cache des cartes consultées
5. **Intégration GPS:** Détection automatique de la position actuelle

## Support

Pour toute question ou problème concernant l'intégration Google Maps, consultez:
- La documentation JavaFX WebView
- La documentation Google Maps Embed API
- Les logs de l'application pour les erreurs spécifiques
