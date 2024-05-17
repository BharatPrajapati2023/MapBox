package com.map.mapbox.BLE_Example

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.map.mapbox.R
import com.map.mapbox.databinding.ActivitySelectDeviceBinding

class SelectDeviceActivity : AppCompatActivity() {
    private val b by lazy {
        ActivitySelectDeviceBinding.inflate(layoutInflater)
    }
    private var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var pairDevice: Set<BluetoothDevice>
    private val REQUEST_ENABLE_BLUETOOTH = 1

    companion object {
        val EXTRA_ADDRESS: String = "Device_address"

    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(
                applicationContext,
                "this device doesn't support bluetooth",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (!bluetoothAdapter!!.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }
        b.selectDeviceBtn.setOnClickListener {
            pairedDeviceList()
        }
    }

    @SuppressLint("MissingPermission")
    private fun pairedDeviceList() {
        pairDevice = bluetoothAdapter!!.bondedDevices
        val list: ArrayList<BluetoothDevice> = ArrayList()
        if (!pairDevice.isEmpty()) {
            for (device: BluetoothDevice in pairDevice) {
                list.add(device)
                Log.i("TAG", "device :$device")
            }
        } else {
            Toast.makeText(
                applicationContext,
                "no paired bluetooth device found",
                Toast.LENGTH_SHORT
            ).show()
        }
        val adapter =
            ArrayAdapter(this@SelectDeviceActivity, android.R.layout.simple_list_item_1, list)
        b.selectDeviceList.adapter=adapter
        b.selectDeviceList.onItemClickListener=AdapterView.OnItemClickListener { parent, view, position, id ->
            val device:BluetoothDevice=list[position]
            val address:String=device.address
            val intent=Intent(this,ControlActivity::class.java)
            intent.putExtra(ControlActivity.EXTRA_ADDRESS,address)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (bluetoothAdapter!!.isEnabled) {
                    Toast.makeText(
                        applicationContext,
                        "Bluetooth has been enabled",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Bluetooth has been disabled",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(
                    applicationContext,
                    "Bluetooth enable has been canceled",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}