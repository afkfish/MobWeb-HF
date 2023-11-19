package hu.bme.aut.android.cinemadb.model.filmEvent

import hu.bme.aut.android.cinemadb.model.film.FilmResponse.Body.Film

data class FilmEventResponse(
    var body: FilmEventBody
) {
    data class FilmEventBody(
        var films: List<Film>,
        var events: List<FilmEvent>
    ) {
        data class FilmEvent(
            var id: String,
            var filmId: String,
            var cinemaId: String,
            var businessDay: String,
            var eventDateTime: String,
            var attributeIds: List<String>,
            var bookingLink: String,
            var compositeBookingLink: CompositeBookingLink,
            var soldOut: Boolean,
            var auditorium: String,
        ) {
            data class CompositeBookingLink(
                var type: String,
                var bookingUrl: BookingUrl
            ) {
                data class BookingUrl(
                    var url: String,
                    var params: Params
                ) {
                    data class Params(
                        var lang: String,
                        var key: String,
                    )
                }
            }
        }
    }
}