package hu.bme.aut.android.cinemadb.model.cinema

data class Cinema(
    var id: String,
    var groupId: String,
    var displayName: String,
    var link: String,
    var imageUrl: String,
    var address: String,
    var addressInfo: AddressInfo,
    var bookingUrl: String,
    var blockOnlineSales: Boolean,
    var blockOnlineSalesUntil: String?,
    var latitude: Double,
    var longitude: Double
)
