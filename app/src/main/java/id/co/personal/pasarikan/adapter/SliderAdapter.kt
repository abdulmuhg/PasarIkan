package id.co.personal.pasarikan.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.models.Image

internal class SliderAdapter(
    private val listImage: ArrayList<Image>
) :
    SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_slider_layout_item, null)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(
        viewHolder: SliderAdapterVH,
        position: Int
    ) {
        val image = listImage[position]
        viewHolder.textViewDescription.text = image.desc
        for (img in listImage) {
            Glide.with(viewHolder.imageViewBackground)
                .load(image.source)
                .into(viewHolder.imageViewBackground)
        }
    }

    override fun getCount(): Int {
        return listImage.size
    }

    internal inner class SliderAdapterVH(itemView: View) :
        ViewHolder(itemView) {
        var imageViewBackground: ImageView = itemView.findViewById(R.id.iv_auto_image_slider)
        var textViewDescription: TextView = itemView.findViewById(R.id.tv_auto_image_slider)

    }

}