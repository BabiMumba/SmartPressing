package cd.cleanto.clean.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cd.cleanto.clean.Adapters.ServiceAd
import cd.cleanto.clean.Models.Service
import cd.cleanto.clean.R
import cd.cleanto.clean.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        initservice()

        return binding.root
    }

    fun initservice(){
        val services = ArrayList<Service>()
        //dry
        services.add(Service("Nettoyage a sec","https://cdn-icons-png.flaticon.com/128/1685/1685982.png"))
        //laundry
        services.add(Service("Blanchisserie","https://cdn-icons-png.flaticon.com/128/2528/2528015.png"))
        //repassage
        services.add(Service("Repassage","https://cdn-icons-png.flaticon.com/128/2681/2681732.png"))
        services.add(Service("lavage des mains","https://cdn-icons-png.flaticon.com/128/5859/5859261.png"))
        binding.recyclerServices.apply {
            adapter = ServiceAd(services)
        }

    }
}