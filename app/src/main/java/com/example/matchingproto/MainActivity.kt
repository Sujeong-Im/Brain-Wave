package com.example.matchingproto

import android.Manifest;
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matchingproto.databinding.ActivityMainBinding
import com.example.matchingproto.databinding.ItemMainBinding
import com.example.matchingproto.databinding.TopMenuLayoutBinding
import com.example.matchingproto.databinding.WritePartyBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationRequest.create
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener


class MainActivity : AppCompatActivity(),GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,OnMapReadyCallback{
    lateinit var binding: ActivityMainBinding
    lateinit var writebinding:WritePartyBinding
    lateinit var topbinding:TopMenuLayoutBinding
    var googleMap: GoogleMap? = null
    lateinit var providerClinet : FusedLocationProviderClient
    lateinit var apiClient : GoogleApiClient

    val datas = mutableListOf<ListData>()
    var latitude:Double =0.0
    var longitude:Double=0.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        writebinding=WritePartyBinding.inflate(layoutInflater)
        topbinding= TopMenuLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (supportFragmentManager.findFragmentById(R.id.mapView) as
                SupportMapFragment?)!!.getMapAsync(this)

        providerClinet=LocationServices.getFusedLocationProviderClient(this)
        apiClient=GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
        if(ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) !==
            PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) !==
                    PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_NETWORK_STATE) !==
                    PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_NETWORK_STATE),
                            100
                        )
                    }
            else{
                apiClient.connect()
        }
        //setContentView(listBinding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10 * 1000) // 10 seconds
            .setFastestInterval(1 * 1000) // 1 second


        datas.add(ListData("밥 같이 먹을사람1","초밥먹고싶어\n연어덮밥",
            37.300899,127.0939063))
        datas.add(ListData("밥","라면먹고싶어",
            37.298485,127.044458))
        datas.add(ListData("피자","피자먹고싶어어",
           37.300773,127.031019))


        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        binding.recyclerView.adapter=MyAdapter(datas,this)
        binding.recyclerView.addItemDecoration(MyDecoration(this))

        binding.write.setOnClickListener{
            setContentView(writebinding.root)
        }
        writebinding.makeParty.setOnClickListener{
            makeParty()
            setContentView(binding.root)
            (binding.recyclerView.adapter as MyAdapter).notifyDataSetChanged()
            Log.d("data : ",datas.toString())


        }

    }

    public fun moveMap(latitude: Double, longitude: Double, title:String){
        googleMap?.clear()
        val latLng = LatLng(latitude, longitude)
        val position = CameraPosition.Builder()
            .target(latLng)
            .zoom(14f)
            .build()
        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(position))

        val markerOption=MarkerOptions()
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker2))
        markerOption.position(latLng)
        markerOption.title(title)

        googleMap?.addMarker(markerOption)
    }

    private fun makeParty(){
        val title=writebinding.partyTitle.text.toString()
        val body=writebinding.partyBody.text.toString()

        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val requestCode = 100

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            fetchLocation()
        }
        datas.add(ListData(title,body,latitude,longitude))


    }


    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0

    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.lastLocation?.let {
                    latitude = it.latitude
                    longitude = it.longitude
                    // do something with latitude and longitude
                }
            }
        }, null)
    }


    override fun onConnected(p0: Bundle?) {
        if(ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) ===
                    PackageManager.PERMISSION_GRANTED)
        {
            providerClinet.getLastLocation().addOnSuccessListener (
                this@MainActivity,
                object :OnSuccessListener<Location>{
                    override fun onSuccess(location: Location?) {
                        location?.let{
                            latitude = location.latitude
                            longitude = location.longitude
                        }
                    }


            })
            apiClient.disconnect()
        }

    }
    override fun onConnectionSuspended(p0: Int){

    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

}