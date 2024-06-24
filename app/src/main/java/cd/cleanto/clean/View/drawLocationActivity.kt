package cd.cleanto.clean.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cd.cleanto.clean.R
import cd.cleanto.clean.databinding.ActivityDrawLocationBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment

class DrawLocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrawLocationBinding
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            googleMap = map

        }
    }
}