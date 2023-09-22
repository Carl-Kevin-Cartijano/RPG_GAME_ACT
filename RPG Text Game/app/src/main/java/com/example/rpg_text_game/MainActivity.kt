package com.example.rpg_text_game

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Random

class MainActivity : AppCompatActivity() {
    private lateinit var playerHealthTextView: TextView
    private lateinit var enemyHealthTextView: TextView
    private lateinit var playerImageView: ImageView
    private lateinit var enemyImageView: ImageView
    private lateinit var attackButton: Button
    private lateinit var defendButton: Button
    private lateinit var healButton: Button
    private lateinit var resetButton: Button
    private lateinit var gameStatusTextView: TextView
    private var playerHealth = 100
    private var enemyHealth = 100
    private var isPlayerDefending = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerHealthTextView = findViewById(R.id.playerHealth)
        enemyHealthTextView = findViewById(R.id.enemyHealth)
        playerImageView = findViewById(R.id.playerImageView)
        enemyImageView = findViewById(R.id.enemyImageView)
        attackButton = findViewById(R.id.attackButton)
        defendButton = findViewById(R.id.defendButton)
        healButton = findViewById(R.id.healButton)
        resetButton = findViewById(R.id.resetButton)
        gameStatusTextView = findViewById(R.id.gameStatus)

        updateHealthViews()

        attackButton.setOnClickListener {
            playerAttack()
            enemyTurn()
        }

        defendButton.setOnClickListener {
            playerDefend()
            enemyTurn()
        }

        healButton.setOnClickListener {
            playerHeal()
            enemyTurn()
        }

        resetButton.setOnClickListener {
            resetGame()
        }
    }

    private fun rollDice(sides: Int): Int {
        return Random().nextInt(sides) + 1
    }

    private fun playerAttack() {
        val damageDealt = rollDice(20) + 10 // Roll a 20-sided dice and add 10 for damage
        enemyHealth -= damageDealt
        if (enemyHealth < 0) {
            enemyHealth = 0
        }
        updateHealthViews()
        checkGameStatus()
        displayPlayerAction("You attack for $damageDealt damage.")
    }

    private fun playerDefend() {
        isPlayerDefending = true
        displayPlayerAction("You defend against the enemy's attack.")
    }

    private fun playerHeal() {
        if (playerHealth < 100) { // Limit healing to not exceed maximum health.
            val healingAmount = rollDice(15) + 10 // Roll a 15-sided dice and add 10 for healing
            playerHealth += healingAmount
            displayPlayerAction("You heal for $healingAmount health.")
        }
    }

    private fun enemyTurn() {
        val action = rollDice(3) // Roll a 3-sided dice (0: Attack, 1: Defend, 2: Heal)
        if (action == 0) {
            val damageDealt = if (isPlayerDefending) {
                rollDice(10) + 5 // Reduced damage if player is defending
            } else {
                rollDice(15) + 10 // Roll a 15-sided dice and add 10 for damage
            }
            playerHealth -= damageDealt
            if (playerHealth < 0) {
                playerHealth = 0
            }
            isPlayerDefending = false // Reset defense state after enemy's turn
            updateHealthViews()
            checkGameStatus()
            displayEnemyAction("Enemy attacks for $damageDealt damage.")
        } else if (action == 1) {
            displayEnemyAction("Enemy defends.")
        } else {
            val healing = rollDice(15) + 5 // Roll a 15-sided dice and add 5 for healing
            enemyHealth += healing
            updateHealthViews()
            displayEnemyAction("Enemy heals for $healing health.")
        }
    }

    private fun updateHealthViews() {
        playerHealthTextView.text = "Player Health: $playerHealth"
        enemyHealthTextView.text = "Enemy Health: $enemyHealth"
    }

    private fun checkGameStatus() {
        if (playerHealth <= 0) {
            gameStatusTextView.text = "Game over. You lose."
            disableButtons()
        } else if (enemyHealth <= 0) {
            gameStatusTextView.text = "Congratulations! You win."
            disableButtons()
        }
    }

    private fun disableButtons() {
        attackButton.isEnabled = false
        defendButton.isEnabled = false
        healButton.isEnabled = false
    }

    private fun displayEnemyAction(action: String) {
        gameStatusTextView.text = action
    }

    private fun displayPlayerAction(action: String) {
        gameStatusTextView.text = action
    }

    private fun resetGame() {
        playerHealth = 100
        enemyHealth = 100
        updateHealthViews()
        enableButtons()
        gameStatusTextView.text = ""
    }

    private fun enableButtons() {
        attackButton.isEnabled = true
        defendButton.isEnabled = true
        healButton.isEnabled = true
    }
}
