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

import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.provider.MediaStore.Images.Media.getBitmap
import layout.imageAdapter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class Nextpage : AppCompatActivity() {

    lateinit var myDb: Databate
    //lateinit var myAdapter:imageAdapter
    var list =ArrayList<String>()
    var time = ArrayList<String>()
    //var arr = ArrayList<Uri>()
    private val IMAGE_CAPTURE_CODE= 1001;
    private val PERMISSION_CODE = 1000;
    var image_uri:Uri? = null
    var recyclerView: RecyclerView? = null
    var arrBitmap = ArrayList<Bitmap>()
    var hashMap:HashMap<String,String> = HashMap<String,String>()
    var objects = ArrayList<storeImage>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nextpage)

        myDb = Databate(this)
        showData()
        recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        val arr = ArrayList<Bitmap>()

        arrBitmap.forEach {
            arr.add(resizeBitmap(it, 150, 150))
        }


        var myAdapter = imageAdapter(objects,time,this)
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
       // val path = image_uri.getPath()
        //val imagePath = get
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE)

        //val rawTakenImage = BitmapFactory.decodeFile(image_uri.getPath())
        //val rawTakenImage = BitmapFactory.decodeFile(image_uri.toString())

        //val resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, SOME_WIDTH);
        //val resizedBitmap = BitmapScaler.scaleToFitWidth

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

//    fun BitmapToString(bitmap: Bitmap): String {
//        val outputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//        return android.util.Base64.encodeToString(outputStream.toByteArray(), android.util.Base64.DEFAULT)
//    }
//    fun StringToBitmap(string: String):Bitmap{
//        val imageBytes = android.util.Base64.decode(string, 0)
//        val image=BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size);
//        return image
//    }
    private fun resizeBitmap(bitmap:Bitmap, width:Int, height:Int):Bitmap{

        return Bitmap.createScaledBitmap(bitmap, width, height, false)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode == Activity.RESULT_OK){

            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())

//            System.out.println(" C DATE is  "+currentDate.toString())


            //list.add(image_uri.toString())
            //time.add(currentDate.toString())

            var mBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, image_uri)
            arrBitmap.add(mBitmap)
            //hashMap.put()
            println("bbbbbbbbbbbbbb"+mBitmap)

            val isInsert = myDb.insertData(arrBitmap.toString(),currentDate.toString())

            //imageView.setImageURI(image_uri)



            showData()
            val arr = ArrayList<Bitmap>()

            arrBitmap.forEach {
                arr.add(resizeBitmap(it, 150, 150))
                var small = resizeBitmap(it, 150, 150)
                var large = resizeBitmap(it, 600, 600)
                hashMap.put(BitmapToString(small),BitmapToString(large))
                var Obj:storeImage = storeImage(BitmapToString(small) ,BitmapToString(large))
                //hashMap.put("kk","mm")
                objects.add(Obj)
            }


            val myAdapter = imageAdapter(objects,time,this)
            recyclerView!!.setAdapter(myAdapter)
        }
    }

    fun showData(){
        val res = myDb.getAllData()
        list =ArrayList<String>()
        //arrBitmap = ArrayList<Bitmap>()
        //arr = ArrayList<Uri>()
        time = ArrayList<String>()

        var stringBitmap = ArrayList<String>()

        if (res.getCount() != 0){

            while (res.moveToNext()){
                list.add(res.getString(0))
                time.add(res.getString(1))
            }
        }
    }
//    inner class imageAdapter(var items: ArrayList<Bitmap>,var time:ArrayList<String>) : RecyclerView.Adapter<imageHolder>(){
//        override fun onCreateViewHolder(parentView: ViewGroup, option: Int): imageHolder {
//            val view = LayoutInflater.from(applicationContext).inflate(R.layout.list_item,parentView,false)
//            return imageHolder(view)
//        }
//
//        override fun getItemCount(): Int {
//            return items.size
//        }
//
//        override fun onBindViewHolder(parentView: imageHolder, option: Int) {
////            var uri:Uri = Uri.parse(items.get(option))
//            parentView.getImage?.setImageBitmap(items[option])
//            parentView.getText?.text = time.get(option)
//        }
//
//    }
//
//
//    inner class imageHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
//        val getImage = itemView.imageView3
//        val getText = itemView.textView5
//
//    }


}

