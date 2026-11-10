package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.ui.theme.*

@Composable
fun ExploreScreen(
    destinations: List<Destination>,
    selectedCategory: DestinationCategory,
    onCategorySelected: (DestinationCategory) -> Unit,
    onDestinationClick: (String) -> Unit,
    onToggleSave: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredDestinations = remember(destinations, selectedCategory, searchQuery) {
        destinations.filter { dest ->
            val matchesCategory = (selectedCategory == DestinationCategory.ALL || dest.category == selectedCategory)
            val matchesSearch = if (searchQuery.isBlank()) true else {
                dest.name.contains(searchQuery, ignoreCase = true) ||
                        dest.state.contains(searchQuery, ignoreCase = true) ||
                        dest.description.contains(searchQuery, ignoreCase = true)
            }
            matchesCategory && matchesSearch
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .testTag("explore_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Search field
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by city, temple, hill station...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = OdysafeTeal) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("explore_search_field")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(DestinationCategory.entries) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { onCategorySelected(category) },
                            label = { Text(category.label, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = OdysafeNavy,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("category_chip_${category.name.lowercase()}")
                        )
                    }
                }
            }
        }

        // Demo Notice
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                DemonstrationDataBanner()
            }
        }

        // Result Count or Empty State
        if (filteredDestinations.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        tint = OdysafeTextSecondary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No destinations found",
                        fontWeight = FontWeight.Bold,
                        color = OdysafeNavy
                    )
                    Text(
                        text = "Try adjusting your search query or choosing 'All' categories.",
                        fontSize = 12.sp,
                        color = OdysafeTextSecondary
                    )
                }
            }
        } else {
            items(filteredDestinations) { dest ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    DestinationCard(
                        destination = dest,
                        onClick = { onDestinationClick(dest.id) },
                        onToggleSave = { onToggleSave(dest.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun DestinationDetailScreen(
    destination: Destination,
    nearbyServices: List<NearbyService>,
    onPlanTripHere: (Destination) -> Unit,
    onViewOnMap: (Destination) -> Unit,
    onToggleSave: () -> Unit,
    onDialEmergencyNumber: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val emergencyStations = remember(nearbyServices) {
        nearbyServices.filter { it.isEmergencyFacility }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .verticalScroll(rememberScrollState())
            .testTag("destination_detail_screen")
    ) {
        // Hero visual banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    when (destination.category.name) {
                        "HILLS" -> Color(0xFF2C5449)
                        "BEACHES" -> Color(0xFF16475E)
                        "HERITAGE" -> Color(0xFF5E3920)
                        else -> OdysafeNavy
                    }
                )
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Surface(
                    color = OdysafeMint,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = destination.category.label,
                        color = OdysafeNavy,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = destination.name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = destination.state + ", India",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFFDFEAF0))
                )
            }
        }

        // Action Toolbar
        Surface(
            color = Color.White,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { onPlanTripHere(destination) },
                    colors = ButtonDefaults.buttonColors(containerColor = OdysafeTeal),
                    modifier = Modifier.weight(1f).testTag("detail_plan_trip_button")
                ) {
                    Icon(Icons.Default.CardTravel, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Plan Journey", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { onViewOnMap(destination) },
                    modifier = Modifier.weight(1f).testTag("detail_view_map_button")
                ) {
                    Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Safety Map", fontSize = 13.sp)
                }

                IconButton(onClick = onToggleSave) {
                    Icon(
                        imageVector = if (destination.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (destination.isSaved) OdysafeTeal else OdysafeNavy
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Safety Advisory Section
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SeverityBadge(severity = destination.safetySeverity)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = destination.safetyTimestamp, fontSize = 11.sp, color = OdysafeTextSecondary)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = destination.safetyStatus,
                        fontWeight = FontWeight.Bold,
                        color = OdysafeNavy,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = destination.safetyNotice,
                        fontSize = 12.sp,
                        color = OdysafeTextPrimary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = OdysafeTeal, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Verified Source: ${destination.safetySource}",
                            fontSize = 11.sp,
                            color = OdysafeTeal,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Overview & Description
            Text(
                text = "About this Destination",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = OdysafeNavy
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = destination.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = OdysafeTextPrimary,
                    lineHeight = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Visiting & Accessibility Facts
            DetailFactRow(title = "Best Time to Visit", desc = destination.bestTimeToVisit, icon = Icons.Default.CalendarMonth)
            DetailFactRow(title = "Accessibility", desc = destination.accessibilityRating, icon = Icons.Default.Accessible)
            DetailFactRow(title = "Timings & Entry", desc = "${destination.openingHours} • ${destination.entryFee}", icon = Icons.Default.Schedule)

            Spacer(modifier = Modifier.height(16.dp))

            // Cultural Highlights
            Text(
                text = "Key Highlights",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = OdysafeNavy
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            destination.highlights.forEach { highlight ->
                Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = OdysafeTeal, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = highlight, fontSize = 13.sp, color = OdysafeNavy)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Transport Options
            Text(
                text = "Available Transport Options",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = OdysafeNavy
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            destination.transportHighlights.forEach { transport ->
                Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DirectionsTransit, contentDescription = null, tint = OdysafeNavy, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = transport, fontSize = 13.sp, color = OdysafeTextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Suggested Itinerary
            Text(
                text = "Suggested 1-2 Day Itinerary",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = OdysafeNavy
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            destination.suggestedItinerary.forEach { step ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Text(
                        text = step,
                        fontSize = 12.sp,
                        color = OdysafeNavy,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nearby Emergency Stations preview
            Text(
                text = "Emergency Facilities in Area",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = OdysafeNavy
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            emergencyStations.take(2).forEach { emergency ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = OdysafeDangerContainer),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocalHospital, contentDescription = null, tint = OdysafeDanger)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = emergency.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = OdysafeNavy)
                            Text(text = "${emergency.distanceKm} km away • ${emergency.openHours}", fontSize = 11.sp, color = OdysafeTextSecondary)
                        }
                        IconButton(onClick = { onDialEmergencyNumber(emergency.phone) }) {
                            Icon(Icons.Default.Call, contentDescription = "Call", tint = OdysafeDanger)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailFactRow(
    title: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(OdysafeMint),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = OdysafeTeal, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = OdysafeTextSecondary)
            Text(text = desc, fontSize = 13.sp, color = OdysafeNavy)
        }
    }
}
