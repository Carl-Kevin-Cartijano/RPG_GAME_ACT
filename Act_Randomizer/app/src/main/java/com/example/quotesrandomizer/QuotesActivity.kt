package com.example.quotesrandomizer

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuotesActivity : AppCompatActivity() {
    private var quoteTextView: TextView? = null
    private var saveButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes)
        quoteTextView = findViewById<TextView>(R.id.quoteTextView)
        saveButton = findViewById<Button>(R.id.saveButton)
        val quoteType = intent.getStringExtra("quoteType")

        // Implement logic to fetch and display quotes of the selected type
        @Suppress("ImplicitThis")
        saveButton.setOnClickListener(View.OnClickListener {
            saveQuoteToSharedPreferences(
                quoteType,
                with(quoteTextView) { this?.getText().toString() }
            )
        })
    }

    private fun saveQuoteToSharedPreferences(quoteType: String?, quote: String) {
        val sharedPreferences = getSharedPreferences("MyQuotes", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val key = quoteType + "_quote"
        editor.putString(key, quote)
        editor.apply()
    }
}
