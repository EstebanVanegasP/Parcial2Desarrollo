package com.example.krud.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.krud.Model.VentaModel
import com.example.krud.Repository.ClienteRepository
import com.example.krud.Repository.ProductoRepository
import com.example.krud.Repository.VentaRepository
import kotlinx.coroutines.launch

@Composable
fun HistorialScreen(
    navController: NavController,
    ventaRepository: VentaRepository,
    productoRepository: ProductoRepository,
    clienteRepository: ClienteRepository,
    clienteId: Int
) {
    val scope = rememberCoroutineScope()
    val ventas = remember { mutableStateListOf<VentaModel>() }

    LaunchedEffect(Unit) {
        scope.launch {
            val listaVentas = ventaRepository.obtenerTodasLasVentas()
            ventas.clear()
            ventas.addAll(listaVentas)
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
            Text(text = "Historial de Ventas", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(ventas) { venta ->
                    var clienteNombre by remember { mutableStateOf("") }
                    var productoNombre by remember { mutableStateOf("") }
                    var totalVenta by remember { mutableStateOf(0.0) }

                    LaunchedEffect(venta) {
                        scope.launch {
                            val cliente = clienteRepository.obtenerClientePorId(venta.cliente_id)
                            clienteNombre = cliente?.nombre ?: "Cliente desconocido"

                            val producto = productoRepository.obtenerProductoPorId(venta.producto_id)
                            productoNombre = producto?.nombre ?: "Producto desconocido"

                            totalVenta = (producto?.precio ?: 0.0) * venta.cantidad
                        }
                    }

                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "ID Venta: ${venta.id}")
                        Text(text = "Cliente: $clienteNombre (ID: ${venta.cliente_id})")
                        Text(text = "Producto: $productoNombre")
                        Text(text = "Cantidad: ${venta.cantidad}")
                        Text(text = "Total: $${"%.2f".format(totalVenta)}")
                    }
                    Divider()
                }
            }
        }
    }
}
