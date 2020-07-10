package id.co.personal.pasarikan.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.co.personal.pasarikan.model.Fish
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.activity.ItemDetailActivity

class FishItemAdapter (
    private val context: Context,
    private val listItem: ArrayList<Fish>
) : RecyclerView.Adapter<FishItemAdapter.FishItemViewHolder>() {
    inner class FishItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.iv_fish)
        val itemName: TextView = view.findViewById(R.id.tv_name)
        val itemPrice: TextView = view.findViewById(R.id.tv_price)
        val itemStock: TextView = view.findViewById(R.id.tv_stock)
        val itemCity: TextView = view.findViewById(R.id.tv_city)
        val itemRating: RatingBar = view.findViewById(R.id.rating_seller)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FishItemViewHolder {
        return FishItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_fish_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FishItemViewHolder, position: Int) {
        val item = listItem[position]
        holder.itemName.text = item.name
        holder.itemPrice.text = "Rp "+ item.price
        holder.itemRating.rating = item.rating
        if (item.stock <= 10) {
            holder.itemStock.text = "Stock " + item.stock + " Kg"
        } else {
            holder.itemStock.text = "Stock tersedia"
        }
        holder.itemCity.text = "Kota " + item.city
        Glide.with(context)
            .load(item.photo)
            .into(holder.itemImage)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ItemDetailActivity::class.java)
            intent.putExtra("fishName", item.name)
            intent.putExtra("desc", item.description)
            intent.putExtra("img", item.photo)
            context.startActivity(intent)
        }
    }
}