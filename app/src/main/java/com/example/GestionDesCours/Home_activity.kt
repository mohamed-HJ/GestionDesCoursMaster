package com.example.GestionDesCours

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID

class HomeActivity : AppCompatActivity() {
    private var editTextNews: EditText? = null
    private var listViewNews: ListView? = null
    private var addButton: Button? = null
    private var myRef: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        editTextNews = findViewById(R.id.editTextNews)
        listViewNews = findViewById(R.id.listViewNews)
        addButton = findViewById(R.id.addButton)

        val mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        if (user?.email == "admin@admin.ma") {
            editTextNews?.visibility = View.VISIBLE
            addButton?.visibility = View.VISIBLE
        }

        // Récupérer les références des vues
        myRef = FirebaseDatabase.getInstance().getReference("Remarques")

        // Gérer le clic sur le bouton
        addButton?.setOnClickListener {
            // Ajouter le texte saisi à la liste des actualités
            val newsText = editTextNews?.text.toString()
            val randomId = UUID.randomUUID()
            val idString = randomId.toString()
            myRef?.child(idString)?.setValue(newsText)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    envoyerNotification(newsText)
                    Toast.makeText(this@HomeActivity, "successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@HomeActivity, "unsuccessful", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val newsList = ArrayList<String>()

        myRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                newsList.clear()
                for (data in snapshot.children) {
                    newsList.add(data.value.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun envoyerNotification(message: String) {
        // Obtenez le FCM token de l'utilisateur depuis votre backend ou depuis FirebaseInstanceId si vous l'avez stocké localement

        // Créez un objet de notification
        val builder = NotificationCompat.Builder(this, "channel_id")
            .setSmallIcon(R.drawable.icon_notes)
            .setContentTitle("Nouveau Message")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Envoyez la notification en utilisant le NotificationManager
        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WAKE_LOCK
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(1, builder.build())
    }
}
