package com.example.bluetoothtutorial

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_ble.*

class BleActivity : AppCompatActivity() {

    var bluetoothGatt: BluetoothGatt? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble)

        val bluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner


        val gattCallback = object : BluetoothGattCallback(){

            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)
            }

        }



        val scanCallback = object : ScanCallback(){
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                super.onScanResult(callbackType, result)

                if(result?.device!!.name == "DEV_EM5") {
                    result?.device!!.connectGatt(this@BleActivity,false,gattCallback)


                }else
                {
                    Log.d("onScan result"," ${result?.device?.address} - ${result?.device?.name}")

                }


            }

            override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                super.onBatchScanResults(results)
            }

            override fun onScanFailed(errorCode: Int) {
                super.onScanFailed(errorCode)
                Log.e("Scan Error",errorCode.toString())
            }

        }





        btn_ble_scan.setOnClickListener {
            bluetoothLeScanner.startScan(scanCallback)

        }




/*
       fun scanLeDevice() {
            if (!mScanning) { // Stops scanning after a pre-defined scan period.
                handler.postDelayed({
                    mScanning = false
                    bluetoothLeScanner.stopScan(leScanCallback)
                }, SCAN_PERIOD)
                mScanning = true
                bluetoothLeScanner.startScan(leScanCallback)
            } else {
                mScanning = false
                bluetoothLeScanner.stopScan(leScanCallback)
            }
        }*/










        /*fun PackageManager.missingSystemFeature(name: String): Boolean = !hasSystemFeature(name)
        packageManager.takeIf { it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) }?.also {
            Toast.makeText(this,"Ble Not Supported", Toast.LENGTH_SHORT).show()
            finish()
        }*/
    }
}