package com.mns.ghassedak

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.budiyev.android.codescanner.*
import com.google.zxing.BarcodeFormat

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: com.mns.ghassedak.databinding.ActivityScanBinding
    private lateinit var codeScanner: CodeScanner
    private var ip = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan)
        codeScanner = CodeScanner(this, binding.scannerView)

        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = listOf(BarcodeFormat.QR_CODE) // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            ip = it.text
            finish()
        }
        codeScanner.errorCallback = ErrorCallback {
            // or ErrorCallback.SUPPRESS

        }

        codeScanner.startPreview()

    }


    override fun finish() {
        val intent = Intent()
        intent.putExtra("ip", ip)
        setResult(0, intent)
        super.finish()
    }
}
