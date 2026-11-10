package com.example.data.model

enum class TravelMode(val label: String, val iconName: String) {
    WALKING("Walking", "DirectionsWalk"),
    CYCLING("Cycling", "DirectionsBike"),
    CAR("Car", "DirectionsCar"),
    BUS("Bus", "DirectionsBus"),
    TRAIN("Train", "Train")
}

enum class DestinationCategory(val label: String) {
    ALL("All"),
    HERITAGE("Heritage"),
    NATURE("Nature"),
    BEACHES("Beaches"),
    HILLS("Hills"),
    SPIRITUAL("Spiritual"),
    ADVENTURE("Adventure"),
    FAMILY("Family")
}

data class Destination(
    val id: String,
    val name: String,
    val state: String,
    val category: DestinationCategory,
    val tagline: String,
    val description: String,
    val bestTimeToVisit: String,
    val accessibilityRating: String, // e.g., "Wheelchair accessible paths in central temple"
    val entryFee: String,
    val openingHours: String,
    val safetyStatus: String, // "Normal precautions", "Monsoon advisory active"
    val safetySeverity: AlertSeverity,
    val safetyNotice: String,
    val safetySource: String,
    val safetyTimestamp: String,
    val transportHighlights: List<String>,
    val highlights: List<String>,
    val suggestedItinerary: List<String>,
    val latitude: Double,
    val longitude: Double,
    val isSaved: Boolean = false
)

enum class ServiceType(val label: String) {
    HOSPITAL("Hospitals & Medical"),
    PHARMACY("Pharmacies"),
    POLICE("Police Stations"),
    HOTEL("Hotels & Stays"),
    RESTAURANT("Restaurants & Food"),
    ATTRACTION("Attractions")
}

data class NearbyService(
    val id: String,
    val name: String,
    val type: ServiceType,
    val address: String,
    val distanceKm: Double,
    val phone: String,
    val isEmergencyFacility: Boolean,
    val openHours: String,
    val verifiedBadge: Boolean = true,
    val latitude: Double,
    val longitude: Double,
    val isSaved: Boolean = false
)

enum class AlertSeverity {
    INFO,
    CAUTION,
    DANGER
}

enum class AlertCategory(val label: String) {
    ALL("All"),
    WEATHER("Weather"),
    OFFICIAL("Official Advisories"),
    ROUTE("Route Alerts"),
    SAVED("Saved Destinations")
}

data class SafetyAlert(
    val id: String,
    val title: String,
    val description: String,
    val affectedArea: String,
    val severity: AlertSeverity,
    val category: AlertCategory,
    val source: String,
    val timestamp: String,
    val recommendedActions: List<String>,
    val safeAlternative: String? = null
)

data class Trip(
    val id: String,
    val title: String,
    val origin: String,
    val destination: String,
    val travelMode: TravelMode,
    val date: String,
    val durationEst: String,
    val distanceKm: Double,
    val status: TripStatus,
    val stops: List<String>,
    val precautions: List<String>,
    val checklist: List<TripChecklistItem>,
    val isSharingLocation: Boolean = false
)

enum class TripStatus {
    UPCOMING,
    ACTIVE,
    COMPLETED
}

data class TripChecklistItem(
    val id: String,
    val text: String,
    val isChecked: Boolean = false
)

data class EmergencyContact(
    val id: String,
    val name: String,
    val relationship: String,
    val phone: String,
    val isPrimary: Boolean = false,
    val receivesLocationUpdates: Boolean = true
)

data class PhraseItem(
    val id: String,
    val category: String,
    val english: String,
    val hindi: String,
    val tamil: String,
    val pronunciation: String
)

enum class SuggestionType(val label: String) {
    ALL("All Suggestions"),
    TOURIST("Tourist Spots"),
    HOTEL("Hotels & Stays"),
    FOOD("Food & Dining"),
    SAFETY_TIP("Safety & Transit Tips")
}

data class LocationSuggestion(
    val id: String,
    val title: String,
    val type: SuggestionType,
    val subtitle: String,
    val description: String,
    val rating: String = "4.8 ★",
    val distanceText: String,
    val safetyTip: String,
    val bestTimeToVisit: String? = null,
    val address: String,
    val phone: String? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isVerified: Boolean = true
)

data class UserProfile(
    val name: String = "Ananya Sharma",
    val email: String = "ananya.travels@example.com",
    val phone: String = "+91 98765 43210",
    val bloodGroup: String = "O+",
    val medicalNotes: String = "Allergic to penicillin. Carries inhaler.",
    val preferredLanguage: String = "English",
    val reducedMotion: Boolean = false,
    val highContrast: Boolean = false,
    val largeText: Boolean = false
)
