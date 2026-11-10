package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.model.Destination
import com.example.data.model.EmergencyContact
import com.example.data.model.NearbyService
import com.example.data.model.UserProfile
import com.example.ui.components.DemonstrationDataBanner
import com.example.ui.components.DestinationCard
import com.example.ui.components.ServiceItemCard
import com.example.ui.state.NotificationItem
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    onNavigateToContacts: () -> Unit,
    onNavigateToSaved: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("profile_screen")
    ) {
        // User Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(OdysafeNavy),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "AS",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = userProfile.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = OdysafeNavy
                    )
                    Text(text = userProfile.email, fontSize = 12.sp, color = OdysafeTextSecondary)
                    Text(text = "${userProfile.phone} • Blood Group: ${userProfile.bloodGroup}", fontSize = 11.sp, color = OdysafeTeal)
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Safety & Preferences",
            fontWeight = FontWeight.Bold,
            color = OdysafeNavy,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        ProfileMenuItem(icon = Icons.Default.ContactPhone, title = "Trusted Emergency Contacts", subtitle = "2 contacts configured", onClick = onNavigateToContacts)
        ProfileMenuItem(icon = Icons.Default.Bookmark, title = "Saved Places & Facilities", subtitle = "Bookmarks for offline reference", onClick = onNavigateToSaved)
        ProfileMenuItem(icon = Icons.Default.Settings, title = "Settings & Accessibility", subtitle = "Text size, contrast, language, privacy", onClick = onNavigateToSettings)
        ProfileMenuItem(icon = Icons.Default.HelpOutline, title = "Help, FAQs & Guidelines", subtitle = "Offline guide and emergency limitations", onClick = onNavigateToHelp)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = onSignOut,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = OdysafeDanger),
            border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeDanger),
            modifier = Modifier.fillMaxWidth().testTag("profile_sign_out_button")
        ) {
            Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Sign Out of Session", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
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
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = OdysafeNavy)
                Text(text = subtitle, fontSize = 11.sp, color = OdysafeTextSecondary)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = OdysafeTextSecondary)
        }
    }
}

@Composable
fun SettingsHubScreen(
    userProfile: UserProfile,
    onUpdateAccessibility: (largeText: Boolean, highContrast: Boolean, reducedMotion: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var largeText by remember { mutableStateOf(userProfile.largeText) }
    var highContrast by remember { mutableStateOf(userProfile.highContrast) }
    var reducedMotion by remember { mutableStateOf(userProfile.reducedMotion) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("settings_hub_screen")
    ) {
        Text(
            text = "Settings & Accessibility",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = OdysafeNavy
            )
        )
        Text(
            text = "Customize your visual preferences, language, and privacy controls.",
            style = MaterialTheme.typography.bodySmall.copy(color = OdysafeTextSecondary),
            modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
        )

        // Accessibility Section
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Accessibility Preferences",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = OdysafeNavy
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Larger Text", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = OdysafeNavy)
                        Text("Increases readable font sizes for comfortable viewing", fontSize = 11.sp, color = OdysafeTextSecondary)
                    }
                    Switch(
                        checked = largeText,
                        onCheckedChange = {
                            largeText = it
                            onUpdateAccessibility(it, highContrast, reducedMotion)
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = OdysafeTeal)
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 10.dp), color = OdysafeBorder)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Higher Contrast", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = OdysafeNavy)
                        Text("Enhances border visibility and contrast for outdoor sunlight", fontSize = 11.sp, color = OdysafeTextSecondary)
                    }
                    Switch(
                        checked = highContrast,
                        onCheckedChange = {
                            highContrast = it
                            onUpdateAccessibility(largeText, it, reducedMotion)
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = OdysafeTeal)
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 10.dp), color = OdysafeBorder)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Reduced Motion", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = OdysafeNavy)
                        Text("Minimizes transitions and animated indicators", fontSize = 11.sp, color = OdysafeTextSecondary)
                    }
                    Switch(
                        checked = reducedMotion,
                        onCheckedChange = {
                            reducedMotion = it
                            onUpdateAccessibility(largeText, highContrast, it)
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = OdysafeTeal)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy Summary
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Privacy & Location Data",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = OdysafeNavy
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Location is accessed on-demand for nearby services.\n• Background tracking is never initiated without active location sharing.\n• All safety alerts come from official published channels.",
                    fontSize = 12.sp,
                    color = OdysafeTextSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun EmergencyContactsScreen(
    contacts: List<EmergencyContact>,
    onAddContact: (name: String, phone: String, relationship: String) -> Unit,
    onRemoveContact: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newPhone by remember { mutableStateOf("") }
    var newRelationship by remember { mutableStateOf("Family") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .padding(16.dp)
            .testTag("emergency_contacts_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Trusted Emergency Contacts",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = OdysafeNavy
                )
            )
            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = OdysafeTeal),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Contacts who receive your SOS broadcasts and location updates.",
            fontSize = 12.sp,
            color = OdysafeTextSecondary
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(contacts) { contact ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (contact.isPrimary) OdysafeNavy else OdysafeMint),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = if (contact.isPrimary) Color.White else OdysafeTeal,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = contact.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = OdysafeNavy)
                                if (contact.isPrimary) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(color = OdysafeMint, shape = RoundedCornerShape(4.dp)) {
                                        Text(
                                            text = "PRIMARY",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = OdysafeNavy,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                            Text(text = "${contact.phone} • ${contact.relationship}", fontSize = 11.sp, color = OdysafeTextSecondary)
                        }

                        IconButton(onClick = { onRemoveContact(contact.id) }) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = OdysafeDanger)
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Trusted Contact", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Full Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPhone,
                        onValueChange = { newPhone = it },
                        label = { Text("Phone (+91)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newRelationship,
                        onValueChange = { newRelationship = it },
                        label = { Text("Relationship (Family, Friend, Partner)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newName.isNotBlank() && newPhone.isNotBlank()) {
                            onAddContact(newName, newPhone, newRelationship)
                            showAddDialog = false
                            newName = ""
                            newPhone = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OdysafeTeal)
                ) {
                    Text("Save Contact")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SavedPlacesScreen(
    destinations: List<Destination>,
    services: List<NearbyService>,
    onDestinationClick: (String) -> Unit,
    onToggleSaveDestination: (String) -> Unit,
    onToggleSaveService: (String) -> Unit,
    onDialService: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val savedDestinations = remember(destinations) { destinations.filter { it.isSaved } }
    val savedServices = remember(services) { services.filter { it.isSaved } }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .padding(16.dp)
            .testTag("saved_places_screen")
    ) {
        item {
            Text(
                text = "Saved Places & Bookmarks",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = OdysafeNavy
                )
            )
            Text(
                text = "Available for offline reference even with weak connectivity in India.",
                style = MaterialTheme.typography.bodySmall.copy(color = OdysafeTextSecondary),
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )
        }

        if (savedDestinations.isEmpty() && savedServices.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = OdysafeTextSecondary, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("No saved bookmarks yet", fontWeight = FontWeight.Bold, color = OdysafeNavy)
                        Text("Tap the bookmark icon on any destination or hospital to save it.", fontSize = 12.sp, color = OdysafeTextSecondary)
                    }
                }
            }
        } else {
            if (savedDestinations.isNotEmpty()) {
                item {
                    Text("Saved Destinations", fontWeight = FontWeight.Bold, color = OdysafeNavy, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                }
                items(savedDestinations) { dest ->
                    Box(modifier = Modifier.padding(vertical = 4.dp)) {
                        DestinationCard(
                            destination = dest,
                            onClick = { onDestinationClick(dest.id) },
                            onToggleSave = { onToggleSaveDestination(dest.id) }
                        )
                    }
                }
            }

            if (savedServices.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Saved Facilities & Services", fontWeight = FontWeight.Bold, color = OdysafeNavy, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                }
                items(savedServices) { service ->
                    Box(modifier = Modifier.padding(vertical = 4.dp)) {
                        ServiceItemCard(
                            service = service,
                            onCallClick = { onDialService(it) },
                            onDirectionsClick = { },
                            onToggleSave = { onToggleSaveService(service.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem>,
    onMarkAllRead: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .padding(16.dp)
            .testTag("notifications_screen")
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notifications & Advisories",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = OdysafeNavy
                    )
                )
                TextButton(onClick = onMarkAllRead) {
                    Text("Mark all read", color = OdysafeTeal, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(notifications) { notif ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (notif.isRead) Color.White else OdysafeMint.copy(alpha = 0.4f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = notif.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = OdysafeNavy)
                        Text(text = notif.timestamp, fontSize = 10.sp, color = OdysafeTextSecondary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = notif.message, fontSize = 12.sp, color = OdysafeTextPrimary)
                }
            }
        }
    }
}

@Composable
fun HelpAndSupportScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("help_center_screen")
    ) {
        Text(
            text = "Help, FAQs & Safety Ethics",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = OdysafeNavy
            )
        )
        Text(
            text = "Transparent answers about how Odysafe works and our safety standards.",
            style = MaterialTheme.typography.bodySmall.copy(color = OdysafeTextSecondary),
            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
        )

        FaqItem(
            q = "Where does safety advisory data come from?",
            a = "All weather advisories are sourced from the India Meteorological Department (IMD) and regional State Disaster Management Authorities (SDMA). Crowd and transit notices are verified by local traffic police bureaus."
        )

        FaqItem(
            q = "What capabilities work when offline?",
            a = "Saved destinations, pre-loaded emergency medical ID, saved emergency contacts, and the emergency phrasebook are available completely offline. Standard national emergency numbers 112 and 108 connect directly via cellular voice."
        )

        FaqItem(
            q = "Does Odysafe track my location continuously?",
            a = "No. Odysafe never engages background location tracking unless you explicitly turn on a Live Location Sharing session with a selected duration. You can stop sharing at any second."
        )

        FaqItem(
            q = "What happens when I trigger SOS in demonstration mode?",
            a = "The app demonstrates packaging your coordinates and notifying your chosen contacts. As stated in our prototype transparency guidelines, demonstration mode does not dispatch live government emergency personnel."
        )
    }
}

@Composable
private fun FaqItem(q: String, a: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = q, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = OdysafeNavy)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = a, fontSize = 12.sp, color = OdysafeTextSecondary, lineHeight = 18.sp)
        }
    }
}
