# LibraryReg

LibraryReg adalah aplikasi Android untuk pendaftaran dan pengelolaan anggota perpustakaan. Aplikasi ini menyediakan dua alur utama: calon anggota dapat mengisi formulir pendaftaran, sedangkan petugas dapat melihat, mencari, mengubah, dan menghapus data anggota.

## Fitur

- Pendaftaran anggota baru.
- Validasi formulir dan pencegahan duplikasi NIM/NIP.
- Pilihan jenis anggota: Mahasiswa, Dosen, atau Staf.
- Daftar anggota dengan pencarian berdasarkan nama, NIM/NIP, atau program studi.
- Detail, edit, dan hapus data anggota.
- Ringkasan jumlah anggota pada halaman beranda.
- Data disimpan secara lokal menggunakan SQLite, tanpa koneksi internet.
- Database otomatis berisi tiga data contoh saat pertama kali dibuat.

## Teknologi

- Kotlin
- Jetpack Compose dan Material 3
- Android SDK 37 (minimum SDK 24)
- SQLite melalui `SQLiteOpenHelper`
- ViewModel dan Kotlin Coroutines
- Gradle Kotlin DSL

## Menjalankan project

1. Buka folder project ini menggunakan Android Studio.
2. Pastikan Android SDK dan JDK 17 tersedia.
3. Tunggu proses Gradle sync selesai.
4. Jalankan konfigurasi `app` pada emulator atau perangkat Android.

Atau gunakan Gradle Wrapper dari terminal:

```bash
./gradlew assembleDebug
```

Pada Windows PowerShell:

```powershell
.\gradlew.bat assembleDebug
```

APK debug akan tersedia di `app/build/outputs/apk/debug/`.

## Pengujian

```powershell
.\gradlew.bat test
.\gradlew.bat connectedAndroidTest
```

## Screenshot aplikasi

> Tambahkan screenshot aplikasi ke folder `screenshots/`, lalu ganti nama file
> di bawah sesuai screenshot yang tersedia.

### Beranda

![Beranda](<img width="720" height="1600" alt="WhatsApp Image 2026-10-20 at 14 12 54(1)" src="https://github.com/user-attachments/assets/fe53754a-5c5b-4ab9-972c-ed4e1dc236ea" />)

### Form pendaftaran anggota

![Form pendaftaran](<img width="720" height="1600" alt="WhatsApp Image 2026-10-20 at 14 12 53" src="https://github.com/user-attachments/assets/584607c6-741e-438d-9d5e-857bb1fad62a" />)

### Daftar anggota

![Daftar anggota](<img width="1080" height="2400" alt="WhatsApp Image 2026-10-20 at 14 12 53(1)" src="https://github.com/user-attachments/assets/2dba1266-3287-4310-b7d5-016bbb61493a" />)

### Detail anggota

![Detail anggota](<img width="1080" height="2400" alt="WhatsApp Image 2026-10-20 at 14 12 53(2)" src="https://github.com/user-attachments/assets/eb756a42-ebb5-4b25-98c0-91dec2331092" />)

## Struktur utama

```text
app/src/main/java/com/example/ets/
├── data/             # Model Member, repository, dan SQLite helper
├── ui/components/    # Komponen UI yang digunakan bersama
├── ui/navigation/    # Definisi tujuan layar
├── ui/screens/       # Halaman beranda, pendaftaran, dan pengelolaan anggota
├── ui/theme/         # Warna, tipografi, dan tema Compose
├── ui/viewmodel/     # State dan logika alur aplikasi
└── MainActivity.kt   # Entry point dan navigasi aplikasi
```

## Catatan data

Data anggota tersimpan di database lokal aplikasi dengan nama `library_reg.db`. Menghapus data aplikasi akan menghapus seluruh data anggota dan membuat ulang tiga data contoh pada pembukaan berikutnya.
