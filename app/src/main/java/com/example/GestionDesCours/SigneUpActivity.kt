package com.example.GestionDesCours

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var edtNomUser: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtPasswordConfirmation: EditText
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signe_up)

        mAuth = FirebaseAuth.getInstance()
        val inscrire = findViewById<Button>(R.id.inscrire)
        edtNomUser = findViewById(R.id.edt_nom_user)
        edtEmail = findViewById(R.id.edt_emailLogin)
        edtPassword = findViewById(R.id.edt_passwordLogin)
        edtPasswordConfirmation = findViewById(R.id.edt_passwordLoginconfirmation)
        progressBar = findViewById(R.id.progressBar)

        inscrire.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val passwordConfirmation = edtPasswordConfirmation.text.toString()
            val name = edtNomUser.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && passwordConfirmation.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE

                when {
                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        edtEmail.error = "Use email like L13*******@usms.ac.ma"
                        progressBar.visibility = View.GONE
                    }
                    name.length > 20 -> {
                        edtNomUser.error = "Invalid Name"
                        progressBar.visibility = View.GONE
                    }
                    password.length < 4 -> {
                        edtPassword.error = "Invalid Password"
                        progressBar.visibility = View.GONE
                    }
                    password != passwordConfirmation -> {
                        edtPasswordConfirmation.error = "Passwords do not match"
                        progressBar.visibility = View.GONE
                    }
                    else -> {
                        mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_LONG).show()
                                    progressBar.visibility = View.GONE

                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                                    progressBar.visibility = View.GONE
                                }
                            }
                    }
                }
            } else {
                if (name.isEmpty()) edtNomUser.error = "Please enter your Full Name"
                if (email.isEmpty()) edtEmail.error = "Please enter your Email"
                if (password.isEmpty()) edtPassword.error = "Please enter your Password"
                if (passwordConfirmation.isEmpty()) edtPasswordConfirmation.error = "Please confirm your Password"
            }
        }
    }

    data class PDFs(var title: String, var url: String, var id: String? = null)
}
