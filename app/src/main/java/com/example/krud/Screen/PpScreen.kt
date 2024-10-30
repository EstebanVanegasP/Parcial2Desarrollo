package com.example.krud.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.krud.R

@Composable
fun PpScreen(navController: NavController) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.pp1_welcome),
            contentDescription = "Imagen de bienvenida",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 24.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "¡Bienvenido a la App de la canasta campesina \n " +
                    "Inicie sesión o Registrese",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )


        Button(
            onClick = {
                navController.navigate("loginScreen")
            },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 18.sp)
            )
        }
    }
}
