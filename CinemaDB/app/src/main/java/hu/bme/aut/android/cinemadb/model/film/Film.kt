package hu.bme.aut.android.cinemadb.model.film

data class Film(
    var id: String,
    var name: String,
    var length: Int,
    var posterLink: String,
    var videoLink: String,
    var link: String,
    var releaseYear: String,
    var attributeIds: List<String>
)
