package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SuggestionType
import com.example.data.service.GroundedPlaceResult
import com.example.ui.state.MapStyle
import com.example.ui.state.OdysafeUiState
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    uiState: OdysafeUiState,
    onDialNumber: (String) -> Unit,
    onSearchLiveMaps: (String) -> Unit,
    onSelectCity: (String) -> Unit,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onRecenter: () -> Unit,
    onToggleWeatherRadar: () -> Unit,
    onToggleTrafficFlow: () -> Unit,
    onSelectPlaceMarker: (GroundedPlaceResult?) -> Unit,
    onViewSuggestions: () -> Unit,
    onSetMapStyle: (MapStyle) -> Unit = {},
    onSetMapCategory: (String) -> Unit = {},
    onToggleLiveSimulation: () -> Unit = {},
    onSetRouteToPlace: (GroundedPlaceResult?) -> Unit = {},
    onFocusPlace: (String, Double, Double, String) -> Unit = { _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var liveSearchInput by remember { mutableStateOf("") }
    var panOffsetX by remember { mutableStateOf(0f) }
    var panOffsetY by remember { mutableStateOf(0f) }
    var showSuggestionsSheet by remember { mutableStateOf(false) }

    // Pulsing radar animation for live GPS user beacon
    val infiniteTransition = rememberInfiniteTransition(label = "live_gps_pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 18f,
        targetValue = 44f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_radius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.55f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_alpha"
    )

    // Animated weather radar sweep angle
    val radarSweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radar_sweep"
    )

    val indianCities = listOf(
        "Madurai, Tamil Nadu",
        "Chennai, Tamil Nadu",
        "Ooty, Tamil Nadu",
        "Rameswaram, Tamil Nadu",
        "Jaipur, Rajasthan",
        "Kochi, Kerala",
        "Varanasi, Uttar Pradesh",
        "Munnar, Kerala",
        "Bengaluru, Karnataka",
        "Delhi NCR"
    )

    val activePlace = uiState.activeMarkerPlace
    val places = uiState.liveGroundedPlaces

    // Reset pan offset when city changes
    LaunchedEffect(uiState.selectedCity) {
        panOffsetX = 0f
        panOffsetY = 0f
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                when (uiState.mapStyle) {
                    MapStyle.NIGHT_SAFETY -> Color(0xFF0F1722)
                    MapStyle.SATELLITE -> Color(0xFF1E281F)
                    MapStyle.STREET -> Color(0xFFE8EEF3)
                }
            )
            .testTag("interactive_map_screen")
    ) {
        // ==========================================
        // 1. Interactive High-Precision Canvas Map
        // ==========================================
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        panOffsetX += dragAmount.x
                        panOffsetY += dragAmount.y
                    }
                }
                .pointerInput(places, uiState.mapZoomLevel, panOffsetX, panOffsetY) {
                    detectTapGestures { tapOffset ->
                        val w = size.width
                        val h = size.height
                        val zoom = uiState.mapZoomLevel

                        // Detect tap on place markers
                        places.forEachIndexed { index, place ->
                            val baseRelX = 0.5f + ((place.longitude - uiState.userLocationCoords.second) * 22.0).toFloat() + (index * 0.07f - 0.14f)
                            val baseRelY = 0.5f + ((place.latitude - uiState.userLocationCoords.first) * 22.0).toFloat() + (index * 0.07f - 0.14f)

                            val markerScreenX = (w / 2f) + (baseRelX * w - w / 2f) * zoom + panOffsetX
                            val markerScreenY = (h / 2f) + (baseRelY * h - h / 2f) * zoom + panOffsetY

                            val dx = tapOffset.x - markerScreenX
                            val dy = tapOffset.y - markerScreenY
                            if (dx * dx + dy * dy < (42f * 42f)) {
                                onSelectPlaceMarker(place)
                                onSetRouteToPlace(place)
                                return@detectTapGestures
                            }
                        }
                    }
                }
        ) {
            val w = size.width
            val h = size.height
            val zoom = uiState.mapZoomLevel
            val isNight = uiState.mapStyle == MapStyle.NIGHT_SAFETY
            val isSatellite = uiState.mapStyle == MapStyle.SATELLITE

            translate(left = panOffsetX, top = panOffsetY) {
                scale(scale = zoom, pivot = Offset(w / 2f, h / 2f)) {

                    // A. Waterways, Ocean Coastline, or Mountain Terraces
                    val isCoastal = uiState.selectedCity.contains("Chennai") ||
                            uiState.selectedCity.contains("Rameswaram") ||
                            uiState.selectedCity.contains("Kochi")
                    val isHill = uiState.selectedCity.contains("Ooty") || uiState.selectedCity.contains("Munnar")

                    val waterColor = when {
                        isNight -> Color(0xFF092338)
                        isSatellite -> Color(0xFF133644)
                        else -> Color(0xFFBEDBE5)
                    }

                    if (isCoastal) {
                        // Coastline along east
                        drawRect(
                            color = waterColor,
                            topLeft = Offset(w * 0.74f, 0f),
                            size = Size(w * 0.35f, h)
                        )
                        // Beach Sand Corridor
                        val sandColor = if (isNight) Color(0xFF2C2A1E) else Color(0xFFF3E5C8)
                        drawRect(
                            color = sandColor,
                            topLeft = Offset(w * 0.70f, 0f),
                            size = Size(w * 0.04f, h)
                        )
                    } else if (isHill) {
                        // Mist hill contours
                        val hillGreen = if (isNight) Color(0xFF10281E) else Color(0xFFCCE4D3)
                        drawCircle(color = hillGreen, radius = w * 0.38f, center = Offset(w * 0.25f, h * 0.30f))
                        drawCircle(color = hillGreen, radius = w * 0.42f, center = Offset(w * 0.75f, h * 0.70f))
                    } else {
                        // City River (Vaigai in Madurai, Ganga in Varanasi)
                        val river = Path().apply {
                            moveTo(0f, h * 0.22f)
                            cubicTo(w * 0.35f, h * 0.28f, w * 0.65f, h * 0.16f, w, h * 0.26f)
                            lineTo(w, h * 0.34f)
                            cubicTo(w * 0.65f, h * 0.24f, w * 0.35f, h * 0.36f, 0f, h * 0.30f)
                            close()
                        }
                        drawPath(path = river, color = waterColor)
                    }

                    // B. Protected Parks & Heritage Sanctuaries
                    val parkColor = when {
                        isNight -> Color(0xFF142921)
                        isSatellite -> Color(0xFF1B3822)
                        else -> Color(0xFFD4EADB)
                    }
                    drawRoundRect(
                        color = parkColor,
                        topLeft = Offset(w * 0.10f, h * 0.58f),
                        size = Size(w * 0.30f, h * 0.22f),
                        cornerRadius = CornerRadius(20f, 20f)
                    )
                    drawRoundRect(
                        color = parkColor,
                        topLeft = Offset(w * 0.60f, h * 0.38f),
                        size = Size(w * 0.28f, h * 0.18f),
                        cornerRadius = CornerRadius(20f, 20f)
                    )

                    // C. Arterial Road Network & Streets
                    val mainRoadColor = when {
                        isNight -> Color(0xFF243242)
                        isSatellite -> Color(0xFF424A3D)
                        else -> Color.White
                    }
                    val secondaryRoadColor = when {
                        isNight -> Color(0xFF1B2735)
                        isSatellite -> Color(0xFF2F362C)
                        else -> Color(0xFFF2F4F8)
                    }

                    // Main Primary Corridors
                    drawLine(color = mainRoadColor, start = Offset(0f, h * 0.42f), end = Offset(w, h * 0.42f), strokeWidth = 14f)
                    drawLine(color = mainRoadColor, start = Offset(0f, h * 0.58f), end = Offset(w, h * 0.58f), strokeWidth = 14f)
                    drawLine(color = mainRoadColor, start = Offset(w * 0.35f, 0f), end = Offset(w * 0.35f, h), strokeWidth = 14f)
                    drawLine(color = mainRoadColor, start = Offset(w * 0.65f, 0f), end = Offset(w * 0.65f, h), strokeWidth = 14f)

                    // Diagonal Arterials & Radial Roads
                    drawLine(color = secondaryRoadColor, start = Offset(0f, h * 0.14f), end = Offset(w, h * 0.86f), strokeWidth = 8f)
                    drawLine(color = secondaryRoadColor, start = Offset(w * 0.12f, h), end = Offset(w * 0.88f, 0f), strokeWidth = 8f)

                    // D. Live Traffic Flow Corridors (if enabled)
                    if (uiState.showTrafficFlow) {
                        // Free flowing green corridor (45 km/h)
                        drawLine(
                            color = Color(0xFF4CAF50).copy(alpha = 0.85f),
                            start = Offset(0f, h * 0.42f),
                            end = Offset(w * 0.48f, h * 0.42f),
                            strokeWidth = 6f
                        )
                        // Amber moderate congestion junction (22 km/h)
                        drawLine(
                            color = Color(0xFFFFA000).copy(alpha = 0.9f),
                            start = Offset(w * 0.48f, h * 0.42f),
                            end = Offset(w * 0.68f, h * 0.42f),
                            strokeWidth = 6f
                        )
                        // Clear flow on vertical bypass
                        drawLine(
                            color = Color(0xFF4CAF50).copy(alpha = 0.85f),
                            start = Offset(w * 0.65f, 0f),
                            end = Offset(w * 0.65f, h * 0.58f),
                            strokeWidth = 6f
                        )
                    }

                    // E. Live Weather Radar Layer (if enabled)
                    if (uiState.showWeatherRadar) {
                        val sweepCenter = Offset(w * 0.50f, h * 0.46f)
                        val radarRadius = w * 0.32f

                        // Radar background rings
                        val radarRingColor = if (isNight) Color(0x3500E5FF) else Color(0x30087F8C)
                        drawCircle(color = radarRingColor, radius = radarRadius, center = sweepCenter, style = Stroke(width = 1.5f))
                        drawCircle(color = radarRingColor, radius = radarRadius * 0.6f, center = sweepCenter, style = Stroke(width = 1.5f))

                        // Sweeping radar beam line
                        val rad = Math.toRadians(radarSweepAngle.toDouble())
                        val sweepEnd = Offset(
                            (sweepCenter.x + radarRadius * cos(rad)).toFloat(),
                            (sweepCenter.y + radarRadius * sin(rad)).toFloat()
                        )
                        drawLine(
                            color = if (isNight) Color(0x9900E5FF) else OdysafeTeal.copy(alpha = 0.6f),
                            start = sweepCenter,
                            end = sweepEnd,
                            strokeWidth = 2.5f
                        )

                        // Mist / Cloud patches
                        val cloudColor = if (isNight) Color(0x2800E5FF) else Color(0x2A607D8B)
                        drawCircle(color = cloudColor, radius = w * 0.18f, center = Offset(w * 0.42f, h * 0.38f))
                        drawCircle(color = cloudColor, radius = w * 0.14f, center = Offset(w * 0.62f, h * 0.44f))
                    }

                    // F. Illuminated Safe Route Path (With dynamic progress if simulation active)
                    val targetPlace = uiState.activeRouteToPlace ?: places.firstOrNull()
                    val targetX = if (targetPlace != null) {
                        val baseRelX = 0.5f + ((targetPlace.longitude - uiState.userLocationCoords.second) * 22.0).toFloat()
                        (w * baseRelX.coerceIn(0.15f, 0.85f))
                    } else w * 0.65f

                    val targetY = if (targetPlace != null) {
                        val baseRelY = 0.5f + ((targetPlace.latitude - uiState.userLocationCoords.first) * 22.0).toFloat()
                        (h * baseRelY.coerceIn(0.20f, 0.80f))
                    } else h * 0.35f

                    val safePath = Path().apply {
                        moveTo(w * 0.50f, h * 0.50f)
                        lineTo(w * 0.50f, h * 0.42f)
                        lineTo(targetX, h * 0.42f)
                        lineTo(targetX, targetY)
                    }

                    // Glow underline
                    drawPath(
                        path = safePath,
                        color = if (isNight) Color(0x5500E5FF) else OdysafeMint,
                        style = Stroke(width = 14f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
                    )
                    // Core route line
                    drawPath(
                        path = safePath,
                        color = if (isNight) Color(0xFF00E5FF) else OdysafeTeal,
                        style = Stroke(width = 6f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
                    )

                    // Streetlight security markers along safe path
                    val lampColor = if (isNight) Color(0xFFFFD54F) else Color(0xFFFFB300)
                    drawCircle(color = lampColor, radius = 4f, center = Offset(w * 0.50f, h * 0.46f))
                    drawCircle(color = lampColor, radius = 4f, center = Offset(w * 0.50f, h * 0.42f))
                    drawCircle(color = lampColor, radius = 4f, center = Offset((w * 0.50f + targetX) / 2f, h * 0.42f))

                    // G. Live User Location GPS Pulse Beacon
                    val simProg = uiState.liveSimulationProgress
                    val userX = if (uiState.isLiveSimulationActive) {
                        w * 0.50f + (targetX - w * 0.50f) * simProg
                    } else w * 0.50f

                    val userY = if (uiState.isLiveSimulationActive) {
                        h * 0.50f + (targetY - h * 0.50f) * simProg
                    } else h * 0.50f

                    val userCenter = Offset(userX, userY)

                    // Pulse outer circle
                    drawCircle(
                        color = (if (isNight) Color(0xFF00E5FF) else OdysafeTeal).copy(alpha = pulseAlpha),
                        radius = pulseRadius,
                        center = userCenter
                    )
                    // Secondary aura
                    drawCircle(
                        color = (if (isNight) Color(0xFF00E5FF) else OdysafeTeal).copy(alpha = 0.35f),
                        radius = 20f,
                        center = userCenter
                    )
                    // User solid center beacon
                    drawCircle(
                        color = if (isNight) Color(0xFF00E5FF) else OdysafeTeal,
                        radius = 10f,
                        center = userCenter
                    )
                    drawCircle(color = Color.White, radius = 4.5f, center = userCenter)

                    // H. Live Grounded Place Markers
                    places.forEachIndexed { index, place ->
                        val baseRelX = 0.5f + ((place.longitude - uiState.userLocationCoords.second) * 22.0).toFloat() + (index * 0.07f - 0.14f)
                        val baseRelY = 0.5f + ((place.latitude - uiState.userLocationCoords.first) * 22.0).toFloat() + (index * 0.07f - 0.14f)
                        val markerCenter = Offset(
                            w * baseRelX.coerceIn(0.12f, 0.88f),
                            h * baseRelY.coerceIn(0.18f, 0.82f)
                        )
                        val isSelected = (activePlace?.title == place.title)

                        val markerColor = when {
                            place.category.contains("HOSPITAL") -> OdysafeDanger
                            place.category.contains("POLICE") -> if (isNight) Color(0xFF4FC3F7) else OdysafeNavy
                            place.category.contains("PHARMACY") -> Color(0xFF2E7D32)
                            place.category.contains("HOTEL") -> Color(0xFF5E35B1)
                            place.category.contains("FOOD") -> Color(0xFFEF6C00)
                            else -> Color(0xFFD84315) // Tourist & Heritage
                        }

                        if (isSelected) {
                            // Pulsing selection highlight ring
                            drawCircle(color = markerColor.copy(alpha = 0.40f), radius = 28f, center = markerCenter)
                            drawCircle(color = markerColor.copy(alpha = 0.20f), radius = 38f, center = markerCenter)
                        }

                        // Outer pin head
                        drawCircle(color = markerColor, radius = if (isSelected) 16f else 12f, center = markerCenter)
                        // Inner contrast dot
                        drawCircle(color = Color.White, radius = if (isSelected) 6.5f else 4.5f, center = markerCenter)
                    }
                }
            }
        }

        // ==========================================
        // 2. Top Header Bar & Interactive Controls
        // ==========================================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .align(Alignment.TopCenter)
        ) {
            // Live Search Bar
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 5.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = OdysafeTeal,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))

                    OutlinedTextField(
                        value = liveSearchInput,
                        onValueChange = { liveSearchInput = it },
                        placeholder = { Text("Search hospitals, hotels, spots, food...", fontSize = 12.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("live_map_search_input")
                    )

                    if (uiState.isSearchingLiveMaps) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = OdysafeTeal,
                            strokeWidth = 2.dp
                        )
                    } else {
                        IconButton(
                            onClick = {
                                if (liveSearchInput.isNotBlank()) {
                                    onSearchLiveMaps(liveSearchInput)
                                }
                            },
                            modifier = Modifier.testTag("live_map_search_btn")
                        ) {
                            Icon(Icons.Default.ArrowForward, contentDescription = "Search", tint = OdysafeNavy)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Category Filter Pills (All, Hospital, Police, Pharmacy, Tourist, Hotel, Food)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val categoryPills = listOf(
                    Triple("ALL", "All Places", Icons.Default.Layers),
                    Triple("HOSPITAL", "Hospitals", Icons.Default.LocalHospital),
                    Triple("POLICE", "Police (112)", Icons.Default.LocalPolice),
                    Triple("PHARMACY", "Pharmacies", Icons.Default.MedicalServices),
                    Triple("TOURIST", "Tourist Spots", Icons.Default.TempleHindu),
                    Triple("HOTEL", "Safe Hotels", Icons.Default.Hotel),
                    Triple("FOOD", "Dining", Icons.Default.Restaurant)
                )

                items(categoryPills) { (catKey, label, icon) ->
                    val isSelected = uiState.selectedMapCategory == catKey
                    Surface(
                        color = if (isSelected) OdysafeNavy else Color.White.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 2.dp,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) OdysafeNavy else OdysafeBorder),
                        modifier = Modifier
                            .clickable { onSetMapCategory(catKey) }
                            .testTag("map_cat_filter_${catKey.lowercase()}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else OdysafeTeal,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                color = if (isSelected) Color.White else OdysafeNavy
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Indian City Switcher Bar
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(indianCities) { city ->
                    val cityName = city.split(",").first().trim()
                    val isSelected = uiState.selectedCity.contains(cityName, ignoreCase = true)
                    Surface(
                        color = if (isSelected) OdysafeTeal else Color.White.copy(alpha = 0.92f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) OdysafeTeal else OdysafeBorder),
                        modifier = Modifier
                            .clickable { onSelectCity(city) }
                            .testTag("city_chip_${cityName.lowercase()}")
                    ) {
                        Text(
                            text = cityName,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else OdysafeNavy,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Live Telemetry Badge Row (Live GPS status & Simulation Toggle)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = OdysafeMint,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(OdysafeSuccess)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "LIVE GPS • ±${uiState.gpsAccuracyMeters}m precision • Heading ${uiState.headingDegrees.toInt()}° NE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = OdysafeNavy
                        )
                    }
                }

                // Live Walk Simulation Button
                Surface(
                    color = if (uiState.isLiveSimulationActive) OdysafeDanger else Color.White,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (uiState.isLiveSimulationActive) OdysafeDanger else OdysafeBorder),
                    modifier = Modifier
                        .clickable { onToggleLiveSimulation() }
                        .testTag("map_live_simulation_toggle")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (uiState.isLiveSimulationActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = if (uiState.isLiveSimulationActive) Color.White else OdysafeNavy,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (uiState.isLiveSimulationActive) "Tracking Live..." else "Live Walk Sim",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (uiState.isLiveSimulationActive) Color.White else OdysafeNavy
                        )
                    }
                }
            }
        }

        // ==========================================
        // 3. Floating Quick Map Controls (Right Side)
        // ==========================================
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Compass
            SmallFloatingActionButton(
                onClick = { onRecenter() },
                containerColor = Color.White,
                contentColor = OdysafeNavy,
                modifier = Modifier.testTag("map_compass_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Explore,
                    contentDescription = "Compass",
                    modifier = Modifier.rotate(uiState.headingDegrees)
                )
            }

            // Zoom In
            SmallFloatingActionButton(
                onClick = onZoomIn,
                containerColor = Color.White,
                contentColor = OdysafeNavy,
                modifier = Modifier.testTag("map_zoom_in_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Zoom In")
            }

            // Zoom Out
            SmallFloatingActionButton(
                onClick = onZoomOut,
                containerColor = Color.White,
                contentColor = OdysafeNavy,
                modifier = Modifier.testTag("map_zoom_out_button")
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Zoom Out")
            }

            // Map Style Switcher (Street / Night Safety / Satellite)
            SmallFloatingActionButton(
                onClick = {
                    val nextStyle = when (uiState.mapStyle) {
                        MapStyle.STREET -> MapStyle.NIGHT_SAFETY
                        MapStyle.NIGHT_SAFETY -> MapStyle.SATELLITE
                        MapStyle.SATELLITE -> MapStyle.STREET
                    }
                    onSetMapStyle(nextStyle)
                },
                containerColor = when (uiState.mapStyle) {
                    MapStyle.NIGHT_SAFETY -> OdysafeNavy
                    MapStyle.SATELLITE -> Color(0xFF2E7D32)
                    MapStyle.STREET -> Color.White
                },
                contentColor = if (uiState.mapStyle == MapStyle.STREET) OdysafeNavy else Color.White,
                modifier = Modifier.testTag("map_style_toggle")
            ) {
                Icon(
                    imageVector = when (uiState.mapStyle) {
                        MapStyle.NIGHT_SAFETY -> Icons.Default.DarkMode
                        MapStyle.SATELLITE -> Icons.Default.Satellite
                        MapStyle.STREET -> Icons.Default.LightMode
                    },
                    contentDescription = "Map Style"
                )
            }

            // Traffic Layer Toggle
            SmallFloatingActionButton(
                onClick = onToggleTrafficFlow,
                containerColor = if (uiState.showTrafficFlow) OdysafeMint else Color.White,
                contentColor = OdysafeNavy,
                modifier = Modifier.testTag("map_traffic_toggle")
            ) {
                Icon(Icons.Default.Traffic, contentDescription = "Traffic Flow")
            }

            // Weather Radar Toggle
            SmallFloatingActionButton(
                onClick = onToggleWeatherRadar,
                containerColor = if (uiState.showWeatherRadar) OdysafeMint else Color.White,
                contentColor = OdysafeTeal,
                modifier = Modifier.testTag("map_weather_toggle")
            ) {
                Icon(Icons.Default.Cloud, contentDescription = "Weather Radar")
            }

            // Recenter GPS
            FloatingActionButton(
                onClick = onRecenter,
                containerColor = Color.White,
                contentColor = OdysafeNavy,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("map_recenter_button")
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "My Location")
            }

            // View Suggestions Shortcut (Opens drawer or navigates)
            FloatingActionButton(
                onClick = { showSuggestionsSheet = !showSuggestionsSheet },
                containerColor = OdysafeNavy,
                contentColor = OdysafeMint,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("map_local_suggestions_btn")
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "Local Suggestions")
            }
        }

        // ==========================================
        // 4. Floating Suggestion Drawer on Map
        // ==========================================
        AnimatedVisibility(
            visible = showSuggestionsSheet,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                shadowElevation = 12.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.48f)
                    .testTag("map_suggestions_drawer")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = OdysafeTeal, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Local Suggestions for ${uiState.selectedCity.split(",").first()}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = OdysafeNavy)
                            )
                        }

                        IconButton(onClick = { showSuggestionsSheet = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = OdysafeTextSecondary)
                        }
                    }

                    Text(
                        text = "Tap any suggestion to plot safe route & view details on map",
                        fontSize = 11.sp,
                        color = OdysafeTextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(uiState.locationSuggestions) { sug ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = OdysafeBg),
                                border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
                                modifier = Modifier
                                    .width(220.dp)
                                    .clickable {
                                        onFocusPlace(
                                            sug.title,
                                            sug.latitude,
                                            sug.longitude,
                                            when (sug.type) {
                                                SuggestionType.HOTEL -> "HOTEL"
                                                SuggestionType.FOOD -> "FOOD"
                                                SuggestionType.TOURIST -> "TOURIST"
                                                else -> "SERVICE"
                                            }
                                        )
                                        showSuggestionsSheet = false
                                    }
                                    .testTag("drawer_sug_${sug.id}")
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Surface(
                                            color = when (sug.type) {
                                                SuggestionType.HOTEL -> Color(0xFFEDE7F6)
                                                SuggestionType.FOOD -> Color(0xFFFFF3E0)
                                                SuggestionType.TOURIST -> Color(0xFFE3F2FD)
                                                else -> OdysafeMint
                                            },
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = sug.type.label.split(" ").first(),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = OdysafeNavy,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Text(sug.rating, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OdysafeTeal)
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = sug.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = OdysafeNavy,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Text(
                                        text = sug.subtitle,
                                        fontSize = 10.sp,
                                        color = OdysafeTextSecondary,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "📍 ${sug.distanceText}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = OdysafeTeal
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            showSuggestionsSheet = false
                            onViewSuggestions()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = OdysafeNavy),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Open Full Guides & AI Suggestions", fontSize = 12.sp)
                    }
                }
            }
        }

        // ==========================================
        // 5. Bottom Selected Place Detail Card
        // ==========================================
        if (activePlace != null && !showSuggestionsSheet) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(14.dp)
                    .testTag("live_map_place_bottom_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        val icon = when {
                            activePlace.category.contains("HOSPITAL") -> Icons.Default.LocalHospital
                            activePlace.category.contains("POLICE") -> Icons.Default.LocalPolice
                            activePlace.category.contains("PHARMACY") -> Icons.Default.MedicalServices
                            activePlace.category.contains("HOTEL") -> Icons.Default.Hotel
                            activePlace.category.contains("FOOD") -> Icons.Default.Restaurant
                            else -> Icons.Default.TempleHindu
                        }
                        val tint = when {
                            activePlace.isEmergency -> OdysafeDanger
                            activePlace.category.contains("HOTEL") -> Color(0xFF5E35B1)
                            activePlace.category.contains("FOOD") -> Color(0xFFEF6C00)
                            else -> OdysafeTeal
                        }
                        val bg = when {
                            activePlace.isEmergency -> OdysafeDangerContainer
                            activePlace.category.contains("HOTEL") -> Color(0xFFEDE7F6)
                            activePlace.category.contains("FOOD") -> Color(0xFFFFF3E0)
                            else -> OdysafeMint
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(bg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = activePlace.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = OdysafeNavy,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.Verified, contentDescription = "Verified", tint = OdysafeTeal, modifier = Modifier.size(14.dp))
                            }

                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = activePlace.address,
                                fontSize = 11.sp,
                                color = OdysafeTextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = activePlace.distanceText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OdysafeTeal
                                )
                                Text(
                                    text = " • ${activePlace.openStatus}",
                                    fontSize = 11.sp,
                                    color = if (activePlace.isEmergency) OdysafeDanger else OdysafeSuccess,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = " • ${activePlace.rating}",
                                    fontSize = 11.sp,
                                    color = OdysafeNavy,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Safety telemetry banner
                            Surface(
                                color = OdysafeBg,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = "🛡️ ${activePlace.safetyHighlights}",
                                    fontSize = 10.sp,
                                    color = OdysafeNavy,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        IconButton(onClick = { onSelectPlaceMarker(null) }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = OdysafeTextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (activePlace.phone != null) {
                            OutlinedButton(
                                onClick = { onDialNumber(activePlace.phone) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("live_place_call_btn")
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Call", fontSize = 11.sp)
                            }
                        }

                        OutlinedButton(
                            onClick = { onSetRouteToPlace(activePlace) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("live_place_draw_route_btn")
                        ) {
                            Icon(Icons.Default.Route, contentDescription = null, tint = OdysafeTeal, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Safe Route", fontSize = 11.sp)
                        }

                        Button(
                            onClick = {
                                try {
                                    val gmmIntentUri = Uri.parse("geo:${activePlace.latitude},${activePlace.longitude}?q=${Uri.encode(activePlace.title)}")
                                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                    context.startActivity(mapIntent)
                                } catch (_: Exception) {
                                    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(activePlace.sourceUrl ?: "https://maps.google.com"))
                                    context.startActivity(webIntent)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = OdysafeNavy),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("live_place_directions_btn")
                        ) {
                            Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Directions", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
