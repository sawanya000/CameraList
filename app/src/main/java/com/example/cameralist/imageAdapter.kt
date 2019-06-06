package layout

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.list_item.view.*
import android.R.attr.bitmap
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.support.design.widget.Snackbar
import com.example.cameralist.*
import java.io.ByteArrayOutputStream

//
//class imageAdapter(var items: ArrayList<Bitmap>, var time:ArrayList<String>,val context: Context) : RecyclerView.Adapter<imageAdapter.imageHolder>(){
//    override fun onBindViewHolder(p0: imageHolder, p1: Int) {
//        p0.getImage?.setImageBitmap(items[p1])
//        p0.getText?.text = time.get(p1)
//
//        p0.itemView.setOnClickListener{
//            println("pppppp")
//
//            val outputStream = ByteArrayOutputStream()
//            items[p1].compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//
//            val intent = Intent(p0.itemView.context,LargeImage::class.java)
//            intent.putExtra("large_image", Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT));
//
//            p0.itemView.context.startActivity(intent)
//
//        }
//    }
//
//
//    override fun onCreateViewHolder(parentView: ViewGroup, option: Int): imageHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.list_item,parentView,false)
//        return imageHolder(view)
//
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//
//    inner class imageHolder(itemView: View?):RecyclerView.ViewHolder(itemView!!) {
//        //val getImage = itemView?.imageView3
//        //val getText = itemView?.textView5
//
//        val getImage = itemView?.findViewById<ImageView>(R.id.imageView3)
//        val getText = itemView?.findViewById<TextView>(R.id.textView5)
//
//    }
//
//}


class imageAdapter(var items: ArrayList<storeImage>, var time:ArrayList<String>,val context: Context) : RecyclerView.Adapter<imageAdapter.imageHolder>(){

    private var removedPosition: Int = 0
    private var removedItem: String = ""

    override fun onBindViewHolder(p0: imageHolder, p1: Int) {
        p0.getImage?.setImageBitmap(StringToBitmap(items[p1].getsmall()))
//        p0.getImage?.setImageBitmap(items[p1])
        p0.getText?.text = time.get(p1)

        p0.itemView.setOnClickListener{
            println("pppppp")



            var intent = Intent(p0.itemView.context,LargeImage::class.java)

            val sp = context.getSharedPreferences("large_image", Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString("image",items[p1].getlarge() )
            editor.commit()

            intent.putExtra("large_image","")


            p0.itemView.context.startActivity(intent)
        }
    }



    override fun onCreateViewHolder(parentView: ViewGroup, option: Int): imageHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item,parentView,false)
        return imageHolder(view)

    }

    override fun getItemCount(): Int {
        return items.size
    }
    fun removeImage(p0:RecyclerView.ViewHolder,db:Databate,obj:ArrayList<storeImage>){
        removedItem = obj[p0.position].getsmall()
        removedPosition = p0.position
        println("pppppppppppppppppppooooooooooooooooooo   "+p0.position)

        db.daleteData(obj[p0.position].getsmall())



    }
    inner class imageHolder(itemView: View?):RecyclerView.ViewHolder(itemView!!) {

        val getImage = itemView?.findViewById<ImageView>(R.id.imageView3)
        val getText = itemView?.findViewById<TextView>(R.id.textView5)

    }

}
