package com.example.krud.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.krud.DAO.ProductoDao
import com.example.krud.Model.ProductoModel

@Database(entities = [ProductoModel::class], version = 2, exportSchema = false)
abstract class ProductoDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao

    companion object {
        @Volatile
        private var INSTANCE: ProductoDatabase? = null

        fun getDatabase(context: Context): ProductoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductoDatabase::class.java,
                    "producto_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
