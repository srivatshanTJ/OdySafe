package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Destination
import com.example.ui.components.DemonstrationDataBanner
import com.example.ui.components.DestinationCard
import com.example.ui.theme.*

@Composable
fun LandingScreen(
    destinations: List<Destination>,
    onStartExploring: () -> Unit,
    onSignInClick: () -> Unit,
    onDestinationClick: (String) -> Unit,
    onToggleSave: (String) -> Unit,
    onSafetyGuidelinesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .testTag("landing_screen"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Top Header
        item {
            Surface(
                color = Color.White,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(OdysafeNavy),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = OdysafeMint,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Odysafe",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = OdysafeNavy
                            )
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextButton(
                            onClick = onSignInClick,
                            modifier = Modifier.testTag("landing_sign_in_button")
                        ) {
                            Text("Sign In", color = OdysafeNavy, fontWeight = FontWeight.SemiBold)
                        }
                        Button(
                            onClick = onStartExploring,
                            colors = ButtonDefaults.buttonColors(containerColor = OdysafeTeal),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("landing_explore_button")
                        ) {
                            Text("Start", fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Demo Data Notice
        item {
            Box(modifier = Modifier.padding(16.dp)) {
                DemonstrationDataBanner()
            }
        }

        // Hero Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = OdysafeNavy)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Surface(
                        color = OdysafeMint.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "INTELLIGENT TRAVEL SAFETY IN INDIA",
                            color = OdysafeMint,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "See more of India.\nTravel with confidence.",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            lineHeight = 34.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Proactive safety advisories, verified emergency facilities, private location sharing, and offline guidance designed for solo travelers, families, and pilgrims across India.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFFD4E2EB),
                            lineHeight = 22.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onStartExploring,
                            colors = ButtonDefaults.buttonColors(containerColor = OdysafeTeal),
                            modifier = Modifier.weight(1f).testTag("hero_start_exploring_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Explore, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Explore Places", fontWeight = FontWeight.SemiBold)
                        }

                        OutlinedButton(
                            onClick = onSignInClick,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Join Odysafe", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // Philosophy & Value Pillars
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Core Safety Philosophy",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = OdysafeNavy
                    )
                )
                Text(
                    text = "“Explore freely. Travel confidently. Stay connected.”",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = OdysafeTeal,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PillarCard(
                        icon = Icons.Default.VerifiedUser,
                        title = "Verified Data",
                        desc = "Sources from IMD & Police",
                        modifier = Modifier.weight(1f)
                    )
                    PillarCard(
                        icon = Icons.Default.LocationOn,
                        title = "Private Sharing",
                        desc = "Explicit contact consent",
                        modifier = Modifier.weight(1f)
                    )
                    PillarCard(
                        icon = Icons.Default.Translate,
                        title = "Multilingual",
                        desc = "Tamil, Hindi & English",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Featured Indian Destinations Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Popular Indian Destinations",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OdysafeNavy
                        )
                    )
                    Text(
                        text = "Heritage corridors, hill stations, and sacred sanctuaries",
                        style = MaterialTheme.typography.bodySmall.copy(color = OdysafeTextSecondary)
                    )
                }

                TextButton(onClick = onStartExploring) {
                    Text("See all", color = OdysafeTeal, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // Destinations list
        items(destinations.take(4)) { dest ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                DestinationCard(
                    destination = dest,
                    onClick = { onDestinationClick(dest.id) },
                    onToggleSave = { onToggleSave(dest.id) }
                )
            }
        }

        // Safety Guidelines Callout & Footer
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = OdysafeMint.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, tint = OdysafeTeal)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Before You Travel Checklist",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = OdysafeNavy
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "1. Confirm ghat road fog advisories before early departures.\n2. Add at least one primary trusted contact.\n3. Note emergency numbers 112 (Unified) and 108 (Ambulance).",
                        style = MaterialTheme.typography.bodySmall.copy(color = OdysafeNavy, lineHeight = 20.sp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextButton(onClick = onSafetyGuidelinesClick) {
                        Text("Read Complete Travel Safety Guidelines →", color = OdysafeTeal, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun PillarCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = OdysafeTeal, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = OdysafeNavy)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = desc, fontSize = 10.sp, color = OdysafeTextSecondary, lineHeight = 14.sp)
        }
    }
}
