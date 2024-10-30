package com.example.krud.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ClienteScreen(
    navController: NavController,
    clienteId: Int
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bienvenido", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("productoScreen/$clienteId")
        }) {
            Text(text = "Gestionar Productos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("compraScreen/$clienteId")
        }) {
            Text(text = "Comprar Productos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("ventaScreen/$clienteId")
        }) {
            Text(text = "Ver Mis Compras")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("historialScreen/$clienteId")
        }) {
            Text(text = "Historial Completo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("usuarioScreen")
        }) {
            Text(text = "Gestionar Usuarios")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
