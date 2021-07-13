package mx.edu.itm.link.dadm_u3proyb.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Producto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("producto")
    val product: String,
    @SerializedName("descripcion_producto")
    val productDescription: String,
    @SerializedName("id_negocio")
    val idCommerce: Int,
    @SerializedName("precio")
    val price: Double,
    @SerializedName("foto")
    val photo: String?
) : Serializable
