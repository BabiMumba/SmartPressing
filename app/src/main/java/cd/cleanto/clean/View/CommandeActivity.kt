package cd.cleanto.clean.View

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cd.cleanto.clean.Adapters.CartHabiAda
import cd.cleanto.clean.Adapters.CommandeHabiAda
import cd.cleanto.clean.Models.cart_item
import cd.cleanto.clean.R
import cd.cleanto.clean.Utils.Utils
import cd.cleanto.clean.databinding.ActivityCommandeBinding

class CommandeActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommandeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommandeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.commandeLyt.commanderBtn.text = "Valider et payer"
        //changer la couleur du bouton
        binding.commandeLyt.commanderBtn.backgroundTintList = resources.getColorStateList(R.color.green)
        binding.commandeLyt.commanderBtn.setOnClickListener {
            Utils.newIntent(this, MyMapsActivity::class.java)
        }

        initdata()
        binding.back.setOnClickListener {
            onBackPressed()
        }

    }

    fun initdata(){
        val listeDesPaniers = intent.extras?.getParcelableArrayList<cart_item>("liste_panier")
        //initialisation des elements de la page
        if (listeDesPaniers != null) {
            binding.recyclerHab.apply {
                adapter = CommandeHabiAda(listeDesPaniers)
            }
            val prixtout = listeDesPaniers.sumByDouble { it.price * it.quantity }
            binding.commandeLyt.totalPrice.text = prixtout.toString()
            (binding.recyclerHab.adapter as CommandeHabiAda).setOnItemClickListener(object :
                CommandeHabiAda.OnItemClickListener {
                override fun onItemClick(item: cart_item) {
                    val prixtout = listeDesPaniers.sumByDouble { it.price * it.quantity }
                    binding.commandeLyt.totalPrice.text = prixtout.toString()


                }
            })


        }else{
            Utils.showtoast(this, "Veuillez ajouter des articles à votre panier")
        }

    }
}