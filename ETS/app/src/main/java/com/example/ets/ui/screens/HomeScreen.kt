package com.example.ets.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ets.data.Member

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    members: List<Member>,
    onNavigateToRegister: () -> Unit,
    onNavigateToMemberList: (String) -> Unit
) {
    val totalMembers = members.size
    val mahasiswaCount = members.count { it.membershipType == "Mahasiswa" }
    val dosenCount = members.count { it.membershipType == "Dosen" }
    val stafCount = members.count { it.membershipType == "Staf" }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "LibraryReg", 
                        fontWeight = FontWeight.Bold, 
                        color = MaterialTheme.colorScheme.primary
                    ) 
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            
            // Hero / Welcome Section
            Column {
                Text(
                    text = "Selamat Datang, \uD83D\uDC4B",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sistem Pendaftaran Anggota Perpustakaan",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Dashboard Stats Section
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Dashboard Statistik",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                // Total Card (Full Width)
                StatCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Total Anggota",
                    count = totalMembers.toString(),
                    icon = Icons.Default.Group,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = { onNavigateToMemberList("") }
                )

                // Grid 2x2 for Sub-stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Mahasiswa",
                        count = mahasiswaCount.toString(),
                        icon = Icons.Default.School,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary,
                        onClick = { onNavigateToMemberList("Mahasiswa") }
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Dosen",
                        count = dosenCount.toString(),
                        icon = Icons.Default.Person,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary,
                        onClick = { onNavigateToMemberList("Dosen") }
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Staf",
                        count = stafCount.toString(),
                        icon = Icons.Default.Work,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary,
                        onClick = { onNavigateToMemberList("Staf") }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            // Quick Actions Section
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Menu Utama",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                ElevatedCard(
                    onClick = onNavigateToRegister,
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle, 
                            contentDescription = null, 
                            modifier = Modifier.size(40.dp), 
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                "Pendaftaran Baru", 
                                style = MaterialTheme.typography.titleMedium, 
                                fontWeight = FontWeight.Bold, 
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Daftar sebagai anggota perpustakaan", 
                                style = MaterialTheme.typography.bodyMedium, 
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                Card(
                    onClick = { onNavigateToMemberList("") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ManageAccounts, 
                            contentDescription = null, 
                            modifier = Modifier.size(40.dp), 
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                "Kelola Anggota", 
                                style = MaterialTheme.typography.titleMedium, 
                                fontWeight = FontWeight.Bold, 
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Hanya untuk petugas perpustakaan", 
                                style = MaterialTheme.typography.bodyMedium, 
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    count: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (containerColor == Color.White) 2.dp else 0.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon, 
                contentDescription = null, 
                tint = contentColor, 
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = count, 
                style = MaterialTheme.typography.headlineMedium, 
                fontWeight = FontWeight.Bold, 
                color = contentColor
            )
            Text(
                text = title, 
                style = MaterialTheme.typography.bodyMedium, 
                color = contentColor.copy(alpha = 0.8f),
                maxLines = 1
            )
        }
    }
}