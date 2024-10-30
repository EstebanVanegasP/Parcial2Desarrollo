package com.example.krud.DAO

import androidx.room.*
import com.example.krud.Model.VentaModel

@Dao
interface VentaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVenta(venta: VentaModel): Long

    @Query("SELECT * FROM ventas")
    suspend fun getAllVentas(): List<VentaModel>

    @Query("SELECT * FROM ventas WHERE id = :id")
    suspend fun getVentaById(id: Int): VentaModel?

    @Query("SELECT * FROM ventas WHERE cliente_id = :clienteId")
    suspend fun getVentasByCliente(clienteId: Int): List<VentaModel>

    @Query("SELECT * FROM ventas WHERE producto_id = :productoId")
    suspend fun getVentasByProducto(productoId: Int): List<VentaModel>

    @Update
    suspend fun updateVenta(venta: VentaModel)

    @Delete
    suspend fun deleteVenta(venta: VentaModel)
}
