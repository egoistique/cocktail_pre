package dev.weazyexe.wretches.storage.crimes

import android.net.Uri
import androidx.room.TypeConverter
import java.util.Arrays


class IngsListConverter {
    companion object {
        private const val SEPARATOR = ", "
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return list.joinToString(separator = SEPARATOR)
    }

    @TypeConverter
    fun toStringList(rawString: String): List<String> {
        return if (rawString.isNotEmpty()) {
            rawString.split(SEPARATOR).map { it.trim() }
        } else {
            emptyList()
        }
    }
}