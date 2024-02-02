    package com.example.basededatos

    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.util.Log
    import android.widget.Button
    import android.widget.EditText
    import android.widget.TextView
    import android.widget.Toast

    class MainActivity : AppCompatActivity() {
        private lateinit var nameEditText: EditText
        private lateinit var emailEditText: EditText
        private lateinit var savebutton: Button
        private lateinit var db: DatabaseHandler
        private lateinit var consultaboton: Button
        private lateinit var consultaDato: TextView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            nameEditText = findViewById(R.id.NameEditText)
            emailEditText = findViewById(R.id.EmailEditText)
            savebutton = findViewById(R.id.guardarcontacto)
            consultaboton = findViewById(R.id.consultabutton)
            consultaDato = findViewById(R.id.consulta)

            db = DatabaseHandler(this)

            savebutton.setOnClickListener {
                val name = nameEditText.text.toString().trim()
                val email = emailEditText.text.toString().trim()

                if (name.isNotEmpty() && email.isNotEmpty()) {
                    val id = db.addcontact(name, email)
                    if (id == (-1L)) {
                        // error al guardar en BBD
                    } else {
                        // toast para avisar de que se ha guardado el registro en la BBD
                        nameEditText.text.clear()
                        emailEditText.text.clear()
                    }
                } else {
                    // cuando el usuario no ha ingresado algún campo
                    Toast.makeText(applicationContext, "Te falta algún campo por rellenar", Toast.LENGTH_LONG).show()
                }
            }

            consultaboton.setOnClickListener {
                val contactList = db.getAllContacts()
                val consultaTexto = StringBuilder()

                for (contact in contactList) {
                    Log.d("Contacto", "ID: ${contact.id}, Nombre: ${contact.name}, Email: ${contact.email}")
                    consultaTexto.append("ID: ${contact.id}, Nombre: ${contact.name}, Email: ${contact.email}\n")
                }

                consultaDato.text = consultaTexto.toString()
            }
        }
    }
