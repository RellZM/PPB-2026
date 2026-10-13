package com.example.kalkulators

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kalkulators.ui.theme.KalkulatorsTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KalkulatorsTheme {
                CalculatorApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Dua Angka (Tugas)", "Keypad Lengkap")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Kalkulator Sederhana",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = if (selectedTab == 0) "Mode Input Dua Angka" else "Mode Keypad Standar",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            if (selectedTab == 0) {
                SimpleTwoInputCalculatorScreen(
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                KeypadCalculatorScreen(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

// ==========================================
// 1. MODE DUA ANGKA (SESUAI LOGIKA KONSOL)
// ==========================================
@Composable
fun SimpleTwoInputCalculatorScreen(modifier: Modifier = Modifier) {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var selectedOperator by remember { mutableStateOf("+") }
    var resultText by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var formulaText by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    val operators = listOf(
        "+" to "Tambah (+)",
        "-" to "Kurang (-)",
        "*" to "Kali (×)",
        "/" to "Bagi (÷)",
        "%" to "Modulus (%)"
    )

    fun executeCalculation(op: String = selectedOperator) {
        focusManager.clearFocus()
        val clean1 = num1.trim().replace(',', '.')
        val clean2 = num2.trim().replace(',', '.')

        if (clean1.isEmpty() || clean2.isEmpty()) {
            isError = true
            resultText = "Harap masukkan angka pertama dan kedua!"
            formulaText = ""
            return
        }

        val n1 = clean1.toDoubleOrNull()
        val n2 = clean2.toDoubleOrNull()

        if (n1 == null || n2 == null) {
            isError = true
            resultText = "Format angka tidak valid!"
            formulaText = ""
            return
        }

        when (op) {
            "+" -> {
                val res = n1 + n2
                isError = false
                formulaText = "${formatNumber(n1)} + ${formatNumber(n2)} ="
                resultText = formatNumber(res)
            }
            "-" -> {
                val res = n1 - n2
                isError = false
                formulaText = "${formatNumber(n1)} - ${formatNumber(n2)} ="
                resultText = formatNumber(res)
            }
            "*" -> {
                val res = n1 * n2
                isError = false
                formulaText = "${formatNumber(n1)} × ${formatNumber(n2)} ="
                resultText = formatNumber(res)
            }
            "/" -> {
                if (n2 != 0.0) {
                    val res = n1 / n2
                    isError = false
                    formulaText = "${formatNumber(n1)} ÷ ${formatNumber(n2)} ="
                    resultText = formatNumber(res)
                } else {
                    isError = true
                    formulaText = "${formatNumber(n1)} ÷ 0 ="
                    resultText = "Error: Tidak bisa dibagi dengan nol!"
                }
            }
            "%" -> {
                if (n2 != 0.0) {
                    val res = n1 % n2
                    isError = false
                    formulaText = "${formatNumber(n1)} % ${formatNumber(n2)} ="
                    resultText = formatNumber(res)
                } else {
                    isError = true
                    formulaText = "${formatNumber(n1)} % 0 ="
                    resultText = "Error: Modulo dengan nol tidak terdefinisi!"
                }
            }
            else -> {
                isError = true
                formulaText = ""
                resultText = "Operator tidak valid!"
            }
        }
    }

    fun resetAll() {
        num1 = ""
        num2 = ""
        selectedOperator = "+"
        resultText = ""
        formulaText = ""
        isError = false
        focusManager.clearFocus()
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Kartu Tampilan Hasil
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = if (isError) {
                    MaterialTheme.colorScheme.errorContainer
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                }
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "HASIL PERHITUNGAN",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isError) {
                        MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (formulaText.isNotEmpty()) {
                    Text(
                        text = formulaText,
                        fontSize = 16.sp,
                        color = if (isError) {
                            MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.85f)
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (resultText.isEmpty()) "0" else resultText,
                    fontSize = if (resultText.length > 15) 24.sp else 34.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = if (isError) {
                        MaterialTheme.colorScheme.onErrorContainer
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    }
                )
            }
        }

        // Input Angka Pertama
        OutlinedTextField(
            value = num1,
            onValueChange = {
                num1 = it
                if (resultText.isNotEmpty() && !isError) executeCalculation()
            },
            label = { Text("Angka Pertama") },
            placeholder = { Text("Contoh: 12.5") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            trailingIcon = {
                if (num1.isNotEmpty()) {
                    TextButton(onClick = { num1 = "" }) {
                        Text("✕", fontSize = 16.sp)
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        // Pilihan Operator
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Pilih Operator:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf(
                    "+" to "+",
                    "-" to "−",
                    "*" to "×",
                    "/" to "÷",
                    "%" to "%"
                ).forEach { (op, label) ->
                    val isSelected = selectedOperator == op
                    FilledTonalButton(
                        onClick = {
                            selectedOperator = op
                            if (num1.isNotEmpty() && num2.isNotEmpty()) {
                                executeCalculation(op)
                            }
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            },
                            contentColor = if (isSelected) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .height(52.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Input Angka Kedua
        OutlinedTextField(
            value = num2,
            onValueChange = {
                num2 = it
                if (resultText.isNotEmpty() && !isError) executeCalculation()
            },
            label = { Text("Angka Kedua") },
            placeholder = { Text("Contoh: 4") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { executeCalculation() }
            ),
            trailingIcon = {
                if (num2.isNotEmpty()) {
                    TextButton(onClick = { num2 = "" }) {
                        Text("✕", fontSize = 16.sp)
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Tombol Aksi: Hitung dan Reset
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { resetAll() },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
            ) {
                Text(
                    text = "Reset (C)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = { executeCalculation() },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
            ) {
                Text(
                    text = "Hitung (=)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ==========================================
// 2. MODE KEYPAD (GRID KALKULATOR PENUH)
// ==========================================
@Composable
fun KeypadCalculatorScreen(modifier: Modifier = Modifier) {
    var display by remember { mutableStateOf("0") }
    var history by remember { mutableStateOf("") }
    var previousValue by remember { mutableStateOf<Double?>(null) }
    var pendingOperator by remember { mutableStateOf<String?>(null) }
    var isNewInput by remember { mutableStateOf(true) }

    fun onDigit(d: String) {
        if (isNewInput) {
            display = d
            isNewInput = false
        } else {
            if (display == "0") {
                display = d
            } else if (display.length < 12) {
                display += d
            }
        }
    }

    fun onDot() {
        if (isNewInput) {
            display = "0."
            isNewInput = false
        } else if (!display.contains(".")) {
            display += "."
        }
    }

    fun onClear() {
        display = "0"
        history = ""
        previousValue = null
        pendingOperator = null
        isNewInput = true
    }

    fun onDelete() {
        if (!isNewInput && display.isNotEmpty()) {
            display = if (display.length > 1) {
                display.dropLast(1)
            } else {
                "0"
            }
        }
    }

    fun onToggleSign() {
        val current = display.toDoubleOrNull() ?: return
        display = formatNumber(-current)
    }

    fun onPercent() {
        val current = display.toDoubleOrNull() ?: return
        display = formatNumber(current / 100.0)
    }

    fun onOperator(op: String) {
        val current = display.toDoubleOrNull() ?: return

        if (previousValue != null && pendingOperator != null && !isNewInput) {
            val res = compute(previousValue!!, current, pendingOperator!!)
            if (res == null) {
                display = "Error: Bagi 0"
                history = ""
                previousValue = null
                pendingOperator = null
                isNewInput = true
                return
            } else {
                display = formatNumber(res)
                previousValue = res
            }
        } else {
            previousValue = current
        }

        pendingOperator = op
        val opSymbol = when (op) {
            "+" -> "+"
            "-" -> "−"
            "*" -> "×"
            "/" -> "÷"
            else -> op
        }
        history = "${formatNumber(previousValue!!)} $opSymbol"
        isNewInput = true
    }

    fun onEquals() {
        val current = display.toDoubleOrNull() ?: return
        val prev = previousValue
        val op = pendingOperator

        if (prev != null && op != null) {
            val res = compute(prev, current, op)
            val opSymbol = when (op) {
                "+" -> "+"
                "-" -> "−"
                "*" -> "×"
                "/" -> "÷"
                else -> op
            }
            if (res == null) {
                display = "Error: Bagi 0"
                history = "$prev $opSymbol $current ="
            } else {
                history = "${formatNumber(prev)} $opSymbol ${formatNumber(current)} ="
                display = formatNumber(res)
            }
            previousValue = null
            pendingOperator = null
            isNewInput = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Layar Tampilan Angka
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = history,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = display,
                    fontSize = if (display.length > 9) 36.sp else 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
            }
        }

        // Grid Tombol
        val buttonSpacing = 10.dp
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            // Baris 1: C, ⌫, %, ÷
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                KeypadButton("C", Modifier.weight(1f), KeypadButtonType.Action) { onClear() }
                KeypadButton("⌫", Modifier.weight(1f), KeypadButtonType.Action) { onDelete() }
                KeypadButton("%", Modifier.weight(1f), KeypadButtonType.Action) { onPercent() }
                KeypadButton("÷", Modifier.weight(1f), KeypadButtonType.Operator) { onOperator("/") }
            }

            // Baris 2: 7, 8, 9, ×
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                KeypadButton("7", Modifier.weight(1f), KeypadButtonType.Number) { onDigit("7") }
                KeypadButton("8", Modifier.weight(1f), KeypadButtonType.Number) { onDigit("8") }
                KeypadButton("9", Modifier.weight(1f), KeypadButtonType.Number) { onDigit("9") }
                KeypadButton("×", Modifier.weight(1f), KeypadButtonType.Operator) { onOperator("*") }
            }

            // Baris 3: 4, 5, 6, −
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                KeypadButton("4", Modifier.weight(1f), KeypadButtonType.Number) { onDigit("4") }
                KeypadButton("5", Modifier.weight(1f), KeypadButtonType.Number) { onDigit("5") }
                KeypadButton("6", Modifier.weight(1f), KeypadButtonType.Number) { onDigit("6") }
                KeypadButton("−", Modifier.weight(1f), KeypadButtonType.Operator) { onOperator("-") }
            }

            // Baris 4: 1, 2, 3, +
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                KeypadButton("1", Modifier.weight(1f), KeypadButtonType.Number) { onDigit("1") }
                KeypadButton("2", Modifier.weight(1f), KeypadButtonType.Number) { onDigit("2") }
                KeypadButton("3", Modifier.weight(1f), KeypadButtonType.Number) { onDigit("3") }
                KeypadButton("+", Modifier.weight(1f), KeypadButtonType.Operator) { onOperator("+") }
            }

            // Baris 5: ±, 0, ., =
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                KeypadButton("±", Modifier.weight(1f), KeypadButtonType.Number) { onToggleSign() }
                KeypadButton("0", Modifier.weight(1f), KeypadButtonType.Number) { onDigit("0") }
                KeypadButton(".", Modifier.weight(1f), KeypadButtonType.Number) { onDot() }
                KeypadButton("=", Modifier.weight(1f), KeypadButtonType.Equals) { onEquals() }
            }
        }
    }
}

enum class KeypadButtonType {
    Number, Operator, Action, Equals
}

@Composable
fun KeypadButton(
    text: String,
    modifier: Modifier = Modifier,
    type: KeypadButtonType = KeypadButtonType.Number,
    onClick: () -> Unit
) {
    val containerColor = when (type) {
        KeypadButtonType.Number -> MaterialTheme.colorScheme.surfaceVariant
        KeypadButtonType.Operator -> MaterialTheme.colorScheme.primaryContainer
        KeypadButtonType.Action -> MaterialTheme.colorScheme.secondaryContainer
        KeypadButtonType.Equals -> MaterialTheme.colorScheme.primary
    }

    val contentColor = when (type) {
        KeypadButtonType.Number -> MaterialTheme.colorScheme.onSurfaceVariant
        KeypadButtonType.Operator -> MaterialTheme.colorScheme.onPrimaryContainer
        KeypadButtonType.Action -> MaterialTheme.colorScheme.onSecondaryContainer
        KeypadButtonType.Equals -> MaterialTheme.colorScheme.onPrimary
    }

    FilledTonalButton(
        onClick = onClick,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .height(64.dp)
    ) {
        Text(
            text = text,
            fontSize = 22.sp,
            fontWeight = if (type == KeypadButtonType.Number) FontWeight.Normal else FontWeight.Bold
        )
    }
}

// ==========================================
// FUNGSI UTILITAS PERHITUNGAN & FORMATTING
// ==========================================
fun compute(n1: Double, n2: Double, op: String): Double? {
    return when (op) {
        "+" -> n1 + n2
        "-" -> n1 - n2
        "*" -> n1 * n2
        "/" -> if (n2 != 0.0) n1 / n2 else null
        "%" -> if (n2 != 0.0) n1 % n2 else null
        else -> null
    }
}

fun formatNumber(value: Double): String {
    return if (value.isNaN() || value.isInfinite()) {
        "Error"
    } else if (value % 1.0 == 0.0 && value >= Long.MIN_VALUE && value <= Long.MAX_VALUE) {
        value.toLong().toString()
    } else {
        val formatted = String.format(Locale.US, "%.8f", value).trimEnd('0').trimEnd('.')
        formatted
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorAppPreview() {
    KalkulatorsTheme {
        CalculatorApp()
    }
}