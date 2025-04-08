package com.example.networkapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.io.File



class MainActivity : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue
    lateinit var titleTextView: TextView
    lateinit var descriptionTextView: TextView
    lateinit var numberEditText: EditText
    lateinit var showButton: Button
    lateinit var comicImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)

        titleTextView = findViewById<TextView>(R.id.comicTitleTextView)
        descriptionTextView = findViewById<TextView>(R.id.comicDescriptionTextView)
        numberEditText = findViewById<EditText>(R.id.comicNumberEditText)
        showButton = findViewById<Button>(R.id.showComicButton)
        comicImageView = findViewById<ImageView>(R.id.comicImageView)

        showButton.setOnClickListener {
            downloadComic(numberEditText.text.toString())
        }
        loadComic()
    }

    // Fetches comic from web as JSONObject
    private fun downloadComic (comicId: String) {
        val url = "https://xkcd.com/$comicId/info.0.json"
        requestQueue.add (
            JsonObjectRequest(url,
                { response ->
                    showComic(response)
                    saveComic(response)
                },
                {}
            )
        )
    }

    // Display a comic for a given comic JSON object
    private fun showComic (comicObject: JSONObject) {
        titleTextView.text = comicObject.getString("title")
        descriptionTextView.text = comicObject.getString("alt")
        Picasso.get().load(comicObject.getString("img")).into(comicImageView)
    }

    // Implement this function
    private fun saveComic(comicObject: JSONObject) {
        val filename = "cached_comic.json"
        val fileContents = comicObject.toString()

        openFileOutput(filename, MODE_PRIVATE).use { output ->
            output.write(fileContents.toByteArray())
        }
    }

    private fun loadComic() {
        val filename = "cached_comic.json"


        val file = File(filesDir, filename)
        if (!file.exists()) return

        val comic = JSONObject(file.readText())

        titleTextView.text = comic.getString("title")
        descriptionTextView.text = comic.getString("alt")
        Picasso.get().load(comic.getString("img")).into(comicImageView)


    }


}