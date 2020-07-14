package id.co.personal.pasarikan.models

import id.co.personal.pasarikan.R
import java.util.ArrayList

class Fish(
    val id: String = "",
    var name: String = "",
    var description: String = "",
    var price: Int = 0,
    var rating: Float = 0f,
    var stock: Int = 0,
    var city: String = "",
    var photo: Int = 0
) {
    private val fishNames = arrayOf(
        "Ikan Tuna",
        "Ikan Baronang",
        "Ikan Belanak",
        "Ikan Cakalang",
        "Ikan Kakap",
        "Ikan Kembung",
        "Ikan Makarel",
        "Ikan Tenggiri",
        "Ikan Teri",
        "Ikan Tongkol"
    )

    private val itemDescription = arrayOf(
        "Tuna adalah ikan laut pelagik yang termasuk bangsa Thunnini, terdiri dari beberapa spesies dari famili skombride, terutama genus Thunnus. Ikan ini adalah perenang andal (pernah diukur mencapai 77 km/jam). Tidak seperti kebanyakan ikan yang memiliki daging berwarna putih, daging tuna berwarna merah muda sampai merah tua. Hal ini karena otot tuna lebih banyak mengandung myoglobin daripada ikan lainnya. Beberapa spesies tuna yang lebih besar, seperti tuna sirip biru Atlantik (Thunnus thynnus), dapat menaikkan suhu darahnya di atas suhu air dengan aktivitas ototnya. Hal ini menyebabkan mereka dapat hidup di air yang lebih dingin dan dapat bertahan dalam kondisi yang beragam. Kebanyakan bertubuh besar, tuna adalah ikan yang memiliki nilai komersial tinggi.",
        "Baronang (Siganus Sp.) adalah sekelompok ikan laut yang masuk dalam keluarga Siginidae. Ikan ini dikenal oleh masyarakat dengan nama yang berbeda-beda satu sama lain sesuai spesiesnya, seperti di Kepulauan Seribu dinamakan kea-kea, di Jawa Tengah dengan nama biawas dan nelayan-nelayan di Pulau Maluku menamakan dengan sebutan samadar. Baronang ditemukan di perairan dangkal laguna di wilayah Indo-Pasifik dan timur Laut Tengah. ikan ini dalam bahasa inggris disebut rabbitfish karena prilaku makannya yang memakan tumbuh-tumbuhan (rumput laut) secara rapi seperti dipangkas mesin rumput kecil. Baronang merupakan salah satu ikan yang menjadi favorit bagi para pemancing di laut.",
        "Belanak (Moolgarda seheli, sinonim Valamugil seheli (Forsskål, 1775); suku Mugilidae) adalah sejenis ikan laut tropis dan subtropis yang bentuknya hampir menyerupai bandeng. Dalam bahasa Inggris dikenal sebagai blue-spot mullet atau blue-tail mullet.",
        "Ikan Cakalang (Katsuwonus pelamis) adalah ikan berukuran sedang dari familia Skombride (tuna). Satu-satunya spesies dari genus Katsuwonus. Cakalang terbesar, panjang tubuhnya bisa mencapai 1 m dengan berat lebih dari 18 kg. Cakalang yang banyak tertangkap berukuran panjang sekitar 50 cm. Nama-nama lainnya di antaranya cakalan, cakang, kausa, kambojo, karamojo, turingan, dan ada pula yang menyebutnya tongkol. Dalam bahasa Inggris dikenal sebagai skipjack tuna.",
        "Kakap adalah keluarga ikan laut dasaran yang hidup secara berkelompok di dasar-dasar karang atau terumbu karang. Mempunyai ciri tubuh yang bulat pipih dengan sirip memanjang sepanjang punggung. Jenis ikan kakap yang banyak ditemui di Indonesia adalah jenis kakap merah (L. campechanus). Beberapa jenis yang lain yang juga banyak ditemui adalah kakap kuning, kakap hitam dan lain-lain.\n" +
                "\n" +
                "Kakap merah merupakan fauna khas provinsi Kepulauan Riau dikarenakan provinsi ini merupakan tempat tinggal banyak kakap dan kakap sendiri sering dijadikan bahan makanan khas yaitu asam pedas.",
        "Kembung adalah nama sekelompok ikan laut yang tergolong ke dalam marga Rastrelliger, suku Scombridae. Meskipun bertubuh kecil, ikan ini masih sekerabat dengan tenggiri, tongkol, tuna, madidihang, dan makerel. Di Ambon, ikan ini dikenal dengan nama lema atau tatare, di Makassar disebut banyar atau banyara. Dari sini didapat sebutan kembung banjar.\n" +
                "\n" +
                "Kembung termasuk ikan pelagis kecil yang memiliki nilai ekonomis menengah, sehingga terhitung sebagai komoditas yang cukup penting bagi nelayan lokal. Kembung biasanya dijual segar atau diproses menjadi ikan pindang dan ikan asin yang lebih tahan lama. Ikan kembung yang masih kecil juga sering digunakan sebagai umpan hidup untuk memancing cakalang.",
        "Makerel atau makarel (dari bahasa Inggris, mackerel) adalah sebutan bagi sekelompok ikan laut yang terdiri dari beberapa marga anggota famili Scombridae. Dalam peristilahan bahasa Inggris, sebutan mackerel juga mencakup kelompok ikan tenggiri dan kembung.\n" +
                "\n" +
                "Mackarel adalah ikan pelagis, umumnya hidup jauh di laut lepas, meski beberapa jenisnya juga bisa didapati di perairan teluk yang tak jauh dari pantai. Jenis-jenis ikan ini tersebar di pelbagai lautan tropis dan ugahari. Sebagian jenisnya mampu menyelam hingga kedalaman lebih dari 1.000 meter. Beberapa spesies makerel yang lebih besar, seperti makerel sirip biru (bluefin mackerel), dapat menaikkan suhu darahnya di atas suhu air dengan aktivitas ototnya. Hal ini menyebabkan mereka dapat hidup di air yang lebih dingin dan dapat bertahan dalam kondisi yang beragam.\n" +
                "\n" +
                "Makerel adalah ikan yang memiliki nilai komersial sedang. Ikan ini cocok digunakan sebagai makanan dihidangkan dengan saus cabe atau saus tomat. Sebagaimana sarden, makerel juga sering dikalengkan.",
        "Tenggiri adalah nama umum bagi sekelompok ikan yang tergolong ke dalam marga Scomberomorus, suku Scombridae. Ikan ini merupakan kerabat dekat tuna, tongkol, madidihang, makerel dan kembung. Tenggiri banyak disukai orang, diperdagangkan dalam bentuk segar, ikan kering, atau diolah menjadi kerupuk, siomay, dan lain-lain.",
        "Ikan teri atau ikan bilis adalah sekelompok ikan laut kecil anggota suku Engraulidae. Nama ini mencakup berbagai ikan dengan warna tubuh perak kehijauan atau kebiruan.\n" +
                "\n" +
                "Walaupun anggota Engraulidae ada yang memiliki panjang maksimum 23 cm, nama ikan teri biasanya diberikan bagi ikan dengan panjang maksimum 5 cm. Moncongnya tumpul dengan gigi yang kecil dan tajam pada kedua-dua rahangnya. Mangsa utama ikan teri ialah plankton.",
        "Tongkol (Euthynnus affinis) adalah sejenis ikan laut dari suku Scombridae. Terutama menjelajah di perairan dangkal dekat pesisir di kawasan Indo-Pasifik Barat, tongkol merupakan salah satu jenis ikan tangkapan yang penting bagi nelayan. Dalam perdagangan internasional dikenal sebagai kawakawa, little tuna, mackerel tuna, atau false albacore."

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
        R.drawable.ikan_tuna,
        R.drawable.ikan_baronang,
        R.drawable.ikan_belanak,
        R.drawable.ikan_cakalang,
        R.drawable.ikan_kakap,
        R.drawable.ikan_kembung,
        R.drawable.ikan_makarel,
        R.drawable.ikan_tenggiri,
        R.drawable.ikan_teri,
        R.drawable.ikan_tuna
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
            fish.description = itemDescription[position]
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