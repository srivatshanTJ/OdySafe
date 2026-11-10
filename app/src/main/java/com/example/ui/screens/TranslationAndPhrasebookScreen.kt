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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PhraseItem
import com.example.ui.components.DemonstrationDataBanner
import com.example.ui.theme.*

@Composable
fun TranslationScreen(
    phrases: List<PhraseItem>,
    inputText: String,
    outputText: String,
    isTranslating: Boolean,
    sourceLang: String,
    targetLang: String,
    onInputTextChange: (String) -> Unit,
    onTranslateClick: () -> Unit,
    onLanguagesChange: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Medical", "Police", "Lost", "Direction", "Allergy")

    val filteredPhrases = remember(phrases, selectedCategory) {
        if (selectedCategory == "All") phrases
        else phrases.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .testTag("translation_screen"),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                text = "Multilingual Travel Translator",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = OdysafeNavy
                )
            )
            Text(
                text = "Communicate with locals, medical staff, and transport operators across India.",
                style = MaterialTheme.typography.bodySmall.copy(color = OdysafeTextSecondary),
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )
        }

        item {
            DemonstrationDataBanner()
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Language Switcher Row
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = sourceLang, fontWeight = FontWeight.Bold, color = OdysafeNavy)
                    IconButton(
                        onClick = { onLanguagesChange(targetLang, sourceLang) }
                    ) {
                        Icon(Icons.Default.SwapHoriz, contentDescription = "Swap Languages", tint = OdysafeTeal)
                    }
                    Text(text = targetLang, fontWeight = FontWeight.Bold, color = OdysafeTeal)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Input & Translation Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = onInputTextChange,
                        placeholder = { Text("Enter phrase e.g. Where is the nearest hospital?") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .testTag("translate_input_field"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = OdysafeBg,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Machine translation tool",
                                fontSize = 10.sp,
                                color = OdysafeTextSecondary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }

                        Button(
                            onClick = onTranslateClick,
                            enabled = !isTranslating,
                            colors = ButtonDefaults.buttonColors(containerColor = OdysafeTeal),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                            modifier = Modifier.testTag("translate_submit_button")
                        ) {
                            if (isTranslating) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.Translate, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Translate", fontSize = 13.sp)
                            }
                        }
                    }

                    if (outputText.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Divider(color = OdysafeBorder)
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Translated Phrase ($targetLang):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OdysafeTextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = outputText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = OdysafeNavy,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Emergency Phrasebook Section
        item {
            Text(
                text = "Emergency Phrasebook",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = OdysafeNavy
                )
            )
            Text(
                text = "Pre-verified essential safety sentences ready for offline use in India.",
                style = MaterialTheme.typography.bodySmall.copy(color = OdysafeTextSecondary),
                modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
            )

            // Category Filter
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OdysafeNavy,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(filteredPhrases) { phrase ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .testTag("phrase_card_${phrase.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = OdysafeMint,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = phrase.category,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = OdysafeNavy,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Speak Pronunciation",
                            tint = OdysafeTeal,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = phrase.english,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = OdysafeNavy
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Tamil: ${phrase.tamil}",
                        fontSize = 13.sp,
                        color = OdysafeTeal,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "Hindi: ${phrase.hindi}",
                        fontSize = 12.sp,
                        color = OdysafeNavy.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Spoken: “${phrase.pronunciation}”",
                        fontSize = 11.sp,
                        color = OdysafeTextSecondary
                    )
                }
            }
        }
    }
}
