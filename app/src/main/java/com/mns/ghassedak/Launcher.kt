package com.mns.ghassedak

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class Launcher : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}