package com.example.quotesrandomizer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var motivationalButton: Button
    private lateinit var funnyButton: Button
    private lateinit var loveButton: Button
    private lateinit var favoritesButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        motivationalButton = findViewById(R.id.motivationalButton)
        funnyButton = findViewById(R.id.funnyButton)
        loveButton = findViewById(R.id.loveButton)
        favoritesButton = findViewById(R.id.favoritesButton)

        motivationalButton.setOnClickListener {
            startQuotesActivity("Motivational")
        }
        funnyButton.setOnClickListener {
            startQuotesActivity("Funny")
        }
        loveButton.setOnClickListener {
            startQuotesActivity("Love")
        }
        favoritesButton.setOnClickListener {
            startFavoritesActivity()
        }
    }

    private fun startQuotesActivity(quoteType: String) {
        val intent = Intent(this, QuotesActivity::class.java)
        intent.putExtra("quoteType", quoteType)
        startActivity(intent)
    }

    private fun startFavoritesActivity() {
        val intent = Intent(this, FavoritesActivity::class.java)
        startActivity(intent)
    }
}
