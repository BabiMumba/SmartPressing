package cd.cleanto.clean.Auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cd.cleanto.clean.MainActivity
import cd.cleanto.clean.R
import cd.cleanto.clean.Utils.Utils
import cd.cleanto.clean.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnLogin.setOnClickListener {
            Utils.isloading(binding.btnLogin,binding.progress,true)
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            val mail = auth.currentUser?.email.toString()
                            val uid = Utils.getUID(mail)
                            Utils.GetUsername(uid) {name->
                                Utils.savename(this,name)
                            }
                            Utils.isloading(binding.btnLogin,binding.progress,false)
                            Utils.newIntent(this,MainActivity::class.java)
                            finish()
                        }else{
                            Utils.isloading(binding.btnLogin,binding.progress,false)
                            Utils.showtoast(this,it.exception.toString())
                        }
                    }

            }else{
                Utils.isloading(binding.btnLogin,binding.progress,false)
                Utils.showtoast(this,"Please Enter Email and Password")
            }
        }
        binding.txtRegister.setOnClickListener {
            //redirection vers la page d'accueil
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }
}