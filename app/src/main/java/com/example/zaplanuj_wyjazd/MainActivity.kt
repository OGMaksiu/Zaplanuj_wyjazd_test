package com.example.zaplanuj_wyjazd

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val kalendarz = findViewById<CalendarView>(R.id.kalendarz)
        val poczatek = findViewById<Button>(R.id.buttonpocz)
        val koniec = findViewById<Button>(R.id.buttonwyj)
        val datawyj = findViewById<TextView>(R.id.datawyj)
        val datakon = findViewById<TextView>(R.id.datakon)
        val iloscdni = findViewById<TextView>(R.id.iloscdni)

        kalendarz.minDate = Date().time
        kalendarz.maxDate = Date().time + 63072000000

        val pierwszydzien = mutableListOf(0,0,0)
        val ostatnidzien = mutableListOf(0,0,0)
        val selDate = arrayListOf(zmienNaDate(kalendarz.date)[0],zmienNaDate(kalendarz.date)[1],zmienNaDate(kalendarz.date)[2])
        kalendarz.setOnDateChangeListener{ _, d, m, y ->
            selDate[0] = d
            selDate[1] = m+1
            selDate[2] = y
        }
        poczatek.setOnClickListener {
            if(ostatnidzien[0] <= selDate[0] || ostatnidzien[1] <= selDate[1] || ostatnidzien[2] <= selDate[2]) for (i in 0 until 3)
                pierwszydzien[i] = selDate[i]
            datawyj.text = "Poczatek Wyjazdu: ${System.lineSeparator()}${pierwszydzien[0]}-${pierwszydzien[1]}-${pierwszydzien[2]}"


            if(ostatnidzien[0] != 0 && pierwszydzien[0] != 0)
                if(pierwszydzien[2] > ostatnidzien[2] && pierwszydzien[1] == ostatnidzien[1])
                    Toast.makeText(applicationContext, "Nie mozesz najpierw wrocic potem wyjechac", Toast.LENGTH_SHORT).show()
                else
                    policz(ostatnidzien, pierwszydzien, iloscdni)
        }


        koniec.setOnClickListener {
            for (i in 0 until 3)
                ostatnidzien[i] = selDate[i]
            datakon.text = "Koniec Wyjazdu: ${System.lineSeparator()}${ostatnidzien[0]}-${ostatnidzien[1]}-${ostatnidzien[2]}"


            if(ostatnidzien[0] != 0 && pierwszydzien[0] != 0)
                if(pierwszydzien[2] > ostatnidzien[2] && pierwszydzien[1] == ostatnidzien[1])
                    Toast.makeText(applicationContext, "Nie mozesz najpierw wrocic potem wyjechac", Toast.LENGTH_SHORT).show()
                else
                    policz(ostatnidzien, pierwszydzien, iloscdni)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun policz(lastDay: MutableList<Int>, firstDay : MutableList<Int>, TxtView : TextView) {
        val dzienwyjazdu = (firstDay[0]*360) + (firstDay[1]*30) + firstDay[2]
        val dzienpowrotu = (lastDay[0]*360) + (lastDay[1]*30) + lastDay[2]
        val diff =  dzienwyjazdu.toChar() - dzienpowrotu.toChar()
        TxtView.text = "Ilosc dni: ${System.lineSeparator()}${diff.absoluteValue+1}"
    }

    @SuppressLint("SimpleDateFormat")
    private fun zmienNaDate(czas : Long): List<Int> {
        val date = Date(czas)
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val formDate = dateFormat.format(date).split("/").map {
            it.toInt()
        }
        return formDate
    }
}