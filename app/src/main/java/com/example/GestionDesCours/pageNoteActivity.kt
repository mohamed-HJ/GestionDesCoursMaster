package com.example.GestionDesCours

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PageNoteActivity : AppCompatActivity() {
    private var titleTextView: TextView? = null
    private var noteTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_note)

        titleTextView = findViewById(R.id.title_text_view)
        noteTextView = findViewById(R.id.note_text_view)

        val title = intent.extras?.getString("Title_key")
        val note = intent.extras?.getString("Note_key")

        titleTextView?.text = title
        noteTextView?.text = note
    }
}
