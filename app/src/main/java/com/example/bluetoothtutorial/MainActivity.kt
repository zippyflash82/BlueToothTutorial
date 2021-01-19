package com.example.bluetoothtutorial

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    var bluetoothAdapter: BluetoothAdapter?= null
    var pairedDevices: Set<BluetoothDevice>? = null
    private val REQUEST_ENABLE_BT = 2
    var xlist = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Ble coding
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter_ble = bluetoothManager.adapter

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        pairedDevices = bluetoothAdapter?.bondedDevices







        //Checking if The Bluetooth is Available or Not
        btn_check_bt_availability.setOnClickListener {
            if(bluetoothAdapter==null)
            {
                Toast.makeText(this,"Your Device Does't Support Bluetooth",Toast.LENGTH_LONG).show()
            } else
            {
                Toast.makeText(this,"Your Device Supports Bluetooth",Toast.LENGTH_LONG).show()
            }
        }

        //Turing On BT
        btn_enable_bt.setOnClickListener {
            if (bluetoothAdapter?.isEnabled==false){
              val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent,REQUEST_ENABLE_BT)
            }
            else
            {
                Toast.makeText(this,"BT is Already On",Toast.LENGTH_LONG).show()
            }
        }
        //Turning Off BT
        btn_disable_bt.setOnClickListener {
            if (bluetoothAdapter?.isEnabled == true){
                bluetoothAdapter?.disable()
                Toast.makeText(this,"BlueTooth Disabled",Toast.LENGTH_LONG).show()
            } else
            {
                Toast.makeText(this,"BT is Already Off",Toast.LENGTH_LONG).show()
            }
        }

        btn_get_paired_devices.setOnClickListener {
                pairedDevices?.forEach { device->
                    Log.i("Name", device.name)
                    Log.i("Address", device.address)
                    Log.i("Alias", device.alias.toString())
                    Log.i("Searched devices",xlist.toString())
            }
        }
        btn_scan_devices.setOnClickListener {
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(receiver,filter)
            bluetoothAdapter?.startDiscovery()
        }

        btn_make_discoverable.setOnClickListener {
            val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300)
            }
            startActivity(discoverableIntent)
        }

        btn_ble_activity.setOnClickListener {
            val intent = Intent(this,BleActivity::class.java)
            startActivity(intent)
        }


    }




    //Checking if bluetooth enabled or not
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT)
        {
            if(resultCode==Activity.RESULT_OK){
                Toast.makeText(this,"BlueTooth Enabled",Toast.LENGTH_LONG).show()
            }
            else
            {
                Toast.makeText(this,"SomeThing Went Wrong",Toast.LENGTH_LONG).show()
            }
        }
    }


    val receiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val action: String? = intent!!.action

            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device!!.address
                    xlist.add(deviceName)

                }
            }
        }

    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }


}