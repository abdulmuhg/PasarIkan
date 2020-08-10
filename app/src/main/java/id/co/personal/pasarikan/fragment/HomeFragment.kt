package id.co.personal.pasarikan.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import id.co.personal.pasarikan.R
import id.co.personal.pasarikan.activity.ItemShowAllActivity
import id.co.personal.pasarikan.adapter.ItemAdapter
import id.co.personal.pasarikan.adapter.SliderAdapter
import id.co.personal.pasarikan.models.Image
import id.co.personal.pasarikan.models.Item
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var listItemItem: ArrayList<Item>
    private var database: FirebaseDatabase = Firebase.database
    private var dbRef: DatabaseReference
    private var currentUser: FirebaseUser? = null
    private var uid: String = ""
    private lateinit var auth: FirebaseAuth
    private var listImages: ArrayList<Image>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    init {
        dbRef = database.getReference("users")
        listImages = ArrayList()
    }

    private fun getOnSaleItem() {
        Glide.with(context!!)
            .load("https://firebasestorage.googleapis.com/v0/b/pasar-ikan.appspot.com/o/sample_images%2Fsummer_sale.jpg?alt=media&token=c0408a25-e032-4c1e-b146-32407eddd6c9")
            .into(sale_1)
        Glide.with(context!!)
            .load("https://firebasestorage.googleapis.com/v0/b/pasar-ikan.appspot.com/o/sample_images%2Fsuper_sale.jpg?alt=media&token=21e6fd1d-15d8-40b3-98a6-5bde79495020")
            .into(sale_2)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        getUserId()
        getOnSaleItem()
        listItemItem = ArrayList()
        listItemItem.clear()
        getItem()
        onClickEvents()

        val img1 = Image(
            "https://firebasestorage.googleapis.com/v0/b/pasar-ikan.appspot.com/o/sample_images%2Fdisc_1.jpg?alt=media&token=700fc7f8-b377-4553-aebe-08b315cd7f38",
            ""
        )
        val img2 = Image(
            "https://firebasestorage.googleapis.com/v0/b/pasar-ikan.appspot.com/o/sample_images%2Fdisc_2.jpg?alt=media&token=8e5c1ef8-b45d-4e77-8432-d920c1c121c9",
            ""
        )
        val img3 = Image(
            "https://firebasestorage.googleapis.com/v0/b/pasar-ikan.appspot.com/o/sample_images%2Fdisc_3.jpg?alt=media&token=6262fbda-8262-4fdb-88e2-5985ca411adc",
            ""
        )
        val img4 = Image(
            "https://firebasestorage.googleapis.com/v0/b/pasar-ikan.appspot.com/o/sample_images%2Fdisc_4.jpg?alt=media&token=b81eccbd-1c4e-477c-b590-c31c1a6da2d8",
            ""
        )
        listImages.add(img1)
        listImages.add(img2)
        listImages.add(img3)
        listImages.add(img4)
        val adapter = SliderAdapter(listImages)
        sliderView.sliderAdapter = adapter
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        sliderView.scrollTimeInSec = 3
        sliderView.startAutoCycle()

        Glide.with(context!!)
            .load("https://firebasestorage.googleapis.com/v0/b/pasar-ikan.appspot.com/o/sample_images%2FMale-Fish-Market-Maldives.jpg?alt=media&token=119a70cf-9789-494d-9029-a867a6200981")
            .into(market_1)
        Glide.with(context!!)
            .load("https://firebasestorage.googleapis.com/v0/b/pasar-ikan.appspot.com/o/sample_images%2F20190226_140138-1.jpg?alt=media&token=4485048a-1cb1-481f-b0e5-3fbfe4def0d2")
            .into(market_2)
    }

    private fun getUserId() {
        currentUser = auth.currentUser
        currentUser?.let {
            uid = currentUser!!.uid
            readUserProfile(uid)
        }
    }

    private fun readUserProfile(uid: String) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.child(uid).addListenerForSingleValueEvent(userListener)
    }

    private fun getItem() {
        dbRef = database.getReference("items")
        val itemListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listItemItem.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val items = postSnapshot?.getValue<Item>()
                    items?.let { it1 -> listItemItem.add(it1) }
                }
                showRecyclerList()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataChange", "loadPost:onCancelled", databaseError.toException())
            }
        }
        dbRef.addListenerForSingleValueEvent(itemListener)
    }

    private fun showRecyclerList() {
        tv_empty_today.visibility = View.GONE
        listItemItem.reverse()
        val topList = listItemItem
        if (listItemItem.size > 5) {
            val topLists = listItemItem.subList(0, 5)
            val adapter = ItemAdapter(context!!, topLists)
            rv_today_fish.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            rv_today_fish.adapter = adapter
        } else {
            val adapter = ItemAdapter(context!!, topList)
            rv_today_fish.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            rv_today_fish.adapter = adapter
        }
    }

    private fun onClickEvents(){
        btn_t_show_all.setOnClickListener {
            val intent = Intent(context, ItemShowAllActivity::class.java)
            intent.putExtra("extra", "all")
            startActivity(intent)
        }
        btn_pangandaran.setOnClickListener {
            val intent = Intent(context, ItemShowAllActivity::class.java)
            intent.putExtra("extra", "loc")
            intent.putExtra("request", "Pangandaran")
            startActivity(intent)
        }
        btn_cirebon.setOnClickListener {
            val intent = Intent(context, ItemShowAllActivity::class.java)
            intent.putExtra("extra", "loc")
            intent.putExtra("request", "Cirebon")
            startActivity(intent)
        }
    }
}