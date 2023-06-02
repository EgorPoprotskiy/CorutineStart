package com.egorpoprotskiy.corutinestart

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.egorpoprotskiy.corutinestart.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonLoad.setOnClickListener {
//            lifecycleScope.launch {
//                loadData()
//            }
            loadWithoutCoroutines()
        }
    }

    private suspend fun loadData() {
        Log.d("MainActivity", "Load started: $this")
        binding.progress.isVisible = true
        binding.buttonLoad.isEnabled = false
        val city = loadCity()

        binding.tvLocation.text = city
        val temp = loadTemperature(city)

        binding.tvTrmperature.text = temp.toString()
        binding.progress.isVisible = false
        binding.buttonLoad.isEnabled = true
        Log.d("MainActivity", "Load finished: $this")
    }

    private fun loadWithoutCoroutines(step: Int = 0, obj: Any? = null) {
        when (step) {
            0 -> {
                Log.d("MainActivity", "Load started: $this")
                binding.progress.isVisible = true
                binding.buttonLoad.isEnabled = false
                loadCityWithoutCoroutines {
                    loadWithoutCoroutines(1, it)
                }
            }
            1 -> {
                val city = obj as String
                binding.tvLocation.text = city
                loadTemperatureWithoutCoroutines(city) {
                    loadWithoutCoroutines(2, it)
                }
            }
            2 -> {
                val temp = obj as Int
                binding.tvTrmperature.text = temp.toString()
                binding.progress.isVisible = false
                binding.buttonLoad.isEnabled = true
                Log.d("MainActivity", "Load finished: $this")
            }
        }
    }

    private fun loadCityWithoutCoroutines(callback: (String) -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            callback.invoke("Moscow")
        }, 5000)
    }

    //callback создается для возврата значений(в данном случае нам надо вернуть строку)
    private suspend fun loadCity(): String {
        delay(5000)
        return "Moscow"
    }

    private fun loadTemperatureWithoutCoroutines(city: String, callback: (Int) -> Unit) {
        Toast.makeText(
            this,
            getString(R.string.load_temperature_toast, city),
            Toast.LENGTH_SHORT
        ).show()
        Handler(Looper.getMainLooper()).postDelayed({
            callback.invoke(17)
        }, 5000)
    }

    //callback создается для возврата значений(в данном случае нам надо вернуть число)
    private suspend fun loadTemperature(city: String): Int {
        Toast.makeText(
            this,
            getString(R.string.load_temperature_toast, city),
            Toast.LENGTH_SHORT
        ).show()
        delay(5000)
        return 17
    }
}