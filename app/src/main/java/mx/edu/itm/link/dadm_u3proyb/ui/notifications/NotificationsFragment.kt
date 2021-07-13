package mx.edu.itm.link.dadm_u3proyb.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import mx.edu.itm.link.dadm_u3proyb.EditarUsuario
import mx.edu.itm.link.dadm_u3proyb.MainActivity
import mx.edu.itm.link.dadm_u3proyb.R
import mx.edu.itm.link.dadm_u3proyb.databinding.FragmentNotificationsBinding
import mx.edu.itm.link.dadm_u3proyb.ui.GlobalViewModel

class NotificationsFragment : Fragment() {

    private val viewModel: GlobalViewModel by activityViewModels()

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usuario = MainActivity.usuarioLogueado
        binding.nombre.text = usuario.name
        binding.email.text = usuario.usr
        binding.telefono.text = usuario.celphone

        binding.btnPerfilUsuarioEditar.setOnClickListener {
            val intent = Intent(view.context, EditarUsuario::class.java)
            startActivity(intent)
        }
    }
}