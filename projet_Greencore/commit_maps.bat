@echo off
echo Adding Google Maps integration to Git...
cd /d "C:\Users\User_01\projet_Greencore"
git add .
git commit -m "feat: Add Google Maps integration for event visualization

- Add MapController with JavaScript bridge
- Implement interactive Google Maps with event markers
- Create map_view.fxml with WebView interface
- Add map button to event management interface
- Include geocoding for event locations
- Add comprehensive documentation
- Support real-time event updates on map"
git push origin main
echo.
echo Google Maps integration committed and pushed to GitHub!
pause
