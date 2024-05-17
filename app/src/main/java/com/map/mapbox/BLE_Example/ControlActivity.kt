package com.map.mapbox.BLE_Example

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface
import com.map.mapbox.R
import com.map.mapbox.databinding.ActivityControlBinding
import java.util.UUID

class ControlActivity : AppCompatActivity() {
    private val b by lazy {
        ActivityControlBinding.inflate(layoutInflater)
    }

    /*usb variable*/
    private lateinit var usbManager: UsbManager
    private var device: UsbDevice? = null
    var serial: UsbSerialDevice? = null
    var connection: UsbDeviceConnection? = null
    val ACTION_USB_PERMISSION = "permission"
    /*usb variable end*/

    companion object {
        val EXTRA_ADDRESS: String? = "device_address"
        val myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var bluetoothSocket: BluetoothSocket? = null
        lateinit var myProgress: ProgressDialog
        lateinit var bluetoothAdapter: BluetoothAdapter
        var isConnected: Boolean = false
        lateinit var address: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        /*ble code*/
        address = intent.getStringExtra(EXTRA_ADDRESS) as String
        connectToDevice(this).execute()
        b.btnOn.setOnClickListener {
            sendCommand("a")
        }
        b.btnOff.setOnClickListener {
            sendCommand("a")
        }
        b.btnDisconnect.setOnClickListener {
            sendCommand("a")
        }
        /*ble code end */


        /*for usb code and broadcaste*/
        usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        val filter = IntentFilter()
        filter.addAction(ACTION_USB_PERMISSION)
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        registerReceiver(broadCastReceiver, filter)

        b.btnOn.setOnClickListener {
            sendCommand("o")
        }
        b.btnOff.setOnClickListener { sendCommand("x") }
        b.btnDisconnect.setOnClickListener {
            disconnect()
        }
        b.btnConnect.setOnClickListener {
            startUsbConnection()
        }
        /*for usb code and broadcaste end*/
    }





    private fun sendCommand(input: String) {
        //usb code
        serial?.write(input.toByteArray())
        Log.i("TAG", "sendCommand: sendingData${input.toByteArray()}")

        /*
        //ble code
        if (bluetoothAdapter != null) {
            try {

                bluetoothSocket!!.outputStream.write(input.toByteArray())

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }*/

    }

    private fun disconnect() {
        //usb code
        serial?.close()
        /*
        //ble code
        if (bluetoothAdapter != null) {
            try {

                bluetoothSocket!!.close()
                bluetoothSocket = null
                isConnected = false

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        finish()*/
    }

    /*ble code start*/
    private class connectToDevice(c: Context) : AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = true
        private val context: Context

        init {
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            myProgress = ProgressDialog.show(context, "Connecting....", "Please wait...")

        }

        @SuppressLint("MissingPermission")
        override fun doInBackground(vararg params: Void?): String? {
            try {

                if (bluetoothSocket != null || isConnected) {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice(address)
                    bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    bluetoothSocket!!.connect()
                }

            } catch (e: Exception) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.i("TAG", "onPostExecute: could not connect ")

            } else {
                isConnected = true
            }
            myProgress.dismiss()
        }
    }

    /*ble code end*/


    /*usb code start*/
    private fun startUsbConnection() {
        val usbDevice: HashMap<String, UsbDevice>? = usbManager.deviceList
        if (!usbDevice?.isEmpty()!!) {
            var keep = true
            usbDevice.forEach { entry ->
                device = entry.value
                val deviceVendetId: Int? = device?.vendorId
                Log.i("TAG", "startUsbConnection:veinder id  $deviceVendetId")
                if (deviceVendetId == 1027) {
                    val intent: PendingIntent = PendingIntent.getBroadcast(this, 0, Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE)
                    usbManager.requestPermission(device, intent)
                    keep = false
                    Log.i("TAG", "startUsbConnection: connection successfull")

                } else {
                    connection = null
                    device = null
                    Log.i("TAG", "startUsbConnection:veinder id  unable to connect")
                }
                if (!keep) {
                    return
                }
            }
        } else {
            Log.i("TAG", "startUsbConnection: no device connected")
        }
    }

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action!! == ACTION_USB_PERMISSION) {
                val granted: Boolean =
                    intent.extras!!.getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED)
                if (granted) {
                    connection = usbManager.openDevice(device)
                    serial = UsbSerialDevice.createUsbSerialDevice(device, connection)
                    if (serial != null) {
                        if (serial!!.open()) {
                            serial!!.setBaudRate(9600)
                            serial!!.setDataBits(UsbSerialInterface.DATA_BITS_8)
                            serial!!.setStopBits(UsbSerialInterface.STOP_BITS_1)
                            serial!!.setParity(UsbSerialInterface.PARITY_NONE)
                            serial!!.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF)
                        } else {
                            Log.i("TAG", "onReceive: port not open ")

                        }
                    } else {
                        Log.i("TAG", "onReceive:port is null ")
                    }
                } else {
                    Log.i("TAG", "onReceive: permission not granted")
                }
            } else if (intent.action == UsbManager.ACTION_USB_ACCESSORY_ATTACHED) {
                startUsbConnection()
            } else if (intent.action == UsbManager.ACTION_USB_ACCESSORY_DETACHED) {
                disconnect()
            }
        }

    }
    /*usb code end*/

}