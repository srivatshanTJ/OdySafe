package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EmergencyContact
import com.example.ui.theme.*

@Composable
fun AuthLoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignup: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    var emailOrPhone by remember { mutableStateOf("ananya.travels@example.com") }
    var password by remember { mutableStateOf("SecureOdysafe123") }
    var showPassword by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Badge
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(OdysafeNavy),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = "Odysafe Logo",
                tint = OdysafeMint,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome to Odysafe",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = OdysafeNavy
            )
        )

        Text(
            text = "Sign in to access your travel plans & safety network",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = OdysafeTextSecondary,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        // Form Fields
        OutlinedTextField(
            value = emailOrPhone,
            onValueChange = { emailOrPhone = it },
            label = { Text("Email or Phone Number") },
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null, tint = OdysafeTeal) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_email_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = OdysafeTeal) },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle password"
                    )
                }
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_password_input")
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onNavigateToForgotPassword) {
                Text("Forgot Password?", color = OdysafeTeal, fontSize = 12.sp)
            }
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = OdysafeDanger,
                fontSize = 12.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (emailOrPhone.isBlank() || password.isBlank()) {
                    errorMessage = "Please enter valid login credentials"
                } else {
                    onLoginSuccess()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = OdysafeNavy),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("login_submit_button")
        ) {
            Text("Sign In", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Don't have an account?", fontSize = 13.sp, color = OdysafeTextSecondary)
            TextButton(onClick = onNavigateToSignup) {
                Text("Create Account", color = OdysafeTeal, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun AuthSignupScreen(
    onSignupSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var agreedToTerms by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Create Your Account",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = OdysafeNavy
            )
        )

        Text(
            text = "Join travelers using proactive safety tools across India",
            style = MaterialTheme.typography.bodyMedium.copy(color = OdysafeTextSecondary),
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            leadingIcon = { Icon(Icons.Outlined.Badge, contentDescription = null, tint = OdysafeTeal) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("signup_name_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, tint = OdysafeTeal) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("signup_email_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number (+91)") },
            leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null, tint = OdysafeTeal) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("signup_phone_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Create Password") },
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = OdysafeTeal) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("signup_password_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = agreedToTerms,
                onCheckedChange = { agreedToTerms = it },
                colors = CheckboxDefaults.colors(checkedColor = OdysafeTeal)
            )
            Text(
                text = "I agree to Odysafe Terms & Privacy Guidelines",
                fontSize = 12.sp,
                color = OdysafeNavy
            )
        }

        if (error != null) {
            Text(text = error!!, color = OdysafeDanger, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (fullName.isBlank()) {
                    error = "Please enter your name"
                } else {
                    onSignupSuccess()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = OdysafeTeal),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("signup_submit_button")
        ) {
            Text("Continue to Verification", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Already registered?", fontSize = 13.sp, color = OdysafeTextSecondary)
            TextButton(onClick = onNavigateToLogin) {
                Text("Sign In", color = OdysafeNavy, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun OnboardingFlowScreen(
    currentStep: Int,
    onNextStep: () -> Unit,
    onSkip: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OdysafeBg)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Step indicator dots & progress
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Step $currentStep of 4",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = OdysafeTeal
                )
            )

            if (currentStep < 4) {
                TextButton(onClick = onSkip) {
                    Text("Skip", color = OdysafeTextSecondary, fontSize = 13.sp)
                }
            }
        }

        LinearProgressIndicator(
            progress = { currentStep / 4f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = OdysafeTeal,
            trackColor = OdysafeBorder
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(modifier = Modifier.weight(1f)) {
            when (currentStep) {
                1 -> OnboardingStepWelcome()
                2 -> OnboardingStepPreferences()
                3 -> OnboardingStepContacts()
                4 -> OnboardingStepPermissions()
            }
        }

        // Action Button
        Button(
            onClick = {
                if (currentStep < 4) onNextStep() else onComplete()
            },
            colors = ButtonDefaults.buttonColors(containerColor = OdysafeNavy),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("onboarding_continue_button")
        ) {
            Text(
                text = if (currentStep < 4) "Continue" else "Get Started",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun OnboardingStepWelcome() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(OdysafeMint),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Explore, contentDescription = null, tint = OdysafeTeal, modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Welcome to Odysafe",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = OdysafeNavy
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Your intelligent companion designed to empower confident journeys across Indian cities, mountain passes, and sacred trails.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = OdysafeTextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Surface(
            color = Color.White,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "“Explore freely. Travel confidently. Stay connected.”",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = OdysafeTeal,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun OnboardingStepPreferences() {
    val languages = listOf("English", "Tamil (தமிழ்)", "Hindi (हिन्दी)", "Telugu (తెలుగు)", "Malayalam (മലയാളം)")
    var selectedLang by remember { mutableStateOf("English") }

    Column {
        Text(
            text = "Select Language & Travel Style",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = OdysafeNavy
            )
        )
        Text(
            text = "We will adapt alerts and travel translations accordingly.",
            style = MaterialTheme.typography.bodySmall.copy(color = OdysafeTextSecondary),
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        languages.forEach { lang ->
            Surface(
                color = if (selectedLang == lang) OdysafeMint else Color.White,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (selectedLang == lang) OdysafeTeal else OdysafeBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { selectedLang = lang }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedLang == lang,
                        onClick = { selectedLang = lang },
                        colors = RadioButtonDefaults.colors(selectedColor = OdysafeTeal)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = lang, fontWeight = FontWeight.Medium, color = OdysafeNavy)
                }
            }
        }
    }
}

@Composable
private fun OnboardingStepContacts() {
    Column {
        Text(
            text = "Setup Trusted Contacts",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = OdysafeNavy
            )
        )
        Text(
            text = "Trusted contacts can receive live location updates and emergency SOS messages with your explicit permission.",
            style = MaterialTheme.typography.bodySmall.copy(color = OdysafeTextSecondary),
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ContactPhone, contentDescription = null, tint = OdysafeTeal)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pre-configured Contact", fontWeight = FontWeight.Bold, color = OdysafeNavy)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Rahul Sharma (Brother)", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text("+91 98401 23456 • Primary Emergency Contact", fontSize = 12.sp, color = OdysafeTextSecondary)
            }
        }
    }
}

@Composable
private fun OnboardingStepPermissions() {
    Column {
        Text(
            text = "Permissions & Privacy",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = OdysafeNavy
            )
        )
        Text(
            text = "Odysafe operates on strict privacy-first principles. We only request permissions when they provide genuine safety value.",
            style = MaterialTheme.typography.bodySmall.copy(color = OdysafeTextSecondary),
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        PermissionExplainerCard(
            icon = Icons.Default.LocationOn,
            title = "Approximate & Precise Location",
            desc = "Used to show nearby hospitals, police stations, and share coordinates only when you explicitly start sharing."
        )

        Spacer(modifier = Modifier.height(10.dp))

        PermissionExplainerCard(
            icon = Icons.Default.Notifications,
            title = "Severe Weather & Route Alerts",
            desc = "Used to notify you of critical IMD advisories like heavy rainfall, landslides, or festive road closures."
        )
    }
}

@Composable
private fun PermissionExplainerCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, OdysafeBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
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
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = OdysafeNavy)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = desc, fontSize = 12.sp, color = OdysafeTextSecondary, lineHeight = 16.sp)
            }
        }
    }
}
