package cd.cleanto.clean.Utils

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import cd.cleanto.clean.R
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

object  Utils {

    fun getFormattedPrice(price: Double): String {
        return "$price $/pc"
    }

    fun showtoast(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun loadFragment(context: Context, fragment: Fragment){
        val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    fun newIntent(context: Context, activity: Class<*>) {
        context.startActivity(Intent(context, activity))
    }
    fun isloading(button: View, progressBar: CircularProgressIndicator, isLoading: Boolean) {
        if (isLoading) {
            button.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } else {
            button.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }

    }
    fun username(context: Context):String{
        val sharedPreferences = context.getSharedPreferences(DATA.PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString("name","").toString()
    }
    fun getCurrentDate(): Timestamp {
        return Timestamp.now()
    }
    fun getUID(mail:String):String{
        var id_vendeur = mail.substringBefore("@").toString()
        //retirer les points et les caracteres speciaux
        for (i in id_vendeur.indices){
            if (id_vendeur[i] == '.' || id_vendeur[i] == '#' || id_vendeur[i] == '$' || id_vendeur[i] == '[' || id_vendeur[i] == ']'){
                id_vendeur = id_vendeur.replace(id_vendeur[i].toString(),"")
            }
        }

        return id_vendeur
    }
    fun savename(context: Context,name_user:String){
        val sharedPreferences = context.getSharedPreferences(DATA.PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name",name_user)
        editor.apply()
    }

    //fonction asynchrone pour recuperer le nom de l'utilisateur dans firestore en fonction de son uid
    fun GetUsername(uid:String, callback: (String) -> Unit){
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(uid).get()
            .addOnSuccessListener {
                if (it.exists()){
                    val name = it.getString("nom")
                    callback(name!!)
                }
            }


    }

    fun logout() {
        val auth = Firebase.auth
        auth.signOut()

    }
}