package layout

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.list_item.view.*

class imageHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val getImage = itemView.imageView3
    val getText = itemView.textView5

}