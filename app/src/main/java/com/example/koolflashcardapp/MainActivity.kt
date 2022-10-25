package com.example.koolflashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var flashcardDatabase: FlashcardDatabase
    var allFlashcards = mutableListOf<Flashcard>()
    //variable to help keep track of which card we are currently looking at
    var currentCardDisplayedIndex = 0

    //create a variable to keep track of the index of the current card we are showing
    var currentCardDisplayIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Define class variables
        flashcardDatabase = FlashcardDatabase(this)
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)

        if (allFlashcards.size > 0) {
            flashcardQuestion.text = allFlashcards[0].question
            flashcardAnswer.text = allFlashcards[0].answer
        }

        flashcardQuestion.setOnClickListener {
            flashcardAnswer.visibility = View.VISIBLE
            flashcardQuestion.visibility = View.INVISIBLE
            // make a Snackbar for touch feedback
            Snackbar.make(flashcardQuestion, "Question button was clicked",
                Snackbar.LENGTH_SHORT).show()

            val answerSideView = findViewById<View>(R.id.flashcard_answer)

// get the center for the clipping circle

// get the center for the clipping circle
            val cx = answerSideView.width / 2
            val cy = answerSideView.height / 2

// get the final radius for the clipping circle

// get the final radius for the clipping circle
            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

// create the animator for this view (the start radius is zero)

// create the animator for this view (the start radius is zero)
            val anim = ViewAnimationUtils.createCircularReveal(answerSideView, cx, cy, 0f, finalRadius)

// hide the question and show the answer to prepare for playing the animation!

// hide the question and show the answer to prepare for playing the animation! Testing
           // val questionSideView = null
            //questionSideView.visibility = View.INVISIBLE
            //answerSideView.visibility = View.VISIBLE

            anim.duration = 3000
            anim.start()
        }

        flashcardAnswer.setOnClickListener {
            flashcardAnswer.visibility = View.INVISIBLE
            flashcardQuestion.visibility = View.VISIBLE
        }
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
// block of code determines which data to display once main act returns to screen

            val data: Intent? = result.data
            if (data != null) {
                val questionString = data.getStringExtra("QUESTION_KEY")
                val answerString = data.getStringExtra("ANSWER_KEY")

                //displays newly created flashcard
                flashcardQuestion.text = questionString
                flashcardAnswer.text = answerString

                Log.i("Clark: MainActivity", "question: $questionString")
                Log.i("Clark: MainActivity", "answer: $answerString")

                if (!questionString.isNullOrEmpty() && !answerString.isNullOrEmpty()) {
                    flashcardDatabase.insertCard(Flashcard(questionString, answerString))
                    allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                }

            } else {
                Log.i("Clark: MainActivity", "Returned null data from AddCardActivity")
            }

        }
//
        val addQuestionButton = findViewById<ImageView>(R.id.add_question_button)
        addQuestionButton.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
            overridePendingTransition(R.anim.right_in, R.anim.left_out)
        }

        val leftOutAnim = AnimationUtils.loadAnimation(this, R.anim.left_out)
        val rightInAnim = AnimationUtils.loadAnimation(this, R.anim.right_in)


        val nextButton = findViewById<ImageView>(R.id.flashcard_next_button)
        nextButton.setOnClickListener {
            if(allFlashcards.isEmpty()){
                //early return so that the rest of the code doesn't execute - edge-case logic
                return@setOnClickListener
            }

            currentCardDisplayedIndex++

            if(currentCardDisplayedIndex >= allFlashcards.size){
                //return to the start
                currentCardDisplayedIndex = 0
            }

            allFlashcards = flashcardDatabase.getAllCards().toMutableList() // keep database data current

            val question = allFlashcards[currentCardDisplayedIndex].question
            val answer = allFlashcards[currentCardDisplayedIndex].answer
//
            leftOutAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // this method is called when the animation first starts
                    findViewById<View>(R.id.flashcard_question).startAnimation(leftOutAnim)
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // this method is called when the animation is finished playing
                    findViewById<View>(R.id.flashcard_question).startAnimation(rightInAnim)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // we don't need to worry about this method
                }
            })
            findViewById<View>(R.id.flashcard_question).startAnimation(leftOutAnim)

            flashcardQuestion.text = question
            flashcardAnswer.text = answer
        }

    }

}