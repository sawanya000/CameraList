package com.example.cameralist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
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

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.provider.MediaStore.Images.Media.getBitmap
import android.support.v4.content.ContextCompat
import android.support.v7.widget.helper.ItemTouchHelper
import layout.imageAdapter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class Nextpage : AppCompatActivity() {

    lateinit var myDb: Databate

    var list =ArrayList<String>()
    var time = ArrayList<String>()

    private val IMAGE_CAPTURE_CODE= 1001;
    private val PERMISSION_CODE = 1000;
    var image_uri:Uri? = null
    var recyclerView: RecyclerView? = null
    var arrBitmap = ArrayList<Bitmap>()
    var hashMap:HashMap<String,String> = HashMap<String,String>()
    var objects = ArrayList<storeImage>()
    private lateinit var deleteIcon:Drawable
    private  var swipBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nextpage)

        myDb = Databate(this)
        showData()
        recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager

        deleteIcon = ContextCompat.getDrawable(this,R.drawable.ic_delete)!!


        val arr = ArrayList<Bitmap>()

        arrBitmap.forEach {
            arr.add(resizeBitmap(it, 150, 150))
        }


        var myAdapter = imageAdapter(objects,time,this)
        recyclerView!!.setAdapter(myAdapter)

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT ){

            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                return false
             }

            override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
                //var isInsert = myDb.insertData(BitmapToString(small),BitmapToString(large),currentDate.toString())
                (myAdapter as imageAdapter).removeImage(p0,myDb,objects)
                showData()

                //               // finish();
                //startActivity(getIntent());
                val myAdapter = imageAdapter(objects,time,baseContext)
                recyclerView!!.setAdapter(myAdapter)


            }
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val iconMarginVertical = (itemView.height - deleteIcon.intrinsicHeight) / 2

                if (dX > 0) {
//                    colorDrawableBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
//
                    swipBackground.setBounds(itemView.left,itemView.top,dX.toInt(),itemView.bottom)
                    deleteIcon.setBounds(itemView.left + iconMarginVertical, itemView.top + iconMarginVertical,
                       itemView.left + iconMarginVertical + deleteIcon.intrinsicWidth, itemView.bottom - iconMarginVertical)
                } else {
//                    colorDrawableBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    swipBackground.setBounds(itemView.right+dX.toInt(),itemView.top,itemView.right,itemView.bottom)
                    deleteIcon.setBounds(itemView.right - iconMarginVertical - deleteIcon.intrinsicWidth, itemView.top + iconMarginVertical,
                        itemView.right - iconMarginVertical, itemView.bottom - iconMarginVertical)
                    deleteIcon.level = 0

                }

                //colorDrawableBackground.draw(c)
                swipBackground.draw(c)


                c.save()
//
                if (dX > 0)
                    c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                else
                    c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)

                deleteIcon.draw(c)

//
//                deleteIcon.draw(c)
//
                c.restore()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }
//        myAdapter = imageAdapter(objects,time,this)
//        recyclerView!!.setAdapter(myAdapter)


        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

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



    }

    override fun onBackPressed() {

        val alertdialog = AlertDialog.Builder(this)
        alertdialog.setTitle("LogOut")
        alertdialog.setMessage("Are you sure you Want to LogOut the app???")
        alertdialog.setPositiveButton(
            "yes"
        ) { dialog, which -> super.onBackPressed() }

        alertdialog.setNegativeButton(
            "No"
        ) { dialog, which -> dialog.cancel() }

        val alert = alertdialog.create()
        alertdialog.show()

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


    private fun resizeBitmap(bitmap:Bitmap, width:Int, height:Int):Bitmap{

        return Bitmap.createScaledBitmap(bitmap, width, height, false)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode == Activity.RESULT_OK){

            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())


            time.add(currentDate.toString())

            var mBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, image_uri)
            arrBitmap.add(mBitmap)
            var small = resizeBitmap(mBitmap, 150, 150)
            var large = resizeBitmap(mBitmap, 600, 600)

            var Obj:storeImage = storeImage(BitmapToString(small) ,BitmapToString(large))
            objects.add(Obj)
            var isInsert = myDb.insertData(BitmapToString(small),BitmapToString(large),currentDate.toString())
            val myAdapter = imageAdapter(objects,time,this)
            recyclerView!!.setAdapter(myAdapter)
        }
    }

    fun showData(){
        val res = myDb.getAllData()

        time = ArrayList<String>()
        objects = ArrayList<storeImage>()
        if (res.getCount() != 0){

            while (res.moveToNext()){
                var Obj:storeImage = storeImage(res.getString(0) ,res.getString(1))
                objects.add(Obj)
                time.add(res.getString(2))

            }
        }
    }



}

