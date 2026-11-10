package com.example.ui.state

sealed class AppScreen {
    // Public & Onboarding
    data object Landing : AppScreen()
    data object AuthLogin : AppScreen()
    data object AuthSignup : AppScreen()
    data class AuthVerify(val emailOrPhone: String) : AppScreen()
    data class Onboarding(val step: Int = 1) : AppScreen()

    // Main App Navigation
    data object Dashboard : AppScreen()
    data object Explore : AppScreen()
    data class DestinationDetail(val destinationId: String) : AppScreen()
    data object MapView : AppScreen()
    data object JourneyPlanner : AppScreen()
    data class TripDetail(val tripId: String) : AppScreen()
    data object TripsList : AppScreen()
    data class NearbyServices(val filterType: String = "ALL") : AppScreen()
    data object SafetyOverview : AppScreen()
    data class AreaSafetyDetail(val alertId: String) : AppScreen()
    data object AlertsCenter : AppScreen()
    data object Translate : AppScreen()
    data class LocationSuggestions(val city: String = "") : AppScreen()
    data object ShareLocation : AppScreen()
    data object Emergency : AppScreen()
    data object EmergencyContacts : AppScreen()
    data object SavedPlaces : AppScreen()
    data object Notifications : AppScreen()
    data object Profile : AppScreen()
    data object SettingsHub : AppScreen()
    data object SettingsPrivacy : AppScreen()
    data object SettingsLanguage : AppScreen()
    data object SettingsAccessibility : AppScreen()
    data object SettingsNotifications : AppScreen()
    data object HelpCenter : AppScreen()
    data object OfflineView : AppScreen()
}

enum class NavigationTab {
    HOME,
    EXPLORE,
    MAP,
    TRIPS,
    PROFILE
}
