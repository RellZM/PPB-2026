package com.example.ets.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ets.data.Member

@Composable
fun RegistrationSuccessScreen(
    member: Member,
    onBackToHome: () -> Unit,
    onViewInList: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Sukses",
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Pendaftaran Berhasil",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Nama   : ${member.fullName}")
                Text("NIM    : ${member.nimNip}")
                Text("Jenis  : ${member.membershipType}")
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onBackToHome,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Kembali ke Beranda")
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onViewInList,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Lihat Daftar Anggota")
        }
    }
}