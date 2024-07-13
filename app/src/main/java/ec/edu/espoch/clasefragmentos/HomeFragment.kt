package ec.edu.espoch.clasefragmentos

import ConexionSQL
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    private lateinit var txtNombre: EditText
    private lateinit var txtApellido: EditText
    private lateinit var rdSexo: RadioGroup
    private lateinit var rdAuto: RadioGroup
    private lateinit var btnIngresar: Button
    private lateinit var txtImpr: TextView
    private lateinit var spinnerEdad: Spinner

    private lateinit var conexionSQL: ConexionSQL

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtNombre = view.findViewById(R.id.txt_nombre)
        txtApellido = view.findViewById(R.id.txt_apellido)
        rdSexo = view.findViewById(R.id.rd_sexo)
        rdAuto = view.findViewById(R.id.rd_auto)
        btnIngresar = view.findViewById(R.id.btn_ingresar)
        txtImpr = view.findViewById(R.id.txt_impr)
        spinnerEdad = view.findViewById(R.id.spinner_edad)

        conexionSQL = ConexionSQL(requireContext())

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.edades,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerEdad.adapter = adapter
        }

        btnIngresar.setOnClickListener {
            val nombre = txtNombre.text.toString().trim()
            val apellido = txtApellido.text.toString().trim()
            val sexoId = rdSexo.checkedRadioButtonId
            val transporte = obtenerTransporteSeleccionado(view)
            val edad = spinnerEdad.selectedItem.toString()

            if (nombre.isNotEmpty() && apellido.isNotEmpty() && sexoId != -1 && transporte.isNotEmpty()) {
                val sexo = view.findViewById<RadioButton>(sexoId).text.toString()
                val resultado = "Nombre: $nombre\nApellido: $apellido\nGénero: $sexo\nTransporte: $transporte\nEdad: $edad"
                txtImpr.text = resultado

                val idInsertado = conexionSQL.insertData(nombre, apellido, sexo, transporte, edad)
                if (idInsertado != -1L) {
                    Toast.makeText(requireContext(), "Datos insertados correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error al insertar los datos", Toast.LENGTH_SHORT).show()
                }

                Log.d("HomeFragment", "Resultado: $resultado")
            } else {
                Toast.makeText(requireContext(), "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun obtenerTransporteSeleccionado(view: View): String {
        val transporte = mutableListOf<String>()
        if (view.findViewById<CheckBox>(R.id.rd_carro).isChecked) {
            transporte.add("Carro")
        }
        if (view.findViewById<CheckBox>(R.id.rd_moto).isChecked) {
            transporte.add("Moto")
        }
        if (view.findViewById<CheckBox>(R.id.rd_triciclo).isChecked) {
            transporte.add("Triciclo")
        }
        return transporte.joinToString(", ")
    }

    override fun onDestroy() {
        super.onDestroy()
        conexionSQL.close()
    }
}
