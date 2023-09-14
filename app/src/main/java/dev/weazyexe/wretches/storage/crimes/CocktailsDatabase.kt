package dev.weazyexe.wretches.storage.crimes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

//    companion object {
//        @Volatile
//        private var INSTANCE: CocktailsDatabase? = null
//
//        fun getDatabase(context: Context): CocktailsDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    CocktailsDatabase::class.java,
//                    "crime"
//                )
//                    .fallbackToDestructiveMigration() // Allow destructive migration
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }

}