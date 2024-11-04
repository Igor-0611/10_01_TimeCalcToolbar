package com.example.a10_01_timecalctoolbar

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private const val s = "@string/app_title"

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var toolbarMain: Toolbar
    private lateinit var firstOperandET: EditText
    private lateinit var secondOperandET: EditText
    private lateinit var buttonSumBTN: Button
    private lateinit var buttonDifBTN: Button
    private lateinit var resultTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toolbarMain = findViewById(R.id.toolbarMain)
        setSupportActionBar(toolbarMain)
        title = "Калькулятор времени"
        toolbarMain.subtitle = "версия 1.0"
        toolbarMain.setLogo(R.drawable.ic_calculate)

        firstOperandET = findViewById(R.id.firstOperandET)
        secondOperandET = findViewById(R.id.secondOperandET)
        buttonSumBTN = findViewById(R.id.buttonSumBTN)
        buttonDifBTN = findViewById(R.id.buttonDifBTN)
        resultTV = findViewById(R.id.resultTV)

        buttonSumBTN.setOnClickListener (this)
        buttonDifBTN.setOnClickListener (this)
    }

    override fun onClick(v: View) {

        if (firstOperandET.text.isEmpty() || secondOperandET.text.isEmpty()) {
            return
        }

        val first = timeToSeconds(firstOperandET.text.toString())
        val second = timeToSeconds(secondOperandET.text.toString())

        val result = when(v.id) {
            R.id.buttonSumBTN -> secondsToTime(Operation(first, second).sum())
            R.id.buttonDifBTN -> secondsToTime(Operation(first, second).dif())
            else -> 0.0
        }

        resultTV.text = result.toString()
    }

    private fun timeToSeconds(timeString: String): Int {
        val regex = Regex("(\\d+)([hms])")
        val matches = regex.findAll(timeString)

        val timePairs = matches.map {
            val (number, unit) = it.destructured
            Pair(number.toInt(), unit)
        }.toList()

        val sumSeconds = timePairs.map { pair ->
            val (number, unit) = pair
            when (unit) {
                "h" -> number * 3600
                "m" -> number * 60
                "s" -> number
                else -> 0
            }
        }.sum()

        return sumSeconds
    }

    private fun secondsToTime(sec: Int): String {
        var strOfTime = ""
        var secOfTime = sec

        if (sec < 0) {
            strOfTime += "- "
            secOfTime *= -1
        }

        val hours = secOfTime / 3600
        val minutes = (secOfTime % 3600) / 60
        val seconds = secOfTime % 60

        if (hours > 0) strOfTime += "$hours h "
        if (minutes > 0) strOfTime += "$minutes m "
        if (seconds > 0) strOfTime += "$seconds s"


        return strOfTime
    }

}