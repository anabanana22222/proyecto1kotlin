package com.example.kotlinpetmanager.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.kotlinpetmanager.model.Paciente

class PacienteViewModel : ViewModel() {

    var pacientes by mutableStateOf(listOf<Paciente>())
        private set

    // formulario
    var nombre by mutableStateOf("")
        private set
    var especie by mutableStateOf("")
        private set
    var edad by mutableStateOf("")
        private set
    var contacto by mutableStateOf("")
        private set
    var esUrgencia by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf<String?>(null)
        private set

    private var siguienteId = 1

    fun onNombreChange(valor: String) { nombre = valor }
    fun onEspecieChange(valor: String) { especie = valor }
    fun onContactoChange(valor: String) { contacto = valor }
    fun onUrgenciaChange(valor: Boolean) { esUrgencia = valor }

    fun onEdadChange(valor: String) {
        //solo deja escribir números, antierrores
        if (valor.all { it.isDigit() }) edad = valor
    }

    fun agregarPaciente() {
        val edadNumero = edad.toIntOrNull()

        mensajeError = when {
            nombre.isBlank() -> "El nombre es obligatorio"
            especie.isBlank() -> "La especie y/o raza es obligatoria"
            edadNumero == null -> "Ingrese una edad"
            edadNumero > 100 -> "Ingrese una edad válida"
            else -> null
        }
        if (mensajeError != null || edadNumero == null) return

        val nuevo = Paciente(
            id = siguienteId++,
            nombre = nombre.trim(),
            especie = especie.trim(),
            edad = edadNumero,
            esUrgencia = esUrgencia,
            contactoEmergencia = contacto.trim().ifBlank { null }
        )

        // pacientes marcados urgentes tienen prioridad, asi que van primero en la lista
        pacientes = (pacientes + nuevo).sortedByDescending { it.esUrgencia }
        limpiarFormulario()
    }

    fun limpiarFormulario() {
        nombre = ""
        especie = ""
        edad = ""
        contacto = ""
        esUrgencia = false
        mensajeError = null
    }
}