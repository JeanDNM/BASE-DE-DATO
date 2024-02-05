    package com.example.basededatos

    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.util.Log
    import android.widget.ArrayAdapter
    import android.widget.Button
    import android.widget.EditText
    import android.widget.Spinner
    import android.widget.TextView
    import android.widget.Toast

    class MainActivity : AppCompatActivity() {
        private lateinit var nameEditText: EditText
        private lateinit var emailEditText: EditText
        private lateinit var provinciaEditText: EditText
        private lateinit var guardarcontacto: Button
        private lateinit var consultaboton: Button
        private lateinit var consultaDato: EditText
        private lateinit var consultaporProvinciaButton: Button
        private lateinit var consultar: TextView
        private lateinit var db: DatabaseHandler



        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.activity_main)


            nameEditText = findViewById(R.id.NameEditText)
            emailEditText = findViewById(R.id.EmailEditText)
            provinciaEditText = findViewById(R.id.ProvinciaEditText)
            guardarcontacto = findViewById(R.id.GuardarContacto)
            consultaboton = findViewById(R.id.consultabutton)
            consultaDato = findViewById(R.id.ProvinciaConsultaEditText)
            consultaporProvinciaButton = findViewById(R.id.ConsultaPorProvinciaButton)
            consultar = findViewById(R.id.consulta)

            db = DatabaseHandler(this)


            guardarcontacto.setOnClickListener {
                val name = nameEditText.text.toString().trim()
                val email = emailEditText.text.toString().trim()
                val provincia = provinciaEditText.text.toString().trim()

                if (name.isNotEmpty() && email.isNotEmpty() && provincia.isNotEmpty()) {
                    val id = db.addContact(name, email, provincia)
                    if (id == (-1L)) {
                        // error al guardar en BBD
                    } else {
                        nameEditText.text.clear()
                        emailEditText.text.clear()
                        provinciaEditText.text.clear()
                    }
                } else {
                    Toast.makeText(applicationContext, "Te falta algún campo por rellenar", Toast.LENGTH_LONG).show()
                }
            }

            consultaboton.setOnClickListener {
                val contactList = db.getAllContacts()
                val consultaTexto = StringBuilder()

                for (contact in contactList) {
                    Log.d("Contacto", "ID: ${contact.id}, Nombre: ${contact.name}, Email: ${contact.email}, Provincia: ${contact.provincia}")
                    consultaTexto.append("ID: ${contact.id}, Nombre: ${contact.name}, Email: ${contact.email}, Provincia: ${contact.provincia}\n")
                }

                consultaDato.setText(consultaTexto.toString())
            }

            consultaporProvinciaButton.setOnClickListener {
                val provinciaConsulta = consultaDato.text.toString().trim()
                val contactList = db.queryProvinciaContacts(provinciaConsulta)
                val consultaTexto = StringBuilder()

                for (contact in contactList) {
                    Log.d("Contacto", "ID: ${contact.id}, Nombre: ${contact.name}, Email: ${contact.email}, Provincia: ${contact.provincia}")
                    consultaTexto.append("ID: ${contact.id}, Nombre: ${contact.name}, Email: ${contact.email}, Provincia: ${contact.provincia}\n")
                }

                consultaDato.setText(consultaTexto.toString())
            }

            // Dentro de onCreate
            val provinciaSpinner: Spinner = findViewById(R.id.ProvinciaSpinner)
            val provincias = resources.getStringArray(R.array.provincias)
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provincias)
            provinciaSpinner.adapter = adapter

            val consultaButton: Button = findViewById(R.id.ConsutaSpinner)

            consultaButton.setOnClickListener {
                val provinciaConsulta = provinciaSpinner.selectedItem.toString()
                val contactList = db.queryProvinciaContacts(provinciaConsulta)
                val consultaTexto = StringBuilder()

                for (contact in contactList) {
                    Log.d("Contacto", "ID: ${contact.id}, Nombre: ${contact.name}, Email: ${contact.email}, Provincia: ${contact.provincia}")
                    consultaTexto.append("ID: ${contact.id}, Nombre: ${contact.name}, Email: ${contact.email}, Provincia: ${contact.provincia}\n")
                }

                consultaDato.setText(consultaTexto.toString())
            }

        }
    }

