package cd.cleanto.clean.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cd.cleanto.clean.Fragment.HabiFragment
import cd.cleanto.clean.Models.Service
import cd.cleanto.clean.R
import cd.cleanto.clean.Utils.Utils
import com.bumptech.glide.Glide

class ServiceAd(val liste:ArrayList<Service>): RecyclerView.Adapter<ServiceAd.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_services,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(liste[position])

        holder.itemView.setOnClickListener {


            Utils.newIntent(holder.itemView.context, HabiFragment::class.java)

        }
    }

    override fun getItemCount(): Int {
        return liste.size
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindItems(service: Service){
            val name: TextView = itemView.findViewById(R.id.service_name)
            val image: ImageView = itemView.findViewById(R.id.image_service)
            name.text = service.name
            Glide.with(itemView.context).load(service.image).into(image)
        }
    }
}