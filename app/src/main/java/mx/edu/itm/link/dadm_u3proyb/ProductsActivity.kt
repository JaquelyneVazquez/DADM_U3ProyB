package mx.edu.itm.link.dadm_u3proyb

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import mx.edu.itm.link.dadm_u3proyb.adapters.ProductsAdapter
import mx.edu.itm.link.dadm_u3proyb.models.Negocio
import mx.edu.itm.link.dadm_u3proyb.models.Producto
import mx.edu.itm.link.dadm_u3proyb.models.Usuario
import mx.edu.itm.link.dadm_u3proyb.ui.GlobalViewModel
import mx.edu.itm.link.dadm_u3proyb.ui.dashboard.DashboardFragment
import mx.edu.itm.link.dadm_u3proyb.ui.home.HomeFragment
import mx.edu.itm.link.dadm_u3proyb.ui.notifications.NotificationsFragment
import mx.edu.itm.link.dadm_u3proyb.utils.MyUtils
import mx.edu.itm.link.dadm_u3proyb.utils.MyUtils.Companion.dbGet
import mx.edu.itm.link.dadm_u3proyb.utils.MyUtils.Companion.toast
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast

class ProductsActivity : AppCompatActivity() {

    private lateinit var imgProducts: ImageView
    private lateinit var textInfoCommerce: TextView
    private lateinit var recyclerProducts: RecyclerView
    private lateinit var btnBuy: ExtendedFloatingActionButton
    private lateinit var fab: FloatingActionButton

    private lateinit var negocio: Negocio

    private val viewModel: GlobalViewModel by viewModels()

    private lateinit var url : String
    private val pedido = ArrayList<String>()
    private var total = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        imgProducts = findViewById(R.id.imgProducts)
        textInfoCommerce = findViewById(R.id.textInfoCommerce)
        recyclerProducts = findViewById(R.id.recyclerProducts)
        btnBuy = findViewById(R.id.btnBuyProducts)
        fab = findViewById(R.id.fabFavProducts)

        negocio = intent.getSerializableExtra("negocio") as Negocio

        if(negocio == null) {
            finish()
            "No tiene datos este negocio".toast(this)
        }

        url = resources.getString(R.string.api)

        negocio.photo?.let {
            val urlPhoto = "${url}assets/images/$it"

            Picasso.get().load(urlPhoto).into(imgProducts)
        }
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = negocio.commerce

        textInfoCommerce.text = "Descripción: ${negocio.description}"
        textInfoCommerce.text = "${textInfoCommerce.text}\nDirección: ${negocio.address} (${negocio.lat},${negocio.lng})"
        textInfoCommerce.text = "${textInfoCommerce.text}\nCategoria: ${negocio.category}"

        try {
            val endPoint = "${url}menu_negocio.php?id=${negocio.id}"
            object : MyUtils() {
                override fun formatResponse(response: String) {
                    try {
                        val json = JSONObject(response)
                        val output = json.getJSONArray("output")

                        val productos = ArrayList<Producto>()
                        for(i in 0..output.length()-1) {
                            val jsonProduct = output.getJSONObject(i)

                            val producto = Gson().fromJson(jsonProduct.toString(), Producto::class.java)

                            productos.add(producto)
                        }

                        recyclerProducts.adapter = object: ProductsAdapter(
                            this@ProductsActivity,
                            R.layout.recycler_row_products,
                            productos
                        ) {
                            override fun crearPedido(producto: Producto) {
                                this@ProductsActivity.pedir(producto)
                            }
                        }
                        recyclerProducts.layoutManager = LinearLayoutManager(this@ProductsActivity)

                    } catch (e: Exception) {
                        e.printStackTrace()
                        "Error, no hay productos disponibles".toast(this@ProductsActivity)
                    }
                }
            }.consumeGet(this, endPoint)
        } catch (e: Exception) {
            "Error al cargar productos".toast(this)
            println("Error:\n$e")
            Log.e("Productos","Error\n$e")
        }

        setSupportActionBar(findViewById(R.id.toolbar))

        fab.setOnClickListener { view ->
            try {
                this.dbGet()?.let{
                    val endP = "${url}fav.php"

                    val params = HashMap<String,String>()
                    params.put("usr", it.id.toString())
                    params.put("com", negocio.id.toString())

                    object : MyUtils(){
                        override fun formatResponse(response: String) {
                            //Log.d("Fav",response)
                            try {
                                val json = JSONObject(response)
                                val jsonOutput = json.getJSONArray("output")

                                if(jsonOutput.getString(0).equals("1")) {
                                    fab.setImageResource(R.mipmap.heart_24dp)
                                } else {
                                    fab.setImageResource(R.mipmap.heart_border_24dp)
                                }
                            } catch (e: Error) {
                                e.printStackTrace()
                                "Error en favoritos".toast(this@ProductsActivity)
                            }
                        }
                    }.consumePost(this,endP,params)

                    Snackbar.make(view, "Se actualizo favoritos", Snackbar.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                "Error al actualizar favoritos".toast(this)
                Log.e("Fav","Error:\n$e")
            }
        }

        btnBuy.setOnClickListener {
            val intent = Intent(this, NotificationsFragment::class.java)
            startActivity(intent)
        }

    }

    private fun pedir(p: Producto) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Tu pedido:")

        // Lista del pedido y su evento click
        alert.setAdapter(ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            pedido
        )) { dialogInterface: DialogInterface, i: Int ->
            // Evento click de la lista
        }

        // Campo para agregar la cantidad del producto
        val editCantidad = EditText(this)
        editCantidad.setHint("Cantidad de ${p.product}:")
        editCantidad.inputType = InputType.TYPE_CLASS_NUMBER
        alert.setView(editCantidad)

        // Botones
        alert.setNegativeButton("Cancelar") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }
        alert.setPositiveButton("Agregar") { dialogInterface: DialogInterface, i: Int ->
            if(editCantidad.text.isNotEmpty()) {
                val subtotal = (p.price * editCantidad.text.toString().toDouble())
                total += subtotal
                pedido.add("${editCantidad.text} ... ${p.product}: $subtotal")
                btnBuy.text = "Realizar compra por \$${total}"

                MotionToast.createToast(
                    this,
                    "Se agregó",
                    "Producto agregado a tu pedido",
                    MotionToast.TOAST_SUCCESS,
                    MotionToast.GRAVITY_CENTER,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,R.font.helvetica_regular)
                )
            }
            dialogInterface.dismiss()
        }

        alert.show()
    }

}