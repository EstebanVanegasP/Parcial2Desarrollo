package com.example.krud.Repository

import com.example.krud.DAO.VentaDao
import com.example.krud.Model.VentaModel

class VentaRepository(private val ventaDao: VentaDao) {

    suspend fun insertarVenta(venta: VentaModel): Long {
        return ventaDao.insertVenta(venta)
    }

    suspend fun obtenerTodasLasVentas(): List<VentaModel> {
        return ventaDao.getAllVentas()
    }

    suspend fun obtenerVentaPorId(id: Int): VentaModel? {
        return ventaDao.getVentaById(id)
    }

    suspend fun obtenerVentasPorCliente(clienteId: Int): List<VentaModel> {
        return ventaDao.getVentasByCliente(clienteId)
    }

    suspend fun obtenerVentasPorProducto(productoId: Int): List<VentaModel> {
        return ventaDao.getVentasByProducto(productoId)
    }

    suspend fun actualizarVenta(venta: VentaModel) {
        ventaDao.updateVenta(venta)
    }

    suspend fun eliminarVenta(venta: VentaModel) {
        ventaDao.deleteVenta(venta)
    }
}
