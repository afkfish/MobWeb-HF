package hu.bme.aut.android.cinemadb.model.cinema

data class AddressInfo(
    var address1: String?,
    var address2: String?,
    var address3: String?,
    var address4: String?,
    var city: String,
    var state: String?,
    var postalCode: String
)
