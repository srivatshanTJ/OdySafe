package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LocationSuggestion
import com.example.data.model.SuggestionType
import com.example.ui.theme.*

@Composable
fun LocationSuggestionsScreen(
    currentCity: String,
    suggestions: List<LocationSuggestion>,
    selectedType: SuggestionType,
    isLoading: Boolean,
    customAssistantAnswer: String? = null,
    isAssistantThinking: Boolean = false,
    onFilterType: (SuggestionType) -> Unit,
    onSelectCity: (String) -> Unit,
    onDialNumber: (String) -> Unit,
    onRefreshSuggestions: (String) -> Unit,
    onFocusPlaceOnMap: (String, Double, Double, String) -> Unit = { _, _, _, _ -> },
    onAskAssistant: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var userPromptQuery by remember { mutableStateOf("") }
    var onlyTopRated by remember { mutableStateOf(false) }
    var onlyNearMe by remember { mutableStateOf(false) }

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

    val filteredSuggestions = remember(suggestions, selectedType, searchQuery, onlyTopRated, onlyNearMe) {
        suggestions.filter { item ->
            val matchesType = (selectedType == SuggestionType.ALL || item.type == selectedType)
            val matchesQuery = if (searchQuery.isBlank()) true else {
                item.title.contains(searchQuery, ignoreCase = true) ||
                        item.subtitle.contains(searchQuery, ignoreCase = true) ||
                        item.description.contains(searchQuery, ignoreCase = true) ||
                        item.safetyTip.contains(searchQuery, ignoreCase = true) ||
                        item.address.contains(searchQuery, ignoreCase = true)
            }
            val matchesRating = if (onlyTopRated) {
                val r = item.rating.replace("★", "").trim().toDoubleOrNull() ?: 0.0
                r >= 4.8
            } else true
            val matchesNear = if (onlyNearMe) {
                item.distanceText.contains("0.") || item.distanceText.contains("1.")
            } else true

            matchesType && matchesQuery && matchesRating && matchesNear
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .testTag("location_suggestions_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // ==========================================
        // 1. Location Header Banner with Live Telemetry
        // ==========================================
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = OdysafeNavy),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
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
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(OdysafeSuccess)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "LOCATION-BASED INTELLIGENCE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OdysafeNavy
                                )
                            }
                        }

                        if (isLoading) {
                            CircularProgressIndicator(
                                color = OdysafeMint,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            IconButton(
                                onClick = { onRefreshSuggestions(currentCity) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Curated Guide for ${currentCity.split(",").first()}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color.White
                    )

                    Text(
                        text = "Verified tourist spots, safe hotels, authentic dining & transit safety tips customized for your current location in India.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFD4E2EB), lineHeight = 18.sp),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // ==========================================
        // 2. City Switcher Row
        // ==========================================
        item {
            Column {
                Text(
                    text = "Select Indian Destination / Region:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OdysafeNavy
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(indianCities) { city ->
                        val cityName = city.split(",").first().trim()
                        val isSelected = currentCity.contains(cityName, ignoreCase = true)
                        Surface(
                            color = if (isSelected) OdysafeTeal else Color.White,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) OdysafeTeal else OdysafeBorder),
                            modifier = Modifier
                                .clickable { onSelectCity(city) }
                                .testTag("sug_city_${cityName.lowercase()}")
                        ) {
                            Text(
                                text = cityName,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else OdysafeNavy,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // ==========================================
        // 3. Search & Quick Filters
        // ==========================================
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = OdysafeTeal, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Filter hotels, spots, food, tips...", fontSize = 12.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("suggestions_search_input")
                        )
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = OdysafeTextSecondary, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                // Quick toggle chips
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = onlyTopRated,
                        onClick = { onlyTopRated = !onlyTopRated },
                        label = { Text("⭐ Top Rated (4.8+)", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OdysafeNavy,
                            selectedLabelColor = Color.White
                        )
                    )
                    FilterChip(
                        selected = onlyNearMe,
                        onClick = { onlyNearMe = !onlyNearMe },
                        label = { Text("📍 Near Me (< 1.5 km)", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OdysafeTeal,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // ==========================================
        // 4. Category Tabs
        // ==========================================
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val categoryTabs = listOf(
                    SuggestionType.ALL to "All Suggestions",
                    SuggestionType.TOURIST to "🏛️ Tourist Spots",
                    SuggestionType.HOTEL to "🏨 Safe Hotels",
                    SuggestionType.FOOD to "🍛 Dining & Food",
                    SuggestionType.SAFETY_TIP to "🛡️ Safety Tips"
                )
                items(categoryTabs) { (type, label) ->
                    val isSelected = selectedType == type
                    Surface(
                        color = if (isSelected) OdysafeNavy else Color.White,
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) OdysafeNavy else OdysafeBorder),
                        modifier = Modifier
                            .clickable { onFilterType(type) }
                            .testTag("sug_tab_${type.name.lowercase()}")
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else OdysafeNavy,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // ==========================================
        // 5. Ask Odysafe AI Assistant Section
        // ==========================================
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = OdysafeTeal, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ask Local Safety & Travel Assistant",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = OdysafeNavy
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Need specific advice in ${currentCity.split(",").first()}? (e.g. Solo female safe areas, 24hr check-in, late night pure-veg food)",
                        fontSize = 11.sp,
                        color = OdysafeTextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = userPromptQuery,
                            onValueChange = { userPromptQuery = it },
                            placeholder = { Text("Ask anything about $currentCity...", fontSize = 12.sp) },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("ai_assistant_query_input")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (userPromptQuery.isNotBlank()) {
                                    onAskAssistant(userPromptQuery)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = OdysafeNavy),
                            modifier = Modifier.testTag("ai_assistant_ask_btn")
                        ) {
                            if (isAssistantThinking) {
                                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
                            } else {
                                Icon(Icons.Default.Send, contentDescription = "Ask", modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    if (customAssistantAnswer != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            color = OdysafeMint,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Odysafe Local Intelligence Response:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = OdysafeNavy
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = customAssistantAnswer,
                                    fontSize = 12.sp,
                                    color = OdysafeNavy,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // ==========================================
        // 6. Suggestion Items List
        // ==========================================
        if (filteredSuggestions.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.SearchOff, contentDescription = null, tint = OdysafeTextSecondary, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("No matching suggestions found", fontWeight = FontWeight.Bold, color = OdysafeNavy)
                        Text(
                            text = "Try clearing filters or search query to see all recommendations for $currentCity.",
                            fontSize = 12.sp,
                            color = OdysafeTextSecondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(onClick = {
                            searchQuery = ""
                            onlyTopRated = false
                            onlyNearMe = false
                            onFilterType(SuggestionType.ALL)
                        }) {
                            Text("Reset All Filters")
                        }
                    }
                }
            }
        } else {
            items(filteredSuggestions) { item ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("sug_card_${item.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Type pill & Rating row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val pillBg = when (item.type) {
                                SuggestionType.TOURIST -> Color(0xFFE3F2FD)
                                SuggestionType.HOTEL -> Color(0xFFEDE7F6)
                                SuggestionType.FOOD -> Color(0xFFFFF3E0)
                                SuggestionType.SAFETY_TIP -> OdysafeMint
                                else -> OdysafeBg
                            }
                            val pillColor = when (item.type) {
                                SuggestionType.TOURIST -> Color(0xFF1565C0)
                                SuggestionType.HOTEL -> Color(0xFF5E35B1)
                                SuggestionType.FOOD -> Color(0xFFEF6C00)
                                SuggestionType.SAFETY_TIP -> OdysafeNavy
                                else -> OdysafeNavy
                            }

                            Surface(
                                color = pillBg,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = item.type.label.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = pillColor,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = item.rating,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = OdysafeTeal
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = item.distanceText,
                                    fontSize = 11.sp,
                                    color = OdysafeTextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Title & Verification
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OdysafeNavy
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            if (item.isVerified) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = OdysafeSuccessContainer,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Verified, contentDescription = "Verified", tint = OdysafeSuccess, modifier = Modifier.size(11.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text("VERIFIED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = OdysafeSuccess)
                                    }
                                }
                            }
                        }

                        Text(
                            text = item.subtitle,
                            fontSize = 12.sp,
                            color = OdysafeNavy.copy(alpha = 0.85f),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 2.dp)
                        )

                        Text(
                            text = item.description,
                            fontSize = 12.sp,
                            color = OdysafeTextSecondary,
                            lineHeight = 17.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        // Best time to visit / Timings
                        if (item.bestTimeToVisit != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AccessTime, contentDescription = null, tint = OdysafeTeal, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Hours / Best time: ${item.bestTimeToVisit}",
                                    fontSize = 11.sp,
                                    color = OdysafeTextSecondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Dedicated Safety Advice Callout Box
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            color = OdysafeMint,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = OdysafeTeal,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Odysafe Local Advisory:",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OdysafeNavy
                                    )
                                    Text(
                                        text = item.safetyTip,
                                        fontSize = 11.sp,
                                        color = OdysafeNavy,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Interactive Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // View on Map (plots marker & route on Live MapScreen)
                            Button(
                                onClick = {
                                    val cat = when (item.type) {
                                        SuggestionType.HOTEL -> "HOTEL"
                                        SuggestionType.FOOD -> "FOOD"
                                        SuggestionType.TOURIST -> "TOURIST"
                                        else -> "SERVICE"
                                    }
                                    onFocusPlaceOnMap(item.title, item.latitude, item.longitude, cat)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = OdysafeNavy),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("sug_view_on_map_${item.id}")
                            ) {
                                Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("View on Map", fontSize = 11.sp)
                            }

                            // Directions
                            OutlinedButton(
                                onClick = {
                                    try {
                                        val geoUri = Uri.parse("geo:${item.latitude},${item.longitude}?q=${Uri.encode(item.title)}")
                                        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
                                        context.startActivity(mapIntent)
                                    } catch (_: Exception) {
                                        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/?q=${Uri.encode(item.title)}"))
                                        context.startActivity(webIntent)
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("sug_directions_${item.id}")
                            ) {
                                Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Directions", fontSize = 11.sp)
                            }

                            // Call if available
                            if (item.phone != null) {
                                IconButton(
                                    onClick = { onDialNumber(item.phone) },
                                    modifier = Modifier
                                        .size(38.dp)
                                        .testTag("sug_call_${item.id}")
                                ) {
                                    Icon(Icons.Default.Call, contentDescription = "Call", tint = OdysafeTeal)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
