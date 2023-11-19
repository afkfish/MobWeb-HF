package hu.bme.aut.android.cinemadb.model.cinema

data class CinemaResponse(
    var body: Body
) {
    data class Body(
        var cinemas: List<Cinema>
    ) {
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
        ) {
            data class AddressInfo(
                var address1: String?,
                var address2: String?,
                var address3: String?,
                var address4: String?,
                var city: String,
                var state: String?,
                var postalCode: String
            )
        }
    }
}
