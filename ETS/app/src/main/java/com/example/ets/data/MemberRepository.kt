package com.example.ets.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemberRepository(private val dbHelper: LibraryDatabaseHelper) {

    suspend fun getAllMembers(): List<Member> = withContext(Dispatchers.IO) {
        dbHelper.getAllMembers()
    }

    suspend fun searchMembers(query: String): List<Member> = withContext(Dispatchers.IO) {
        if (query.isBlank()) {
            dbHelper.getAllMembers()
        } else {
            dbHelper.searchMembers(query)
        }
    }

    suspend fun getMemberById(id: Long): Member? = withContext(Dispatchers.IO) {
        dbHelper.getMemberById(id)
    }

    suspend fun insertMember(member: Member): Long = withContext(Dispatchers.IO) {
        dbHelper.insertMember(member)
    }

    suspend fun updateMember(member: Member): Int = withContext(Dispatchers.IO) {
        dbHelper.updateMember(member)
    }

    suspend fun deleteMember(id: Long): Int = withContext(Dispatchers.IO) {
        dbHelper.deleteMember(id)
    }

    suspend fun isNimNipExists(nimNip: String, excludeId: Long? = null): Boolean = withContext(Dispatchers.IO) {
        dbHelper.isNimNipExists(nimNip, excludeId)
    }
}
