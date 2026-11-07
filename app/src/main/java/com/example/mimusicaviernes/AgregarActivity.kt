package com.example.mimusicaviernes

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AgregarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)

        val txtCancion = findViewById<EditText>(
            R.id.txtCancion
        )
        val txtArtista = findViewById<EditText>(
            R.id.txtArtista
        )

        findViewById<Button>(R.id.btnGuardar)
            .setOnClickListener {

                val nombre = txtCancion.text.toString().trim()
                val artista = txtArtista.text.toString().trim()

                if (nombre.isEmpty() || artista.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Completa nombre y artista",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val prefs = getSharedPreferences(
                    "mi_musica", MODE_PRIVATE
                )

                val canciones = prefs.getStringSet(
                    "canciones", emptySet()
                )?.toMutableSet() ?: mutableSetOf()

                canciones.add("$nombre - $artista")

                prefs.edit()
                    .putStringSet("canciones", canciones)
                    .apply()

                Toast.makeText(
                    this,
                    "Canción guardada",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }

        findViewById<Button>(R.id.btnVolver)
            .setOnClickListener {
                finish()
            }
    }
}
