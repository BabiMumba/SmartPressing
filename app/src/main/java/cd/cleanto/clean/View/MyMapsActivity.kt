package cd.cleanto.clean.View

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cd.cleanto.clean.Models.cart_item
import cd.cleanto.clean.R
import cd.cleanto.clean.Utils.Utils
import cd.cleanto.clean.databinding.ActivityMyMapsBinding
import cd.cleanto.clean.databinding.BottomSheetValBinding
import com.bumptech.glide.util.Util

import com.google.android.gms.location.LocationRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MyMapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private var mMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_CODE_LOCATION_PERMISSION = 123
    private lateinit var mLastLocation: Location
    private var marker: Marker? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mLocationRequest: LocationRequest

    private var address: Address? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private lateinit var binding: ActivityMyMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.back.setOnClickListener {
            onBackPressed()
        }
        binding.selectAd.setOnClickListener {
            bottomsheetsend()
        }


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-11.671828362588464, 27.480711936950684), 13f))
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient()
            mMap?.setOnMapClickListener { latLng ->
                mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap?.animateCamera(CameraUpdateFactory.zoomTo(18f))
                handleMapClick(latLng)
                binding.selectAd.visibility = View.VISIBLE
                latitude = latLng.latitude
                longitude= latLng.longitude
            }
            mMap?.isMyLocationEnabled = true
        } else {
            Utils.showtoast(this, "Permission Denied")
        }
    }
    private fun handleMapClick(latLng: LatLng) {
        // Supprimez le marqueur précédent s'il existe
        marker?.remove()
        // Créez un nouveau marqueur à l'emplacement cliqué
        marker = mMap!!.addMarker(MarkerOptions().position(latLng))
        // Affichez les coordonnées dans les logs
        Log.d("MapsActivity", "Latitude: ${latLng.latitude}, Longitude: ${latLng.longitude}")
    }

    fun bottomsheetsend(){
        val mail = Firebase.auth.currentUser?.email.toString()
        val listeDesPaniers = intent.extras?.getParcelableArrayList<cart_item>("liste_panier")
        val bindig_btn_sheet = BottomSheetValBinding.inflate(layoutInflater)
        val btn = bindig_btn_sheet.sendColisBtn
        val dialog = BottomSheetDialog(this)
        val prixtotal = bindig_btn_sheet.totalPrice
        prixtotal.text = "${listeDesPaniers?.sumOf { it.price * it.quantity }}$"
        val total_item = bindig_btn_sheet.totalItem
        total_item.text = "${listeDesPaniers?.size}"

        val date_livraison = bindig_btn_sheet.dateLivraison
        val date_recuperation = bindig_btn_sheet.dateRamassage

        date_recuperation.text = getdate(1)
        date_livraison.text = getdate(3)


        btn.setOnClickListener {
            Utils.isloading(bindig_btn_sheet.sendColisBtn,bindig_btn_sheet.progress,true)
            val db = FirebaseFirestore.getInstance()
            val uid = Utils.getUID(mail)
            val data = hashMapOf(
                "latitude" to latitude,
                "longitude" to longitude,
                "total" to listeDesPaniers?.sumOf { it.price * it.quantity },
                "items" to listeDesPaniers?.size,
                "date_livraison" to getdate(3),
                "date_recuperation" to getdate(1),
                "uid" to uid
            )
            db.collection("colis").add(data)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        Utils.showtoast(this, "Colis envoyé avec succès")
                        Utils.isloading(bindig_btn_sheet.sendColisBtn,bindig_btn_sheet.progress,false)
                        dialog.dismiss()
                        onBackPressed()
                    }else{
                        Utils.showtoast(this, "Erreur lors de l'envoi du colis")
                        Utils.isloading(bindig_btn_sheet.sendColisBtn,bindig_btn_sheet.progress,false)
                    }
                }

        }
        dialog.setContentView(bindig_btn_sheet.root)
        dialog.show()

    }

    fun getdate(nb:Int):String{
        val c = Calendar.getInstance()
        c.add(Calendar.DATE, nb)
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = format.format(c.time)

        return  "$formattedDate"


    }
    private fun handleValidation() {
        // Vérifiez si un marqueur a été placé
        marker?.let { m ->
            // Récupérez les coordonnées du marqueur
            val latitude = m.position.latitude
            val longitude = m.position.longitude


            // Faites quelque chose avec les coordonnées (les envoyer à un serveur, les afficher, etc.)
            Log.d("MapsActivity", "Latitude choisie: $latitude, Longitude choisie: $longitude")
        } ?: run {
            Log.d("MapsActivity", "Veuillez choisir un emplacement sur la carte.")
        }
    }


    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient?.connect()
    }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        marker?.remove()

        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title("Current Position")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

        marker = mMap?.addMarker(markerOptions)
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap?.animateCamera(CameraUpdateFactory.zoomTo(15f))

        if (mGoogleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onConnected(bundle: Bundle?) {
        mLocationRequest = LocationRequest()
            .setInterval(1000)
            .setFastestInterval(1000)
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        Utils.showtoast(this, "Connection Suspended")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Utils.showtoast(this, "Connection Failed")
    }

    private fun searchLocation() {
        val location = binding.editText.text.toString()
        if (location.isBlank()) {
            Toast.makeText(applicationContext, "Please provide a location", Toast.LENGTH_SHORT).show()
            return
        }

        val geoCoder = Geocoder(this)
        try {
            val addressList = geoCoder.getFromLocationName(location, 1)
            if (addressList!!.isNotEmpty()) {
                address = addressList[0]
                val latLng = LatLng(address!!.latitude, address!!.longitude)
                mMap?.addMarker(MarkerOptions().position(latLng).title(location))
                mMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f))
                Toast.makeText(applicationContext, "${address?.latitude} ${address?.longitude}", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext, "Can't Find location", Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Please enter a valid address", Toast.LENGTH_LONG).show()
        }
    }

    private fun handleLocationAddedToDatabase() {
        if (this.address != null) {
            handleValidation()
            Utils.showtoast(this, "Location Added to Database")
            Log.d("Location", "Latitude: ${address?.latitude} Longitude: ${address?.longitude}")

        } else {
            Utils.showtoast(this, "Please select a location")
        }
    }


    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    mMap?.apply {
                        addMarker(MarkerOptions().position(currentLocation).title("Votre position"))
                        moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 20f))
                    }
                }
            }
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION_PERMISSION
            )
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            }
        }
    }
}