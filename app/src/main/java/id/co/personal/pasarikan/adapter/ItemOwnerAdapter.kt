package id.co.personal.pasarikan.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.activity.EditItemActivity
import id.co.personal.pasarikan.activity.ItemDetailActivity
import id.co.personal.pasarikan.models.Item


class ItemOwnerAdapter(
    private val context: Context,
    private val listItem: List<Item>
) : RecyclerView.Adapter<ItemOwnerAdapter.FishItemViewHolder>() {
    inner class FishItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.iv_fish)
        val itemName: TextView = view.findViewById(R.id.tv_name)
        val itemPrice: TextView = view.findViewById(R.id.tv_price)
        val itemStock: TextView = view.findViewById(R.id.tv_stock)
        val itemCity: TextView = view.findViewById(R.id.tv_city)
        val itemRating: RatingBar = view.findViewById(R.id.rating_seller)
        val btnEdit: MaterialButton = view.findViewById(R.id.btn_edit)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FishItemViewHolder {
        return FishItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_owner, parent, false)
        )
    }
    override fun getItemCount(): Int {
        return listItem.size
    }
    companion object {
        const val EXTRA_ITEM_ID = "e_i_id"
    }

    override fun onBindViewHolder(holder: FishItemViewHolder, position: Int) {
        val item = listItem[position]
        holder.itemName.text = item.name
        holder.itemPrice.text = "Rp " + item.price
        holder.itemRating.rating = item.rating
        if (item.stock <= 10) {
            holder.itemStock.text = "Stock " + item.stock + " Kg"
            holder.itemStock.setTextColor(Color.parseColor("#E53935"))
        } else {
            holder.itemStock.text = "Stock tersedia"
        }
        holder.itemCity.text = "Kota " + item.address
        Glide.with(context)
            .load(item.item_images)
            .into(holder.itemImage)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ItemDetailActivity::class.java)
            intent.putExtra(EXTRA_ITEM_ID, item.item_id)
            context.startActivity(intent)
        }
        holder.btnEdit.setOnClickListener {
            val intent = Intent(context, EditItemActivity::class.java)
            intent.putExtra(EXTRA_ITEM_ID, item.item_id)
            context.startActivity(intent)
        }
    }
}