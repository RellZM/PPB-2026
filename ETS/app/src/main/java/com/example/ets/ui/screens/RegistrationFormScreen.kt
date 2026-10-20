package com.example.ets.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ets.data.Member
import com.example.ets.ui.viewmodel.FormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationFormScreen(
    formState: FormState,
    onFullNameChange: (String) -> Unit,
    onNimNipChange: (String) -> Unit,
    onStudyProgramChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onMembershipTypeChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pendaftaran Anggota") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = formState.fullName,
                onValueChange = onFullNameChange,
                label = { Text("Nama Lengkap *") },
                isError = formState.fullNameError != null,
                supportingText = formState.fullNameError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.nimNip,
                onValueChange = onNimNipChange,
                label = { Text("NIM/NIP *") },
                isError = formState.nimNipError != null,
                supportingText = formState.nimNipError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.studyProgram,
                onValueChange = onStudyProgramChange,
                label = { Text("Program Studi *") },
                isError = formState.studyProgramError != null,
                supportingText = formState.studyProgramError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.address,
                onValueChange = onAddressChange,
                label = { Text("Alamat *") },
                isError = formState.addressError != null,
                supportingText = formState.addressError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.phone,
                onValueChange = onPhoneChange,
                label = { Text("No. Telepon *") },
                isError = formState.phoneError != null,
                supportingText = formState.phoneError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.email,
                onValueChange = onEmailChange,
                label = { Text("Email *") },
                isError = formState.emailError != null,
                supportingText = formState.emailError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = formState.membershipType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Jenis Keanggotaan *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Member.MEMBERSHIP_TYPES.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                onMembershipTypeChange(selectionOption)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("DAFTAR")
            }
        }
    }
}