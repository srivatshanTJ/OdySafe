package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AlertSeverity
import com.example.data.model.Destination
import com.example.data.model.NearbyService
import com.example.ui.state.NavigationTab
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OdysafeTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    onNavigateBack: () -> Unit,
    onEmergencyClick: () -> Unit,
    onNotificationsClick: (() -> Unit)? = null,
    unreadNotifications: Int = 0,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier.testTag("odysafe_top_app_bar"),
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = OdysafeNavy
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("nav_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = OdysafeNavy
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(OdysafeNavy),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Odysafe Logo",
                        tint = OdysafeMint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        actions = {
            // Quick SOS Button in shell header
            FilledTonalButton(
                onClick = onEmergencyClick,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = OdysafeDangerContainer,
                    contentColor = OdysafeDanger
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .testTag("top_bar_sos_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "SOS",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            if (onNotificationsClick != null) {
                IconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier.testTag("top_bar_notifications_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (unreadNotifications > 0) {
                                Badge(
                                    containerColor = OdysafeTeal,
                                    contentColor = Color.White
                                ) {
                                    Text(unreadNotifications.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = OdysafeNavy
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = OdysafeNavy
        )
    )
}

@Composable
fun OdysafeBottomNavigationBar(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .testTag("odysafe_bottom_navigation")
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentTab == NavigationTab.HOME,
            onClick = { onTabSelected(NavigationTab.HOME) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OdysafeNavy,
                selectedTextColor = OdysafeNavy,
                indicatorColor = OdysafeMint,
                unselectedIconColor = OdysafeTextSecondary,
                unselectedTextColor = OdysafeTextSecondary
            ),
            modifier = Modifier.testTag("bottom_tab_home")
        )
        NavigationBarItem(
            selected = currentTab == NavigationTab.EXPLORE,
            onClick = { onTabSelected(NavigationTab.EXPLORE) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.EXPLORE) Icons.Filled.Explore else Icons.Outlined.Explore,
                    contentDescription = "Explore"
                )
            },
            label = { Text("Explore", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OdysafeNavy,
                selectedTextColor = OdysafeNavy,
                indicatorColor = OdysafeMint,
                unselectedIconColor = OdysafeTextSecondary,
                unselectedTextColor = OdysafeTextSecondary
            ),
            modifier = Modifier.testTag("bottom_tab_explore")
        )
        NavigationBarItem(
            selected = currentTab == NavigationTab.MAP,
            onClick = { onTabSelected(NavigationTab.MAP) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.MAP) Icons.Filled.Map else Icons.Outlined.Map,
                    contentDescription = "Map"
                )
            },
            label = { Text("Map", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OdysafeNavy,
                selectedTextColor = OdysafeNavy,
                indicatorColor = OdysafeMint,
                unselectedIconColor = OdysafeTextSecondary,
                unselectedTextColor = OdysafeTextSecondary
            ),
            modifier = Modifier.testTag("bottom_tab_map")
        )
        NavigationBarItem(
            selected = currentTab == NavigationTab.TRIPS,
            onClick = { onTabSelected(NavigationTab.TRIPS) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.TRIPS) Icons.Filled.CardTravel else Icons.Outlined.CardTravel,
                    contentDescription = "Trips"
                )
            },
            label = { Text("Trips", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OdysafeNavy,
                selectedTextColor = OdysafeNavy,
                indicatorColor = OdysafeMint,
                unselectedIconColor = OdysafeTextSecondary,
                unselectedTextColor = OdysafeTextSecondary
            ),
            modifier = Modifier.testTag("bottom_tab_trips")
        )
        NavigationBarItem(
            selected = currentTab == NavigationTab.PROFILE,
            onClick = { onTabSelected(NavigationTab.PROFILE) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Profile"
                )
            },
            label = { Text("Profile", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OdysafeNavy,
                selectedTextColor = OdysafeNavy,
                indicatorColor = OdysafeMint,
                unselectedIconColor = OdysafeTextSecondary,
                unselectedTextColor = OdysafeTextSecondary
            ),
            modifier = Modifier.testTag("bottom_tab_profile")
        )
    }
}

@Composable
fun SeverityBadge(
    severity: AlertSeverity,
    text: String? = null,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, icon) = when (severity) {
        AlertSeverity.INFO -> Triple(Color(0xFFE5F5F7), OdysafeTeal, Icons.Default.Info)
        AlertSeverity.CAUTION -> Triple(OdysafeCautionContainer, OdysafeCaution, Icons.Default.WarningAmber)
        AlertSeverity.DANGER -> Triple(OdysafeDangerContainer, OdysafeDanger, Icons.Default.ErrorOutline)
    }
    val label = text ?: when (severity) {
        AlertSeverity.INFO -> "Normal Conditions"
        AlertSeverity.CAUTION -> "Caution Advisory"
        AlertSeverity.DANGER -> "High Risk Warning"
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun DemonstrationDataBanner(
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFFF1F5F8),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.VerifiedUser,
                contentDescription = null,
                tint = OdysafeTextSecondary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Demonstration Mode • Factual Indian Travel Safety Data",
                fontSize = 11.sp,
                color = OdysafeTextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun DestinationCard(
    destination: Destination,
    onClick: () -> Unit,
    onToggleSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("destination_card_${destination.id}")
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Visual header representation with color styling and badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(
                        when (destination.category.name) {
                            "HILLS" -> Color(0xFF355C54)
                            "BEACHES" -> Color(0xFF1E526B)
                            "HERITAGE" -> Color(0xFF6B4226)
                            "SPIRITUAL" -> Color(0xFF543C6B)
                            else -> OdysafeNavy
                        }
                    )
            ) {
                // Category tag
                Surface(
                    color = Color.White.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = destination.category.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = OdysafeNavy,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                // Bookmark icon button
                IconButton(
                    onClick = onToggleSave,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .testTag("save_button_${destination.id}")
                ) {
                    Icon(
                        imageVector = if (destination.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (destination.isSaved) OdysafeTeal else Color.White
                    )
                }

                // Title overlay
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = destination.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = destination.state,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = destination.tagline,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = OdysafeTextPrimary,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Safety advisory preview pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SeverityBadge(severity = destination.safetySeverity)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = destination.safetyTimestamp,
                        fontSize = 10.sp,
                        color = OdysafeTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceItemCard(
    service: NearbyService,
    onCallClick: (String) -> Unit,
    onDirectionsClick: () -> Unit,
    onToggleSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("service_card_${service.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                val icon = when (service.type.name) {
                    "HOSPITAL" -> Icons.Default.LocalHospital
                    "PHARMACY" -> Icons.Default.MedicalServices
                    "POLICE" -> Icons.Default.LocalPolice
                    "HOTEL" -> Icons.Default.Hotel
                    "RESTAURANT" -> Icons.Default.Restaurant
                    else -> Icons.Default.Place
                }
                val iconBg = if (service.isEmergencyFacility) OdysafeDangerContainer else OdysafeMint

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (service.isEmergencyFacility) OdysafeDanger else OdysafeTeal,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = service.name,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = OdysafeTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        if (service.verifiedBadge) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified Facility",
                                tint = OdysafeTeal,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = service.address,
                        style = MaterialTheme.typography.bodySmall.copy(color = OdysafeTextSecondary),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${service.distanceKm} km away",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OdysafeTeal
                        )
                        Text(
                            text = " • ${service.openHours}",
                            fontSize = 11.sp,
                            color = OdysafeTextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(onClick = onToggleSave) {
                    Icon(
                        imageVector = if (service.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (service.isSaved) OdysafeTeal else OdysafeTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onCallClick(service.phone) },
                    modifier = Modifier.weight(1f).testTag("call_button_${service.id}"),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Call ${service.phone.take(12)}", fontSize = 12.sp)
                }

                Button(
                    onClick = onDirectionsClick,
                    colors = ButtonDefaults.buttonColors(containerColor = OdysafeNavy),
                    modifier = Modifier.weight(1f).testTag("directions_button_${service.id}"),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Directions", fontSize = 12.sp)
                }
            }
        }
    }
}
