package com.example.cameralist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Databate(context:Context):SQLiteOpenHelper(context,DATABASE_NAME,null,1) {
    companion object{
        val DATABASE_NAME = "Data.db"
        val TABLE_NAME = "image_table"
        val COL_1 = "IMAGE"
        val COL_2 = "TIME"
    }
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME(IMAGE STRING,TIME STRING)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    fun insertData(image:String,time:String):Boolean?{
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_1,image)
        cv.put(COL_2,time)
        val res = db.insert(TABLE_NAME,null,cv)
        return !res.equals(-1)
    }

    fun getAllData():Cursor{
        val db = this.writableDatabase
        return  db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }
}