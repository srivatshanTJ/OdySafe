package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.DemonstrationDataBanner
import com.example.ui.components.DestinationCard
import com.example.ui.components.SeverityBadge
import com.example.ui.state.OdysafeUiState
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    uiState: OdysafeUiState,
    onSearchClick: () -> Unit,
    onDestinationClick: (String) -> Unit,
    onToggleSaveDestination: (String) -> Unit,
    onTripClick: (String) -> Unit,
    onNearbyCategoryClick: (String) -> Unit,
    onSafetyAdvisoryClick: (String) -> Unit,
    onLocationSharingClick: () -> Unit,
    onEmergencySosClick: () -> Unit,
    onPlanTripClick: () -> Unit,
    onTranslateClick: () -> Unit,
    onViewLocationSuggestions: () -> Unit,
    onDialNumber: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeTrip = uiState.trips.find { it.status == TripStatus.ACTIVE }
    val primaryAlert = uiState.safetyAlerts.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Top Greeting & Location Chip
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Good Morning, ${uiState.userProfile.name.split(" ").first()}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = OdysafeNavy
                            )
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = null,
                                tint = OdysafeTeal,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Madurai, Tamil Nadu • 28°C Clear",
                                fontSize = 12.sp,
                                color = OdysafeTextSecondary
                            )
                        }
                    }

                    // Emergency SOS Shortcut
                    FilledTonalButton(
                        onClick = onEmergencySosClick,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = OdysafeDangerContainer,
                            contentColor = OdysafeDanger
                        ),
                        modifier = Modifier.testTag("dashboard_sos_button")
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SOS", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Search Bar trigger
                Surface(
                    color = OdysafeBg,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSearchClick() }
                        .testTag("dashboard_search_bar")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = OdysafeTeal
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Search Indian destinations, hospitals, police...",
                            color = OdysafeTextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Demonstration Data Notice
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                DemonstrationDataBanner()
            }
        }

        // Active Location Sharing Banner (if active)
        if (uiState.isSharingLocation) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable { onLocationSharingClick() }
                        .testTag("active_location_sharing_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = OdysafeMint)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(OdysafeTeal),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ShareLocation, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Live Location Sharing Active",
                                fontWeight = FontWeight.Bold,
                                color = OdysafeNavy,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Shared with Rahul Sharma • ~${uiState.sharingTimeRemainingMinutes} mins remaining",
                                fontSize = 11.sp,
                                color = OdysafeNavy.copy(alpha = 0.8f)
                            )
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = OdysafeNavy)
                    }
                }
            }
        }

        // Active Trip Card
        if (activeTrip != null) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "Current Journey",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OdysafeNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTripClick(activeTrip.id) }
                            .testTag("dashboard_active_trip_card"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = OdysafeSuccessContainer,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "IN PROGRESS",
                                        color = OdysafeSuccess,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(
                                    text = "${activeTrip.distanceKm} km • ${activeTrip.durationEst}",
                                    fontSize = 11.sp,
                                    color = OdysafeTextSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = activeTrip.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = OdysafeNavy
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "${activeTrip.origin}  ➔  ${activeTrip.destination}",
                                fontSize = 12.sp,
                                color = OdysafeTextSecondary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Checklist Progress preview
                            val checkedCount = activeTrip.checklist.count { it.isChecked }
                            val totalCount = activeTrip.checklist.size
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                LinearProgressIndicator(
                                    progress = { checkedCount.toFloat() / totalCount },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = OdysafeTeal,
                                    trackColor = OdysafeBorder
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "$checkedCount/$totalCount items ready",
                                    fontSize = 11.sp,
                                    color = OdysafeNavy,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Safety Advisory Highlight Banner
        if (primaryAlert != null) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "Verified Safety Advisory",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OdysafeNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSafetyAdvisoryClick(primaryAlert.id) }
                            .testTag("dashboard_primary_alert_card"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SeverityBadge(severity = primaryAlert.severity)
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = primaryAlert.timestamp,
                                    fontSize = 10.sp,
                                    color = OdysafeTextSecondary
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = primaryAlert.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = OdysafeNavy
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = primaryAlert.description,
                                fontSize = 12.sp,
                                color = OdysafeTextSecondary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Source: ${primaryAlert.source}",
                                fontSize = 10.sp,
                                color = OdysafeTeal,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Quick Nearby Assistance Shortcuts
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                Text(
                    text = "Nearby Assistance & Utilities",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = OdysafeNavy
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickServiceButton(
                        icon = Icons.Default.LocalHospital,
                        label = "Hospitals",
                        badge = "24/7",
                        onClick = { onNearbyCategoryClick("HOSPITAL") },
                        modifier = Modifier.weight(1f)
                    )
                    QuickServiceButton(
                        icon = Icons.Default.LocalPolice,
                        label = "Police",
                        badge = "112",
                        onClick = { onNearbyCategoryClick("POLICE") },
                        modifier = Modifier.weight(1f)
                    )
                    QuickServiceButton(
                        icon = Icons.Default.MedicalServices,
                        label = "Pharmacies",
                        badge = "Nearby",
                        onClick = { onNearbyCategoryClick("PHARMACY") },
                        modifier = Modifier.weight(1f)
                    )
                    QuickServiceButton(
                        icon = Icons.Default.Translate,
                        label = "Translate",
                        badge = "Audio",
                        onClick = onTranslateClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Location-Based Suggestions (Hotels, Tourist Spots, Food, Safety Tips)
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = OdysafeTeal, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Local Suggestions: ${uiState.selectedCity.split(",").first()}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OdysafeNavy
                                )
                            )
                        }
                        Text(
                            text = "Hotels, tourist spots & dining based on where you are",
                            style = MaterialTheme.typography.bodySmall.copy(color = OdysafeTextSecondary)
                        )
                    }

                    TextButton(
                        onClick = onViewLocationSuggestions,
                        modifier = Modifier.testTag("dashboard_view_all_suggestions")
                    ) {
                        Text("View all", color = OdysafeTeal, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (uiState.locationSuggestions.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(uiState.locationSuggestions.take(4)) { suggestion ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier
                                    .width(260.dp)
                                    .clickable { onViewLocationSuggestions() }
                                    .testTag("dash_sug_card_${suggestion.id}")
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            color = when (suggestion.type.name) {
                                                "TOURIST" -> Color(0xFFE3F2FD)
                                                "HOTEL" -> Color(0xFFE8F5E9)
                                                "FOOD" -> Color(0xFFFFF3E0)
                                                else -> OdysafeMint
                                            },
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = suggestion.type.label.uppercase(),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = when (suggestion.type.name) {
                                                    "TOURIST" -> Color(0xFF1565C0)
                                                    "HOTEL" -> Color(0xFF2E7D32)
                                                    "FOOD" -> Color(0xFFE65100)
                                                    else -> OdysafeTeal
                                                },
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }

                                        Text(text = suggestion.rating, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OdysafeCaution)
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = suggestion.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = OdysafeNavy,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Text(
                                        text = suggestion.subtitle,
                                        fontSize = 11.sp,
                                        color = OdysafeTextSecondary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "Tip: ${suggestion.safetyTip}",
                                        fontSize = 10.sp,
                                        color = OdysafeTextPrimary,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        lineHeight = 14.sp
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = suggestion.distanceText,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = OdysafeTeal
                                        )

                                        if (suggestion.phone != null) {
                                            IconButton(
                                                onClick = { onDialNumber(suggestion.phone) },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Default.Call, contentDescription = "Call", tint = OdysafeNavy, modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Suggested Destinations
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Explore Destinations in India",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = OdysafeNavy
                    )
                )
                TextButton(onClick = { onNearbyCategoryClick("ALL") }) {
                    Text("View all", color = OdysafeTeal, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        items(uiState.destinations.take(3)) { dest ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                DestinationCard(
                    destination = dest,
                    onClick = { onDestinationClick(dest.id) },
                    onToggleSave = { onToggleSaveDestination(dest.id) }
                )
            }
        }
    }
}

@Composable
private fun QuickServiceButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    badge: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .testTag("quick_service_${label.lowercase()}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(OdysafeMint),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = OdysafeTeal, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = OdysafeNavy,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = badge,
                fontSize = 10.sp,
                color = OdysafeTextSecondary
            )
        }
    }
}
