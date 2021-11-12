package com.vanchi.permissionsdemo

import android.Manifest
import android.app.AlertDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.vanchi.permissionsdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private val cameraResultLauncher : ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
            if(isGranted){
                Toast.makeText(this,"Permission Granted", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
            }
        }

    private val cameraAndLocationResultLauncher : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ results ->
            results.entries.forEach{entry ->
                val permissionName = entry.key
                val isGranted = entry.value
                if(isGranted){
                    when(permissionName){
                        Manifest.permission.CAMERA -> {
                            Toast.makeText(this,"Permission Granted for Camera!", Toast.LENGTH_SHORT).show()
                        }
                        Manifest.permission.ACCESS_FINE_LOCATION -> {
                            Toast.makeText(this,"Permission Granted for Location!", Toast.LENGTH_SHORT).show()
                        }
                        Manifest.permission.ACCESS_COARSE_LOCATION -> {
                            Toast.makeText(this,"Permission Granted for Coarse Location!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else {
                    when(permissionName){
                        Manifest.permission.CAMERA -> {
                            Toast.makeText(this,"Permission Denied for Camera!", Toast.LENGTH_SHORT).show()
                        }
                        Manifest.permission.ACCESS_FINE_LOCATION -> {
                            Toast.makeText(this,"Permission Denied for Location!", Toast.LENGTH_SHORT).show()
                        }
                        Manifest.permission.ACCESS_COARSE_LOCATION -> {
                            Toast.makeText(this,"Permission Denier for Coarse Location!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.btnCameraPermission?.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                showReasonDialog("Demo requires camera access",
                "Camera cannot be used because access was denied!")
            }else{
                cameraAndLocationResultLauncher.launch(arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION))
            }
        }
    }

    private fun showReasonDialog(title: String, message: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel"){dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}