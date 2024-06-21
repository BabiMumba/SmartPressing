package cd.cleanto.clean.View

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cd.cleanto.clean.Auth.LoginActivity
import cd.cleanto.clean.R
import cd.cleanto.clean.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //delai de 3 secondes
        val handler = android.os.Handler()
        handler.postDelayed({
            //redirection vers la page d'accueil
            startActivity(Intent(this,LoginActivity::class.java))
        }, 3000)
    }
}