package com.mns.ghassedak

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.erz.joysticklibrary.JoyStick

class MainActivity : AppCompatActivity(), JoyStick.JoyStickListener {

    private lateinit var binding: com.mns.ghassedak.databinding.ActivityMainBinding
    private lateinit var joyStick: JoyStick
    private val ip : String = "192.168.43.60"
    private val port : String = "80"
    private lateinit var connector: ESP8266Connector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        joyStick = binding.joystick
        joyStick.setListener(this)
        connector = ESP8266Connector(this, ip, port)

    }

    override fun onTap() {
        //Nothing yet
    }

    override fun onDoubleTap() {
        //Nothing yet
    }

    override fun onMove(joyStick: JoyStick?, angle: Double, power: Double, direction: Int) {
        when(direction){
            0 -> connector.turnLeft()
            2 -> connector.moveForward()
            4 -> connector.turnRight()
            6 -> connector.moveBackward()
            else -> connector.stopMoving()
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
