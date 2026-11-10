package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DestinationCategory
import com.example.ui.components.OdysafeBottomNavigationBar
import com.example.ui.components.OdysafeTopAppBar
import com.example.ui.screens.*
import com.example.ui.state.*
import com.example.ui.theme.OdysafeMint
import com.example.ui.theme.OdysafeNavy
import com.example.ui.theme.OdysafeTheme

class MainActivity : ComponentActivity() {
    private val viewModel: OdysafeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            OdysafeTheme {
                val uiState by viewModel.uiState.collectAsState()
                val context = LocalContext.current
                val configuration = LocalConfiguration.current
                val isTablet = configuration.screenWidthDp >= 600

                // System Dialer helper
                val dialNumber: (String) -> Unit = { rawNumber ->
                    val cleanNumber = rawNumber.replace(" ", "").replace("-", "")
                    try {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:$cleanNumber")
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Calling $cleanNumber", Toast.LENGTH_SHORT).show()
                    }
                }

                // Show toast for feedback
                LaunchedEffect(uiState.userFeedbackMessage) {
                    uiState.userFeedbackMessage?.let { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        viewModel.clearFeedback()
                    }
                }

                val canGoBack = uiState.backStack.size > 1 ||
                        (uiState.currentScreen != AppScreen.Dashboard && uiState.currentScreen != AppScreen.Landing)

                // Handle system back navigation
                BackHandler(enabled = canGoBack) {
                    viewModel.navigateBack()
                }

                val currentScreen = uiState.currentScreen
                val showBottomNav = currentScreen in listOf(
                    AppScreen.Dashboard,
                    AppScreen.Explore,
                    AppScreen.MapView,
                    AppScreen.TripsList,
                    AppScreen.Profile
                )

                val screenTitle = when (currentScreen) {
                    is AppScreen.Landing -> "Odysafe"
                    is AppScreen.AuthLogin -> "Sign In"
                    is AppScreen.AuthSignup -> "Create Account"
                    is AppScreen.AuthVerify -> "Verify Account"
                    is AppScreen.Onboarding -> "Welcome to Odysafe"
                    is AppScreen.Dashboard -> "Odysafe"
                    is AppScreen.Explore -> "Explore Destinations"
                    is AppScreen.DestinationDetail -> "Destination Details"
                    is AppScreen.MapView -> "Interactive Safety Map"
                    is AppScreen.JourneyPlanner -> "Journey Planner"
                    is AppScreen.TripDetail -> "Trip Details"
                    is AppScreen.TripsList -> "My Journeys"
                    is AppScreen.NearbyServices -> "Nearby Services"
                    is AppScreen.SafetyOverview, is AppScreen.AlertsCenter -> "Safety & Alerts"
                    is AppScreen.AreaSafetyDetail -> "Safety Advisory"
                    is AppScreen.Translate -> "Travel Translator"
                    is AppScreen.LocationSuggestions -> "Local Guide & Suggestions"
                    is AppScreen.ShareLocation -> "Location Sharing"
                    is AppScreen.Emergency -> "Emergency SOS"
                    is AppScreen.EmergencyContacts -> "Emergency Contacts"
                    is AppScreen.SavedPlaces -> "Saved Places"
                    is AppScreen.Notifications -> "Notifications"
                    is AppScreen.Profile -> "Profile"
                    is AppScreen.SettingsHub -> "Settings"
                    is AppScreen.SettingsAccessibility -> "Accessibility"
                    is AppScreen.SettingsPrivacy -> "Privacy"
                    is AppScreen.SettingsLanguage -> "Language"
                    is AppScreen.SettingsNotifications -> "Notifications"
                    is AppScreen.HelpCenter -> "Help & FAQs"
                    is AppScreen.OfflineView -> "Offline Guide"
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (currentScreen !is AppScreen.Landing && currentScreen !is AppScreen.Onboarding) {
                            OdysafeTopAppBar(
                                title = screenTitle,
                                canNavigateBack = canGoBack,
                                onNavigateBack = { viewModel.navigateBack() },
                                onEmergencyClick = { viewModel.navigateTo(AppScreen.Emergency) },
                                onNotificationsClick = { viewModel.navigateTo(AppScreen.Notifications) },
                                unreadNotifications = uiState.unreadNotificationCount
                            )
                        }
                    },
                    bottomBar = {
                        if (showBottomNav && !isTablet) {
                            OdysafeBottomNavigationBar(
                                currentTab = uiState.activeTab,
                                onTabSelected = { tab -> viewModel.onTabSelected(tab) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // Adaptive Tablet Navigation Rail (expanded screens >= 600dp)
                        if (showBottomNav && isTablet) {
                            NavigationRail(
                                containerColor = Color.White,
                                contentColor = OdysafeNavy
                            ) {
                                NavigationRailItem(
                                    selected = uiState.activeTab == NavigationTab.HOME,
                                    onClick = { viewModel.onTabSelected(NavigationTab.HOME) },
                                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                    label = { Text("Home", fontSize = 11.sp) }
                                )
                                NavigationRailItem(
                                    selected = uiState.activeTab == NavigationTab.EXPLORE,
                                    onClick = { viewModel.onTabSelected(NavigationTab.EXPLORE) },
                                    icon = { Icon(Icons.Default.Explore, contentDescription = "Explore") },
                                    label = { Text("Explore", fontSize = 11.sp) }
                                )
                                NavigationRailItem(
                                    selected = uiState.activeTab == NavigationTab.MAP,
                                    onClick = { viewModel.onTabSelected(NavigationTab.MAP) },
                                    icon = { Icon(Icons.Default.Map, contentDescription = "Map") },
                                    label = { Text("Map", fontSize = 11.sp) }
                                )
                                NavigationRailItem(
                                    selected = uiState.activeTab == NavigationTab.TRIPS,
                                    onClick = { viewModel.onTabSelected(NavigationTab.TRIPS) },
                                    icon = { Icon(Icons.Default.CardTravel, contentDescription = "Trips") },
                                    label = { Text("Trips", fontSize = 11.sp) }
                                )
                                NavigationRailItem(
                                    selected = uiState.activeTab == NavigationTab.PROFILE,
                                    onClick = { viewModel.onTabSelected(NavigationTab.PROFILE) },
                                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                                    label = { Text("Profile", fontSize = 11.sp) }
                                )
                            }
                        }

                        // Screen Content Renderer
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            when (currentScreen) {
                                is AppScreen.Landing -> {
                                    LandingScreen(
                                        destinations = uiState.destinations,
                                        onStartExploring = { viewModel.navigateTo(AppScreen.Explore) },
                                        onSignInClick = { viewModel.navigateTo(AppScreen.AuthLogin) },
                                        onDestinationClick = { id -> viewModel.navigateTo(AppScreen.DestinationDetail(id)) },
                                        onToggleSave = { id -> viewModel.toggleSaveDestination(id) },
                                        onSafetyGuidelinesClick = { viewModel.navigateTo(AppScreen.HelpCenter) }
                                    )
                                }

                                is AppScreen.AuthLogin -> {
                                    AuthLoginScreen(
                                        onLoginSuccess = { viewModel.navigateTo(AppScreen.Dashboard) },
                                        onNavigateToSignup = { viewModel.navigateTo(AppScreen.AuthSignup) },
                                        onNavigateToForgotPassword = {
                                            Toast.makeText(context, "Password recovery link dispatched", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }

                                is AppScreen.AuthSignup -> {
                                    AuthSignupScreen(
                                        onSignupSuccess = { viewModel.navigateTo(AppScreen.Onboarding(1)) },
                                        onNavigateToLogin = { viewModel.navigateTo(AppScreen.AuthLogin) }
                                    )
                                }

                                is AppScreen.AuthVerify -> {
                                    AuthLoginScreen(
                                        onLoginSuccess = { viewModel.navigateTo(AppScreen.Onboarding(1)) },
                                        onNavigateToSignup = { viewModel.navigateTo(AppScreen.AuthSignup) },
                                        onNavigateToForgotPassword = {}
                                    )
                                }

                                is AppScreen.Onboarding -> {
                                    OnboardingFlowScreen(
                                        currentStep = currentScreen.step,
                                        onNextStep = { viewModel.navigateTo(AppScreen.Onboarding(currentScreen.step + 1)) },
                                        onSkip = { viewModel.navigateTo(AppScreen.Dashboard) },
                                        onComplete = { viewModel.navigateTo(AppScreen.Dashboard) }
                                    )
                                }

                                is AppScreen.Dashboard -> {
                                    DashboardScreen(
                                        uiState = uiState,
                                        onSearchClick = { viewModel.navigateTo(AppScreen.Explore) },
                                        onDestinationClick = { id -> viewModel.navigateTo(AppScreen.DestinationDetail(id)) },
                                        onToggleSaveDestination = { id -> viewModel.toggleSaveDestination(id) },
                                        onTripClick = { id -> viewModel.navigateTo(AppScreen.TripDetail(id)) },
                                        onNearbyCategoryClick = { filter -> viewModel.navigateTo(AppScreen.NearbyServices(filter)) },
                                        onSafetyAdvisoryClick = { id -> viewModel.navigateTo(AppScreen.AreaSafetyDetail(id)) },
                                        onLocationSharingClick = { viewModel.navigateTo(AppScreen.ShareLocation) },
                                        onEmergencySosClick = { viewModel.navigateTo(AppScreen.Emergency) },
                                        onPlanTripClick = { viewModel.navigateTo(AppScreen.JourneyPlanner) },
                                        onTranslateClick = { viewModel.navigateTo(AppScreen.Translate) },
                                        onViewLocationSuggestions = { viewModel.navigateTo(AppScreen.LocationSuggestions(uiState.selectedCity)) },
                                        onDialNumber = dialNumber
                                    )
                                }

                                is AppScreen.Explore -> {
                                    ExploreScreen(
                                        destinations = uiState.destinations,
                                        selectedCategory = uiState.selectedDestinationCategory,
                                        onCategorySelected = { cat ->
                                            /* update category */
                                            viewModel.navigateTo(AppScreen.Explore)
                                        },
                                        onDestinationClick = { id -> viewModel.navigateTo(AppScreen.DestinationDetail(id)) },
                                        onToggleSave = { id -> viewModel.toggleSaveDestination(id) }
                                    )
                                }

                                is AppScreen.DestinationDetail -> {
                                    val destination = uiState.destinations.find { it.id == currentScreen.destinationId }
                                        ?: uiState.destinations.first()
                                    DestinationDetailScreen(
                                        destination = destination,
                                        nearbyServices = uiState.nearbyServices,
                                        onPlanTripHere = { dest ->
                                            viewModel.navigateTo(AppScreen.JourneyPlanner)
                                        },
                                        onViewOnMap = { viewModel.navigateTo(AppScreen.MapView) },
                                        onToggleSave = { viewModel.toggleSaveDestination(destination.id) },
                                        onDialEmergencyNumber = dialNumber
                                    )
                                }

                                is AppScreen.MapView -> {
                                    MapScreen(
                                        uiState = uiState,
                                        onDialNumber = dialNumber,
                                        onSearchLiveMaps = { query -> viewModel.searchLiveMaps(query) },
                                        onSelectCity = { city -> viewModel.selectCity(city) },
                                        onZoomIn = { viewModel.zoomIn() },
                                        onZoomOut = { viewModel.zoomOut() },
                                        onRecenter = { viewModel.recenterMap() },
                                        onToggleWeatherRadar = { viewModel.toggleWeatherRadar() },
                                        onToggleTrafficFlow = { viewModel.toggleTrafficFlow() },
                                        onSelectPlaceMarker = { place -> viewModel.setActiveMarkerPlace(place) },
                                        onViewSuggestions = { viewModel.navigateTo(AppScreen.LocationSuggestions(uiState.selectedCity)) },
                                        onSetMapStyle = { style -> viewModel.setMapStyle(style) },
                                        onSetMapCategory = { cat -> viewModel.setMapCategory(cat) },
                                        onToggleLiveSimulation = { viewModel.toggleLiveSimulation() },
                                        onSetRouteToPlace = { place -> viewModel.setRouteToPlace(place) },
                                        onFocusPlace = { title, lat, lon, cat -> viewModel.focusPlaceOnMap(title, lat, lon, cat) }
                                    )
                                }

                                is AppScreen.LocationSuggestions -> {
                                    LocationSuggestionsScreen(
                                        currentCity = uiState.selectedCity,
                                        suggestions = uiState.locationSuggestions,
                                        selectedType = uiState.selectedSuggestionType,
                                        isLoading = uiState.isLoadingSuggestions,
                                        customAssistantAnswer = uiState.customAssistantAnswer,
                                        isAssistantThinking = uiState.isAssistantThinking,
                                        onFilterType = { type -> viewModel.filterSuggestionType(type) },
                                        onSelectCity = { city -> viewModel.selectCity(city) },
                                        onDialNumber = dialNumber,
                                        onRefreshSuggestions = { city -> viewModel.loadLocationSuggestions(city) },
                                        onFocusPlaceOnMap = { title, lat, lon, cat -> viewModel.focusPlaceOnMap(title, lat, lon, cat) },
                                        onAskAssistant = { query -> viewModel.askCustomAssistant(query) }
                                    )
                                }

                                is AppScreen.JourneyPlanner -> {
                                    JourneyPlannerScreen(
                                        onSaveTrip = { title, origin, destination, mode, date ->
                                            viewModel.createTrip(title, origin, destination, mode, date)
                                        }
                                    )
                                }

                                is AppScreen.TripDetail -> {
                                    val trip = uiState.trips.find { it.id == currentScreen.tripId } ?: uiState.trips.first()
                                    TripDetailScreen(
                                        trip = trip,
                                        onToggleChecklist = { itemId -> viewModel.toggleTripChecklist(trip.id, itemId) },
                                        onToggleLocationSharing = {
                                            viewModel.setLocationSharing(!trip.isSharingLocation)
                                        }
                                    )
                                }

                                is AppScreen.TripsList -> {
                                    TripsListScreen(
                                        trips = uiState.trips,
                                        onTripClick = { id -> viewModel.navigateTo(AppScreen.TripDetail(id)) },
                                        onPlanNewTripClick = { viewModel.navigateTo(AppScreen.JourneyPlanner) }
                                    )
                                }

                                is AppScreen.NearbyServices -> {
                                    NearbyServicesScreen(
                                        services = uiState.nearbyServices,
                                        initialFilter = currentScreen.filterType,
                                        onDialNumber = dialNumber,
                                        onToggleSave = { id -> viewModel.toggleSaveService(id) }
                                    )
                                }

                                is AppScreen.SafetyOverview, is AppScreen.AlertsCenter -> {
                                    AlertsCenterScreen(
                                        alerts = uiState.safetyAlerts,
                                        onAlertClick = { id -> viewModel.navigateTo(AppScreen.AreaSafetyDetail(id)) }
                                    )
                                }

                                is AppScreen.AreaSafetyDetail -> {
                                    val alert = uiState.safetyAlerts.find { it.id == currentScreen.alertId } ?: uiState.safetyAlerts.first()
                                    AreaSafetyDetailScreen(
                                        alert = alert,
                                        onViewSafeAlternative = { viewModel.navigateTo(AppScreen.MapView) }
                                    )
                                }

                                is AppScreen.Translate -> {
                                    TranslationScreen(
                                        phrases = uiState.phrases,
                                        inputText = uiState.translateInputText,
                                        outputText = uiState.translateOutputText,
                                        isTranslating = uiState.isTranslating,
                                        sourceLang = uiState.translateSourceLang,
                                        targetLang = uiState.translateTargetLang,
                                        onInputTextChange = { viewModel.updateTranslateInput(it) },
                                        onTranslateClick = { viewModel.performTranslate() },
                                        onLanguagesChange = { src, tgt -> viewModel.setTranslateLanguages(src, tgt) }
                                    )
                                }

                                is AppScreen.ShareLocation -> {
                                    LocationSharingScreen(
                                        isSharingActive = uiState.isSharingLocation,
                                        sharingRemainingMins = uiState.sharingTimeRemainingMinutes,
                                        contacts = uiState.emergencyContacts,
                                        onToggleSharing = { enabled, hours ->
                                            viewModel.setLocationSharing(enabled, hours)
                                        },
                                        onManageContactsClick = { viewModel.navigateTo(AppScreen.EmergencyContacts) }
                                    )
                                }

                                is AppScreen.Emergency -> {
                                    EmergencySosScreen(
                                        sosStatus = uiState.sosStatus,
                                        lastSosTimestamp = uiState.lastSosTimestamp,
                                        contacts = uiState.emergencyContacts,
                                        userProfile = uiState.userProfile,
                                        onTriggerSos = { viewModel.triggerSos() },
                                        onCancelSos = { viewModel.cancelSos() },
                                        onDialHelpline = dialNumber
                                    )
                                }

                                is AppScreen.EmergencyContacts -> {
                                    EmergencyContactsScreen(
                                        contacts = uiState.emergencyContacts,
                                        onAddContact = { name, phone, rel ->
                                            viewModel.addEmergencyContact(name, phone, rel)
                                        },
                                        onRemoveContact = { id -> viewModel.removeEmergencyContact(id) }
                                    )
                                }

                                is AppScreen.SavedPlaces -> {
                                    SavedPlacesScreen(
                                        destinations = uiState.destinations,
                                        services = uiState.nearbyServices,
                                        onDestinationClick = { id -> viewModel.navigateTo(AppScreen.DestinationDetail(id)) },
                                        onToggleSaveDestination = { id -> viewModel.toggleSaveDestination(id) },
                                        onToggleSaveService = { id -> viewModel.toggleSaveService(id) },
                                        onDialService = dialNumber
                                    )
                                }

                                is AppScreen.Notifications -> {
                                    NotificationsScreen(
                                        notifications = uiState.notificationsList,
                                        onMarkAllRead = { viewModel.markAllNotificationsRead() }
                                    )
                                }

                                is AppScreen.Profile -> {
                                    ProfileScreen(
                                        userProfile = uiState.userProfile,
                                        onNavigateToContacts = { viewModel.navigateTo(AppScreen.EmergencyContacts) },
                                        onNavigateToSaved = { viewModel.navigateTo(AppScreen.SavedPlaces) },
                                        onNavigateToSettings = { viewModel.navigateTo(AppScreen.SettingsHub) },
                                        onNavigateToHelp = { viewModel.navigateTo(AppScreen.HelpCenter) },
                                        onSignOut = { viewModel.navigateTo(AppScreen.Landing) }
                                    )
                                }

                                is AppScreen.SettingsHub, is AppScreen.SettingsAccessibility, is AppScreen.SettingsPrivacy, is AppScreen.SettingsLanguage, is AppScreen.SettingsNotifications -> {
                                    SettingsHubScreen(
                                        userProfile = uiState.userProfile,
                                        onUpdateAccessibility = { large, high, motion ->
                                            viewModel.updateAccessibilitySettings(large, high, motion)
                                        }
                                    )
                                }

                                is AppScreen.HelpCenter, is AppScreen.OfflineView -> {
                                    HelpAndSupportScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
