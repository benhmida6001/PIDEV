# Google Maps Integration for GreenCore

## Overview
This integration adds interactive Google Maps functionality to your GreenCore application, allowing users to visualize event locations on a map.

## Features
- 🗺️ **Interactive Map**: Display all events as markers on Google Maps
- 📍 **Location Markers**: Each event shows as a green marker with location information
- 🔄 **Real-time Updates**: Map refreshes to show current event data
- 🔍 **Geocoding**: Automatically converts event addresses to map coordinates
- 📱 **Responsive Design**: Map adapts to window size

## Files Created

### Java Controllers
- `MapController.java` - Main map controller handling JavaScript bridge and event loading
- `GestionEvenementsController.java` - Updated with map button handler

### FXML Layouts
- `map_view.fxml` - Map interface with WebView and control buttons
- `gestion_evenements.fxml` - Updated with "🗺️ Carte" button

### Web Resources
- `map.html` - Google Maps implementation with JavaScript API

## How to Use

### 1. Access the Map
1. Launch your GreenCore application
2. Go to "Gestion des événements" 
3. Click the **"🗺️ Carte"** button
4. Map window opens showing all event locations

### 2. Map Controls
- **🔄 Actualiser**: Reload events and refresh markers
- **📍 Tunis**: Center map on Tunis (default location)
- **Click markers**: View event details in info window

### 3. Event Information
Each marker shows:
- Event name
- Location address
- Description
- "Voir détails" button (for future enhancement)

## Technical Implementation

### Architecture
```
JavaFX Application
├── MapController (Java)
│   ├── Loads events from EvenementService
│   ├── Manages WebView and WebEngine
│   └── JavaScript bridge for communication
├── map.html (JavaScript)
│   ├── Google Maps API integration
│   ├── Geocoding addresses to coordinates
│   └── Interactive markers and info windows
└── WebView (JavaFX)
    └── Renders HTML/JavaScript map
```

### Data Flow
1. **MapController.initialize()** → Loads map.html in WebView
2. **JavaScript ready** → Notifies Java via bridge
3. **Java loads events** → From EvenementService
4. **Geocoding** → JavaScript converts addresses to coordinates
5. **Markers added** → Events displayed on map

## Requirements

### Dependencies (Already Included)
- ✅ JavaFX WebView (javafx-web)
- ✅ JavaFX Controls (javafx-controls)
- ✅ EvenementService integration

### External Requirements
- 🌐 **Internet Connection**: Required for Google Maps API
- 🗺️ **Google Maps Access**: Works without API key for development

## Troubleshooting

### Map Not Loading
1. Check internet connection
2. Verify WebView is enabled in your JavaFX setup
3. Check console for JavaScript errors

### Markers Not Showing
1. Ensure events have valid addresses in `lieuEvent` field
2. Check EvenementService is working correctly
3. Verify database connection

### Performance Issues
1. Limit number of events displayed (consider pagination)
2. Optimize geocoding (cache coordinates)
3. Use marker clustering for many events

## Future Enhancements

### Planned Features
- 📍 **Add Event**: Click on map to add new event location
- ✏️ **Edit Locations**: Drag markers to update event locations  
- 🔍 **Advanced Search**: Filter events by map area
- 🚗 **Directions**: Show routes to event locations
- 📊 **Heat Map**: Show event density areas
- 🎨 **Custom Styles**: Match map to GreenCore theme

### API Integration
- 🔑 **Google Maps API Key**: Add for production use
- 📊 **Analytics**: Track map usage
- 🌍 **Multiple Maps**: Support different map providers

## Security Notes

### JavaScript Bridge Security
- All JavaScript strings are properly escaped
- No direct SQL queries from JavaScript
- Validation on Java side for all data

### Data Privacy
- Only event locations are shared with Google Maps
- No personal user data sent to external services
- Geocoding requests are minimal

## Support

For issues with the Google Maps integration:
1. Check this README for common solutions
2. Verify all files are correctly placed
3. Test EvenementService separately
4. Check JavaFX WebView configuration

---

**Integration Complete! 🎉**

Your GreenCore application now has fully functional Google Maps integration for visualizing event locations.
