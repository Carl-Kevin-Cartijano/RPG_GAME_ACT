package com.example.quotesrandomizer

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class QuotesActivity : AppCompatActivity() {
    private lateinit var quoteTextView: TextView
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes)

        quoteTextView = findViewById(R.id.quoteTextView)
        saveButton = findViewById(R.id.saveButton)

        val quoteType = intent.getStringExtra("quoteType")


        val quotes = when (quoteType) {
            "Motivational" -> arrayOf(  "When you have a dream, you've got to grab it and never let go.",
                                        "Nothing is impossible. The word itself says 'I'm possible!'",
                                        "There is nothing impossible to they who will try.",
                                        "The bad news is time flies. The good news is you're the pilot.",
                                        "Life has got all those twists and turns. You've got to hold on tight and off you go.")
            "Funny" -> arrayOf( "There is no sunrise so beautiful that it is worth waking me up to see it.",
                                "Life is short. Drive fast and leave a sexy corpse. That's one of my mottos.",
                                "I always cook with wine. Sometimes I even add it to the food.",
                                "People say money is not the key to happiness, but I have always figured if you have enough money, you can have a key made.",
                                "Do not take life too seriously. You will never get out of it alive.")
            "Love" -> arrayOf(  "Thinking of you keeps me awake. Dreaming of you keeps me asleep. Being with you keeps me alive.",
                                "What we have once enjoyed we can never lose. All that we love deeply becomes a part of us.",
                                "Of all forms of caution, caution in love is perhaps the most fatal to true happiness.",
                                "Loved you yesterday, love you still, always have, always will.",
                                "To love well is the task in all meaningful relationships, not just romantic bonds.")
            else -> arrayOf()
        }


        val randomIndex = Random.nextInt(quotes.size)
        val randomQuote = quotes[randomIndex]


        quoteTextView.text = randomQuote

        saveButton.setOnClickListener {
            val quote = randomQuote
            if (quote.isNotEmpty()) {
                saveQuoteToSharedPreferences(quoteType, quote)
                Toast.makeText(this, "Quote Saved.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveQuoteToSharedPreferences(quoteType: String?, quote: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyQuotes", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val key = "$quoteType" + "_quote"
        editor.putString(key, quote)
        editor.apply()

    }
}
