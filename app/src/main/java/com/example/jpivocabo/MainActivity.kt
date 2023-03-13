package com.example.jpivocabo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.material3.ListItem
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jpivocabo.database.*
import com.example.jpivocabo.ui.theme.JPIvocaboTheme
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class MainActivity : ComponentActivity() {
    private var requestingLocationUpdates: Boolean = false

    //lateinit var devicelist: List<Device>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vmLocationStateViewModel = ViewModelProvider(this).get(LocationStateViewModel::class.java)
        setContent {
            JPIvocaboTheme {
                locationPermissionRequest.launch(LOCATION_PERMISSIONS)
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {

                }
                MainPage(vmLocationStateViewModel)
            }
        }
    }


    @SuppressLint("MissingPermission")
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                startLocationUpdates()
            }
            /*permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            }*/
            else -> {
                // No location access granted.
            }
        }
    }


    @RequiresPermission(
        allOf = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    fun startLocationUpdates() {
        requestingLocationUpdates = false
        val locationRequest = LocationRequest.Builder(3000).apply {
            //setIntervalMillis(3000)
            setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            //setDurationMillis(1000)
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest.build(), locationCallback, Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        requestingLocationUpdates = true
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult ?: return
            //Log.v(TAG, "Last Location Latitude : " + locationResult.locations.last().latitude)

            vmLocationStateViewModel.apply {
                onChange(
                    DLatLng(
                        locationResult.locations.last().latitude,
                        locationResult.locations.last().longitude
                    )
                )
            }
            Log.v(
                TAG,
                "Last Location Latitude 2 : " + vmLocationStateViewModel.locationStateData.latitude
            )
            /*for (location in locationResult.locations){
                // Update UI with location data
                // ...
            }*/
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }


    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private val LOCATION_PERMISSION_CODE = 120
        private val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        )
        private var latlng: LatLng? = LatLng(1.35, 103.87)
        private lateinit var fusedLocationClient: FusedLocationProviderClient
        private lateinit var locationCallback: LocationCallback
        private lateinit var vmLocationStateViewModel: LocationStateViewModel
        val rdOptAddDevice = listOf("Scan QRCode", "Input Mac Address")
    }


}

private var devicelist: List<Device>? = null

//@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainPage(locationStateViewModel: LocationStateViewModel) {
    //val singapore = LatLng(1.35, 103.87)
    val context = LocalContext.current
    var locState = LatLng(
        locationStateViewModel.locationStateData.latitude,
        locationStateViewModel.locationStateData.longitude
    )
    val dismissState = rememberDismissState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(locState, 18f)
    }
    LaunchedEffect(key1 = locState) {
        cameraPositionState.move(update = CameraUpdateFactory.newLatLng(locState))
    }
    val fillcolor = Color(0x20ff0000)

    val openDeviceAddDialog = remember { mutableStateOf(false) }
    val dbViewModel: DBViewModel =
        viewModel(factory = DBViewModelFactory(context.applicationContext as IvocaboApplication))

    devicelist = dbViewModel.readAllDevice.observeAsState(initial = listOf()).value

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(indoorLevelPickerEnabled = true, compassEnabled = true),
                properties = MapProperties(isIndoorEnabled = true, isBuildingEnabled = true),
            ) {
                Marker(
                    state = MarkerState(position = locState),
                    title = "Location Detail",
                    alpha = .82f,
                    snippet = "Long.: " + locState.longitude + " Latt.: " + locState.latitude
                )
                Circle(center = locState, fillColor = fillcolor, strokeWidth = 0f, radius = 22.00)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.CenterVertically)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                text = stringResource(id = R.string.device_list),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(10.dp, 8.dp)
            )
            Spacer(Modifier.weight(1f))
            Button(onClick = {
                if (locState != null) {
                    openDeviceAddDialog.value = true
                } else {
                    Toast.makeText(
                        context,
                        "Please check internet connection!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }) {
                Text(text = stringResource(id = R.string.add_device))
            }
        }



        if (devicelist!!.isNotEmpty()) {

            LazyColumn(modifier = Modifier.wrapContentHeight()) {
                itemsIndexed(
                    items = devicelist!!,
                    key = { index: Int, item: Device -> item.id }) { index: Int, item: Device ->
                    val dismissState = rememberDismissState(
                        confirmValueChange = {
                            if (it == DismissValue.DismissedToStart) {
                                devicelist = devicelist!!.toMutableList().also { it.remove(item) }
                            } else if (it == DismissValue.DismissedToEnd) {
                                val intforminput = Intent(context, AddDeviceForm::class.java)
                                intforminput.putExtra("device",item )
                                context.startActivity(intforminput)
                            }
                            true
                        }
                    )
                    SwipeToDismiss(
                        modifier = Modifier
                            .padding(vertical = 1.dp)
                            .animateItemPlacement(),
                        directions = setOf(
                            DismissDirection.StartToEnd,
                            DismissDirection.EndToStart
                        ),

                        state = dismissState,
                        background = {
                            val direction =
                                dismissState.dismissDirection ?: return@SwipeToDismiss
                            val color by animateColorAsState(
                                when (dismissState.targetValue) {
                                    DismissValue.Default -> Color.LightGray
                                    DismissValue.DismissedToEnd -> Color.Green
                                    DismissValue.DismissedToStart -> Color.Red
                                }
                            )
                            val alignment = when (direction) {
                                DismissDirection.StartToEnd -> Alignment.CenterStart
                                DismissDirection.EndToStart -> Alignment.CenterEnd
                            }
                            val icon = when (direction) {
                                DismissDirection.StartToEnd -> R.drawable.baseline_text_snippet_white_24
                                DismissDirection.EndToStart -> R.drawable.baseline_delete_forever__white24
                            }
                            val iconscale by animateFloatAsState(targetValue = if (dismissState.targetValue == DismissValue.Default) 0.8f else 1.2f)
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(color),
                                contentAlignment = alignment
                            ) {

                                androidx.compose.material3.Icon(
                                    painter = painterResource(id = icon),
                                    contentDescription = "Remove Ivocabo!",
                                    Modifier.scale(iconscale)
                                )

                            }
                        },
                        dismissContent = {
                            //ListItemView(item!!)
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = animateDpAsState(
                                        targetValue = if (dismissState.dismissDirection != null) 4.dp else 0.dp
                                    ).value
                                )

                            ) {

                                ListItem(
                                    leadingContent = {
                                        androidx.compose.material3.Icon(
                                            painter = painterResource(id = R.drawable.baseline_bluetooth_orange_24),
                                            contentDescription = "Ivocabo Device"
                                        )
                                    },
                                    headlineText = { item.name?.let { Text(text = it) } },
                                    supportingText = { item.macaddress?.let { Text(text = it) } }

                                )
                                Divider()
                            }

                        }
                    )
                }
            }
        }
    }
    DeviceAddBottomSheet(openDeviceAddDialog, locationStateViewModel)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceAddBottomSheet(
    openDialog: MutableState<Boolean>,
    locationStateViewModel: LocationStateViewModel
) {
    val context = LocalContext.current;
    var latLng = locationStateViewModel.locationStateData

    val intforminput = Intent(context, AddDeviceForm::class.java)
    intforminput.putExtra("location", Json.encodeToString(latLng))


    if (openDialog.value) {
        val barcodeLauncher = rememberLauncherForActivityResult<ScanOptions, ScanIntentResult>(
            ScanContract()
        ) { result: ScanIntentResult ->
            if (result.contents == null) {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                openDialog.value = false
                intforminput.putExtra("scannedqrcode", result.contents)
                context.startActivity(intforminput)
            }
        }


        val (selectedOption, onOptionSelected) = remember { mutableStateOf(MainActivity.rdOptAddDevice[0]) }
        var rBDeviceAddState by remember { mutableStateOf(true) }

        ModalBottomSheet(
            onDismissRequest = { openDialog.value = false },
        ) {
            Column() {
                TextButton(onClick = {
                    val options = ScanOptions()
                    options.setOrientationLocked(false)
                    barcodeLauncher.launch(options)

                },
                    content = {
                        androidx.compose.material3.Icon(
                            painter = painterResource(id = R.drawable.baseline_qr_code_scanner_white_24),
                            contentDescription = "Scan QrCode"
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = "Scan QrCode")
                    }
                )
                TextButton(onClick = {
                    context.startActivity(intforminput)
                    openDialog.value = false
                },
                    content = {
                        androidx.compose.material3.Icon(
                            painter = painterResource(id = R.drawable.baseline_text_snippet_white_24),
                            contentDescription = "Input Mac Address"
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = "Input Mac Address")
                    }
                )

            }
        }
    }
}
/*@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

    JPIvocaboTheme {

        Greeting("Android Koray")
    }
}*/
