package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.data.model.EmergencyContact
import com.example.ui.components.DemonstrationDataBanner
import com.example.ui.theme.*

@Composable
fun LocationSharingScreen(
    isSharingActive: Boolean,
    sharingRemainingMins: Int,
    contacts: List<EmergencyContact>,
    onToggleSharing: (Boolean, Int) -> Unit,
    onManageContactsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDurationHours by remember { mutableStateOf(4) }
    val durations = listOf(1, 4, 8, 24)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("location_sharing_screen")
    ) {
        Text(
            text = "Private Location Sharing",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = OdysafeNavy
            )
        )
        Text(
            text = "Share your journey with trusted contacts. Explicit consent, zero background tracking when turned off.",
            style = MaterialTheme.typography.bodySmall.copy(color = OdysafeTextSecondary),
            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
        )

        DemonstrationDataBanner()

        Spacer(modifier = Modifier.height(14.dp))

        // Active Status Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSharingActive) OdysafeMint else Color.White
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSharingActive) OdysafeTeal else OdysafeBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSharingActive) OdysafeTeal else OdysafeBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShareLocation,
                                contentDescription = null,
                                tint = if (isSharingActive) Color.White else OdysafeTextSecondary
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isSharingActive) "Location Sharing Active" else "Location Sharing Disabled",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = OdysafeNavy
                            )
                            Text(
                                text = if (isSharingActive) "~$sharingRemainingMins minutes remaining" else "Your location is completely private",
                                fontSize = 12.sp,
                                color = OdysafeTextSecondary
                            )
                        }
                    }

                    Switch(
                        checked = isSharingActive,
                        onCheckedChange = { enabled ->
                            onToggleSharing(enabled, selectedDurationHours)
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = OdysafeTeal),
                        modifier = Modifier.testTag("toggle_location_sharing_switch")
                    )
                }

                if (isSharingActive) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { onToggleSharing(false, selectedDurationHours) },
                        colors = ButtonDefaults.buttonColors(containerColor = OdysafeDanger),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("stop_sharing_button")
                    ) {
                        Text("Stop Sharing Immediately", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Sharing Duration Selection (When configuring)
        Text(
            text = "Select Sharing Duration",
            fontWeight = FontWeight.Bold,
            color = OdysafeNavy,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(durations) { hours ->
                val isSelected = selectedDurationHours == hours
                Surface(
                    color = if (isSelected) OdysafeNavy else Color.White,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) OdysafeNavy else OdysafeBorder),
                    modifier = Modifier
                        .clickable { selectedDurationHours = hours }
                        .testTag("duration_${hours}h")
                ) {
                    Text(
                        text = if (hours == 1) "1 Hour" else "$hours Hours",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else OdysafeNavy,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Trusted Contacts List
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Trusted Contacts to Receive Updates",
                fontWeight = FontWeight.Bold,
                color = OdysafeNavy,
                fontSize = 13.sp
            )
            TextButton(onClick = onManageContactsClick) {
                Text("Manage", color = OdysafeTeal, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        contacts.forEach { contact ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = OdysafeTeal,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
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
                    Checkbox(
                        checked = contact.receivesLocationUpdates,
                        onCheckedChange = { /* toggle recipient */ },
                        colors = CheckboxDefaults.colors(checkedColor = OdysafeTeal)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Transparency Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Information Shared in Session:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = OdysafeNavy
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "• Current GPS coordinates (e.g. Madurai, TN: 9.9195° N, 78.1193° E)\n• Battery percentage and network status\n• Current destination and route progress\n• Timestamp of last ping",
                    fontSize = 11.sp,
                    color = OdysafeTextSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
