package cd.cleanto.clean.View

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cd.cleanto.clean.Auth.LoginActivity
import cd.cleanto.clean.Auth.RegisterActivity
import cd.cleanto.clean.R
import cd.cleanto.clean.databinding.ActivityOnboardBinding

class OnboardActivity : AppCompatActivity() {
    lateinit var binding : ActivityOnboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnCommencer.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
        binding.haveAccount.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}