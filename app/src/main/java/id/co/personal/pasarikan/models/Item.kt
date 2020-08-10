package id.co.personal.pasarikan.models

class Item(
    var user_id: String = "",
    var item_id: String = "",
    var name: String = "",
    var description: String = "",
    var price: Int = 0,
    var rating: Float = 0f,
    var stock: Int = 100,
    var address: String = "",
    var min_buy: Int = 1,
    var item_images: String = "",
    var seller_name: String = "",
    var contact: String = ""
)