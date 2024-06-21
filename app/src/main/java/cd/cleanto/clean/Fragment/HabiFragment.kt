package cd.cleanto.clean.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cd.cleanto.clean.Adapters.CartHabiAda
import cd.cleanto.clean.Models.cart_item
import cd.cleanto.clean.R
import cd.cleanto.clean.Utils.Utils
import cd.cleanto.clean.databinding.FragmentHabiBinding


class HabiFragment : Fragment() {
    lateinit var binding : FragmentHabiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHabiBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        init()
        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.valider.setOnClickListener {
            val fragement = MapsFragment()
            Utils.loadFragment(requireActivity(), fragement)
        }

        return binding.root
    }

    fun init(){
        //initialisation des elements de la page
        val liste_habi = ArrayList<cart_item>()
        liste_habi.add(cart_item("Pantalon", 100.0, 1))
        liste_habi.add(cart_item("Chemise", 50.0, 2))
        liste_habi.add(cart_item("Chaussure", 150.0, 1))
        liste_habi.add(cart_item("T-shirt", 30.0, 3))
        liste_habi.add(cart_item("Veste", 200.0, 1))
        liste_habi.add(cart_item("Blouse", 160.0, 1))
        liste_habi.add(cart_item("Coussin", 100.0, 1))
        liste_habi.add(cart_item("Manteau", 80.0, 1))
        liste_habi.add(cart_item("Robe", 10.0, 1))
        liste_habi.add(cart_item("Jupe", 8.0, 1))
        liste_habi.add(cart_item("Salopette", 100.0, 1))

        //creation de l'adapter
        binding.recyclerHab.apply {
            adapter = CartHabiAda(liste_habi)
        }

    }

}