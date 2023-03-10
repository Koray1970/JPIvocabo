package com.example.jpivocabo

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.jpivocabo.database.*
import com.example.jpivocabo.ui.theme.JPIvocaboTheme
import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.util.*


@Suppress("DEPRECATION")
class AddDeviceForm : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JPIvocaboTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    if(!intent.hasExtra("device")) {
                        val intlatlng = intent.getStringExtra("location")
                        var tlatLng = Json.decodeFromString<DLatLng>(intlatlng.toString())
                        latLng = LatLng(tlatLng.latitude, tlatLng.longitude)
                        if (intent.hasExtra("scannedqrcode")) {
                            scannedqrcode = intent.getStringExtra("scannedqrcode").toString()
                        }
                    }
                    else{

                        device= if(VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU)
                            intent.getParcelableExtra("device",Device::class.java)!!
                        else
                            intent.getParcelableExtra<Device>("device")!!

                    }
                    FormInit(latLng,scannedqrcode)
                }
            }
        }
    }

    companion object {
        private lateinit var latLng: LatLng
        private var scannedqrcode:String=""
        lateinit var device:Device
    }
}

private val TAG = "FormInit"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInit(latLng: LatLng,scannedQRCode:String) {
    val context = LocalContext.current;
    val device:Device
    if(AddDeviceForm.Companion.device!=null)
        device=AddDeviceForm.Companion.device

    var txtmacaddress by rememberSaveable { mutableStateOf("") }
    if(scannedQRCode!="")
        txtmacaddress=device.macaddress?device: AppHelper().StringToMacaddress(scannedQRCode).toString()
    var haserrormacaddress by rememberSaveable { mutableStateOf(false) }
    var txtdevicename by rememberSaveable { mutableStateOf("") }
    var haserrordevicename by rememberSaveable { mutableStateOf(false) }
    val dbViewModel: DBViewModel = viewModel(factory = DBViewModelFactory(context.applicationContext as IvocaboApplication))


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Add Device Form",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            value = txtmacaddress,
            keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Characters),
            onValueChange = { txtmacaddress = it },
            label = { Text(text = "Mac Address :") },
            isError = haserrormacaddress,
            supportingText = {
                Text(
                    text = if (haserrormacaddress) "Please check Mac Address & corrected it!" else "",
                    color = MaterialTheme.colorScheme.error
                )
            },
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            value = txtdevicename,
            onValueChange = { txtdevicename = it },
            label = { Text(text = "Device Name :") },
            isError = haserrordevicename,
            supportingText = {
                Text(
                    text = if (haserrordevicename) "Please input device name!" else "",
                    color = MaterialTheme.colorScheme.error
                )
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                val intgoback = Intent(context, MainActivity::class.java)
                context.startActivity(intgoback)

            }) {
                Text(text = "Cancel")
            }
            Spacer(Modifier.weight(1f))
            Button(onClick = {
                var deviceid = ""
                //start::validate
                var dfmacaddres = txtmacaddress
                var dfdevicename = txtdevicename
                haserrormacaddress = !AppHelper().CheckMacAddress(dfmacaddres)
                if (!haserrormacaddress) {
                    haserrordevicename = dfdevicename == ""
                    if (!haserrordevicename) {
                        //start::db event
                        var device = Device(
                            id = 0,
                            registerdate = LocalDateTime.now().toString(),
                            macaddress = dfmacaddres,
                            name = dfdevicename,
                            latitude = latLng.latitude.toString(),
                            longitude = latLng.longitude.toString()
                        )


                        dbViewModel.addDevice(device)

                        val intgoback = Intent(context.applicationContext, MainActivity::class.java)
                        context.startActivity(intgoback)

                    }
                }
            }) {
                Text(text = "Save")
            }
        }
    }
}


/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JPIvocaboTheme {
        Greeting("Android")
    }
}*/
