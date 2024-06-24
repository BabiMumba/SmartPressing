package cd.cleanto.clean.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cd.cleanto.clean.Models.cart_item
import cd.cleanto.clean.R
import cd.cleanto.clean.Utils.Utils
import com.bumptech.glide.Glide
import java.util.ArrayList

class CommandeHabiAda(val item:ArrayList<cart_item>): RecyclerView.Adapter<CommandeHabiAda.ViewHolder>() {


    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(item: cart_item)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.nom_hab)
        val price = itemView.findViewById<TextView>(R.id.prix_hab)
        val pricetotal = itemView.findViewById<TextView>(R.id.prix_total)
        val btn_moins = itemView.findViewById<TextView>(R.id.moins)
        val btn_plus = itemView.findViewById<TextView>(R.id.plus)
        val quantity = itemView.findViewById<TextView>(R.id.quantite)
        val image = itemView.findViewById<ImageView>(R.id.image_hab)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_hab_cart, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = item[position].name
     //   holder.quantity.text = item[position].quantity.toString()
        Glide.with(holder.itemView.context).load(item[position].image).into(holder.image)
        val price = Utils.getFormattedPrice(item[position].price)
        val quantity = item[position].quantity
        holder.quantity.text = quantity.toString()
        holder.price.text = price
        //prix total = prix unitaire * quantité
        val total = item[position].price * item[position].quantity
        holder.pricetotal.text = "Total: $total $"

        holder.btn_plus.setOnClickListener {
            item[position].quantity += 1
            notifyItemChanged(position)
            onItemClickListener?.onItemClick(item[position])
        }
        holder.btn_moins.setOnClickListener {
            if (item[position].quantity > 1) {
                item[position].quantity -= 1
                notifyItemChanged(position)
                onItemClickListener?.onItemClick(item[position])
            }
        }


    }

    override fun getItemCount(): Int {
        return item.size
    }
}