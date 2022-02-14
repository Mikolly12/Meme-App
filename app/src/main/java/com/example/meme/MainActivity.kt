package com.example.meme

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadmeme()
    }
var currentMeme: String ?= null

    fun loadmeme(){
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"
        next.isEnabled = false
        share.isEnabled =false
        progressB.visibility = View.VISIBLE
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                currentMeme = response.getString("url")
                Glide.with(this).load(currentMeme).listener(object: RequestListener<Drawable>{
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressB.visibility = View.GONE
                        next.isEnabled =true
                        share.isEnabled = true
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressB.visibility = View.GONE
                        return false
                    }
                }).into(imageView)            },
            Response.ErrorListener { error ->
                // TODO: Handle error
                progressB.visibility = View.GONE
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
        )

// Access the RequestQueue through your singleton class.
        //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        queue.add(jsonObjectRequest)
    }
    fun share(view: android.view.View) {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra(Intent.EXTRA_TEXT,"Hi, checkout this meme $currentMeme")
        startActivity(Intent.createChooser(i,"Share this meme with"))
    }
    fun next(view: android.view.View) {
        loadmeme()
    }
}