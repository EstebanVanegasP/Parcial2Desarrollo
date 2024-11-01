package com.example.krud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.krud.Database.AppDatabase
import com.example.krud.Repository.ProductoRepository
import com.example.krud.Repository.ClienteRepository
import com.example.krud.Repository.VentaRepository
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.krud.Screen.*
import com.example.krud.ui.theme.KrudTheme

class MainActivity : ComponentActivity() {

    private lateinit var productoRepository: ProductoRepository
    private lateinit var clienteRepository: ClienteRepository
    private lateinit var ventaRepository: VentaRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        productoRepository = ProductoRepository(db.productoDao())
        clienteRepository = ClienteRepository(db.clienteDao())
        ventaRepository = VentaRepository(db.ventaDao())

        setContent {
            KrudTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    productoRepository = productoRepository,
                    clienteRepository = clienteRepository,
                    ventaRepository = ventaRepository
                )
            }
        }
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    productoRepository: ProductoRepository,
    clienteRepository: ClienteRepository,
    ventaRepository: VentaRepository
) {
    NavHost(navController = navController, startDestination = "ppScreen") {
        composable("ppScreen") {
            PpScreen(navController = navController)
        }


        composable("loginScreen") {
            LoginScreen(navController = navController, clienteRepository = clienteRepository)
        }


        composable("registroScreen") {
            RegistroScreen(navController = navController, clienteRepository = clienteRepository)
        }
        composable("clienteScreen/{clienteId}") { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getString("clienteId")?.toInt() ?: 0
            ClienteScreen(
                navController = navController,
                clienteId = clienteId
            )
        }
        composable("ventaScreen/{clienteId}") { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getString("clienteId")?.toInt() ?: 0
            VentaScreen(
                navController = navController,
                ventaRepository = ventaRepository,
                productoRepository = productoRepository,
                clienteId = clienteId
            )
        }
        composable("compraScreen/{clienteId}") { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getString("clienteId")?.toInt() ?: 0
            CompraScreen(
                navController = navController,
                productoRepository = productoRepository,
                ventaRepository = ventaRepository,
                clienteId = clienteId
            )
        }
        composable("productoScreen/{clienteId}") { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getString("clienteId")?.toInt() ?: 0
            ProductoScreen(
                navController = navController,
                productoRepository = productoRepository,
                clienteId = clienteId
            )
        }
        composable("usuarioScreen") { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getString("clienteId")?.toInt() ?: 0
            UsuarioScreen(
                navController = navController,
                clienteRepository = clienteRepository,
                clienteId = clienteId
            )
        }
        composable("historialScreen/{clienteId}") { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getString("clienteId")?.toInt() ?: 0
            HistorialScreen(
                navController = navController,
                ventaRepository = ventaRepository,
                productoRepository = productoRepository,
                clienteRepository = clienteRepository,
                clienteId = clienteId
            )
        }
    }
}
