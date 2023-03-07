package com.example.jpivocabo

import android.content.Intent
import android.graphics.ColorSpace
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.jpivocabo.ui.theme.JPIvocaboTheme
import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.util.*

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
                    val intlatlng=intent.getStringExtra("location")
                    latLng=Json.decodeFromString<LatLng>(intlatlng.toString())
                    FormInit(latLng)
                }
            }
        }
    }
    companion object{
        private lateinit var latLng:LatLng
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInit(latLng: LatLng) {
    val context = LocalContext.current;
    var txtmacaddress by rememberSaveable { mutableStateOf("") }
    var haserrormacaddress by rememberSaveable { mutableStateOf(false) }
    var txtdevicename by rememberSaveable { mutableStateOf("") }
    var haserrordevicename by rememberSaveable { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Add Device Form", color = Color.White, fontSize=20.sp, fontWeight = FontWeight.Black, modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp))
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
                var deviceid=""
                //start::validate
                var dfmacaddres = txtmacaddress
                var dfdevicename = txtdevicename
                haserrormacaddress = !AppHelper().CheckMacAddress(dfmacaddres)
                if (!haserrormacaddress) {
                    haserrordevicename = dfdevicename == ""
                    if(!haserrordevicename){
                        //start::db event
                        var device=Device(
                            id=deviceid.trim().toInt(),
                            registerdate = LocalDateTime.now().toString(),
                            macaddress =dfmacaddres,
                            name = dfdevicename,
                            latitude = latLng.latitude.toString(),
                            longitude = latLng.longitude.toString()
                        )

                        val appDatabase by lazy { AppRoomDatabase.getInstance(context).dbDao() }
                        viewLifecycleOwner
                        lifecycleScope.launch {
                            appDatabase.addDevice(device)
                        }
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
