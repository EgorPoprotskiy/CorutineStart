package com.egorpoprotskiy.corutinestart

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.core.view.isVisible
import com.egorpoprotskiy.corutinestart.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    //создание handler в главном потоке(устаревшая интерпретация)
//    private val handler = Handler()
    //Обработка сообщений в Handler(для примера)
//    private val handler = object : Handler() {
//        override fun handleMessage(msg: Message) {
//            super.handleMessage(msg)
//            println("HANDKE_MSG $msg")
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonLoad.setOnClickListener {
            loadData()
        }
        //Обработка сообщений в Handler(для примера)
//        handler.sendMessage(Message.obtain(handler, 0, 17))
    }

    private fun loadData() {
        binding.progress.isVisible = true
        binding.buttonLoad.isEnabled = false
        loadCity {
            binding.tvLocation.text = it
            loadTemperature(it) {
                binding.tvTrmperature.text = it.toString()
                binding.progress.isVisible = false
                binding.buttonLoad.isEnabled = true
            }
        }
    }
    //callback создается для возврата значений(в данном случае нам надо вернуть строку)
    private fun loadCity(callback: (String) -> Unit) {
        thread {
            Thread.sleep(5000)
            //2.4 Еще более новая версия вызова Handle-а из главного потока
            runOnUiThread {
                callback.invoke("Moscow")
            }
        }
    }
    //callback создается для возврата значений(в данном случае нам надо вернуть число)
    private fun loadTemperature(city: String, callback: (Int) -> Unit) {
        thread {
            //2.4 Еще более новая версия вызова Handle-а из главного потока
            runOnUiThread {
                Toast.makeText(
                    this,
                    getString(R.string.load_temperature_toast, city),
                    Toast.LENGTH_SHORT
                ).show()
            }
            Thread.sleep(5000)
            runOnUiThread {
                callback.invoke(17)
            }
        }
    }
}