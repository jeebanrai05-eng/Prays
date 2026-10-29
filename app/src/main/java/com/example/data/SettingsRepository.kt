package com.example.data

import android.content.Context
import com.example.model.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepository(context: Context) {

  private val prefs = context.getSharedPreferences("tokdownloader_settings", Context.MODE_PRIVATE)

  private val _settings = MutableStateFlow(
    AppSettings(
      language = prefs.getString("lang", "ne") ?: "ne",
      downloadQuality = prefs.getString("quality", "1080p HD") ?: "1080p HD",
      storageLocation = prefs.getString("storage", "App Internal Storage") ?: "App Internal Storage",
      wifiOnly = prefs.getBoolean("wifi_only", false),
      autoRefreshTrending = prefs.getBoolean("auto_refresh", true),
      refreshIntervalMinutes = prefs.getInt("refresh_interval", 30)
    )
  )
  val settings: StateFlow<AppSettings> = _settings.asStateFlow()

  fun updateLanguage(lang: String) {
    prefs.edit().putString("lang", lang).apply()
    _settings.value = _settings.value.copy(language = lang)
  }

  fun updateQuality(quality: String) {
    prefs.edit().putString("quality", quality).apply()
    _settings.value = _settings.value.copy(downloadQuality = quality)
  }

  fun updateStorageLocation(storage: String) {
    prefs.edit().putString("storage", storage).apply()
    _settings.value = _settings.value.copy(storageLocation = storage)
  }

  fun updateWifiOnly(wifiOnly: Boolean) {
    prefs.edit().putBoolean("wifi_only", wifiOnly).apply()
    _settings.value = _settings.value.copy(wifiOnly = wifiOnly)
  }

  fun updateAutoRefresh(autoRefresh: Boolean) {
    prefs.edit().putBoolean("auto_refresh", autoRefresh).apply()
    _settings.value = _settings.value.copy(autoRefreshTrending = autoRefresh)
  }
}
