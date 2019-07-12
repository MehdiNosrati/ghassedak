package com.mns.ghassedak

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.bitvale.switcher.SwitcherX
import com.erz.joysticklibrary.JoyStick

class MainActivity : AppCompatActivity(), JoyStick.JoyStickListener {

    private lateinit var binding: com.mns.ghassedak.databinding.ActivityMainBinding
    private lateinit var joyStick: JoyStick
    private lateinit var switcherX: SwitcherX
    private lateinit var connector: ESP8266Connector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        connector = ESP8266Connector(this, "192.168.1.100", "80")
        joyStick = binding.joystick
        joyStick.type = JoyStick.TYPE_4_AXIS
        switcherX = binding.switcher
        joyStick.setListener(this)
        switcherX.setOnCheckedChangeListener { connector.sendVip() }

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
        if (requestCode == 101){
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "اسکن انجام نشد.", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(this, ScanActivity::class.java)
                startActivityForResult(intent, 0)
            }
        }
    }

    override fun onTap() {
        //Nothing yet
    }

    override fun onDoubleTap() {
        //Nothing yet
    }

    private var lastDir: Int = 10
    override fun onMove(joyStick: JoyStick?, angle: Double, power: Double, direction: Int) {
        if (lastDir != direction) {
            lastDir = direction
            when (direction) {
                0 -> connector.turnLeft()
                //1 -> connector.moveNW()
                2 -> connector.moveForward()
                //3 -> connector.moveNE()
                4 -> connector.turnRight()
                //5 -> connector.moveSE()
                6 -> connector.moveBackward()
                //7 -> connector.moveSW()
            }
            if (power == 0.0)
                connector.stopMoving()

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
        if (data != null) {
            connector = ESP8266Connector(this, data.getStringExtra("ip"), "80")
            val snackBar = Snackbar.make(binding.logo, "وصل شدید!", Snackbar.LENGTH_SHORT)
            ViewCompat.setLayoutDirection(snackBar.view,ViewCompat.LAYOUT_DIRECTION_RTL)
            snackBar.show()
        }

    }
}
