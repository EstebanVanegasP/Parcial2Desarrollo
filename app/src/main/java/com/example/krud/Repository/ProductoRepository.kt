package com.example.krud.Repository

import com.example.krud.DAO.ProductoDao
import com.example.krud.Model.ProductoModel

class ProductoRepository(private val productoDao: ProductoDao) {

    suspend fun insertarProducto(producto: ProductoModel): Long {
        return productoDao.insertProducto(producto)
    }

    suspend fun obtenerTodosLosProductos(): List<ProductoModel> {
        return productoDao.getAllProductos()
    }

    suspend fun obtenerProductoPorId(id: Int): ProductoModel? {
        return productoDao.getProductoById(id)
    }

    suspend fun actualizarProducto(producto: ProductoModel) {
        productoDao.updateProducto(producto)
    }

    suspend fun eliminarProducto(producto: ProductoModel) {
        productoDao.deleteProducto(producto)
    }
}
