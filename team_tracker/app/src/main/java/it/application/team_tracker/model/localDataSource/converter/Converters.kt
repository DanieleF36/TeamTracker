package it.application.team_tracker.model.localDataSource.converter

import android.net.Uri
import androidx.room.TypeConverter
import java.util.Calendar

class Converters {
    @TypeConverter
    fun fromString(value: String?): Uri? {
        return if(value != null) Uri.parse(value) else null
    }

    @TypeConverter
    fun uriToString(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun fromTimestamp(value: Long): Calendar {
        return value.let {
            Calendar.getInstance().apply { timeInMillis = it }
        }
    }

    @TypeConverter
    fun dateToTimestamp(calendar: Calendar): Long {
        return calendar.timeInMillis
    }
}