package li.sau.exercise5

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity
data class Book(
        @PrimaryKey(autoGenerate = true)
        var id: Long,

        @NotNull
        var name: String,

        var isbn: String,

        var yearOfPublication: Int,

        var dateOfAcquisition: Date
)