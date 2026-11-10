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
import com.example.data.model.AlertCategory
import com.example.data.model.SafetyAlert
import com.example.ui.components.DemonstrationDataBanner
import com.example.ui.components.SeverityBadge
import com.example.ui.theme.*

@Composable
fun AlertsCenterScreen(
    alerts: List<SafetyAlert>,
    onAlertClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(AlertCategory.ALL) }

    val filteredAlerts = remember(alerts, selectedCategory) {
        if (selectedCategory == AlertCategory.ALL) alerts
        else alerts.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .testTag("alerts_center_screen"),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                text = "Safety & Weather Alerts Center",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = OdysafeNavy
                )
            )
            Text(
                text = "Verified advisories from India Meteorological Department (IMD) & State Authorities.",
                style = MaterialTheme.typography.bodySmall.copy(color = OdysafeTextSecondary),
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )
        }

        item {
            DemonstrationDataBanner()
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Category filter chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(AlertCategory.entries) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category.label, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OdysafeNavy,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        if (filteredAlerts.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = OdysafeSuccess, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No active alerts in this category", fontWeight = FontWeight.Bold, color = OdysafeNavy)
                        Text("All routes and destinations in this view have normal conditions.", fontSize = 12.sp, color = OdysafeTextSecondary)
                    }
                }
            }
        } else {
            items(filteredAlerts) { alert ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onAlertClick(alert.id) }
                        .testTag("alert_card_${alert.id}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SeverityBadge(severity = alert.severity)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = alert.timestamp, fontSize = 10.sp, color = OdysafeTextSecondary)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = alert.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = OdysafeNavy
                        )

                        Text(
                            text = "Area: ${alert.affectedArea}",
                            fontSize = 11.sp,
                            color = OdysafeTeal,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = alert.description,
                            fontSize = 12.sp,
                            color = OdysafeTextPrimary,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Source: ${alert.source}",
                            fontSize = 10.sp,
                            color = OdysafeTextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AreaSafetyDetailScreen(
    alert: SafetyAlert,
    onViewSafeAlternative: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("area_safety_detail_screen")
    ) {
        DemonstrationDataBanner()

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SeverityBadge(severity = alert.severity)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = alert.timestamp, fontSize = 11.sp, color = OdysafeTextSecondary)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = alert.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = OdysafeNavy
                )

                Text(
                    text = "Affected Area: ${alert.affectedArea}",
                    fontSize = 12.sp,
                    color = OdysafeTeal,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                )

                Text(
                    text = alert.description,
                    fontSize = 13.sp,
                    color = OdysafeTextPrimary,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = OdysafeBg,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = OdysafeTeal, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Official Data Source: ${alert.source}",
                            fontSize = 11.sp,
                            color = OdysafeNavy,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Recommended Precautions
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Recommended Precautions",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = OdysafeNavy
                )
                Spacer(modifier = Modifier.height(8.dp))

                alert.recommendedActions.forEach { action ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = OdysafeTeal,
                            modifier = Modifier.size(16.dp).padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = action,
                            fontSize = 12.sp,
                            color = OdysafeNavy,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        if (alert.safeAlternative != null) {
            Spacer(modifier = Modifier.height(14.dp))

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = OdysafeMint.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AltRoute, contentDescription = null, tint = OdysafeTeal)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Verified Safe Alternative Route",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = OdysafeNavy
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = alert.safeAlternative,
                        fontSize = 12.sp,
                        color = OdysafeNavy,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
