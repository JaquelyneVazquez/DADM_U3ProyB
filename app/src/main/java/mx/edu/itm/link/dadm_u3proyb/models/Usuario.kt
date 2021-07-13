package mx.edu.itm.link.dadm_u3proyb.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Usuario(
        @SerializedName("id")
        val id: Int,
        @SerializedName("usuario")
        val usr: String,
        @SerializedName("contrasenia")
        val pass: String,
        @SerializedName("nombre")
        val name: String,
        @SerializedName("telefono")
        val celphone: String
) : Serializable
