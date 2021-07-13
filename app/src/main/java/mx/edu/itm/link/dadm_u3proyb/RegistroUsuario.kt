package mx.edu.itm.link.dadm_u3proyb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import mx.edu.itm.link.dadm_u3proyb.databinding.ActivityRegistroUsuarioBinding
import mx.edu.itm.link.dadm_u3proyb.ui.GlobalViewModel
import mx.edu.itm.link.dadm_u3proyb.utils.MyUtils
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast

class RegistroUsuario : AppCompatActivity() {

    private val viewModel: GlobalViewModel by viewModels()
    private lateinit var binding: ActivityRegistroUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistroGuardar.setOnClickListener {
            val nombre = binding.editTextRegistroNombre
            val email = binding.editTextRegistroEmail
            val telefono = binding.editTextRegistroNTelefono
            val password = binding.editTextRegistroContrasena

            //Variable de control
            var correcto = true
            if (nombre.text.toString().isEmpty()) {
                nombre.error = "El nombre del usuario no deben ser vacio."
                correcto = false
            }
            if (!email.text.toString().contains("@") || !email.text.toString().contains(".")
                || email.text.toString().length < 5
            ) {
                email.error = "Email invalido."
                correcto = false
            }
            if (telefono.text.toString().isEmpty()) {
                telefono.error = "El numero de telefono no debe estar vacio"
                correcto = false
            }
            if (password.text.toString().isEmpty()) {
                password.error = "La contraseña no debe ser vacía"
                correcto = false
            }

            if (correcto) {

                val name = nombre.text.toString()
                val email = email.text.toString()
                val telefono = telefono.text.toString()
                val pass = password.text.toString()

                //Consumiendo de la api de node
                val url = "${resources.getString(R.string.apiNode)}altaUsuario"

                val params = HashMap<String, String>()
                params.put("nombre", name)
                params.put("telefono", telefono)
                params.put("email", email)
                params.put("password", pass)

                object : MyUtils() {
                    override fun formatResponse(response: String) {
                        val respuesta = JSONObject(response)
                        val code = respuesta.getInt("code")

                        if (code == 200) {
                            MotionToast.createToast(
                                this@RegistroUsuario,
                                "Exito! >:)",
                                "El registro del usuario fue exitoso!",
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(
                                    this@RegistroUsuario,
                                    R.font.helvetica_regular
                                )
                            )
                            finish()
                        } else {
                            MotionToast.createToast(
                                this@RegistroUsuario,
                                "Error :(",
                                "Verifica que el servidor este corriendo (Node)",
                                MotionToast.TOAST_ERROR,
                                MotionToast.GRAVITY_CENTER,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(
                                    this@RegistroUsuario,
                                    R.font.helvetica_regular
                                )
                            )
                        }
                    }
                }.consumePost(this, url, params)
            } else {
                MotionToast.createToast(
                    this,
                    "Error!",
                    "Verifica tus datos ingresados",
                    MotionToast.TOAST_ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, R.font.helvetica_regular)
                )
            }
        }

        binding.btnRegistroCancelar.setOnClickListener {
            finish()
        }

    }
}