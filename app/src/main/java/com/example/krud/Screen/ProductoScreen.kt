package com.example.krud.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.krud.Model.ProductoModel
import com.example.krud.Repository.ProductoRepository
import kotlinx.coroutines.launch

@Composable
fun ProductoScreen(navController: NavController, productoRepository: ProductoRepository, clienteId: Int) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var isDialogOpen by remember { mutableStateOf(false) }
    var productoAEliminar by remember { mutableStateOf<ProductoModel?>(null) }

    val productos = remember { mutableStateListOf<ProductoModel>() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            val productosList = productoRepository.obtenerTodosLosProductos()
            productos.clear()
            productos.addAll(productosList)
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                content = {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        navController.navigate("clienteScreen/$clienteId") {
                            popUpTo("clienteScreen/$clienteId") { inclusive = false }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Formulario fijo en la parte superior
            Text(text = "Gestión de Productos", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Producto") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = precio,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                        precio = it
                    }
                },
                label = { Text("Precio del Producto") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = stock,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d+$"))) {
                        stock = it
                    }
                },
                label = { Text("Stock del Producto") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (nombre.isEmpty() || precio.isEmpty() || stock.isEmpty()) {
                    return@Button
                }
                val precioDouble = precio.toDoubleOrNull()
                val stockInt = stock.toIntOrNull()

                if (precioDouble == null || stockInt == null) {
                    return@Button
                }

                scope.launch {
                    val producto = ProductoModel(
                        nombre = nombre,
                        precio = precioDouble,
                        stock = stockInt
                    )
                    productoRepository.insertarProducto(producto)
                    val productosList = productoRepository.obtenerTodosLosProductos()
                    productos.clear()
                    productos.addAll(productosList)
                    nombre = ""
                    precio = ""
                    stock = ""
                }
            }) {
                Text(text = "Agregar Producto")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista desplazable de productos
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(productos) { producto ->
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "Producto: ${producto.nombre}")
                        Text(text = "Precio: ${producto.precio}")
                        Text(text = "Stock: ${producto.stock}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = {
                            productoAEliminar = producto
                            isDialogOpen = true
                        }) {
                            Text(text = "Eliminar Producto")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    if (isDialogOpen && productoAEliminar != null) {
        AlertDialog(
            onDismissRequest = { isDialogOpen = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que quieres eliminar este producto?") },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        productoRepository.eliminarProducto(productoAEliminar!!)
                        val productosList = productoRepository.obtenerTodosLosProductos()
                        productos.clear()
                        productos.addAll(productosList)
                    }
                    isDialogOpen = false
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = { isDialogOpen = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
