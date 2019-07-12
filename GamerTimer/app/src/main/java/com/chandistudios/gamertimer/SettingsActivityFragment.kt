package com.chandistudios.gamertimer

import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import androidx.preference.PreferenceFragment
import androidx.preference.PreferenceFragmentCompat

class SettingsActivityFragment : PreferenceFragmentCompat {

    constructor()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}