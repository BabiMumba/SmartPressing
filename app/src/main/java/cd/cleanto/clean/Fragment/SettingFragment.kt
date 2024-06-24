package cd.cleanto.clean.Fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cd.cleanto.clean.R
import cd.cleanto.clean.Utils.Utils
import cd.cleanto.clean.View.SplashActivity
import cd.cleanto.clean.databinding.FragmentSettingBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class SettingFragment : Fragment() {

    lateinit var binding:FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        getusername()

        binding.logoutLyt.setOnClickListener {
            logout()
        }


        return binding.root
    }

    fun getusername(){
        val lien_profil = "https://png.pngtree.com/png-vector/20210921/ourlarge/pngtree-flat-people-profile-icon-png-png-image_3947764.png"
        Glide.with(requireContext()).load(lien_profil).into(binding.profileImage)
        val name = Utils.username(requireContext())
        val auth = FirebaseAuth.getInstance()
        val mail = auth.currentUser!!.email.toString()
        binding.mailTxt.text = "$mail"
        binding.nameTxt.text = "$name"
    }

    fun logout(){
        val dialogue = AlertDialog.Builder(requireContext())
        dialogue.setTitle("Deconnexion")
        dialogue.setMessage("Voulez-vous vraiment vous deconnecter ?")
        dialogue.setPositiveButton("Oui") { dialog, which ->
            //deconnexion
            Utils.logout()
            Utils.newIntent(requireActivity(), SplashActivity::class.java)
            requireActivity().finish()
        }
        dialogue.setNegativeButton("Non") { dialog, which ->
            //ne rien faire
            dialog.dismiss()
        }
        dialogue.show()
    }

}