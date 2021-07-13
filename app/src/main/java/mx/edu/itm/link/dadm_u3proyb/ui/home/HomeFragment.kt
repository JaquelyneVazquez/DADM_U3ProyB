package mx.edu.itm.link.dadm_u3proyb.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewDebug
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mx.edu.itm.link.dadm_u3proyb.MainActivity
import mx.edu.itm.link.dadm_u3proyb.R
import mx.edu.itm.link.dadm_u3proyb.adapters.CommerceAdapter
import mx.edu.itm.link.dadm_u3proyb.models.Negocio
import mx.edu.itm.link.dadm_u3proyb.utils.MyUtils
import org.json.JSONObject

class HomeFragment : Fragment() {

    private lateinit var url : String
    private lateinit var recyclerNegocios: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val recyclerNegocios = view.findViewById<RecyclerView>(R.id.recyclerCommerces)
        val editSearch = view.findViewById<EditText>(R.id.editSearch)
        val fabOnlyFav = view.findViewById<FloatingActionButton>(R.id.fabOnlyFav)

        recyclerNegocios = view.findViewById(R.id.recyclerCommerces)

        url = resources.getString(R.string.api)+"comercios.php"

        object : MyUtils(){
            override fun formatResponse(response: String) {
                //Log.i("Comercios",response)
                try {
                    val json = JSONObject(response)
                    val output = json.getJSONArray("output")

                    val negocios = ArrayList<Negocio>()

                    for(i in 0..output.length()-1) {
                        val jsonCommerce = output.getJSONObject(i)
                        val negocio = Negocio(
                            jsonCommerce.getInt("id"),
                            jsonCommerce.getString("negocio"),
                            jsonCommerce.getString("descripcion"),
                            jsonCommerce.getString("direccion"),
                            jsonCommerce.getDouble("latitud"),
                            jsonCommerce.getDouble("longitud"),
                            jsonCommerce.getInt("id_categoria"),
                            jsonCommerce.getString("categoria"),
                            if(jsonCommerce.getInt("favorito")==1) true else false,
                            jsonCommerce.getString("foto")
                        )

                        negocios.add(negocio)

                        //Creacion de la lista de negocios con el recycler (Lista completa)
                        actualizarLista(view, negocios)
                    }

                    //Busqueda del negocio, por comercio, descripccion o categoria
                    editSearch.doOnTextChanged{
                        text, start, before, count ->
                        val listaFiltrada = negocios.filter{
                            n ->
                                    n.commerce.contains(text.toString(), ignoreCase = true) ||
                                    n.description.contains(text.toString(), ignoreCase = true) ||
                                    n.category.contains(text.toString(), ignoreCase = true)
                        }
                        actualizarLista(view, listaFiltrada as ArrayList<Negocio>)
                    }

                    //Buusqueda por favoritos
                    //Variable de control para el favorito
                    var favoritosSeleccionado = false
                    fabOnlyFav.setOnClickListener{
                        //Se hace el cambio de verdadero a falso o viceversa a segun se encuentre el btn
                        favoritosSeleccionado = !favoritosSeleccionado

                        //Si esta seleccionado
                        if (favoritosSeleccionado){
                            val listaFiltrada = negocios.filter { n ->
                                n.favorite
                            }
                            actualizarLista(view, listaFiltrada as ArrayList<Negocio>)
                        }//Y si no esta seleccionado el btn
                        else{
                            actualizarLista(view, negocios)
                        }
                    }
                    //recyclerNegocios.adapter = CommerceAdapter(view.context, R.layout.recycler_row_commerce, negocios)
                    //recyclerNegocios.layoutManager = LinearLayoutManager(view.context)

                } catch (e: Exception) {
                    e.printStackTrace()
                    "Error, no hay negocios disponibles".toast(view.context)
                }
            }
        }.consumeGet(view.context, url)
    }

    fun actualizarLista(view: View, negocios: ArrayList<Negocio>){
        recyclerNegocios.adapter = object: CommerceAdapter(view.context, R.layout.recycler_row_commerce, negocios){
            override fun setFavorito(negocio: Negocio) {

                val url = "${resources.getString(R.string.api)}/fav.php"

                //Se obtiene el usuario logueado y el comercio para obtener la actualizacion de la lista de comercios a segun el usuario logueado
                val idUser = MainActivity.usuarioLogueado.id
                val params = HashMap<String, String>()
                params.put("usr", idUser.toString())
                params.put("com", negocio.id.toString())

                //Respuesta del servidor
                object : MyUtils(){
                    override fun formatResponse(response: String) {
                        val respuestaFinal = JSONObject(response)
                        val code = respuestaFinal.getInt("code")

                        if (code == 200) {
                            for (n in negocios){
                                if (n == negocio){
                                    n.favorite = !n.favorite
                                }
                            }
                            actualizarLista(view, negocios)
                            Log.d("Favorito", response)
                        }
                    }
                }.consumePost(view.context, url, params)
            }

        }

        recyclerNegocios.layoutManager = LinearLayoutManager(view.context)
    }

}