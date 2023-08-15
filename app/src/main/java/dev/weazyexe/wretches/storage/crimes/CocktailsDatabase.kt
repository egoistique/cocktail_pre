package dev.weazyexe.wretches.storage.crimes

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.weazyexe.wretches.entity.Crime

/**
 * База данных с коктейлями
 */
@Database(entities = [Crime::class], version = 1)
@TypeConverters(PhotosListConverter::class)
abstract class CocktailsDatabase : RoomDatabase() {

    abstract fun cocktailsDao(): CocktailsDao
}