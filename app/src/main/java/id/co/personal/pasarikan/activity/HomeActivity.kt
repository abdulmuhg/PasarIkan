package id.co.personal.pasarikan.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.personal.pasarikan.model.Fish
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.adapter.FishItemAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(){
    private lateinit var listFishItem: ArrayList<Fish>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val fish = Fish().getListData()
        listFishItem = ArrayList()
        listFishItem.clear()
        listFishItem.addAll(fish!!)
        showRecyclerList()
        setOnClickButton()
    }

    private fun showRecyclerList(){
        val adapter = FishItemAdapter (this, listFishItem)
        rv_fish_item.layoutManager =LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        rv_fish_item.adapter = adapter
    }

    private fun setOnClickButton(){
        btn_about.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, StillImageActivity::class.java))
        }
    }
}