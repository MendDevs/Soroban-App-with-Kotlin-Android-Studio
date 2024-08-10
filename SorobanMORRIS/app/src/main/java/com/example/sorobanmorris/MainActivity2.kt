package com.example.sorobanmorris

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity2 : AppCompatActivity() {

    private var correctAnswers = 0
    private var currentLevel = 1
    private var currentQuestion = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val maxScoreText: TextView = findViewById(R.id.MaxScoreTxt)
        val oldScoreText: TextView = findViewById(R.id.OldScoreTxt)
        val newScoreText: TextView = findViewById(R.id.NewScoreTxt)
        val nicknameText: TextView = findViewById(R.id.playerNickNameTxt)
        val questionText: TextView = findViewById(R.id.QuestionTxt)
        val answerText: TextView = findViewById(R.id.AnswerTxt)
        val validateButton: Button = findViewById(R.id.ValidateTxt)
        val acButton: TextView = findViewById(R.id.AC)

        // Retrieve nickname and question from intent
        val nickname = intent.getStringExtra("NicknameExtra")
        nicknameText.text = nickname ?: "Player"

        currentQuestion = intent.getStringExtra("QuestionExtra") ?: ""
        questionText.text = currentQuestion

        val BDD = getSharedPreferences("DATABASE", Context.MODE_PRIVATE)
        val maxScore = BDD.getInt("MaxScore", 0)
        val oldScore = BDD.getInt("${nickname}_OldScore", 0)

        maxScoreText.text = maxScore.toString()
        oldScoreText.text = oldScore.toString()
        newScoreText.text = "0"

        val buttons = listOf<Button>(
            findViewById(R.id.zero), findViewById(R.id.one), findViewById(R.id.two),
            findViewById(R.id.three), findViewById(R.id.four), findViewById(R.id.five),
            findViewById(R.id.six), findViewById(R.id.seven), findViewById(R.id.eight),
            findViewById(R.id.nine), findViewById(R.id.minus)
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                answerText.text = "${answerText.text}${button.text}"
            }
        }

        acButton.setOnClickListener {
            answerText.text = ""
        }

        validateButton.setOnClickListener {
            validateAnswer(answerText, nickname, newScoreText, questionText)
        }
    }

    private fun validateAnswer(
        answerText: TextView,
        nickname: String?,
        newScoreText: TextView,
        questionText: TextView
    ) {
        val answer = answerText.text.toString()
        val correctAnswer = evaluateExpression(currentQuestion).toInt()

        if (answer == correctAnswer.toString()) {
            correctAnswers++
            val newScore = newScoreText.text.toString().toInt() + currentLevel
            newScoreText.text = newScore.toString()

            if (correctAnswers >= 5) {
                currentLevel++
                correctAnswers = 0
            }
            currentQuestion = generateNewQuestion()
            questionText.text = currentQuestion
        } else {
            Toast.makeText(
                this,
                "Incorrect! The correct answer is $correctAnswer",
                Toast.LENGTH_SHORT
            ).show()
        }
        answerText.text = ""

        val BDD = getSharedPreferences("DATABASE", Context.MODE_PRIVATE)
        val editor = BDD.edit()
        editor.putInt(
            "MaxScore",
            newScoreText.text.toString().toInt().coerceAtLeast(BDD.getInt("MaxScore", 0))
        )
        editor.putInt("${nickname}_OldScore", newScoreText.text.toString().toInt())
        editor.apply()
    }

    private fun generateNewQuestion(): String {
        val num1: Int
        val num2: Int
        val operators = arrayOf("+", "-", "x", "/")
        val operator: String

        when (currentLevel) {
            1 -> {
                num1 = Random.nextInt(1, 10)
                num2 = Random.nextInt(1, 10)
                operator = operators[Random.nextInt(operators.size)]
            }

            2 -> {
                num1 = Random.nextInt(10, 20)
                num2 = Random.nextInt(1, 10)
                operator = operators[Random.nextInt(operators.size)]
            }

            3 -> {
                num1 = Random.nextInt(10, 20)
                num2 = Random.nextInt(10, 20)
                operator = operators[Random.nextInt(operators.size)]
            }

            4 -> {
                num1 = Random.nextInt(100, 1000)
                num2 = Random.nextInt(100, 1000)
                operator = operators[Random.nextInt(operators.size)]
            }

            5 -> {
                num1 = Random.nextInt(1000, 10000)
                num2 = Random.nextInt(1000, 10000)
                operator = operators[Random.nextInt(operators.size)]
            }

            else -> {
                num1 = Random.nextInt(10000, 100000)
                num2 = Random.nextInt(10000, 100000)
                operator = operators[Random.nextInt(operators.size)]
            }
        }
        return "$num1 $operator $num2"
    }

    private fun evaluateExpression(expression: String): Double {
        val parts = expression.split(" ")
        val num1 = parts[0].toDouble()
        val operator = parts[1]
        val num2 = parts[2].toDouble()

        return when (operator) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "x" -> num1 * num2
            "/" -> num1 / num2
            else -> 0.0
        }
    }

    /*
    // Ajouter l'icône de déconnexion à la barre d'action
    supportActionBar?.apply {
        setDisplayHomeAsUpEnabled(true) // Afficher le bouton de retour
        setHomeAsUpIndicator(R.drawable.logout_icon) // Définir l'icône de déconnexion
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Afficher la boîte de dialogue de confirmation de déconnexion
                showLogoutConfirmationDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    */
}
