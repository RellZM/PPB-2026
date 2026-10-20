package com.example.ets.data

data class Member(
    val id: Long = 0,
    val fullName: String,
    val nimNip: String,
    val studyProgram: String,
    val address: String,
    val phone: String,
    val email: String,
    val membershipType: String,
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        val MEMBERSHIP_TYPES = listOf("Mahasiswa", "Dosen", "Staf")
    }
}
