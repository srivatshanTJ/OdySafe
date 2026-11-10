package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Odysafe", appName)
  }

  @Test
  fun `viewModel initializes with places and suggestions`() {
    val viewModel = com.example.ui.state.OdysafeViewModel()
    val state = viewModel.uiState.value
    org.junit.Assert.assertTrue(state.liveGroundedPlaces.isNotEmpty())
    org.junit.Assert.assertTrue(state.locationSuggestions.isNotEmpty())
    assertEquals("Madurai, Tamil Nadu", state.selectedCity)
  }
}
