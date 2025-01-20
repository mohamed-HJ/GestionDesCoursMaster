package com.example.GestionDesCours

import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.AdapterView.OnItemClickListener
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

class TDTP : AppCompatActivity() {
    private var addpdf: CardView? = null
    private var pdftitle: EditText? = null
    private var uploadbtn: Button? = null
    private var reference: DatabaseReference? = null
    private var storageReference: StorageReference? = null
    var downloadUr1: String = ""
    private var pd: ProgressDialog? = null
    private val REQ = 1
    var list: ListView? = null
    private var pdfData: Uri? = null
    var Downloadbtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cours2)
        reference = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference

        pd = ProgressDialog(this)

        addpdf = findViewById(R.id.addpdf)
        pdftitle = findViewById(R.id.pdftitle)
        uploadbtn = findViewById(R.id.uploadbtn)



        list = findViewById(R.id.list)


        val mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        if (user!!.email.toString() == "admin@admin.ma") {
            pdftitle.setVisibility(View.VISIBLE)
            uploadbtn.setVisibility(View.VISIBLE)
            addpdf.setVisibility(View.VISIBLE)
        }


        val ref = FirebaseDatabase.getInstance().getReference("PDFs")

        val newsListLink = ArrayList<String>()

        val newsList = ArrayList<String>()
        val newsAdapter = ArrayAdapter(this, R.layout.list_ithem, newsList)
        list.setAdapter(newsAdapter)


        list.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            val link = newsListLink[position]
            val title = newsList[position]
            downloadPDF(link, title)
            // Open the link in a web browser or other appropriate action
        })



        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                newsList.clear()
                newsListLink.clear()
                for (data in snapshot.children) {
                    for (dataSnapshot in data.children) {
                        if (dataSnapshot.key == "title") {
                            newsList.add(dataSnapshot.value.toString())
                        }
                        if (dataSnapshot.key == "pdfUrl") {
                            newsListLink.add(dataSnapshot.value.toString())
                        }
                    }
                }
                newsAdapter.notifyDataSetChanged()
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@TDTP, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })












        uploadbtn.setOnClickListener(View.OnClickListener {
            if (pdfData != null) {
                val id = UUID.randomUUID().toString()
                storageReference!!.child(id + "." + getFileExtension(pdfData)).putFile(pdfData!!)
                    .addOnSuccessListener { taskSnapshot ->
                        Toast.makeText(this@TDTP, "File is added", Toast.LENGTH_SHORT).show()
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                            val downloadUrl = uri.toString()
                            val databaseReference =
                                FirebaseDatabase.getInstance().getReference("PDFs")
                            databaseReference.child(id).child("title")
                                .setValue(pdftitle.getText().toString())
                            databaseReference.child(id).child("pdfUrl").setValue(downloadUrl)
                        }
                    }.addOnFailureListener { e ->
                    Toast.makeText(
                        this@TDTP,
                        e.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this@TDTP, "Please select file", Toast.LENGTH_SHORT).show()
            }
        })















        addpdf.setOnClickListener(View.OnClickListener { view: View? -> openGallery() })
    }


    private fun downloadPDF(pdfUrl: String, title: String) {
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(pdfUrl))

        // Customize download settings:
        request.setTitle(title)
        request.setDescription("Downloading PDF file")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "pdfUrl")


        downloadManager.enqueue(request)
    }


    private fun openGallery() {
        val intent = Intent()
        intent.setType("application/pdf")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), REQ)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ && resultCode == RESULT_OK) {
            pdfData = data!!.data
        }
    }

    fun getFileExtension(uri: Uri?): String? {
        val cr = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri!!))
    }
}