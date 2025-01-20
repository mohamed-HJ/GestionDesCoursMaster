package com.example.GestionDesCours

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class NoteAdapter(context: Context, notelist: ArrayList<Note>) : ArrayAdapter<Note>(
    context, 0, notelist
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.note_layout, parent, false)
        val note = getItem(position)
        val titleTextView = view.findViewById<TextView>(R.id.titileTextView)
        val timeTextView = view.findViewById<TextView>(R.id.timeTextView)

        note?.let {
            titleTextView.text = it.title
            timeTextView.text = it.timestamp
        }

        return view
    }
}
