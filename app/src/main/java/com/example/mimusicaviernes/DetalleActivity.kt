package com.example.mimusicaviernes

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetalleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        val cancion = intent.getStringExtra("cancion") ?: run {
            finish()
            return
        }

        val prefs = getSharedPreferences(
            "mi_musica", MODE_PRIVATE
        )

        val txtDetalle = findViewById<TextView>(
            R.id.txtDetalle
        )
        txtDetalle.text = cancion

        val btnFavorito = findViewById<Button>(
            R.id.btnFavorito
        )

        fun actualizarFavorito() {
            val favoritos = prefs.getStringSet(
                "favoritos", emptySet()
            ) ?: emptySet()

            btnFavorito.text = if (cancion in favoritos) {
                "★ Quitar de favoritas"
            } else {
                "☆ Marcar favorita"
            }
        }

        actualizarFavorito()

        btnFavorito.setOnClickListener {
            val favoritos = prefs.getStringSet(
                "favoritos", emptySet()
            )?.toMutableSet() ?: mutableSetOf()

            if (cancion in favoritos) {
                favoritos.remove(cancion)
            } else {
                favoritos.add(cancion)
            }

            prefs.edit()
                .putStringSet("favoritos", favoritos)
                .apply()

            actualizarFavorito()
        }

        findViewById<Button>(R.id.btnEscuchar)
            .setOnClickListener {
                Toast.makeText(
                    this,
                    "Reproducción pendiente de implementar",
                    Toast.LENGTH_SHORT
                ).show()
            }

        findViewById<Button>(R.id.btnEliminar)
            .setOnClickListener {

                val canciones = prefs.getStringSet(
                    "canciones", emptySet()
                )?.toMutableSet() ?: mutableSetOf()

                canciones.remove(cancion)

                val favoritos = prefs.getStringSet(
                    "favoritos", emptySet()
                )?.toMutableSet() ?: mutableSetOf()

                favoritos.remove(cancion)

                prefs.edit()
                    .putStringSet("canciones", canciones)
                    .putStringSet("favoritos", favoritos)
                    .apply()

                Toast.makeText(
                    this,
                    "Canción eliminada",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }

        findViewById<Button>(R.id.btnVolverDetalle)
            .setOnClickListener {
                finish()
            }
    }
}
