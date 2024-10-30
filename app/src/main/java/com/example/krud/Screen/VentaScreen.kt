package com.example.krud.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.krud.Repository.VentaRepository
import com.example.krud.Repository.ProductoRepository
import com.example.krud.Model.VentaModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun VentaScreen(
    navController: NavController,
    ventaRepository: VentaRepository,
    productoRepository: ProductoRepository,
    clienteId: Int
) {
    val ventas = remember { mutableStateListOf<VentaModel>() }
    val scope = rememberCoroutineScope()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    LaunchedEffect(Unit) {
        scope.launch {
            val listaVentas = ventaRepository.obtenerVentasPorCliente(clienteId)
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
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Listado de Compras")

            Spacer(modifier = Modifier.height(16.dp))

            ventas.forEach { venta ->
                var productoNombre by remember { mutableStateOf("") }
                val formattedDate = remember(venta.fecha) {
                    dateFormat.format(Date(venta.fecha.toLong()))
                }

                LaunchedEffect(venta.producto_id) {
                    scope.launch {
                        val producto = productoRepository.obtenerProductoPorId(venta.producto_id)
                        productoNombre = producto?.nombre ?: "Producto no encontrado"
                    }
                }

                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "Producto: $productoNombre")
                    Text(text = "Cantidad: ${venta.cantidad}")
                    Text(text = "Fecha: $formattedDate")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
