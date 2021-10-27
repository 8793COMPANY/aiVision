package com.corporation8793.aivision

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.naver.maps.map.LocationTrackingMode
import java.util.jar.Manifest
import kotlin.concurrent.timer

class SplashActivity : AppCompatActivity() {
    var visible_check : Boolean = false;

    val PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.INTERNET
    )
    val LOCATION_PERMISSION_REQUEST_CODE = PERMISSIONS.size
    var p_list = mutableListOf<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        visible_check = Application.prefs.getBoolean("invisible",false);
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults.size > 0) {
            for (p in permissions) {
                p_list.add(ContextCompat.checkSelfPermission(this, p) == PackageManager.PERMISSION_GRANTED)
            }

            if(p_list.contains(false)) {
                p_list = mutableListOf()
                ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (visible_check) {
                        var intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        var intent = Intent(this, IntroActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }, 3000)
            }
        }
    }
}