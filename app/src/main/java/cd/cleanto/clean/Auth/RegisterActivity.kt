package cd.cleanto.clean.Auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import cd.cleanto.clean.MainActivity
import cd.cleanto.clean.Models.User
import cd.cleanto.clean.Utils.Utils
import cd.cleanto.clean.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.rpc.context.AttributeContext.Auth

class RegisterActivity : AppCompatActivity() {
    lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnInscrire.setOnClickListener {
            val nom = binding.edtNom.text.toString()
            val  prenom = binding.edtPrenom.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val date = Utils.getCurrentDate()
            if (checkFields()){

                Utils.isloading(binding.btnInscrire,binding.progress,true)
                val createUser =FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                createUser.addOnCompleteListener {
                    if (it.isSuccessful){
                        val user = User(Utils.getUID(email),nom,prenom,email,password,date)
                        SaveToDataBase(user)
                    }else{
                        Utils.showtoast(this, "Erreur d'inscription")
                        Utils.isloading(binding.btnInscrire,binding.progress,false)
                    }
                }
            }

        }


    }

    private fun checkFields():Boolean{
        if (binding.edtNom.text.toString().isEmpty()){
            Utils.showtoast(this, "Nom est vide")
            return false
        }
        if (binding.edtPrenom.text.toString().isEmpty()){
            Utils.showtoast(this, "Prenom est vide")
            return false
        }
        if (binding.edtAdress.text.toString().isEmpty()){
            Utils.showtoast(this, "l'adresse est vide")
            return false
        }
        if (binding.edtEmail.text.toString().isEmpty()){
            Utils.showtoast(this, "Email est vide")
            return false
        }
        if (binding.edtPhone.text.toString().isEmpty()){
            Utils.showtoast(this, "le numero est vide")
            return false
        }
        if (binding.edtPassword.text.toString().isEmpty()){
            Utils.showtoast(this, "Mot de passe est vide")
            return false
        }
        return true
    }
    fun SaveToDataBase(user: User){
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(user.uid).set(user).addOnSuccessListener {
            Utils.showtoast(this, "Inscription reussie")
            Utils.savename(this,user.nom)
            Utils.isloading(binding.btnInscrire,binding.progress,false)
            Utils.newIntent(this, MainActivity::class.java)
            finish()
        }.addOnFailureListener {
            Utils.isloading(binding.btnInscrire,binding.progress,false)
            Utils.showtoast(this, "Erreur d'inscription")
        }
    }
}