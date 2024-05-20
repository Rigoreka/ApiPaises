package com.example.pruebaapi

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pruebaapi.databinding.ActivityMainBinding
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.rclPaises.layoutManager = LinearLayoutManager(this)
        //Realizar la solicitud del endpoint de pi web con el cliente http
        Fuel.get("https://restcountries.com/v3.1/all?fields=name,capital,flags,area,population")
            .response() { request, response, result ->
                //obtenemos resultado en formato json
                val cuerpoJson = response.body().asString("application/json")
                //Gson convertimos de formato json a lista de objetos
                val convertidor = Gson()
                listaPais = convertidor.fromJson(cuerpoJson, Array<Pais>::class.java).toList()

                binding.txtnota.setText("Hay ${listaPais.size} elementos")

                binding.rclPaises.adapter = PaisAdapter(listaPais) { pais ->
                    val intent = Intent(this, Lista_Pais::class.java).apply {
                        putExtra("EXTRA_PAIS_NAME", pais.name.official)
                        putExtra("EXTRA_PAIS_CAPITAL", pais.capital.joinToString(","))
                        putExtra("EXTRA_PAIS_FLAG",pais.flags.png)
                        putExtra("EXTRA_PAIS_POBLACION",pais.population)
                        putExtra("EXTRA_PAIS_AREA",pais.area)
                    }
                    startActivity(intent)
                }
            }
    }
}