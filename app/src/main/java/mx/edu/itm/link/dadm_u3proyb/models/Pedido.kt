package mx.edu.itm.link.dadm_u3proyb.models

data class Pedido(
    val Producto: Producto,
    val negocio: Negocio,
    val usuario: Usuario
    )

