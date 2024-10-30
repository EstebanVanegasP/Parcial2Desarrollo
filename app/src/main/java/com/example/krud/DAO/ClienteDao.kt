package com.example.krud.DAO

import androidx.room.*
import com.example.krud.Model.ClienteModel

@Dao
interface ClienteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCliente(cliente: ClienteModel): Long

    @Query("SELECT * FROM clientes")
    suspend fun getAllClientes(): List<ClienteModel>

    @Query("SELECT * FROM clientes WHERE id = :id")
    suspend fun getClienteById(id: Int): ClienteModel?

    @Query("SELECT * FROM clientes WHERE correo = :correo")
    suspend fun obtenerClientePorCorreo(correo: String): ClienteModel?

    @Update
    suspend fun updateCliente(cliente: ClienteModel)


    @Delete
    suspend fun deleteCliente(cliente: ClienteModel)


    @Update
    suspend fun actualizarCliente(cliente: ClienteModel)






}
