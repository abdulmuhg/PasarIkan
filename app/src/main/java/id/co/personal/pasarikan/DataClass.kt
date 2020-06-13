package id.co.personal.pasarikan

import java.util.ArrayList

class Fish(
    val id: String = "",
    var name: String = "",
    var price: Int = 0,
    var rating: Float = 0f,
    var stock: Int = 0,
    var city: String = "",
    var photo: Int = 0
) {
    private val fishNames = arrayOf(
        "Ahmad Dahlan",
        "Ahmad Yani",
        "Sutomo",
        "Gatot Soebroto",
        "Ki Hadjar Dewantarai",
        "Mohammad Hatta",
        "Soedirman",
        "Soekarno",
        "Soepomo",
        "Tan Malaka"
    )

    private val fishPrice = arrayOf(
        15000,
        23000,
        60000,
        25000,
        75000,
        55000,
        45000,
        35000,
        85000,
        10000
    )

    private val fishRating = floatArrayOf(
        1f,
        2f,
        3f,
        4f,
        5f,
        1f,
        2f,
        3f,
        4f,
        5f
    )

    private val fishesImages = intArrayOf(
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background
    )

    private val fishStock = intArrayOf(
        20,
        30,
        50,
        100,
        25,
        70,
        10,
        80,
        50,
        5
    )

    private val sellerCity = arrayOf(
        "Bandung",
        "Surabaya",
        "Padang",
        "Jakarta Selatan",
        "Jakarta Utara",
        "Medan",
        "Lampung",
        "Batam",
        "Bogor",
        "Makasar"
    )

    fun getListData(): ArrayList<Fish>? {
        val list: ArrayList<Fish> = ArrayList<Fish>()
        for (position in fishNames.indices) {
            val fish = Fish()
            fish.name = fishNames[position]
            fish.price = fishPrice[position]
            fish.stock = fishStock[position]
            fish.rating = fishRating[position]
            fish.city = sellerCity[position]
            fish.photo = fishesImages[position]
            list.add(fish)
        }
        return list
    }
}