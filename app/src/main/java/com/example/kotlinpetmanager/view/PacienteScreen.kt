package com.example.kotlinpetmanager.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.kotlinpetmanager.model.Paciente
import com.example.kotlinpetmanager.viewmodel.PacienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteScreen(viewModel: PacienteViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KotlinPet Manager 🐾") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FormularioPaciente(viewModel)

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Pacientes registrados (${viewModel.pacientes.size})",
                style = MaterialTheme.typography.titleMedium
            )

            // weight(1f) hace que la lista ocupe el espacio de abajo
            ListaPacientes(viewModel.pacientes, Modifier.weight(1f))
        }
    }
}

@Composable
fun FormularioPaciente(viewModel: PacienteViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = viewModel.nombre,
            onValueChange = { viewModel.onNombreChange(it) },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.especie,
            onValueChange = { viewModel.onEspecieChange(it) },
            label = { Text("Especie / Raza") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // hacer que edad y urgencia esten en la misma fila
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.edad,
                onValueChange = { viewModel.onEdadChange(it) },
                label = { Text("Edad") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = viewModel.esUrgencia,
                    onCheckedChange = { viewModel.onUrgenciaChange(it) }
                )
                Spacer(Modifier.width(8.dp))
                Text("Urgencia")
            }
        }

        // Contacto de emergencia va después de la edad
        OutlinedTextField(
            value = viewModel.contacto,
            onValueChange = { viewModel.onContactoChange(it) },
            label = { Text("Contacto de emergencia") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        viewModel.mensajeError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.limpiarFormulario() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Limpiar")
            }
            Button(
                onClick = { viewModel.agregarPaciente() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Registrar Paciente")
            }
        }
    }
}

@Composable
fun ListaPacientes(pacientes: List<Paciente>, modifier: Modifier = Modifier) {
    if (pacientes.isEmpty()) {
        Text(
            text = "Aún no hay pacientes registrados",
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier
        )
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(pacientes, key = { it.id }) { paciente ->
                TarjetaPaciente(paciente)
            }
        }
    }
}

@Composable
fun TarjetaPaciente(paciente: Paciente) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${paciente.id} ${paciente.nombre}",
                    style = MaterialTheme.typography.titleMedium
                )
                if (paciente.esUrgencia) {
                    Text(
                        text = "🚨 Urgencia",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            Text("${paciente.especie} · ${paciente.edad} años")
            Text(
                text = "Contacto: ${paciente.contactoEmergencia ?: "Sin contacto registrado"}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
