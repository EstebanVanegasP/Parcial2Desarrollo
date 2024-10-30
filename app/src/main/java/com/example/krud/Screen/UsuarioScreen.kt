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
import com.example.krud.Model.ClienteModel
import com.example.krud.Repository.ClienteRepository
import kotlinx.coroutines.launch

@Composable
fun UsuarioScreen(
    navController: NavController,
    clienteRepository: ClienteRepository
) {
    val scope = rememberCoroutineScope()
    val clientes = remember { mutableStateListOf<ClienteModel>() }
    var isDialogOpen by remember { mutableStateOf(false) }
    var clienteSeleccionado by remember { mutableStateOf<ClienteModel?>(null) }

    LaunchedEffect(Unit) {
        scope.launch {
            val listaClientes = clienteRepository.obtenerTodosLosClientes()
            clientes.clear()
            clientes.addAll(listaClientes)
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                content = {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        navController.navigate("ppScreen") {
                            popUpTo("ppScreen") { inclusive = true }
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
            Text(text = "Gestión de Usuarios", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(clientes) { cliente ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${cliente.nombre} (${cliente.correo})")
                        Row {
                            Button(onClick = {
                                clienteSeleccionado = cliente
                                isDialogOpen = true
                            }) {
                                Text("Editar")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                scope.launch {
                                    clienteRepository.eliminarCliente(cliente)
                                    clientes.remove(cliente)
                                }
                            }) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }

    if (isDialogOpen && clienteSeleccionado != null) {
        var nombreEditado by remember { mutableStateOf(clienteSeleccionado!!.nombre) }
        var correoEditado by remember { mutableStateOf(clienteSeleccionado!!.correo) }

        AlertDialog(
            onDismissRequest = { isDialogOpen = false },
            title = { Text("Editar Usuario") },
            text = {
                Column {
                    TextField(
                        value = nombreEditado,
                        onValueChange = { nombreEditado = it },
                        label = { Text("Nombre") }
                    )
                    TextField(
                        value = correoEditado,
                        onValueChange = { correoEditado = it },
                        label = { Text("Correo") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        val clienteActualizado = clienteSeleccionado!!.copy(
                            nombre = nombreEditado,
                            correo = correoEditado
                        )
                        clienteRepository.actualizarCliente(clienteActualizado)
                        val index = clientes.indexOfFirst { it.id == clienteActualizado.id }
                        if (index >= 0) {
                            clientes[index] = clienteActualizado
                        }
                        isDialogOpen = false
                    }
                }) {
                    Text("Guardar")
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
