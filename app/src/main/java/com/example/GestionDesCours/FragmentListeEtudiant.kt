package com.example.GestionDesCours

import android.app.AlertDialog

import android.view.View

import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class FragmentListeEtudiant : Fragment() {

    private var mNoteList: ArrayList<Note> = ArrayList()

    private var addNewNote: FloatingActionButton? = null
    private var alertBuilder: AlertDialog.Builder? = null
    private var alertBuilderr: AlertDialog.Builder? = null
    private var alertDialog: AlertDialog? = null
    private var alertDialogg: AlertDialog? = null
    private var view: View? = null
    private var database: FirebaseDatabase? = null
    private var mRef: DatabaseReference? = null
    private var btnSaveNote: Button? = null
    private var titleEditText: EditText? = null
    private var noteEditText: EditText? = null
    private var title: String? = null
    private var note: String? = null
    private var myNote: Note? = null
    private var id: String? = null
    private var noteListView: ListView? = null
    private var titleTextView: TextView? = null

}