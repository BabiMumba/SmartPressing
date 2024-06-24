package cd.cleanto.clean.View

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cd.cleanto.clean.Auth.LoginActivity
import cd.cleanto.clean.MainActivity
import cd.cleanto.clean.R
import cd.cleanto.clean.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth

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
            checkuserlogin()
        }, 3000)
    }

    fun checkuserlogin(){
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }else{
            startActivity(Intent(this,OnboardActivity::class.java))

        }
    }
}