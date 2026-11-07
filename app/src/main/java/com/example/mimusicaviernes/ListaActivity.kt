
package com.example.mimusicaviernes

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ListaActivity : AppCompatActivity() {

    private var soloFavoritos = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        findViewById<TextView>(R.id.btnAgregar)
            .setOnClickListener {
                startActivity(
                    Intent(this, AgregarActivity::class.java)
                )
            }

        findViewById<TextView>(R.id.btnCerrar)
            .setOnClickListener {
                finish()
            }

        findViewById<TextView>(R.id.btnFavoritos)
            .setOnClickListener {
                soloFavoritos = !soloFavoritos
                mostrarCanciones()
            }
    }

    override fun onResume() {
        super.onResume()
        mostrarCanciones()
    }

    private fun dp(valor: Int): Int {
        return (valor * resources.displayMetrics.density).toInt()
    }

    private fun mostrarCanciones() {

        val contenedor = findViewById<LinearLayout>(
            R.id.contenedorCanciones
        )
        contenedor.removeAllViews()

        val prefs = getSharedPreferences(
            "mi_musica", MODE_PRIVATE
        )

        val canciones = prefs.getStringSet(
            "canciones", emptySet()
        ) ?: emptySet()

        val favoritos = prefs.getStringSet(
            "favoritos", emptySet()
        ) ?: emptySet()

        val visibles = if (soloFavoritos) {
            canciones.filter { it in favoritos }
        } else {
            canciones.toList()
        }

        if (visibles.isEmpty()) {
            val mensaje = TextView(this)
            mensaje.text = if (soloFavoritos) {
                "No tienes canciones favoritas"
            } else {
                "Aún no tienes canciones guardadas"
            }
            mensaje.gravity = Gravity.CENTER
            mensaje.textSize = 14f
            mensaje.setTextColor(Color.GRAY)
            mensaje.setPadding(8, dp(30), 8, 8)
            contenedor.addView(mensaje)
            return
        }

        visibles.sorted().forEach { cancion ->

            val partes = cancion.split(" - ", limit = 2)
            val nombre = partes[0]
            val artista = partes.getOrElse(1) { "" }

            val fila = LinearLayout(this)
            fila.orientation = LinearLayout.HORIZONTAL
            fila.gravity = Gravity.CENTER_VERTICAL
            fila.setPadding(dp(8), dp(8), dp(8), dp(8))

            val fondo = GradientDrawable()
            fondo.setColor(Color.WHITE)
            fondo.cornerRadius = dp(10).toFloat()
            fondo.setStroke(dp(2), Color.rgb(255, 24, 44))
            fila.background = fondo

            val filaParams = LinearLayout.LayoutParams(
                -1, -2
            )
            filaParams.bottomMargin = dp(8)

            // Círculo verde
            val circulo = TextView(this)
            val fondoCirculo = GradientDrawable()
            fondoCirculo.shape = GradientDrawable.OVAL
            fondoCirculo.setColor(Color.rgb(46, 211, 137))
            circulo.background = fondoCirculo

            fila.addView(
                circulo,
                LinearLayout.LayoutParams(dp(40), dp(40))
            )

            // Título y artista
            val textos = LinearLayout(this)
            textos.orientation = LinearLayout.VERTICAL
            textos.setPadding(dp(12), 0, 0, 0)

            val titulo = TextView(this)
            titulo.text = nombre
            titulo.textSize = 16f
            titulo.setTextColor(Color.BLACK)
            titulo.setTypeface(null, Typeface.BOLD)

            val subtitulo = TextView(this)
            subtitulo.text = artista
            subtitulo.textSize = 12f
            subtitulo.setTextColor(Color.GRAY)

            textos.addView(titulo)
            textos.addView(subtitulo)

            fila.addView(
                textos,
                LinearLayout.LayoutParams(0, -2, 1f)
            )

            // Botón morado +1
            val detalle = TextView(this)
            detalle.text = "+1"
            detalle.textSize = 14f
            detalle.setTextColor(Color.WHITE)
            detalle.gravity = Gravity.CENTER

            val fondoDetalle = GradientDrawable()
            fondoDetalle.setColor(Color.rgb(103, 80, 164))
            fondoDetalle.cornerRadius = dp(20).toFloat()
            detalle.background = fondoDetalle

            fila.addView(
                detalle,
                LinearLayout.LayoutParams(dp(42), dp(34))
            )

            fila.setOnClickListener {
                val intent = Intent(
                    this, DetalleActivity::class.java
                )
                intent.putExtra("cancion", cancion)
                startActivity(intent)
            }

            contenedor.addView(fila, filaParams)
        }
    }
}
