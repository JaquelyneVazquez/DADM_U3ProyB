package mx.edu.itm.link.dadm_u3proyb.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.findFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import mx.edu.itm.link.dadm_u3proyb.R
import mx.edu.itm.link.dadm_u3proyb.ui.GlobalViewModel

class DashboardFragment : Fragment() {

    private val viewModel: GlobalViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        viewModel.getLatNegocio.observe(viewLifecycleOwner, Observer {
            Log.d("Latitud negocio", "Latitud negocio: $it")
        })

        viewModel.getLngNegocio.observe(viewLifecycleOwner, Observer {
            Log.d("Longirud negocio", "Longitud negocio: $it")
        })

        childFragmentManager.findFragmentById(R.id.map)?.let {
            val map = it as SupportMapFragment

            viewModel.getLng.observe(viewLifecycleOwner, { longitudDeDispositivo ->
                viewModel.getLat.observe(viewLifecycleOwner, { latitudDeDispositivo ->

                    if (latitudDeDispositivo != 0.0 && longitudDeDispositivo != 0.0) {
                        map.getMapAsync { map ->
                            val local = LatLng(latitudDeDispositivo, longitudDeDispositivo)
                            val negocio = LatLng(19.7229386, -101.1858201)
                            map.addMarker(
                                MarkerOptions().position(local).title("Tu ubicacion actual.")
                            )
                            map.addMarker(
                                MarkerOptions().position(negocio).title("Ubicacion de tu pedido.")
                                    .icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_GREEN
                                        )
                                    )
                            )
                            map.moveCamera(CameraUpdateFactory.newLatLng(local))
                            map.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        latitudDeDispositivo,
                                        longitudDeDispositivo
                                    ), 13.0f
                                )
                            )
                        }
                    }
                })
            })


        }

        return root
    }


}