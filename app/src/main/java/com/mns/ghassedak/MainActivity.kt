package com.mns.ghassedak

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.mns.ghassedak.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var connector: ESP8266Connector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        connector = ESP8266Connector(this, "192.168.1.100", "80")

        binding.sound.setOnClickListener {
            connector.setSound()
        }

        binding.scan.setOnClickListener {
            val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 101)
            } else{
                val intent = Intent(this, ScanActivity::class.java)
                startActivityForResult(intent, 0)
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101){
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "اسکن انجام نشد.", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(this, ScanActivity::class.java)
                startActivityForResult(intent, 0)
            }
        }
    }

    override fun onDestroy() {
        connector.stopMoving()
        connector.clearRequestQueue()
        super.onDestroy()
    }

    override fun onPause() {
        connector.stopMoving()
        connector.clearRequestQueue()
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            connector = data.getStringExtra("ip")?.let { ESP8266Connector(this, it, "80") }!!
            if (data.getStringExtra("ip") != ""){
                val snackBar = Snackbar.make(binding.logo, "وصل شدید!", Snackbar.LENGTH_SHORT)
                ViewCompat.setLayoutDirection(snackBar.view,ViewCompat.LAYOUT_DIRECTION_RTL)
                snackBar.show()
            } else {
                val snackBar = Snackbar.make(binding.logo, "اتصال انجام نشد.", Snackbar.LENGTH_SHORT)
                ViewCompat.setLayoutDirection(snackBar.view,ViewCompat.LAYOUT_DIRECTION_RTL)
                snackBar.show()
            }
        }

    }
}
