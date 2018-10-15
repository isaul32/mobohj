package li.sau.exercise7

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
data class Book(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    var name: String,

    var isbn: String? = null,

    var yearOfPublication: Int? = null,

    var dateOfAcquisition: Date? = null
)