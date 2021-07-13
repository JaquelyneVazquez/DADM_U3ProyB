package mx.edu.itm.link.dadm_u3proyb.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mx.edu.itm.link.dadm_u3proyb.models.Pedido

class GlobalViewModel : ViewModel() {
    //Para obtener las coordenada actuales
    private val lat = MutableLiveData<Double>()
    private var lng = MutableLiveData<Double>()

    //Para obtener las coordenada de negocio
    private val latNegocio = MutableLiveData<Double>()
    private val lngNegocio = MutableLiveData<Double>()

    //Getters para las coordenada actuales
    val getLat: LiveData<Double> get() = lat
    val getLng: LiveData<Double> get() = lng

    //Getters para las coordenada de negocio
    val getLatNegocio: LiveData<Double> get() = lat
    val getLngNegocio: LiveData<Double> get() = lng

    //Para los pedidos
    private val pedidoActual = MutableLiveData<Pedido>()
    private val listaPedidos = MutableLiveData<ArrayList<Pedido>>()
    //Getters de pedidos
    val getPedido: LiveData<Pedido> get() = pedidoActual
    val getListaPedidos: LiveData<ArrayList<Pedido>> get() = listaPedidos

    //Setters coordenadas actuales
    fun setLat(latitude: Double) {
        lat.value = latitude
    }
    fun setLng(longitude: Double) {
        lng.value = longitude
    }

    //Setters coordenadas del negocio
    fun setLatNegocio(latitude: Double) {
        lat.value = latitude
    }
    fun setLngNegocio(longitude: Double) {
        lng.value = longitude
    }

    //Setters de pedido
    fun setPedidoActual(pedido: Pedido){
        pedidoActual.value = pedido
    }
    fun addPedidoALaLista(lista: ArrayList<Pedido>){
        listaPedidos.value = lista
    }

}