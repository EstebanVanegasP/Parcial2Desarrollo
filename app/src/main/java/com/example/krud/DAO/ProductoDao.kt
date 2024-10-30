package com.example.krud.DAO

import androidx.room.*
import com.example.krud.Model.ProductoModel

@Dao
interface ProductoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducto(producto: ProductoModel): Long

    @Query("SELECT * FROM productos")
    suspend fun getAllProductos(): List<ProductoModel>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun getProductoById(id: Int): ProductoModel?

    @Update
    suspend fun updateProducto(producto: ProductoModel)

    @Delete
    suspend fun deleteProducto(producto: ProductoModel)
}
