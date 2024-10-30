package com.example.krud.Screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.krud.Repository.ProductoRepository
import com.example.krud.Repository.VentaRepository
import com.example.krud.Model.ProductoModel
import com.example.krud.Model.VentaModel
import kotlinx.coroutines.launch

@Composable
fun CompraScreen(
    navController: NavController,
    productoRepository: ProductoRepository,
    ventaRepository: VentaRepository,
    clienteId: Int
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var productos by remember { mutableStateOf<List<ProductoModel>>(emptyList()) }
    var productosSeleccionados by remember { mutableStateOf<Map<ProductoModel, Int>>(emptyMap()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                isLoading = true
                val listaProductos = productoRepository.obtenerTodosLosProductos()
                productos = listaProductos
                isLoading = false
            } catch (e: Exception) {
                errorMessage = "Error al cargar los productos: ${e.message}"
                isLoading = false
            }
        }
    }

    val totalCompra = productosSeleccionados.entries.sumOf { (producto, cantidad) ->
        producto.precio * cantidad
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
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Comprar Productos", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator()
                } else if (errorMessage == null) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        items(productos) { producto ->
                            var cantidad by remember { mutableStateOf(0) }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${producto.nombre} (Stock: ${producto.stock})",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(1f)
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = {
                                        if (cantidad > 0) cantidad--
                                        productosSeleccionados = productosSeleccionados.toMutableMap().apply {
                                            if (cantidad > 0) {
                                                put(producto, cantidad)
                                            } else {
                                                remove(producto)
                                            }
                                        }
                                    }) {
                                        Text("-")
                                    }
                                    Text(text = cantidad.toString())
                                    IconButton(
                                        onClick = {
                                            if (cantidad < producto.stock) {
                                                cantidad++
                                                productosSeleccionados = productosSeleccionados.toMutableMap().apply {
                                                    put(producto, cantidad)
                                                }
                                            } else {
                                                Toast.makeText(context, "No hay más stock disponible", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        enabled = cantidad < producto.stock
                                    ) {
                                        Text("+")
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Total: $${"%.2f".format(totalCompra)}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        if (productosSeleccionados.isEmpty()) {
                            errorMessage = "Seleccione al menos un producto"
                            return@Button
                        }

                        scope.launch {
                            try {
                                var stockInsuficiente = false
                                productosSeleccionados.forEach { (producto, cantidadSeleccionada) ->
                                    if (cantidadSeleccionada > producto.stock) {
                                        stockInsuficiente = true
                                    }
                                }

                                if (stockInsuficiente) {
                                    errorMessage = "No hay suficiente stock para completar la compra."
                                    return@launch
                                }

                                productosSeleccionados.forEach { (producto, cantidad) ->
                                    val nuevaVenta = VentaModel(
                                        producto_id = producto.id,
                                        cliente_id = clienteId,
                                        cantidad = cantidad,
                                        fecha = System.currentTimeMillis().toString()
                                    )
                                    ventaRepository.insertarVenta(nuevaVenta)

                                    val nuevoStock = producto.stock - cantidad
                                    val productoActualizado = producto.copy(stock = nuevoStock)
                                    productoRepository.actualizarProducto(productoActualizado)
                                }

                                Toast.makeText(context, "Compra realizada con éxito", Toast.LENGTH_LONG).show()
                                navController.navigate("clienteScreen/$clienteId") {
                                    popUpTo("compraScreen/$clienteId") { inclusive = true }
                                }
                            } catch (e: Exception) {
                                errorMessage = "Error al realizar la compra: ${e.message}"
                            }
                        }
                    }) {
                        Text(text = "Comprar Productos")
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }
}
