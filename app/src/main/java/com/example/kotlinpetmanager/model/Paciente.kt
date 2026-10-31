package com.example.kotlinpetmanager.model

data class Paciente(
    val id: Int,
    val nombre: String,
    val especie: String,
    val edad: Int,
    val esUrgencia: Boolean,
    val contactoEmergencia: String? = null
)