package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EmergencyContact
import com.example.data.model.UserProfile
import com.example.data.repository.OdysafeRepository
import com.example.ui.components.DemonstrationDataBanner
import com.example.ui.state.SosStatus
import com.example.ui.theme.*

@Composable
fun EmergencySosScreen(
    sosStatus: SosStatus,
    lastSosTimestamp: String?,
    contacts: List<EmergencyContact>,
    userProfile: UserProfile,
    onTriggerSos: () -> Unit,
    onCancelSos: () -> Unit,
    onDialHelpline: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSosConfirmationDialog by remember { mutableStateOf(false) }
    var locationCopiedMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("emergency_sos_screen")
    ) {
        // High Contrast Top Emergency Banner
        Surface(
            color = OdysafeDanger,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "EMERGENCY ASSISTANCE",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "If you are in immediate life-threatening danger, dial 112 directly below.",
                    color = Color.White.copy(alpha = 0.95f),
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        DemonstrationDataBanner()
        Spacer(modifier = Modifier.height(14.dp))

        // SOS Activation / Status Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            border = androidx.compose.foundation.BorderStroke(
                2.dp,
                if (sosStatus == SosStatus.CONFIRMED) OdysafeSuccess else OdysafeDanger.copy(alpha = 0.2f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (sosStatus) {
                    SosStatus.IDLE -> {
                        Text(
                            text = "Broadcast Emergency SOS",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = OdysafeNavy
                        )
                        Text(
                            text = "Alerts your trusted contacts with your GPS coordinates & medical info.",
                            fontSize = 12.sp,
                            color = OdysafeTextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                        )

                        // Big Red SOS Button
                        Button(
                            onClick = { showSosConfirmationDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = OdysafeDanger),
                            shape = CircleShape,
                            modifier = Modifier
                                .size(110.dp)
                                .testTag("sos_trigger_button")
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Sos, contentDescription = null, modifier = Modifier.size(36.dp), tint = Color.White)
                                Text("TAP SOS", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                            }
                        }
                    }

                    SosStatus.DISPATCHING -> {
                        CircularProgressIndicator(color = OdysafeDanger, strokeWidth = 3.dp, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Dispatching Simulated SOS Alert...",
                            fontWeight = FontWeight.Bold,
                            color = OdysafeDanger,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Packaging coordinates and contacting emergency network",
                            fontSize = 12.sp,
                            color = OdysafeTextSecondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    SosStatus.CONFIRMED -> {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(OdysafeSuccessContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = OdysafeSuccess, modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "SOS Alert Dispatched (Simulated)",
                            fontWeight = FontWeight.Bold,
                            color = OdysafeSuccess,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Delivered to 2 trusted contacts at ${lastSosTimestamp ?: "Just now"}",
                            fontSize = 12.sp,
                            color = OdysafeNavy,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 2.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedButton(
                            onClick = onCancelSos,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = OdysafeNavy),
                            modifier = Modifier.testTag("sos_cancel_button")
                        ) {
                            Text("Reset SOS State")
                        }
                    }

                    else -> {}
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Current Location & Coordinates Readout
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MyLocation, contentDescription = null, tint = OdysafeTeal, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Current Verified Location", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = OdysafeNavy)
                    }
                    Text("GPS Accuracy ~12m", fontSize = 10.sp, color = OdysafeTextSecondary)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Near Meenakshi Amman Temple East Gate, Madurai, Tamil Nadu, 625001", fontSize = 12.sp, color = OdysafeNavy)
                Text("Coordinates: 9.9195° N, 78.1193° E", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OdysafeTeal)

                if (locationCopiedMessage != null) {
                    Text(text = locationCopiedMessage!!, fontSize = 11.sp, color = OdysafeSuccess, modifier = Modifier.padding(top = 4.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { locationCopiedMessage = "Coordinates copied to clipboard: 9.9195, 78.1193" },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("copy_coordinates_button")
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy Coordinates for Dispatcher", fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Official Indian Emergency Helplines (Direct Phone Dialer Intent)
        Text(
            text = "Official National & State Helplines",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = OdysafeNavy
        )
        Text(
            text = "Tap to open dialer with pre-filled official numbers.",
            fontSize = 11.sp,
            color = OdysafeTextSecondary,
            modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
        )

        OdysafeRepository.emergencyHelplines.forEach { helpline ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onDialHelpline(helpline.number) }
                    .testTag("helpline_${helpline.number}")
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (helpline.number in listOf("112", "108")) OdysafeDangerContainer else OdysafeMint),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = helpline.number,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = if (helpline.number in listOf("112", "108")) OdysafeDanger else OdysafeTeal
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = helpline.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = OdysafeNavy)
                        Text(text = helpline.description, fontSize = 11.sp, color = OdysafeTextSecondary)
                    }

                    Icon(Icons.Default.Call, contentDescription = "Call", tint = OdysafeNavy)
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Emergency Medical Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MedicalInformation, contentDescription = null, tint = OdysafeTeal)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Emergency Medical ID", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = OdysafeNavy)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Blood Group: ${userProfile.bloodGroup}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = OdysafeNavy)
                    Text("Allergies: Penicillin", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = OdysafeDanger)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("Notes: ${userProfile.medicalNotes}", fontSize = 11.sp, color = OdysafeTextSecondary)
            }
        }
    }

    // Confirmation Dialog before triggering SOS
    if (showSosConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showSosConfirmationDialog = false },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = OdysafeDanger) },
            title = { Text("Confirm Emergency SOS Alert", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "This will simulate sending your current coordinates (Madurai, TN) and medical card to your 2 trusted emergency contacts (Rahul Sharma, Priya)."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSosConfirmationDialog = false
                        onTriggerSos()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OdysafeDanger)
                ) {
                    Text("Yes, Send Alert")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSosConfirmationDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
