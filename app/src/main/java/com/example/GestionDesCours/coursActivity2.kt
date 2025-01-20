package com.example.GestionDesCours

import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class CoursActivity2 : AppCompatActivity() {
    private var addPdf: CardView? = null
    private var pdfTitle: EditText? = null
    private var uploadBtn: Button? = null
    private var reference: DatabaseReference? = null
    private var storageReference: StorageReference? = null
    private var downloadUr1: String = ""
    private var pd: ProgressDialog? = null
    private val REQ = 1
    private var list: ListView? = null
    private var pdfData: Uri? = null
    private var downloadBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cours2)

        reference = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference
        pd = ProgressDialog(this)

        addPdf = findViewById(R.id.addpdf)
        pdfTitle = findViewById(R.id.pdftitle)
        uploadBtn = findViewById(R.id.uploadbtn)
        list = findViewById(R.id.list)

        val mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        if (user?.email == "admin@admin.ma") {
            pdfTitle?.visibility = View.VISIBLE
            uploadBtn?.visibility = View.VISIBLE
            addPdf?.visibility = View.VISIBLE
        }

        val ref = FirebaseDatabase.getInstance().getReference("PDFs")
        val newsListLink = ArrayList<String>()
        val newsList = ArrayList<String>()

        list?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val link = newsListLink[position]
            val title = newsList[position]
            downloadPDF(link, title)
        }

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                newsList.clear()
                newsListLink.clear()
                for (data in snapshot.children) {
                    for (dataSnapshot in data.children) {
                        when (dataSnapshot.key) {
                            "title" -> newsList.add(dataSnapshot.value.toString())
                            "pdfUrl" -> newsListLink.add(dataSnapshot.value.toString())
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CoursActivity2, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        uploadBtn?.setOnClickListener {
            if (pdfData != null) {
                val id = UUID.randomUUID().toString()
                val fileExtension = getFileExtension(pdfData)
                if (fileExtension != null) {
                    storageReference?.child("$id.$fileExtension")?.putFile(pdfData!!)
                        ?.addOnSuccessListener { taskSnapshot ->
                            Toast.makeText(this@CoursActivity2, "File is added", Toast.LENGTH_SHORT)
                                .show()
                            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                                val downloadUrl = uri.toString()
                                val databaseReference = FirebaseDatabase.getInstance().getReference("PDFs")
                                databaseReference.child(id).child("title").setValue(pdfTitle?.text.toString())
                                databaseReference.child(id).child("pdfUrl").setValue(downloadUrl)
                            }
                        }?.addOnFailureListener { e ->
                            Toast.makeText(this@CoursActivity2, e.toString(), Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this@CoursActivity2, "Please select file", Toast.LENGTH_SHORT).show()
            }
        }

        addPdf?.setOnClickListener { openGallery() }
    }

    private fun downloadPDF(pdfUrl: String, title: String) {
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(pdfUrl))

        request.setTitle(title)
        request.setDescription("Downloading PDF file")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "pdfUrl")

        downloadManager.enqueue(request)
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ && resultCode == RESULT_OK) {
            pdfData = data?.data
        }
    }

    private fun getFileExtension(uri: Uri?): String? {
        val cr = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri!!))
    }
}
