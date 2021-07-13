package mx.edu.itm.link.dadm_u3proyb.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import mx.edu.itm.link.dadm_u3proyb.ProductsActivity
import mx.edu.itm.link.dadm_u3proyb.R
import mx.edu.itm.link.dadm_u3proyb.models.Negocio

abstract class CommerceAdapter(val context: Context, val res: Int, val list: ArrayList<Negocio>) : RecyclerView.Adapter<CommerceAdapter.CommerceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommerceViewHolder {
        return CommerceViewHolder(
            LayoutInflater.from(context).inflate(res, null)
        )
    }

    override fun onBindViewHolder(holder: CommerceViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CommerceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(negocio: Negocio) {
            val imgPhoto = itemView.findViewById<ImageView>(R.id.imgPhotoCommerce)
            val textName = itemView.findViewById<TextView>(R.id.textNameCommerce)
            val textDesc = itemView.findViewById<TextView>(R.id.textDescCommerce)
            val imgFav = itemView.findViewById<ImageView>(R.id.imgFavCommerce)

            negocio.photo?.let {
                var urlPhoto = itemView.resources.getString(R.string.api)
                urlPhoto += "assets/images/$it"

                Picasso.get().load(urlPhoto).into(imgPhoto);
            }
            textName.text = negocio.commerce
            textDesc.text = negocio.description

            if(negocio.favorite) {
                imgFav.setImageResource(R.mipmap.heart_24dp)
            } else {
                imgFav.setImageResource(R.mipmap.heart_border_24dp)
            }

            //Contorno del icono de favoritos
            var contornoFav = negocio.favorite
            imgFav.setOnClickListener{
                setFavorito(negocio)
                contornoFav = !contornoFav
                if (contornoFav){
                    imgFav.setImageResource(R.mipmap.heart_24dp)
                }else{
                    imgFav.setImageResource(R.mipmap.heart_border_24dp)
                }
            }

            itemView.setOnClickListener {
                val intent = Intent(context, ProductsActivity::class.java)
                intent.putExtra("negocio", negocio)
                context.startActivity(intent)
            }
        }

    }

    abstract fun setFavorito(negocio: Negocio)

}