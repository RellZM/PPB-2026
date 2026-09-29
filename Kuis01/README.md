# Student Manager - Android App

Aplikasi Android sederhana untuk mengelola data mahasiswa (CRUD) menggunakan **Jetpack Compose**, **MVVM Architecture**, dan **StateFlow**.

## Fitur Utama

| Fitur | Deskripsi |
|-------|-----------|
| **Splash Screen** | Tampilan pembuka 2 detik dengan animasi progress |
| **Daftar Mahasiswa** | Menampilkan semua data mahasiswa dalam LazyColumn |
| **Pencarian Real-time** | Filter berdasarkan NIM, Nama, atau Program Studi |
| **Tambah Mahasiswa** | Form input dengan validasi field wajib |
| **Edit Mahasiswa** | Pre-fill form dengan data existing |
| **Hapus Mahasiswa** | Konfirmasi dialog sebelum hapus |
| **Dropdown Program Studi** | Pilihan terstruktur (6 program studi) |

## Arsitektur & Tech Stack

```
┌─────────────────────────────────────┐
│           MVVM Pattern              │
├─────────────────────────────────────┤
│  View (Compose UI)                  │
│  ├── StudentListScreen              │
│  ├── StudentFormScreen              │
│  └── SplashScreen                   │
├─────────────────────────────────────┤
│  ViewModel (StudentViewModel)       │
│  ├── StateFlow<List<Student>>       │
│  ├── StateFlow<String> (search)     │
│  └── CRUD Operations                │
├─────────────────────────────────────┤
│  Model (Student Data Class)         │
└─────────────────────────────────────┘
```

**Dependencies Utama:**
- **Jetpack Compose** (Material3) - UI toolkit modern
- **Navigation Compose** - Navigasi type-safe dengan argument
- **Lifecycle ViewModel Compose** - ViewModel lifecycle-aware
- **Kotlin Coroutines & Flow** - Reactive state management
- **Material Icons Extended** - Icon set lengkap

## Struktur Kode

```
app/src/main/java/com/example/kuis_01/
├── Student.kt              # Data model (id, nim, name, programStudi)
├── StudentViewModel.kt     # Business logic + StateFlow
├── MainActivity.kt         # Entry point + Scaffold root
├── AppNavigation.kt        # NavHost + route definitions
├── StudentListScreen.kt    # List + search + delete dialog
├── StudentFormScreen.kt    # Form add/edit + dropdown
├── SplashScreen.kt         # Animated splash (2s delay)
└── ui/theme/               # Material3 theming (Color, Type, Theme)
```

## Highlight Penjelasan Kode

### 1. **StudentViewModel.kt** - Reactive State Management
```kotlin
// StateFlow untuk reactive UI updates
private val _students = MutableStateFlow<List<Student>>(emptyList())
val students: StateFlow<List<Student>> = _students.asStateFlow()

// Thread-safe immutable updates
fun addStudent(nim: String, name: String, programStudi: String) {
    _students.update { currentList ->
        currentList + Student(nim = nim, name = name, programStudi = programStudi)
    }
}

// Search state terpisah dari data
private val _searchQuery = MutableStateFlow("")
val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
```
**Key Points:**
- `StateFlow` = cold stream, hanya emit saat di-collect
- `update { }` = atomic update, thread-safe
- `copy()` pada data class = immutable update pattern

### 2. **AppNavigation.kt** - Type-Safe Navigation
```kotlin
// Route dengan argument: "edit/{studentId}"
composable("edit/{studentId}") { backStackEntry ->
    val studentId = backStackEntry.arguments?.getString("studentId")
    StudentFormScreen(studentId = studentId, ...)
}
```
**Key Points:**
- `rememberNavController()` = single navigation controller
- `popUpTo("splash") { inclusive = true }` = clear back stack setelah splash
- Shared `StudentViewModel` instance via `viewModel()` di NavHost scope

### 3. **StudentListScreen.kt** - Declarative Filtering & Dialog
```kotlin
// Derived state - recompute otomatis saat searchQuery berubah
val filteredStudents = if (searchQuery.isBlank()) {
    students
} else {
    students.filter { it.name.contains(searchQuery, ignoreCase = true) || ... }
}

// Dialog state hoisted ke parent
var studentToDelete by remember { mutableStateOf<Student?>(null) }
if (studentToDelete != null) {
    AlertDialog(...) // Conditional rendering
}
```
**Key Points:**
- `collectAsState()` = observe Flow di Compose
- Derived state (`filteredStudents`) = no redundant state
- Dialog di luar Scaffold content = overlay atas semua UI

### 4. **StudentFormScreen.kt** - Edit vs Add Mode
```kotlin
// Single composable untuk 2 mode via nullable parameter
val isEdit = studentId != null
val student = if (studentId != null) viewModel.getStudentById(studentId) else null

// State initialized dari existing data (edit) atau kosong (add)
var nim by remember { mutableStateOf(student?.nim ?: "") }

// Dropdown read-only (user pilih, tidak ketik)
ExposedDropdownMenuBox(expanded = expanded) {
    OutlinedTextField(readOnly = true, ...)
    ExposedDropdownMenu(expanded = expanded) { ... }
}

// Button enabled hanya saat valid
enabled = nim.isNotBlank() && name.isNotBlank() && programStudi.isNotBlank()
```
**Key Points:**
- `studentId: String? = null` = optional param membedakan mode
- `ExposedDropdownMenuBox` = Material3 dropdown component
- Validasi inline di `enabled` = UX feedback real-time

### 5. **SplashScreen.kt** - Coroutine-based Delay
```kotlin
@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) { // Key = Unit = run once
        delay(2000)
        onTimeout() // Callback ke navigation
    }
    // UI statis selama delay
}
```
**Key Points:**
- `LaunchedEffect(Unit)` = side-effect yang jalan sekali saat compose
- `delay()` = non-blocking suspend, tidak freeze UI thread
- Callback pattern = decouple splash dari navigation logic

## Cara Menjalankan

```bash
# Clone & buka di Android Studio
./gradlew assembleDebug

# Atau run langsung dari Android Studio (Shift+F10)
```

**Requirements:**
- Android Studio Ladybug+
- JDK 11+
- minSdk 24 (Android 7.0)

## Screenshots Flow

```
Splash (2s) → Home (List) → [+] → Form Add
                    ↓
               Search/Filter
                    ↓
               [Edit] → Form Edit
                    ↓
               [Delete] → Confirm Dialog
```

## Testing

```bash
# Unit test
./gradlew test

# Instrumented test
./gradlew connectedAndroidTest
```

## License

Project ini dibuat untuk tujuan pembelajaran (Kuis 01 Mobile Development).