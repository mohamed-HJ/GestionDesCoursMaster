package com.example.GestionDesCours

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var logout: ImageView? = null
    private var note: ImageView? = null
    private var absence: ImageView? = null
    private var home: ImageView? = null
    private var cours: ImageView? = null

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if (currentUser == null) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logout = findViewById(R.id.logout)
        note = findViewById(R.id.note)
        absence = findViewById(R.id.absence)
        home = findViewById(R.id.home)
        cours = findViewById(R.id.cours)

        note?.setOnClickListener {
            val intent = Intent(this@MainActivity, TabNavigation::class.java)
            startActivity(intent)
        }

        home?.setOnClickListener {
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        cours?.setOnClickListener {
            val intent = Intent(this@MainActivity, CoursActivity2::class.java)
            startActivity(intent)
        }

        absence?.setOnClickListener {
            // Intent intent = new Intent(MainActivity.this, NoteActivity.class);
        }

        logout?.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this@MainActivity)
            dialogBuilder.setMessage("Do you want to close this application?")
                .setCancelable(false)
                .setPositiveButton("Proceed") { _, _ ->
                    mAuth.signOut()
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

            val alert = dialogBuilder.create()
            alert.setTitle("Alert Logout")
            alert.show()
        }
    }
}
