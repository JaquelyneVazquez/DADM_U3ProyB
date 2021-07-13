package mx.edu.itm.link.dadm_u3proyb

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.gson.Gson
import mx.edu.itm.link.dadm_u3proyb.adapters.CommerceAdapter
import mx.edu.itm.link.dadm_u3proyb.models.Usuario
import mx.edu.itm.link.dadm_u3proyb.ui.GlobalViewModel

import mx.edu.itm.link.dadm_u3proyb.utils.MyUtils
import mx.edu.itm.link.dadm_u3proyb.utils.MyUtils.Companion.dbGet
import mx.edu.itm.link.dadm_u3proyb.utils.MyUtils.Companion.toast
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import kotlin.math.ln

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationManager: LocationManager

    private lateinit var editUsr : EditText
    private lateinit var editPass : EditText
    private lateinit var switchRemember : Switch
    private lateinit var btnAccess : ExtendedFloatingActionButton
    private lateinit var btnRegister : ExtendedFloatingActionButton

    val viewModel: GlobalViewModel by viewModels()


    private var lat = 0.0
    private var lng = 0.0

    companion object{
        lateinit var usuarioLogueado: Usuario
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editUsr = findViewById(R.id.editUsr)
        editPass = findViewById(R.id.editPass)
        switchRemember = findViewById(R.id.switchRemember)
        btnAccess = findViewById(R.id.btnAccess)
        btnRegister = findViewById(R.id.btnRegister)

        //Registro de un nuevo usuario
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegistroUsuario::class.java)
            startActivity(intent)
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        dbGet()?.let {
            myLocation()
            val intent = Intent(this@MainActivity, MenuActivity::class.java)
            intent.putExtra("usuario", it)
            intent.putExtra("lat", lat)
            intent.putExtra("lng", lng)
            startActivity(intent)
            finish()
        }

        btnAccess.setOnClickListener {
            var correcto = true
            if(editUsr.text.isEmpty()) {
                editUsr.setError("El usuario no debe ser vacío")
                correcto = false
            }
            if(!editUsr.text.contains("@") || !editUsr.text.contains(".")
                || editUsr.text.length < 5) {
                editUsr.error = "El correo no es válid"
                correcto = false
            }
            if(editPass.text.isEmpty()) {
                editPass.setError("La contraseña no debe ser vacía")
                correcto = false
            }
            if(editPass.text.length < 2) {
                editPass.error = "La contraseña es muy corta"
                correcto = false
            }
            if(correcto) {
                myLocation()
                login(editUsr.text.toString(), editPass.text.toString())
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                "Permiso otorgado".toast(this)
            } else {
                Toast.makeText(this, "La conexion fallo", Toast.LENGTH_SHORT).show()
                cannotContinue()
            }
        }
    }

    private fun myLocation() {
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
        }
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGps || hasNetwork) {
            if (hasGps) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F, object: LocationListener {
                    override fun onLocationChanged(location: Location) {
                        location?.let {
                            lat = it.latitude
                            lng = it.longitude
                        }
                    }

                    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

                    override fun onProviderEnabled(provider: String) {}

                    override fun onProviderDisabled(provider: String) {}
                })
                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                localGpsLocation?.let {
                    lat = it.latitude
                    lng = it.longitude
                }
            }
            if (hasNetwork) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0F, object: LocationListener {
                    override fun onLocationChanged(location: Location) {
                        location?.let {
                            lat = it.latitude
                            lng = it.longitude
                        }
                    }

                    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

                    override fun onProviderEnabled(provider: String) {}

                    override fun onProviderDisabled(provider: String) {}
                })
                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                localNetworkLocation?.let {
                    lat = it.latitude
                    lng = it.longitude
                }
            }
            viewModel.setLat(lat)
            viewModel.setLng(lng)
        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }

    }

    private fun cannotContinue() {
        MotionToast.createToast(
            this,
            "Error ☹️",
            "¡No puedo funcionar sin la ubicación!",
            MotionToast.TOAST_ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this,R.font.helvetica_regular)
        )
        "No puedo funcionar sin la ubicación".toast(this)
    }

    private fun login(usr: String, pass: String) {
        val url = "${resources.getString(R.string.api)}encuentra.php"
        Log.d("URL",url)

        val params = HashMap<String,String>()
        params.put("usr", usr)
        params.put("pass", pass)

        object : MyUtils() {
            override fun formatResponse(response: String) {
                Log.i("Consume", response)
                try {
                    val json = JSONObject(response)
                    val output = json.getJSONArray("output")

                    val gson = Gson()
                    val usuario = gson.fromJson(output.getJSONObject(0).toString(), Usuario::class.java)

                    // RememberMe
                    if(switchRemember.isChecked) {
                        this@MainActivity.dbSet(usuario)
                    }

                    usuarioLogueado = usuario

                    val intent = Intent(this@MainActivity, MenuActivity::class.java)
                    intent.putExtra("usuario", usuario)
                    intent.putExtra("lat", lat)
                    intent.putExtra("lng", lng)
                    startActivity(intent)
                    finish()

                } catch (e: Exception) {
                    Log.e("FR", "Error:\n$e")
                    MotionToast.createToast(
                        this@MainActivity,
                        "Error ☹️",
                        "No se encuentra el usuario",
                        MotionToast.TOAST_ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this@MainActivity,R.font.helvetica_regular)
                    )
                    "No se pudo conectar, intente mas tarde".toast(this@MainActivity)
                }
            }
        }.consumePost(this, url, params)
    }

}