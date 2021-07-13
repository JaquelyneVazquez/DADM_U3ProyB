package mx.edu.itm.link.dadm_u3proyb.utils

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import mx.edu.itm.link.dadm_u3proyb.models.LocalDBManager
import mx.edu.itm.link.dadm_u3proyb.models.Usuario

abstract class MyUtils {

    companion object {

        fun String.toast(c: Context) {
            Toast.makeText(c, this, Toast.LENGTH_LONG).show()
        }

        fun Context.dbSet(usuario: Usuario) {
            val dbManager = LocalDBManager(this,"my_eats", null, 1)
            dbManager.setUsr(usuario)
        }

        fun Context.dbGet() : Usuario? {
            val dbManager = LocalDBManager(this,"my_eats", null, 1)
            return dbManager.getUsr()
        }

        fun Context.dbRemove() {
            val dbManager = LocalDBManager(this,"my_eats", null, 1)
            dbManager.removeUsr()
        }

    }

    fun consumePost(c: Context, url: String, params : MutableMap<String,String>) {
        val stringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response ->
                formatResponse(response)
            },
            Response.ErrorListener { println("Error al consumir:\n$it") }

        ) {
            override fun getParams(): MutableMap<String, String> {
                return params
            }
        }
        Volley.newRequestQueue(c).add(stringRequest)
    }

    abstract fun formatResponse(response: String)

    fun consumeGet(c: Context, url: String) {
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->
                    formatResponse(response)
                },
                Response.ErrorListener { println("Error al consumir:\n$it") }
        )
        Volley.newRequestQueue(c).add(stringRequest)
    }

    fun consumeGet(c: Context, url: String, params : MutableMap<String,String>) {
        val stringRequest = object : StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->
                    formatResponse(response)
                },
                Response.ErrorListener { println("Error al consumir:\n$it") }
        ){
            override fun getParams(): MutableMap<String, String> {
                return params
            }
        }
        Volley.newRequestQueue(c).add(stringRequest)
    }

}