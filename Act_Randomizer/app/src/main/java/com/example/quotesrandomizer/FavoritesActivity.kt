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


        val sharedPreferences: SharedPreferences = getSharedPreferences("MyQuotes", MODE_PRIVATE)


        val quoteTypes = arrayOf("Motivational", "Funny", "Love")

        for (quoteType in quoteTypes) {
            val key = "$quoteType" + "_quote"
            val quote = sharedPreferences.getString(key, null)
            if (quote != null) {
                favoriteQuotes.add("$quoteType Quote: $quote")
            }
        }


        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, favoriteQuotes)


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

    private fun showDeleteConfirmationDialog(quote: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Confirm Delete")
        alertDialog.setMessage("Are you sure you want to delete this quote?")
        alertDialog.setPositiveButton("Delete") { _, _ ->

            val sharedPreferences: SharedPreferences = getSharedPreferences("MyQuotes", MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            if (quote.startsWith("Motivational Quote")) {
                editor.remove("Motivational_quote")
            } else if (quote.startsWith("Funny Quote")) {
                editor.remove("Funny_quote")
            } else if (quote.startsWith("Love Quote")) {
                editor.remove("Love_quote")
            }

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
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, favoriteQuotes)
        listView.adapter = adapter
    }
}
