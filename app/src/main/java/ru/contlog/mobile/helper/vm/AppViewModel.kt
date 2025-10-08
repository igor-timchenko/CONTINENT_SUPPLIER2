package ru.contlog.mobile.helper.vm

import androidx.lifecycle.ViewModel
import ru.contlog.mobile.helper.repo.AppPreferencesRepository

class AppViewModel(private val appPreferencesRepository: AppPreferencesRepository) : ViewModel() {
    var login = appPreferencesRepository.getString("app_login", "")
        set(value) {
            field = value
            appPreferencesRepository.saveString("app_login", value)
        }
}