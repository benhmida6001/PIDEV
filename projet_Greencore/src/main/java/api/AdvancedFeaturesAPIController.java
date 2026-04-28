package api;

import java.util.Random;
import java.util.HashMap;
import java.util.Map;

/**
 * API Controller for advanced features and statistics
 * Provides mock data for demonstration purposes
 */
public class AdvancedFeaturesAPIController {
    
    private final Random random = new Random();
    
    /**
     * Get system statistics as JSON string
     */
    public String getSystemStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", 1234);
        stats.put("totalEvents", 567);
        stats.put("totalPoints", 89456);
        stats.put("averageEngagementScore", 87.5);
        
        return mapToJson(stats);
    }
    
    /**
     * Get user growth statistics as JSON string
     */
    public String getUserGrowthStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", 1234);
        stats.put("projectedNextMonth", 1456);
        stats.put("growthRate", 15.8);
        
        return mapToJson(stats);
    }
    
    /**
     * Get event performance statistics as JSON string
     */
    public String getEventPerformanceStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEvents", 567);
        stats.put("averageCapacity", 89.5);
        stats.put("averagePopularity", 92.3);
        stats.put("topEvent", "Conférence Green Tech");
        stats.put("maxPoints", 150);
        
        return mapToJson(stats);
    }
    
    /**
     * Calculate user engagement score for a given user ID
     */
    public double calculateUserEngagementScore(int userId) {
        // Mock calculation based on user ID
        return 60.0 + (userId % 40) + random.nextDouble() * 20;
    }
    
    /**
     * Calculate event popularity index for a given event ID
     */
    public double calculateEventPopularityIndex(int eventId) {
        // Mock calculation based on event ID
        return 70.0 + (eventId % 30) + random.nextDouble() * 30;
    }
    
    /**
     * Calculate days until a given event
     */
    public long calculateDaysUntilEvent(int eventId) {
        // Mock calculation - return days until event
        return 15 + (eventId % 30);
    }
    
    /**
     * Simple Map to JSON converter (basic implementation)
     */
    private String mapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                json.append(",");
            }
            json.append("\"").append(entry.getKey()).append("\":");
            
            Object value = entry.getValue();
            if (value instanceof String) {
                json.append("\"").append(value).append("\"");
            } else {
                json.append(value);
            }
            
            first = false;
        }
        
        json.append("}");
        return json.toString();
    }
}
