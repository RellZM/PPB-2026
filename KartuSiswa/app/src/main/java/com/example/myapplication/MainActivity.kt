package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProfileCardScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileCardScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6)) // Latar belakang abu-abu terang
    ) {
        // Latar Belakang Biru di Atas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(Color(0xFF013880)), // Warna biru ITS
            contentAlignment = Alignment.TopCenter
        ) {
            // Logo ITS (Tengah tanpa teks)
            Image(
                painter = painterResource(id = R.drawable.logo_its),
                contentDescription = "Logo ITS",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .height(80.dp) // Ukuran diubah agak lebih besar agar proposional karena teks dihilangkan
            )
        }

        // Card putih yang menimpa latar belakang ungu
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 160.dp, bottom = 24.dp), // Jarak ke bawah untuk menimpa header
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Foto Profil
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Foto Profil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "PROFIL MAHASISWA",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                ProfileInfoRow(
                    icon = Icons.Default.Person,
                    label = "Nama",
                    value = "Afrel Zharif Muflih"
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                ProfileInfoRow(
                    icon = Icons.Default.School,
                    label = "NRP",
                    value = "5053251023"
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                ProfileInfoRow(
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    label = "Program Studi",
                    value = "Rekayasa Perangkat Lunak"
                )

                Spacer(modifier = Modifier.height(24.dp))
                
                HorizontalDivider(color = Color.LightGray)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = Color(0xFF013880)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Mahasiswa", fontSize = 14.sp, color = Color.Black)
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Color(0xFF013880)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Semester 3", fontSize = 14.sp, color = Color.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF013880),
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.DarkGray // Dibuat lebih gelap dari Gray sebelumnya agar mudah dibaca
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black // Dibuat hitam tegas
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileCardPreview() {
    MyApplicationTheme {
        ProfileCardScreen()
    }
}
