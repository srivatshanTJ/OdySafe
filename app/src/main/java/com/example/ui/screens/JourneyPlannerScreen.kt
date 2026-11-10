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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TravelMode
import com.example.data.model.Trip
import com.example.data.model.TripStatus
import com.example.ui.components.DemonstrationDataBanner
import com.example.ui.theme.*

@Composable
fun JourneyPlannerScreen(
    initialDestinationName: String = "",
    onSaveTrip: (title: String, origin: String, destination: String, mode: TravelMode, date: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var origin by remember { mutableStateOf("Madurai Junction Railway Station") }
    var destination by remember { mutableStateOf(initialDestinationName.ifBlank { "Meenakshi Amman Temple Complex" }) }
    var travelMode by remember { mutableStateOf(TravelMode.WALKING) }
    var travelDate by remember { mutableStateOf("Tomorrow, 08:00 AM") }
    var tripTitle by remember { mutableStateOf("Sacred Corridor Morning Walk") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("journey_planner_screen")
    ) {
        DemonstrationDataBanner()

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Plan a Safe Journey in India",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = OdysafeNavy
                    )
                )
                Text(
                    text = "Review routes, official precautions, and emergency stations before you travel.",
                    style = MaterialTheme.typography.bodySmall.copy(color = OdysafeTextSecondary),
                    modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                )

                // Origin
                OutlinedTextField(
                    value = origin,
                    onValueChange = { origin = it },
                    label = { Text("Starting Location (Origin)") },
                    leadingIcon = { Icon(Icons.Default.TripOrigin, contentDescription = null, tint = OdysafeTeal) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("plan_origin_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Destination
                OutlinedTextField(
                    value = destination,
                    onValueChange = { destination = it },
                    label = { Text("Destination") },
                    leadingIcon = { Icon(Icons.Default.Place, contentDescription = null, tint = OdysafeNavy) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("plan_destination_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Travel Mode Selector
                Text(
                    text = "Travel Mode",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OdysafeNavy
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(TravelMode.entries) { mode ->
                        val isSelected = travelMode == mode
                        Surface(
                            color = if (isSelected) OdysafeNavy else Color.White,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) OdysafeNavy else OdysafeBorder),
                            modifier = Modifier
                                .clickable { travelMode = mode }
                                .testTag("travel_mode_${mode.name.lowercase()}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val icon = when (mode) {
                                    TravelMode.WALKING -> Icons.Default.DirectionsWalk
                                    TravelMode.CYCLING -> Icons.Default.DirectionsBike
                                    TravelMode.CAR -> Icons.Default.DirectionsCar
                                    TravelMode.BUS -> Icons.Default.DirectionsBus
                                    TravelMode.TRAIN -> Icons.Default.Train
                                }
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else OdysafeNavy,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = mode.label,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) Color.White else OdysafeNavy
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = travelDate,
                    onValueChange = { travelDate = it },
                    label = { Text("Date & Departure Time") },
                    leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null, tint = OdysafeTeal) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("plan_date_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = tripTitle,
                    onValueChange = { tripTitle = it },
                    label = { Text("Trip Title / Label") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("plan_title_input")
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Route Preview & Official Safety Notice Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Route Estimates & Precautions",
                        fontWeight = FontWeight.Bold,
                        color = OdysafeNavy,
                        fontSize = 13.sp
                    )
                    Surface(color = OdysafeMint, shape = RoundedCornerShape(6.dp)) {
                        Text(
                            text = "Verified Path",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = OdysafeNavy,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column {
                        Text("Est. Distance", fontSize = 11.sp, color = OdysafeTextSecondary)
                        Text("1.6 km", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OdysafeNavy)
                    }
                    Column {
                        Text("Est. Duration", fontSize = 11.sp, color = OdysafeTextSecondary)
                        Text("22 mins", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OdysafeNavy)
                    }
                    Column {
                        Text("Emergency Assistance", fontSize = 11.sp, color = OdysafeTextSecondary)
                        Text("2 Facilities on Path", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OdysafeTeal)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Precaution list
                Text("Official Route Advice:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OdysafeNavy)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• Footwear prohibited inside Meenakshi sanctum; deposit at authorized stall.\n• Tourist Police Booth is active at Rayar Gopuram (East Gate).\n• Avoid midday heat between 12:00 PM and 03:00 PM.",
                    fontSize = 12.sp,
                    color = OdysafeTextPrimary,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                onSaveTrip(tripTitle, origin, destination, travelMode, travelDate)
            },
            colors = ButtonDefaults.buttonColors(containerColor = OdysafeNavy),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("plan_save_trip_button")
        ) {
            Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Save Journey & Prepare Checklist", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
fun TripDetailScreen(
    trip: Trip,
    onToggleChecklist: (String) -> Unit,
    onToggleLocationSharing: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("trip_detail_screen")
    ) {
        // Status header
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = OdysafeNavy),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Surface(
                    color = if (trip.status == TripStatus.ACTIVE) OdysafeSuccessContainer else OdysafeMint,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = trip.status.name,
                        color = if (trip.status == TripStatus.ACTIVE) OdysafeSuccess else OdysafeNavy,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = trip.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Text(
                    text = "${trip.origin} ➔ ${trip.destination}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFD4E2EB)),
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "${trip.date} • ${trip.distanceKm} km • ${trip.durationEst} (${trip.travelMode.label})",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Location sharing status for this trip
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (trip.isSharingLocation) OdysafeMint else Color.White
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ShareLocation,
                    contentDescription = null,
                    tint = if (trip.isSharingLocation) OdysafeTeal else OdysafeTextSecondary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (trip.isSharingLocation) "Live Location Sharing Active" else "Location Sharing Off",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = OdysafeNavy
                    )
                    Text(
                        text = if (trip.isSharingLocation) "Trusted contacts receive route updates" else "Tap switch to enable private sharing",
                        fontSize = 11.sp,
                        color = OdysafeTextSecondary
                    )
                }
                Switch(
                    checked = trip.isSharingLocation,
                    onCheckedChange = { onToggleLocationSharing() },
                    colors = SwitchDefaults.colors(checkedThumbColor = OdysafeTeal)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Interactive Checklist
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Safety Preparation Checklist",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = OdysafeNavy
                    )
                )
                Text(
                    text = "Ensure these items are confirmed before departure:",
                    fontSize = 12.sp,
                    color = OdysafeTextSecondary,
                    modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                )

                trip.checklist.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleChecklist(item.id) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = item.isChecked,
                            onCheckedChange = { onToggleChecklist(item.id) },
                            colors = CheckboxDefaults.colors(checkedColor = OdysafeTeal)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = item.text,
                            fontSize = 13.sp,
                            color = if (item.isChecked) OdysafeTextSecondary else OdysafeNavy,
                            fontWeight = if (item.isChecked) FontWeight.Normal else FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Stops along route
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Stops Along Route",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = OdysafeNavy
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                trip.stops.forEachIndexed { idx, stop ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = OdysafeNavy,
                            shape = androidx.compose.foundation.shape.CircleShape,
                            modifier = Modifier.size(20.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = (idx + 1).toString(),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = stop, fontSize = 13.sp, color = OdysafeNavy)
                    }
                }
            }
        }
    }
}

@Composable
fun TripsListScreen(
    trips: List<Trip>,
    onTripClick: (String) -> Unit,
    onPlanNewTripClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf("ALL") }

    val filteredTrips = remember(trips, selectedTab) {
        when (selectedTab) {
            "UPCOMING" -> trips.filter { it.status == TripStatus.UPCOMING }
            "ACTIVE" -> trips.filter { it.status == TripStatus.ACTIVE }
            "COMPLETED" -> trips.filter { it.status == TripStatus.COMPLETED }
            else -> trips
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .testTag("trips_list_screen"),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Journeys",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = OdysafeNavy
                    )
                )
                Button(
                    onClick = onPlanNewTripClick,
                    colors = ButtonDefaults.buttonColors(containerColor = OdysafeTeal),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Trip", fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Tabs
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val tabs = listOf(
                    "ALL" to "All Journeys",
                    "ACTIVE" to "Active",
                    "UPCOMING" to "Upcoming",
                    "COMPLETED" to "Completed"
                )
                items(tabs) { (code, label) ->
                    FilterChip(
                        selected = selectedTab == code,
                        onClick = { selectedTab = code },
                        label = { Text(label, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OdysafeNavy,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (filteredTrips.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CardTravel, contentDescription = null, tint = OdysafeTextSecondary, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("No trips found in this category", fontWeight = FontWeight.Bold, color = OdysafeNavy)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(onClick = onPlanNewTripClick) {
                            Text("Start Planning a Trip")
                        }
                    }
                }
            }
        } else {
            items(filteredTrips) { trip ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onTripClick(trip.id) }
                        .testTag("trip_card_${trip.id}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = when (trip.status) {
                                    TripStatus.ACTIVE -> OdysafeSuccessContainer
                                    TripStatus.UPCOMING -> OdysafeMint
                                    TripStatus.COMPLETED -> Color(0xFFEFF2F4)
                                },
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = trip.status.name,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (trip.status) {
                                        TripStatus.ACTIVE -> OdysafeSuccess
                                        TripStatus.UPCOMING -> OdysafeNavy
                                        TripStatus.COMPLETED -> OdysafeTextSecondary
                                    },
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(text = trip.date, fontSize = 11.sp, color = OdysafeTextSecondary)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = trip.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = OdysafeNavy
                        )

                        Text(
                            text = "${trip.origin} ➔ ${trip.destination}",
                            fontSize = 12.sp,
                            color = OdysafeTextSecondary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${trip.distanceKm} km • ${trip.durationEst} (${trip.travelMode.label})",
                                fontSize = 11.sp,
                                color = OdysafeTeal,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (trip.isSharingLocation) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "• Location Shared",
                                    fontSize = 11.sp,
                                    color = OdysafeSuccess,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
