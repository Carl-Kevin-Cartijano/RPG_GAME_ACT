package com.example.quotesrandomizer

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FavoritesActivity : AppCompatActivity() {
    private var favoritesTextView: TextView? = null
    private var removeButton: Button? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        favoritesTextView = findViewById(R.id.favoritesTextView)
        removeButton = findViewById(R.id.removeButton)
        sharedPreferences = getSharedPreferences("MyQuotes", MODE_PRIVATE)
        displaySavedQuotes()

        removeButton?.setOnClickListener {
            // Call removeQuote with the desired quote type
            removeQuote("Motivational") // Change "Motivational" to the desired quote type
        }
    }

    private fun removeQuote(quoteTypeToRemove: String) {
        // Construct the key for the quote based on the quote type
        val keyToRemove = "${quoteTypeToRemove}_quote"

        // Remove the quote from SharedPreferences using the constructed key
        sharedPreferences?.edit()?.remove(keyToRemove)?.apply()

        // Update the displayed quotes
        displaySavedQuotes()
    }

    private fun displaySavedQuotes() {
        // Retrieve all saved quotes from SharedPreferences and display them
        val allEntries = sharedPreferences?.all
        val quotesBuilder = StringBuilder()

        allEntries?.forEach { (key, value) ->
            val quoteType = key.split("_")[0]
            val quote = value.toString()
            quotesBuilder.append("$quoteType: $quote\n")
        }

        favoritesTextView?.text = quotesBuilder.toString()
    }
}
