package com.example.quotesrandomizer

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FavoritesActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var deleteButton: Button

    private val favoriteQuotes = mutableListOf<QuoteWithTimestamp>()

    data class QuoteWithTimestamp(val quote: String, val timestamp: Long)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        listView = findViewById(R.id.listView)
        deleteButton = findViewById(R.id.deleteButton)

        val sharedPreferences: SharedPreferences = getSharedPreferences("MyQuotes", MODE_PRIVATE)

        val quoteTypes = arrayOf("Motivational", "Funny", "Love")

        for (quoteType in quoteTypes) {
            val key = "$quoteType" + "_quote"
            val quote = sharedPreferences.getString(key, null)
            val timestamp = sharedPreferences.getLong(key + "_timestamp", 0)

            if (quote != null) {
                val formattedTimestamp = formatTimestamp(timestamp)
                val quoteWithTimestamp = "$quoteType Quote: $quote\nSaved on: $formattedTimestamp"
                favoriteQuotes.add(QuoteWithTimestamp(quoteWithTimestamp, timestamp))
            }
        }

        val adapter = ArrayAdapter(
            this,
            R.layout.list_item_with_timestamp, // Use the new layout with timestamps
            R.id.quoteTextView, // Use the correct TextView ID for the quote text
            favoriteQuotes.map { it.quote }
        )

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedQuote = favoriteQuotes[position]
            showDeleteConfirmationDialog(selectedQuote)
        }

        deleteButton.setOnClickListener {
            if (favoriteQuotes.isNotEmpty()) {
                val selectedQuote = favoriteQuotes[0] // Delete the first quote
                showDeleteConfirmationDialog(selectedQuote)
            } else {
                Toast.makeText(this, "No quotes available to delete.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeleteConfirmationDialog(quote: QuoteWithTimestamp) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Confirm Delete")
        alertDialog.setMessage("Are you sure you want to delete this quote?")
        alertDialog.setPositiveButton("Delete") { _, _ ->
            val sharedPreferences: SharedPreferences = getSharedPreferences("MyQuotes", MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            val quoteType = extractQuoteType(quote.quote)
            val key = "$quoteType" + "_quote"

            editor.remove(key)
            editor.remove(key + "_timestamp")
            editor.apply()

            favoriteQuotes.remove(quote)
            refreshListView()
            Toast.makeText(this, "Quote deleted.", Toast.LENGTH_SHORT).show()
        }
        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    private fun refreshListView() {
        val adapter = ArrayAdapter(
            this,
            R.layout.list_item_with_timestamp, // Use the new layout with timestamps
            R.id.quoteTextView, // Use the correct TextView ID for the quote text
            favoriteQuotes.map { it.quote }
        )
        listView.adapter = adapter
    }

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun extractQuoteType(quote: String): String {
        return when {
            quote.startsWith("Motivational Quote") -> "Motivational"
            quote.startsWith("Funny Quote") -> "Funny"
            quote.startsWith("Love Quote") -> "Love"
            else -> ""
        }
    }
}
