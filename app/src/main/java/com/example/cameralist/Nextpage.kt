package com.example.cameralist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build

import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_nextpage.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Nextpage : AppCompatActivity() {

    lateinit var myDb: Databate

    var list =ArrayList<String>()
    var time = ArrayList<String>()

    private val IMAGE_CAPTURE_CODE= 1001;
    private val PERMISSION_CODE = 1000;
    var image_uri:Uri? = null
    var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nextpage)

        myDb = Databate(this)
        showData()
        recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        var myAdapter = imageAdapter(list,time)
        recyclerView!!.setAdapter(myAdapter)

        buttonCamera.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED    ){

                    val permission = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

                    requestPermissions(permission,PERMISSION_CODE)

                }
                else{
                    openCamera()

                }
            }
            else{
                openCamera()

            }
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    openCamera()
                }
                else{
                    Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show()

                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){

            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
//            System.out.println(" C DATE is  "+currentDate.toString())


            //list.add(image_uri.toString())
            //time.add(currentDate.toString())

            val isInsert = myDb.insertData(image_uri.toString(),currentDate.toString())

            //imageView.setImageURI(image_uri)



            showData()
            var myAdapter = imageAdapter(list,time)
            recyclerView!!.setAdapter(myAdapter)
        }
    }

    fun showData(){
        val res = myDb.getAllData()
        list =ArrayList<String>()
        time = ArrayList<String>()

        if (res.getCount() != 0){

            while (res.moveToNext()){
                list.add(res.getString(0))
                time.add(res.getString(1))
            }
        }
    }
    inner class imageAdapter(var items: ArrayList<String>,var time:ArrayList<String>) : RecyclerView.Adapter<imageHolder>(){
        override fun onCreateViewHolder(parentView: ViewGroup, option: Int): imageHolder {
            val view = LayoutInflater.from(applicationContext).inflate(R.layout.list_item,parentView,false)
            return imageHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(parentView: imageHolder, option: Int) {
            var uri:Uri = Uri.parse(items.get(option))
            parentView?.getImage?.setImageURI(uri)

            parentView?.getText?.text = time.get(option)
        }

    }


    inner class imageHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val getImage = itemView.imageView3
        val getText = itemView.textView5

    }


}

