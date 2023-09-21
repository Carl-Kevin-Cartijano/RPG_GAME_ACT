package com.example.quotesrandomizer

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class FavoritesActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var deleteButton: Button

    private val favoriteQuotes = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        listView = findViewById(R.id.listView)
        deleteButton = findViewById(R.id.deleteButton)

        // Retrieve saved quotes from SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyQuotes", MODE_PRIVATE)

        // Replace "Motivational", "Funny", and "Love" with the quote types you have
        val quoteTypes = arrayOf("Motivational", "Funny", "Love")

        for (quoteType in quoteTypes) {
            val key = "$quoteType" + "_quote"
            val quote = sharedPreferences.getString(key, null)
            if (quote != null) {
                favoriteQuotes.add("$quoteType Quote: $quote")
            }
        }

        // Create an ArrayAdapter to display the saved quotes in a ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, favoriteQuotes)

        // Set the adapter for the ListView
        listView.adapter = adapter

        // Set click listeners for the ListView items
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedQuote = favoriteQuotes[position]
            showDeleteConfirmationDialog(selectedQuote)
        }

        deleteButton.setOnClickListener {
            // Check if a quote is selected for deletion
            if (favoriteQuotes.isNotEmpty()) {
                val selectedQuote = favoriteQuotes[0] // Delete the first quote
                showDeleteConfirmationDialog(selectedQuote)
            } else {
                Toast.makeText(this, "No quotes available to delete.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeleteConfirmationDialog(quote: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Confirm Delete")
        alertDialog.setMessage("Are you sure you want to delete this quote?")
        alertDialog.setPositiveButton("Delete") { _, _ ->
            // Delete the selected quote from SharedPreferences
            val sharedPreferences: SharedPreferences = getSharedPreferences("MyQuotes", MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            // Determine the quote type from the selectedQuote and remove it
            if (quote.startsWith("Motivational Quote")) {
                editor.remove("Motivational_quote")
            } else if (quote.startsWith("Funny Quote")) {
                editor.remove("Funny_quote")
            } else if (quote.startsWith("Love Quote")) {
                editor.remove("Love_quote")
            }

            editor.apply()

            // Remove the deleted quote from the list and refresh the ListView
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
        // Refresh the ListView with the updated quote list after deletion
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, favoriteQuotes)
        listView.adapter = adapter
    }
}
