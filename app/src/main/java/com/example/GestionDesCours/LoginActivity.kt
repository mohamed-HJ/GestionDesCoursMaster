package com.example.GestionDesCours

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val seconnecter = findViewById<Button>(R.id.se_connecter)
        val inscrireVous = findViewById<TextView>(R.id.inscrire_vous)
        val edtEmailLogin = findViewById<EditText>(R.id.edt_emailLogin)
        val edtPasswordLogin = findViewById<EditText>(R.id.edt_passwordLogin)

        val mAuth = FirebaseAuth.getInstance()
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        seconnecter.setOnClickListener {
            val email = edtEmailLogin.text.toString()
            val password = edtPasswordLogin.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edtEmailLogin.error = "Invalid Email"
                    progressBar.visibility = View.GONE
                } else if (password.length < 4) {
                    edtPasswordLogin.error = "Invalid Password"
                    progressBar.visibility = View.GONE
                } else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            progressBar.visibility = View.GONE
                            val intentToMain = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intentToMain)
                            finish()
                        } else {
                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                this@LoginActivity,
                                "There is no account with this email",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            } else {
                if (email.isEmpty()) {
                    edtEmailLogin.error = "Please enter your Email"
                }
                if (password.isEmpty()) {
                    edtPasswordLogin.error = "Please enter your Password"
                }
            }
        }

        inscrireVous.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
