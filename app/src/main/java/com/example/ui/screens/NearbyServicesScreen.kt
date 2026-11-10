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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NearbyService
import com.example.data.model.ServiceType
import com.example.ui.components.DemonstrationDataBanner
import com.example.ui.components.ServiceItemCard
import com.example.ui.theme.*

@Composable
fun NearbyServicesScreen(
    services: List<NearbyService>,
    initialFilter: String = "ALL",
    onDialNumber: (String) -> Unit,
    onToggleSave: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf(initialFilter) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredServices = remember(services, selectedFilter, searchQuery) {
        services.filter { item ->
            val matchesType = when (selectedFilter) {
                "HOSPITAL" -> item.type == ServiceType.HOSPITAL
                "PHARMACY" -> item.type == ServiceType.PHARMACY
                "POLICE" -> item.type == ServiceType.POLICE
                "HOTEL" -> item.type == ServiceType.HOTEL
                "RESTAURANT" -> item.type == ServiceType.RESTAURANT
                "ATTRACTION" -> item.type == ServiceType.ATTRACTION
                else -> true
            }
            val matchesQuery = if (searchQuery.isBlank()) true else {
                item.name.contains(searchQuery, ignoreCase = true) ||
                        item.address.contains(searchQuery, ignoreCase = true)
            }
            matchesType && matchesQuery
        }
    }

    val (emergencyFacilities, standardServices) = remember(filteredServices) {
        filteredServices.partition { it.isEmergencyFacility }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .testTag("nearby_services_screen"),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Search & Filter header
        item {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search hospitals, pharmacies, police...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = OdysafeTeal) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("nearby_search_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val filterOptions = listOf(
                        "ALL" to "All Places",
                        "HOSPITAL" to "Hospitals (24/7)",
                        "POLICE" to "Police (112)",
                        "PHARMACY" to "Pharmacies",
                        "HOTEL" to "Stays",
                        "RESTAURANT" to "Food",
                        "ATTRACTION" to "Attractions"
                    )
                    items(filterOptions) { (key, label) ->
                        FilterChip(
                            selected = selectedFilter == key,
                            onClick = { selectedFilter = key },
                            label = { Text(label, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = OdysafeNavy,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("filter_chip_${key.lowercase()}")
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            DemonstrationDataBanner()
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Emergency Section Header (if applicable)
        if (emergencyFacilities.isNotEmpty() && (selectedFilter == "ALL" || selectedFilter == "HOSPITAL" || selectedFilter == "POLICE")) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.HealthAndSafety, contentDescription = null, tint = OdysafeDanger, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Official Emergency & Trauma Facilities",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = OdysafeDanger
                    )
                }
            }

            items(emergencyFacilities) { item ->
                Box(modifier = Modifier.padding(vertical = 4.dp)) {
                    ServiceItemCard(
                        service = item,
                        onCallClick = { onDialNumber(it) },
                        onDirectionsClick = { /* Directions */ },
                        onToggleSave = { onToggleSave(item.id) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Standard Services Section
        if (standardServices.isNotEmpty()) {
            if (emergencyFacilities.isNotEmpty()) {
                item {
                    Text(
                        text = "General Services & Amenities",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = OdysafeNavy,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }

            items(standardServices) { item ->
                Box(modifier = Modifier.padding(vertical = 4.dp)) {
                    ServiceItemCard(
                        service = item,
                        onCallClick = { onDialNumber(it) },
                        onDirectionsClick = { /* Directions */ },
                        onToggleSave = { onToggleSave(item.id) }
                    )
                }
            }
        }

        if (filteredServices.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.SearchOff, contentDescription = null, tint = OdysafeTextSecondary, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("No services match your filters", fontWeight = FontWeight.Bold, color = OdysafeNavy)
                    Text("Try expanding the category or clearing the search query.", fontSize = 12.sp, color = OdysafeTextSecondary)
                }
            }
        }
    }
}
