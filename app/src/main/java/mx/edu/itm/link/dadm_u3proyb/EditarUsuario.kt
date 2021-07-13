package mx.edu.itm.link.dadm_u3proyb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mx.edu.itm.link.dadm_u3proyb.databinding.ActivityEditarUsuarioBinding
import androidx.core.content.res.ResourcesCompat
import mx.edu.itm.link.dadm_u3proyb.utils.MyUtils
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast

class EditarUsuario : AppCompatActivity() {

    private lateinit var binding: ActivityEditarUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_usuario)

        binding = ActivityEditarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nombre = binding.editTextEditarUsuarioNombre.text.toString()
        val email = binding.editTextEditarUsuarioEmail.text.toString()
        val telefono = binding.editTextEditarUsuarioTelefono.text.toString()
        val password = binding.editTextEditarUsuarioContrasena.text.toString()

        val params = HashMap<String,String>()
        params.put("id", MainActivity.usuarioLogueado.id.toString())
        params.put("nombre", nombre)
        params.put("email", email)
        params.put("telefono", telefono)
        params.put("password", password)

        //Se consume de la api de NOde
        val url = "${resources.getString(R.string.apiNode)}actualizarUsuario"

        binding.btnEditarUsuarioGuardar.setOnClickListener {
            object : MyUtils(){
                override fun formatResponse(response: String) {
                    //Respuesta que se obtiene desde la api
                    val respuesta = JSONObject(response)
                    val code = respuesta.getInt("code")

                    if (code == 200){
                        MotionToast.createToast(
                            this@EditarUsuario,
                            "Exito ☺",
                            "Se actualizo con exito el usuario: ${nombre}",
                            MotionToast.TOAST_SUCCESS,
                            MotionToast.GRAVITY_CENTER,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@EditarUsuario,R.font.helvetica_regular)
                        )
                        finish()
                    }else{
                        MotionToast.createToast(
                            this@EditarUsuario,
                            "Error :(",
                            "Verifica tu conexion",
                            MotionToast.TOAST_ERROR,
                            MotionToast.GRAVITY_CENTER,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@EditarUsuario,R.font.helvetica_regular)
                        )
                    }
                }
            }.consumePost(this,url,params)
        }

        binding.btnEditarUsuarioCancelar.setOnClickListener {
            finish()
        }
    }
}