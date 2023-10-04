package com.omsi.composetemplate.data.repository

import android.content.Context
import android.os.Build
import com.omsi.cpdevicelib.Tools
import com.omsi.cpdevicelib.can.BAMPacket
import com.omsi.cpdevicelib.can.CANManager
import com.omsi.cpdevicelib.can.CANPacket
import com.omsi.cpdevicelib.can.CanListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CanRepository @Inject constructor(
){
    private val TAG = "CanRepository"
    private val canManager: CANManager = CANManager(1)


    fun initRepo(activity: Context, onReceived: (packet: CANPacket)->Unit):Boolean{
        if(Tools.isDeviceSupported(Build.MODEL)) {
            canManager.initializeCanInterface(activity)
            val canListener = object: CanListener {
                override fun OnStatusChange(p0: Boolean?) {
                    //TODO("Not yet implemented")
                }

                override fun OnReceivedCanMsg(packet: CANPacket) {
                    onReceived(packet)
                }

                override fun OnReceivedBamData(p0: BAMPacket?) {
                   // TODO("Not yet implemented")
                }
            }
            canManager.setCanListener(canListener)
            return true
        }
        else
            return false
    }

    fun invalidate(){
        canManager.invalidate()
    }

    fun sendPacket(packet: CANPacket){
        canManager.send(packet)
    }


    fun getHexString(data: ByteArray?): String? {
        if (null == data) {
            return ""
        }
        val sb = StringBuilder(data.size * 3)
        val HEX = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        )
        for (i in data.indices) {
            val value: Int = (data[i].toUInt()and 255u).toInt()
            sb.append(HEX[value / 16]).append(HEX[value % 16]).append(' ')
        }
        return sb.toString()
    }

}