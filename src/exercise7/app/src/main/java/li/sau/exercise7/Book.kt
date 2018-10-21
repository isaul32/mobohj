package li.sau.exercise7

import java.util.*

data class Book(
        var id: String? = null,

        var name: String,

        var isbn: String? = null,

        var yearOfPublication: Int? = null,

        var dateOfAcquisition: Date? = null
) {
    constructor() : this(null, "nameless", null, null, null)
}