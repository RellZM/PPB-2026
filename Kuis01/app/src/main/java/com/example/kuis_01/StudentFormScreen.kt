package com.example.kuis_01

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentFormScreen(
    viewModel: StudentViewModel,
    studentId: String? = null,
    onNavigateBack: () -> Unit
) {
    val isEdit = studentId != null
    val student = if (studentId != null) viewModel.getStudentById(studentId) else null

    var nim by remember { mutableStateOf(student?.nim ?: "") }
    var name by remember { mutableStateOf(student?.name ?: "") }
    var programStudi by remember { mutableStateOf(student?.programStudi ?: "") }

    var expanded by remember { mutableStateOf(false) }
    val programStudiOptions = listOf(
        "Informatika",
        "Sistem Informasi",
        "Teknik Komputer",
        "Desain Komunikasi Visual",
        "Manajemen",
        "Akuntansi"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) "Edit Mahasiswa" else "Tambah Mahasiswa", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1877F2)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("NIM", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = nim,
                onValueChange = { nim = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Masukkan NIM") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Nama", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Masukkan nama mahasiswa") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Program Studi", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = programStudi,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true),
                    placeholder = { Text("Pilih program studi") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    programStudiOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                programStudi = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Batal")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        if (studentId != null) {
                            viewModel.updateStudent(studentId, nim, name, programStudi)
                        } else {
                            viewModel.addStudent(nim, name, programStudi)
                        }
                        onNavigateBack()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2)),
                    enabled = nim.isNotBlank() && name.isNotBlank() && programStudi.isNotBlank()
                ) {
                    Text("Simpan")
                }
            }
        }
    }
}
