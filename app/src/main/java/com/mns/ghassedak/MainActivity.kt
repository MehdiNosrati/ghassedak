package com.mns.ghassedak

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import com.bitvale.switcher.SwitcherX
import com.erz.joysticklibrary.JoyStick

class MainActivity : AppCompatActivity(), JoyStick.JoyStickListener {

    private lateinit var binding: com.mns.ghassedak.databinding.ActivityMainBinding
    private lateinit var joyStick: JoyStick
    private lateinit var switcherX: SwitcherX
    private lateinit var ipText : EditText
    private lateinit var connect : TextView
    private val port: String = "80"
    private lateinit var connector: ESP8266Connector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        connector = ESP8266Connector(this, "192.168.1.100", "80")
        joyStick = binding.joystick
        switcherX = binding.switcher
        ipText = binding.ipText
        connect = binding.connect
        joyStick.setListener(this)
        switcherX.setOnCheckedChangeListener { connector.sendVip() }
        connect.setOnClickListener {
            connector = ESP8266Connector(applicationContext, ipText.text.toString(), port)
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
                1 -> connector.moveNW()
                2 -> connector.moveForward()
                3 -> connector.moveNE()
                4 -> connector.turnRight()
                5 -> connector.moveSE()
                6 -> connector.moveBackward()
                7 -> connector.moveSW()
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
}
