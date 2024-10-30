package com.example.krud.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.krud.DAO.ProductoDao
import com.example.krud.DAO.ClienteDao
import com.example.krud.DAO.VentaDao
import com.example.krud.Model.ProductoModel
import com.example.krud.Model.ClienteModel
import com.example.krud.Model.VentaModel

@Database(
    entities = [ProductoModel::class, ClienteModel::class, VentaModel::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun clienteDao(): ClienteDao
    abstract fun ventaDao(): VentaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
