# Tugas9

Aplikasi Android sederhana untuk registrasi dan login pengguna secara lokal. Antarmuka dibuat dengan Jetpack Compose, sedangkan data akun disimpan di database Room pada perangkat.

## Fitur

- Registrasi akun dengan username dan password.
- Login dengan memeriksa akun yang tersimpan di perangkat.
- Pesan status untuk login/registrasi berhasil atau gagal.
- Penyimpanan data lokal menggunakan Room.

## Teknologi

- Kotlin
- Android Jetpack Compose dan Material 3
- AndroidX Lifecycle ViewModel
- Room
- Gradle Kotlin DSL

## Persyaratan

- Android Studio dengan Android SDK yang sesuai dengan konfigurasi project (`compileSdk 37`).
- JDK 11 atau lebih baru.
- Perangkat/emulator Android dengan API 24 atau lebih baru.

## Menjalankan aplikasi

1. Buka folder project ini di Android Studio.
2. Tunggu Gradle melakukan sync dan mengunduh dependency yang diperlukan.
3. Pilih emulator atau perangkat Android.
4. Jalankan konfigurasi `app`.

Atau gunakan Gradle Wrapper dari root project:

```powershell
.\gradlew.bat :app:assembleDebug
```

APK debug akan dibuat di `app/build/outputs/apk/debug/`.

## Cara menggunakan

1. Masukkan username dan password.
2. Tekan **Register** untuk membuat akun baru.
3. Setelah registrasi berhasil, gunakan kredensial yang sama dan tekan **Login**.

Data disimpan di database lokal aplikasi (`app_database`) dan tidak menggunakan layanan server.

## Struktur utama

```text
app/src/main/java/com/example/tugas9/
├── MainActivity.kt          # Entry point dan inisialisasi UI
├── LoginViewModel.kt        # Proses login, registrasi, dan state UI
├── LoginViewModelFactory.kt # Pembuatan ViewModel dengan UserDao
├── data/
│   ├── AppDatabase.kt       # Database Room
│   ├── User.kt              # Entitas pengguna
│   └── UserDao.kt           # Akses data pengguna
└── ui/
    ├── LoginScreen.kt       # Layar login dan registrasi
    └── theme/               # Tema Compose
```

## Catatan

Field pada model bernama `passwordHash`, tetapi saat ini aplikasi menerima dan membandingkan password yang dimasukkan secara langsung. Karena itu, project ini ditujukan sebagai contoh pembelajaran dan belum sesuai untuk menyimpan kredensial pada aplikasi produksi.
