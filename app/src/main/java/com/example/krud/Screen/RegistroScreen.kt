package com.example.krud.Screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.krud.Model.ClienteModel
import com.example.krud.Repository.ClienteRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(navController: NavController, clienteRepository: ClienteRepository) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(16.dp)
            )
        } else {
            Button(
                onClick = {
                    if (nombre.isEmpty() || correo.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
                    } else if (!correo.contains("@")) {
                        Toast.makeText(context, "Por favor, ingrese un correo electrónico válido.", Toast.LENGTH_SHORT).show()
                    } else {
                        scope.launch {
                            isLoading = true
                            try {
                                val existingCliente = clienteRepository.obtenerClientePorCorreo(correo)
                                if (existingCliente != null) {
                                    Toast.makeText(context, "El correo ya está registrado.", Toast.LENGTH_SHORT).show()
                                } else {
                                    val cliente = ClienteModel(
                                        nombre = nombre,
                                        correo = correo,
                                        password = password
                                    )
                                    clienteRepository.insertarCliente(cliente)
                                    Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                    navController.navigate("loginScreen")
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error al registrar: ${e.message}", Toast.LENGTH_LONG).show()
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Registrarse")
            }
        }
    }
}
