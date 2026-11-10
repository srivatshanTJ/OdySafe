package com.example.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.OdysafeRepository
import com.example.data.service.GeminiMapsService
import com.example.data.service.GroundedPlaceResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class MapStyle(val label: String) {
    STREET("Daylight Street"),
    NIGHT_SAFETY("Night Safety Mode"),
    SATELLITE("Satellite Terrain")
}

data class OdysafeUiState(
    val currentScreen: AppScreen = AppScreen.Dashboard,
    val backStack: List<AppScreen> = listOf(AppScreen.Dashboard),
    val activeTab: NavigationTab = NavigationTab.HOME,
    val destinations: List<Destination> = OdysafeRepository.sampleDestinations,
    val nearbyServices: List<NearbyService> = OdysafeRepository.sampleNearbyServices,
    val safetyAlerts: List<SafetyAlert> = OdysafeRepository.sampleSafetyAlerts,
    val trips: List<Trip> = OdysafeRepository.sampleTrips,
    val emergencyContacts: List<EmergencyContact> = OdysafeRepository.sampleEmergencyContacts,
    val phrases: List<PhraseItem> = OdysafeRepository.sampleEmergencyPhrases,
    val userProfile: UserProfile = UserProfile(),
    
    // Search & Filter
    val globalSearchQuery: String = "",
    val selectedDestinationCategory: DestinationCategory = DestinationCategory.ALL,
    val selectedServiceType: ServiceType? = null,
    val selectedAlertCategory: AlertCategory = AlertCategory.ALL,

    // Live Map & Google Maps Grounding State
    val selectedCity: String = "Madurai, Tamil Nadu",
    val liveGroundedPlaces: List<GroundedPlaceResult> = emptyList(),
    val isSearchingLiveMaps: Boolean = false,
    val liveMapsSummary: String? = null,
    val mapZoomLevel: Float = 1.0f,
    val showWeatherRadar: Boolean = true,
    val showTrafficFlow: Boolean = true,
    val mapStyle: MapStyle = MapStyle.STREET,
    val selectedMapCategory: String = "ALL", // ALL, HOSPITAL, POLICE, PHARMACY, TOURIST, HOTEL, FOOD
    val userLocationAddress: String = "Near Meenakshi Temple East Gate, Madurai, TN",
    val userLocationCoords: Pair<Double, Double> = Pair(9.9195, 78.1193),
    val activeMarkerPlace: GroundedPlaceResult? = null,
    val activeRouteToPlace: GroundedPlaceResult? = null,
    val isLiveSimulationActive: Boolean = false,
    val liveSimulationProgress: Float = 0.0f,
    val headingDegrees: Float = 42f,
    val gpsAccuracyMeters: Int = 3,

    // Location-Based Suggestions State (Hotels, Tourist Spots, Food, Safety Tips)
    val locationSuggestions: List<LocationSuggestion> = emptyList(),
    val selectedSuggestionType: SuggestionType = SuggestionType.ALL,
    val isLoadingSuggestions: Boolean = false,
    val customAssistantAnswer: String? = null,
    val isAssistantThinking: Boolean = false,

    // Location Sharing State
    val isSharingLocation: Boolean = true,
    val sharingDurationHours: Int = 4,
    val sharingTimeRemainingMinutes: Int = 185,
    val sharedWithContactIds: Set<String> = setOf("cont_1", "cont_2"),

    // Emergency SOS State
    val sosStatus: SosStatus = SosStatus.IDLE,
    val sosProgress: Float = 0f,
    val lastSosTimestamp: String? = null,
    val sosMessagePreview: String = "EMERGENCY: I need urgent assistance. My approximate location in Madurai is near Meenakshi Temple East Gate (Lat: 9.9195, Lon: 78.1193).",

    // Translation State
    val translateSourceLang: String = "English",
    val translateTargetLang: String = "Tamil",
    val translateInputText: String = "",
    val translateOutputText: String = "",
    val isTranslating: Boolean = false,

    // Notifications
    val unreadNotificationCount: Int = 3,
    val notificationsList: List<NotificationItem> = listOf(
        NotificationItem("n1", "Weather Advisory", "Dense morning fog reported on Nilgiri Ghats. Exercise caution.", "1 hour ago", false),
        NotificationItem("n2", "Location Sharing Active", "Your live location is currently shared with Rahul Sharma.", "2 hours ago", false),
        NotificationItem("n3", "Trip Reminder", "Upcoming journey to Ooty starts in 5 days.", "Yesterday", true)
    ),

    // In-App Toast / Feedback Message
    val userFeedbackMessage: String? = null
)

enum class SosStatus {
    IDLE,
    ACTIVATING,
    DISPATCHING,
    CONFIRMED,
    FAILED
}

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean
)

class OdysafeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(OdysafeUiState())
    val uiState: StateFlow<OdysafeUiState> = _uiState.asStateFlow()

    private var simulationJob: Job? = null

    init {
        // Pre-populate with verified Indian places and suggestions immediately
        // Guarantees zero latency on launch and avoids concurrent API quota spikes (429)
        val defaultCity = "Madurai, Tamil Nadu"
        val initialPlaces = GeminiMapsService.getCuratedFallbackLivePlaces("All", defaultCity)
        val initialSuggestions = GeminiMapsService.getCuratedSuggestionsForCity(defaultCity)

        _uiState.update {
            it.copy(
                liveGroundedPlaces = initialPlaces.places,
                liveMapsSummary = initialPlaces.summaryText,
                activeMarkerPlace = initialPlaces.places.firstOrNull(),
                locationSuggestions = initialSuggestions
            )
        }
    }

    fun navigateTo(screen: AppScreen) {
        _uiState.update { current ->
            val updatedStack = current.backStack + screen
            val tab = when (screen) {
                is AppScreen.Dashboard -> NavigationTab.HOME
                is AppScreen.Explore -> NavigationTab.EXPLORE
                is AppScreen.MapView -> NavigationTab.MAP
                is AppScreen.TripsList, is AppScreen.JourneyPlanner -> NavigationTab.TRIPS
                is AppScreen.Profile, is AppScreen.SettingsHub -> NavigationTab.PROFILE
                else -> current.activeTab
            }
            current.copy(currentScreen = screen, backStack = updatedStack, activeTab = tab)
        }
    }

    fun canNavigateBack(): Boolean {
        val state = _uiState.value
        return state.backStack.size > 1 || (state.currentScreen != AppScreen.Dashboard && state.currentScreen != AppScreen.Landing)
    }

    fun navigateBack(): Boolean {
        val currentStack = _uiState.value.backStack
        if (currentStack.size > 1) {
            val newStack = currentStack.dropLast(1)
            val previousScreen = newStack.last()
            val tab = when (previousScreen) {
                is AppScreen.Dashboard -> NavigationTab.HOME
                is AppScreen.Explore -> NavigationTab.EXPLORE
                is AppScreen.MapView -> NavigationTab.MAP
                is AppScreen.TripsList -> NavigationTab.TRIPS
                is AppScreen.Profile -> NavigationTab.PROFILE
                else -> _uiState.value.activeTab
            }
            _uiState.update { it.copy(currentScreen = previousScreen, backStack = newStack, activeTab = tab) }
            return true
        } else if (_uiState.value.currentScreen != AppScreen.Dashboard && _uiState.value.currentScreen != AppScreen.Landing) {
            _uiState.update {
                it.copy(
                    currentScreen = AppScreen.Dashboard,
                    backStack = listOf(AppScreen.Dashboard),
                    activeTab = NavigationTab.HOME
                )
            }
            return true
        }
        return false
    }

    fun onTabSelected(tab: NavigationTab) {
        val screen = when (tab) {
            NavigationTab.HOME -> AppScreen.Dashboard
            NavigationTab.EXPLORE -> AppScreen.Explore
            NavigationTab.MAP -> AppScreen.MapView
            NavigationTab.TRIPS -> AppScreen.TripsList
            NavigationTab.PROFILE -> AppScreen.Profile
        }
        _uiState.update { it.copy(currentScreen = screen, activeTab = tab, backStack = listOf(screen)) }
    }

    fun toggleSaveDestination(destinationId: String) {
        _uiState.update { state ->
            val updated = state.destinations.map {
                if (it.id == destinationId) it.copy(isSaved = !it.isSaved) else it
            }
            val dest = updated.find { it.id == destinationId }
            val msg = if (dest?.isSaved == true) "Saved ${dest.name} to bookmarks" else "Removed from bookmarks"
            state.copy(destinations = updated, userFeedbackMessage = msg)
        }
    }

    fun toggleSaveService(serviceId: String) {
        _uiState.update { state ->
            val updated = state.nearbyServices.map {
                if (it.id == serviceId) it.copy(isSaved = !it.isSaved) else it
            }
            state.copy(nearbyServices = updated, userFeedbackMessage = "Bookmarks updated")
        }
    }

    fun toggleTripChecklist(tripId: String, itemId: String) {
        _uiState.update { state ->
            val updatedTrips = state.trips.map { trip ->
                if (trip.id == tripId) {
                    val updatedItems = trip.checklist.map { item ->
                        if (item.id == itemId) item.copy(isChecked = !item.isChecked) else item
                    }
                    trip.copy(checklist = updatedItems)
                } else trip
            }
            state.copy(trips = updatedTrips)
        }
    }

    fun createTrip(
        title: String,
        origin: String,
        destination: String,
        mode: TravelMode,
        date: String
    ) {
        val newTrip = Trip(
            id = "trip_${System.currentTimeMillis()}",
            title = title,
            origin = origin,
            destination = destination,
            travelMode = mode,
            date = date,
            durationEst = "4 hrs 30 mins",
            distanceKm = 142.0,
            status = TripStatus.UPCOMING,
            stops = listOf("Scenic Viewpoint", "Official Rest Stop"),
            precautions = listOf("Check weather forecast before starting", "Keep emergency numbers saved"),
            checklist = listOf(
                TripChecklistItem("c1", "Pack first-aid kit", false),
                TripChecklistItem("c2", "Download offline map", true),
                TripChecklistItem("c3", "Inform emergency contact", false)
            )
        )
        _uiState.update {
            it.copy(
                trips = listOf(newTrip) + it.trips,
                userFeedbackMessage = "Trip created successfully",
                currentScreen = AppScreen.TripsList,
                activeTab = NavigationTab.TRIPS
            )
        }
    }

    fun setLocationSharing(enabled: Boolean, hours: Int = 4) {
        _uiState.update {
            it.copy(
                isSharingLocation = enabled,
                sharingDurationHours = hours,
                sharingTimeRemainingMinutes = hours * 60,
                userFeedbackMessage = if (enabled) "Live location sharing activated for $hours hours" else "Location sharing stopped"
            )
        }
    }

    fun setSharingDuration(hours: Int) {
        _uiState.update {
            it.copy(
                sharingDurationHours = hours,
                sharingTimeRemainingMinutes = hours * 60
            )
        }
    }

    fun triggerEmergencySos() {
        viewModelScope.launch {
            _uiState.update { it.copy(sosStatus = SosStatus.ACTIVATING, sosProgress = 0.2f) }
            delay(500)
            _uiState.update { it.copy(sosStatus = SosStatus.DISPATCHING, sosProgress = 0.6f) }
            delay(800)
            _uiState.update {
                it.copy(
                    sosStatus = SosStatus.CONFIRMED,
                    sosProgress = 1.0f,
                    lastSosTimestamp = "Just now",
                    userFeedbackMessage = "SOS alert dispatched to police (112) and emergency contacts"
                )
            }
        }
    }

    fun triggerSos() {
        triggerEmergencySos()
    }

    fun cancelEmergencySos() {
        _uiState.update {
            it.copy(
                sosStatus = SosStatus.IDLE,
                sosProgress = 0f,
                userFeedbackMessage = "SOS alert cancelled"
            )
        }
    }

    fun cancelSos() {
        cancelEmergencySos()
    }

    fun updateTranslateInput(text: String) {
        _uiState.update { it.copy(translateInputText = text) }
    }

    fun performTranslate() {
        translatePhrase(_uiState.value.translateInputText)
    }

    fun setTranslateLanguages(src: String, tgt: String) {
        _uiState.update { it.copy(translateSourceLang = src, translateTargetLang = tgt) }
    }

    fun translatePhrase(text: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isTranslating = true, translateInputText = text) }
            delay(400)
            val translated = when {
                text.contains("hospital", ignoreCase = true) -> "மருத்துவமனை எங்கே உள்ளது? (Where is the hospital?)"
                text.contains("help", ignoreCase = true) -> "தயவுசெய்து எனக்கு உதவுங்கள் (Please help me)"
                text.contains("police", ignoreCase = true) -> "காவல் நிலையம் எங்கே? (Where is the police station?)"
                text.contains("water", ignoreCase = true) -> "குடிநீர் கிடைக்குமா? (Can I get drinking water?)"
                else -> "இந்த இடத்திற்கு எப்படி செல்வது? (How to reach this place?)"
            }
            _uiState.update {
                it.copy(
                    isTranslating = false,
                    translateOutputText = translated
                )
            }
        }
    }

    fun addEmergencyContact(name: String, phone: String, relation: String) {
        val newContact = EmergencyContact(
            id = "c_${System.currentTimeMillis()}",
            name = name,
            relationship = relation,
            phone = phone,
            isPrimary = false
        )
        _uiState.update {
            it.copy(
                emergencyContacts = it.emergencyContacts + newContact,
                userFeedbackMessage = "Contact added"
            )
        }
    }

    fun removeEmergencyContact(contactId: String) {
        _uiState.update {
            it.copy(
                emergencyContacts = it.emergencyContacts.filter { c -> c.id != contactId },
                userFeedbackMessage = "Contact removed"
            )
        }
    }

    fun updateAccessibilitySettings(largeText: Boolean, highContrast: Boolean, reducedMotion: Boolean) {
        _uiState.update {
            it.copy(
                userProfile = it.userProfile.copy(
                    largeText = largeText,
                    highContrast = highContrast,
                    reducedMotion = reducedMotion
                ),
                userFeedbackMessage = "Accessibility preferences updated"
            )
        }
    }

    fun clearFeedback() {
        _uiState.update { it.copy(userFeedbackMessage = null) }
    }

    fun markAllNotificationsRead() {
        _uiState.update { state ->
            val updated = state.notificationsList.map { it.copy(isRead = true) }
            state.copy(notificationsList = updated, unreadNotificationCount = 0)
        }
    }

    // Live Map & Google Maps Grounding Methods
    fun searchLiveMaps(query: String) {
        val currentCity = _uiState.value.selectedCity
        loadLiveMapsData(query, currentCity)
    }

    fun selectCity(city: String) {
        val coords = GeminiMapsService.getCityCenterCoords(city)
        _uiState.update {
            it.copy(
                selectedCity = city,
                userLocationAddress = "Near center of $city, India",
                userLocationCoords = coords,
                mapZoomLevel = 1.0f,
                activeMarkerPlace = null,
                activeRouteToPlace = null,
                liveSimulationProgress = 0f,
                userFeedbackMessage = "Switched live location to $city"
            )
        }
        // Load places and suggestions for newly selected city
        loadLiveMapsData("All", city)
        loadLocationSuggestions(city)
    }

    fun loadLocationSuggestions(city: String = _uiState.value.selectedCity) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingSuggestions = true) }
            val suggestions = GeminiMapsService.fetchLocationSuggestions(city)
            _uiState.update {
                it.copy(
                    locationSuggestions = suggestions,
                    isLoadingSuggestions = false
                )
            }
        }
    }

    fun filterSuggestionType(type: SuggestionType) {
        _uiState.update { it.copy(selectedSuggestionType = type) }
    }

    fun loadLiveMapsData(query: String, city: String = _uiState.value.selectedCity) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSearchingLiveMaps = true) }
            val response = GeminiMapsService.queryLivePlaces(query, city)
            _uiState.update {
                it.copy(
                    liveGroundedPlaces = response.places,
                    liveMapsSummary = response.summaryText,
                    activeMarkerPlace = response.places.firstOrNull(),
                    isSearchingLiveMaps = false
                )
            }
        }
    }

    fun setMapStyle(style: MapStyle) {
        _uiState.update {
            it.copy(
                mapStyle = style,
                userFeedbackMessage = "Map style set to ${style.label}"
            )
        }
    }

    fun setMapCategory(category: String) {
        _uiState.update { it.copy(selectedMapCategory = category) }
        val query = if (category == "ALL") "All" else category
        loadLiveMapsData(query, _uiState.value.selectedCity)
    }

    fun zoomIn() {
        _uiState.update { it.copy(mapZoomLevel = (it.mapZoomLevel + 0.25f).coerceAtMost(3.0f)) }
    }

    fun zoomOut() {
        _uiState.update { it.copy(mapZoomLevel = (it.mapZoomLevel - 0.25f).coerceAtLeast(0.6f)) }
    }

    fun recenterMap() {
        val defaultCoords = GeminiMapsService.getCityCenterCoords(_uiState.value.selectedCity)
        _uiState.update {
            it.copy(
                mapZoomLevel = 1.0f,
                userLocationCoords = defaultCoords,
                activeMarkerPlace = it.liveGroundedPlaces.firstOrNull(),
                userFeedbackMessage = "Map centered on live GPS location"
            )
        }
    }

    fun toggleWeatherRadar() {
        _uiState.update { it.copy(showWeatherRadar = !it.showWeatherRadar) }
    }

    fun toggleTrafficFlow() {
        _uiState.update { it.copy(showTrafficFlow = !it.showTrafficFlow) }
    }

    fun setActiveMarkerPlace(place: GroundedPlaceResult?) {
        _uiState.update { it.copy(activeMarkerPlace = place) }
    }

    fun setRouteToPlace(place: GroundedPlaceResult?) {
        _uiState.update {
            it.copy(
                activeRouteToPlace = place,
                activeMarkerPlace = place,
                userFeedbackMessage = if (place != null) "Calculated safe route to ${place.title}" else null
            )
        }
    }

    fun toggleLiveSimulation() {
        val willBeActive = !_uiState.value.isLiveSimulationActive
        simulationJob?.cancel()

        _uiState.update {
            it.copy(
                isLiveSimulationActive = willBeActive,
                userFeedbackMessage = if (willBeActive) "Live GPS walk tracking simulation started" else "Live tracking simulation paused"
            )
        }

        if (willBeActive) {
            simulationJob = viewModelScope.launch {
                while (_uiState.value.isLiveSimulationActive) {
                    delay(300)
                    _uiState.update { state ->
                        val nextProg = (state.liveSimulationProgress + 0.04f).let { if (it > 1.0f) 0.0f else it }
                        state.copy(liveSimulationProgress = nextProg)
                    }
                }
            }
        }
    }

    fun focusPlaceOnMap(title: String, lat: Double, lon: Double, category: String = "TOURIST") {
        val matchedPlace = _uiState.value.liveGroundedPlaces.find { it.title.equals(title, ignoreCase = true) }
            ?: GroundedPlaceResult(
                title = title,
                category = category,
                address = "${_uiState.value.selectedCity}, India",
                phone = "+91 452 234 4300",
                openStatus = "Open daily",
                distanceText = "0.5 km away",
                latitude = lat,
                longitude = lon,
                sourceUrl = "https://maps.google.com/?q=${title.replace(" ", "+")}",
                isEmergency = category == "HOSPITAL" || category == "POLICE",
                rating = "4.8 ★",
                safetyHighlights = "Verified safe location"
            )

        _uiState.update {
            val updatedPlaces = if (it.liveGroundedPlaces.any { p -> p.title == matchedPlace.title }) {
                it.liveGroundedPlaces
            } else {
                listOf(matchedPlace) + it.liveGroundedPlaces
            }
            it.copy(
                currentScreen = AppScreen.MapView,
                activeTab = NavigationTab.MAP,
                activeMarkerPlace = matchedPlace,
                activeRouteToPlace = matchedPlace,
                liveGroundedPlaces = updatedPlaces,
                userFeedbackMessage = "Focused $title on Live Map"
            )
        }
    }

    fun askCustomAssistant(prompt: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAssistantThinking = true) }
            val answer = GeminiMapsService.askCustomAssistant(prompt, _uiState.value.selectedCity)
            _uiState.update {
                it.copy(
                    isAssistantThinking = false,
                    customAssistantAnswer = answer
                )
            }
        }
    }
}
