package mx.edu.itm.link.dadm_u3proyb.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import mx.edu.itm.link.dadm_u3proyb.R
import mx.edu.itm.link.dadm_u3proyb.models.Producto

abstract class ProductsAdapter(val context:Context, val res:Int, val list:ArrayList<Producto>) : RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        return ProductsViewHolder(
            LayoutInflater.from(context).inflate(res, null)
        )
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(producto: Producto) {
            val textName = itemView.findViewById<TextView>(R.id.textNameProduct)
            val textDesc = itemView.findViewById<TextView>(R.id.textDescProduct)
            val textPrice = itemView.findViewById<TextView>(R.id.textPriceProduct)
            val imgProduct = itemView.findViewById<ImageView>(R.id.imgProd)

            textName.text = producto.product
            textDesc.text = producto.productDescription
            textPrice.text = "${producto.price}"

            producto.photo?.let {
                var urlPhoto = itemView.resources.getString(R.string.api)
                urlPhoto += "assets/images/$it"

                Picasso.get().load(urlPhoto).into(imgProduct)
            }

            itemView.setOnClickListener { crearPedido(producto) }
        }

    }

    abstract fun crearPedido(producto: Producto)

}