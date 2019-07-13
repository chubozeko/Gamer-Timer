package com.chandistudios.gamertimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        toolbar2.setDisplayHomeAsUpEnabled(true)
//        toolbar2.setLogo(R.drawable.ic_stop_black_24dp)
        toolbar2.title = "Settings"
    }
}
