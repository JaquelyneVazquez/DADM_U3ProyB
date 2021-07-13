package mx.edu.itm.link.dadm_u3proyb

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import mx.edu.itm.link.dadm_u3proyb.models.Usuario
import mx.edu.itm.link.dadm_u3proyb.ui.GlobalViewModel
import mx.edu.itm.link.dadm_u3proyb.utils.MyUtils.Companion.dbRemove
import mx.edu.itm.link.dadm_u3proyb.utils.MyUtils.Companion.toast
import www.sanju.motiontoast.MotionToast

class MenuActivity : AppCompatActivity() {

    private lateinit var usuario: Usuario

    private val viewModel: GlobalViewModel by viewModels()

    private var lat: Double = 0.0
    private var lng: Double = 0.0

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        usuario = intent.getSerializableExtra("usuario") as Usuario
        lat = intent.getDoubleExtra("lat", 0.0)
        lng = intent.getDoubleExtra("lng", 0.0)

        if(lat == 0.0 && lng == 0.0) {
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
            println("No puedo funcionar sin la ubicación")
            finish()
        }

        //viewModel = ViewModelProvider(this).get(GlobalViewModel::class.java)
        viewModel.setLat(lat)
        viewModel.setLng(lng)

        MotionToast.createToast(
            this,
            "Bienvenido",
            "¿Qué te apetece hoy ${usuario.name}?",
            MotionToast.TOAST_SUCCESS,
            MotionToast.GRAVITY_CENTER,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this,R.font.helvetica_regular)
        )

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        supportActionBar?.let {
            it.setDefaultDisplayHomeAsUpEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menuLogout) {
            this.dbRemove()
            finish()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else if(item.itemId == R.id.menuExit) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}