package com.example.listmakan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
// removed icons import
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.listmakan.ui.theme.ListMakanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListMakanTheme {
                FoodMenuScreen()
            }
        }
    }
}

val RedCustom = Color(0xFFE53935)
val YellowCustom = Color(0xFFFFCA28)
val BackgroundYellow = Color(0xFFFFF9C4)

data class FoodItem(
    val name: String,
    val description: String,
    val price: String,
    val imageRes: Int
)

val sampleFoods = listOf(
    FoodItem("Walter Burger", "Say my name", "$5", R.drawable.burger),
    FoodItem("Cheese Pizza", "Tasty cheese pizza", "$8", R.drawable.pizza),
    FoodItem("Chicken Noodle", "Fresh and tasty", "$4", R.drawable.miayam),
    FoodItem("French Fries", "Crispy golden fries", "$3", R.drawable.kentang),
    FoodItem("Ice Cream", "Sweet vanilla ice cream", "$2", R.drawable.eskrim)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodMenuScreen() {
    var showCart by remember { mutableStateOf(false) }
    val cartItems = remember { mutableStateListOf<FoodItem>() }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Food Menu", fontWeight = FontWeight.Bold, color = YellowCustom) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RedCustom
                ),
                actions = {
                    IconButton(onClick = { showCart = true }) {
                        BadgedBox(
                            badge = {
                                if (cartItems.isNotEmpty()) {
                                    Badge(
                                        containerColor = YellowCustom,
                                        contentColor = RedCustom
                                    ) {
                                        Text(cartItems.size.toString(), fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        ) {
                            Text("🛒", fontSize = 24.sp)
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BackgroundYellow),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleFoods) { food ->
                FoodCard(
                    food = food,
                    onAddClick = { cartItems.add(food) }
                )
            }
        }
    }

    if (showCart) {
        ModalBottomSheet(
            onDismissRequest = { showCart = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            CartSheetContent(cartItems = cartItems)
        }
    }
}

@Composable
fun CartSheetContent(cartItems: List<FoodItem>) {
    val total = cartItems.sumOf { it.price.removePrefix("$").toIntOrNull() ?: 0 }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Your Cart",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = RedCustom,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (cartItems.isEmpty()) {
            Text(
                text = "Your cart is empty.",
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f, fill = false)
            ) {
                items(cartItems) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = item.name, fontSize = 16.sp)
                        Text(text = item.price, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Total", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "$$total", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = RedCustom)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { /* TODO: Implement checkout */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RedCustom,
                    contentColor = YellowCustom
                )
            ) {
                Text("Checkout", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun FoodCard(food: FoodItem, onAddClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = food.imageRes),
                contentDescription = food.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(YellowCustom)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = food.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = RedCustom
                    )
                    Text(
                        text = food.price,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = RedCustom
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = food.description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Button(
                    onClick = onAddClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YellowCustom,
                        contentColor = RedCustom
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .align(Alignment.End)
                        .height(36.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                ) {
                    Text("Add", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodMenuPreview() {
    ListMakanTheme {
        FoodMenuScreen()
    }
}
