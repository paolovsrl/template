package com.omsi.composetemplate.data

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import kotlin.experimental.and


fun checkPermissions(activity:ComponentActivity){

    if(!Settings.canDrawOverlays(activity)){
        Toast.makeText(activity, "Overlay Permission not given.", Toast.LENGTH_SHORT).show();

        val launcher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            /*
            The returned result is not consistent with the actual value.
             */
        }

        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${activity.packageName}")
        )
        // Request the permission
        launcher.launch(intent)
    }
}

fun bytesToInt(byte0:Byte, byte1:Byte):Int{
    //Integer in PLC is Short in Java:
    //positive: 0x0000..0x7FFF
    //negative: 0x8000..0xFFFF
    val short:Short = (byte1.toUInt().shl(8) or (byte0.toUInt() and 255u)).toShort()
    return (short.toInt())
}


fun getBitValue(byte0:Byte, position:Int):Int{
    val n = position.apply { position.coerceAtLeast(0).coerceAtMost(7) }
    val short:Short = ((byte0.toUInt().shr(n) and 1u)).toShort()
    return (short.toInt())
}