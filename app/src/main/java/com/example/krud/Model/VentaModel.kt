package com.example.krud.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ventas",
    foreignKeys = [
        ForeignKey(
            entity = ClienteModel::class,
            parentColumns = ["id"],
            childColumns = ["cliente_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["cliente_id"])]
)
data class VentaModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val producto_id: Int,
    val cliente_id: Int,
    val cantidad: Int,
    val fecha: String
)
