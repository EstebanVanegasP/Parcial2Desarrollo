package com.example.krud.Repository

import com.example.krud.DAO.ClienteDao
import com.example.krud.Model.ClienteModel

class ClienteRepository(private val clienteDao: ClienteDao) {

    suspend fun insertarCliente(cliente: ClienteModel): Long {
        return clienteDao.insertCliente(cliente)
    }

    suspend fun obtenerTodosLosClientes(): List<ClienteModel> {
        return clienteDao.getAllClientes()
    }

    suspend fun obtenerClientePorId(id: Int): ClienteModel? {
        return clienteDao.getClienteById(id)
    }

    suspend fun obtenerClientePorCorreo(correo: String): ClienteModel? {
        return clienteDao.obtenerClientePorCorreo(correo)
    }

    suspend fun actualizarCliente(cliente: ClienteModel) {
        clienteDao.actualizarCliente(cliente)
    }

    suspend fun eliminarCliente(cliente: ClienteModel) {
        clienteDao.deleteCliente(cliente)
    }


}
