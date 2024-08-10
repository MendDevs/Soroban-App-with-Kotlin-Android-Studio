package com.example.sorobanmorris

//Project by Emmanuel Markie Morris
//2024 at ENSA Marrakech

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlin.random.Random

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //declaration des variables
        val button: Button = findViewById(R.id.startBtn)
        val name: EditText = findViewById(R.id.NickName)
        val question = RandomOpp()

        button.setOnClickListener() {
            val input = name.text.toString()
            val activity2 = Intent(this, MainActivity2::class.java)
            val vibrateur = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            if (input.isNotEmpty()) {
                //sending nickname to activity 2
                activity2.putExtra("NicknameExtra", input)

                //sending question to activity 2
                activity2.putExtra("QuestionExtra", question)

                // starting activity 2
                startActivity(activity2)
            } else if (vibrateur.hasVibrator()) {
                vibrateur.vibrate(500)
                Toast.makeText(this, "Please give a name!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun RandomOpp(): String {
        val num1 = Random.nextInt(1,10)
        val num2 = Random.nextInt(1,10)
        val operators = arrayOf("+", "-", "x", "/")
        val operator = operators[Random.nextInt(operators.size)]
        return "$num1 $operator $num2"
    }


}
