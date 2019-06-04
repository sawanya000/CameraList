package com.example.cameralist
import android.content.Intent
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    lateinit var username:String
    lateinit var password:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonLogin.setOnClickListener {
            username = editTextUsername.text.toString()
            password = editTextPassword.text.toString()
            //println(username+" "+password)
            if(username=="test" && password=="test"){
                val intent = Intent(this,Nextpage::class.java)
                startActivity(intent)
            }
        }


    }
}
