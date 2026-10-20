package com.example.ets.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LibraryDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {
        const val DATABASE_NAME = "library_reg.db"
        const val DATABASE_VERSION = 1

        const val TABLE_MEMBERS = "members"
        const val COL_ID = "id"
        const val COL_FULL_NAME = "full_name"
        const val COL_NIM_NIP = "nim_nip"
        const val COL_STUDY_PROGRAM = "study_program"
        const val COL_ADDRESS = "address"
        const val COL_PHONE = "phone"
        const val COL_EMAIL = "email"
        const val COL_MEMBERSHIP_TYPE = "membership_type"
        const val COL_CREATED_AT = "created_at"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_MEMBERS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_FULL_NAME TEXT NOT NULL,
                $COL_NIM_NIP TEXT NOT NULL UNIQUE,
                $COL_STUDY_PROGRAM TEXT NOT NULL,
                $COL_ADDRESS TEXT NOT NULL,
                $COL_PHONE TEXT NOT NULL,
                $COL_EMAIL TEXT NOT NULL,
                $COL_MEMBERSHIP_TYPE TEXT NOT NULL,
                $COL_CREATED_AT INTEGER NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTableQuery)

        // Seed initial data for immediate demo & testing
        seedInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MEMBERS")
        onCreate(db)
    }

    private fun seedInitialData(db: SQLiteDatabase) {
        val initialMembers = listOf(
            Member(
                fullName = "Budi Santoso",
                nimNip = "5025211001",
                studyProgram = "Teknik Informatika",
                address = "Jl. Sukolilo No. 12, Surabaya",
                phone = "081234567890",
                email = "budi.santoso@mhs.ac.id",
                membershipType = "Mahasiswa"
            ),
            Member(
                fullName = "Dr. Sari Wulandari, M.T.",
                nimNip = "198001152005012001",
                studyProgram = "Sistem Informasi",
                address = "Jl. Gebang Wetan No. 45, Surabaya",
                phone = "081298765432",
                email = "sari.wulandari@kampus.ac.id",
                membershipType = "Dosen"
            ),
            Member(
                fullName = "Hendra Pratama, S.Kom.",
                nimNip = "198804122014021003",
                studyProgram = "UPT Perpustakaan",
                address = "Jl. Keputih Permai No. 8, Surabaya",
                phone = "085612345678",
                email = "hendra.pratama@kampus.ac.id",
                membershipType = "Staf"
            )
        )

        for (member in initialMembers) {
            val values = ContentValues().apply {
                put(COL_FULL_NAME, member.fullName)
                put(COL_NIM_NIP, member.nimNip)
                put(COL_STUDY_PROGRAM, member.studyProgram)
                put(COL_ADDRESS, member.address)
                put(COL_PHONE, member.phone)
                put(COL_EMAIL, member.email)
                put(COL_MEMBERSHIP_TYPE, member.membershipType)
                put(COL_CREATED_AT, member.createdAt)
            }
            db.insert(TABLE_MEMBERS, null, values)
        }
    }

    fun insertMember(member: Member): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_FULL_NAME, member.fullName.trim())
            put(COL_NIM_NIP, member.nimNip.trim())
            put(COL_STUDY_PROGRAM, member.studyProgram.trim())
            put(COL_ADDRESS, member.address.trim())
            put(COL_PHONE, member.phone.trim())
            put(COL_EMAIL, member.email.trim())
            put(COL_MEMBERSHIP_TYPE, member.membershipType)
            put(COL_CREATED_AT, member.createdAt)
        }
        return db.insert(TABLE_MEMBERS, null, values)
    }

    fun updateMember(member: Member): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_FULL_NAME, member.fullName.trim())
            put(COL_NIM_NIP, member.nimNip.trim())
            put(COL_STUDY_PROGRAM, member.studyProgram.trim())
            put(COL_ADDRESS, member.address.trim())
            put(COL_PHONE, member.phone.trim())
            put(COL_EMAIL, member.email.trim())
            put(COL_MEMBERSHIP_TYPE, member.membershipType)
        }
        return db.update(
            TABLE_MEMBERS,
            values,
            "$COL_ID = ?",
            arrayOf(member.id.toString())
        )
    }

    fun deleteMember(id: Long): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_MEMBERS,
            "$COL_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun getAllMembers(): List<Member> {
        val members = mutableListOf<Member>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_MEMBERS,
            null,
            null,
            null,
            null,
            null,
            "$COL_CREATED_AT DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                members.add(cursorToMember(it))
            }
        }
        return members
    }

    fun searchMembers(query: String): List<Member> {
        val members = mutableListOf<Member>()
        val db = readableDatabase
        val cleanQuery = "%${query.trim()}%"
        val selection = "$COL_FULL_NAME LIKE ? OR $COL_NIM_NIP LIKE ? OR $COL_STUDY_PROGRAM LIKE ? OR $COL_MEMBERSHIP_TYPE LIKE ?"
        val selectionArgs = arrayOf(cleanQuery, cleanQuery, cleanQuery, cleanQuery)

        val cursor = db.query(
            TABLE_MEMBERS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            "$COL_FULL_NAME ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                members.add(cursorToMember(it))
            }
        }
        return members
    }

    fun getMemberById(id: Long): Member? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_MEMBERS,
            null,
            "$COL_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        cursor.use {
            if (it.moveToFirst()) {
                return cursorToMember(it)
            }
        }
        return null
    }

    fun isNimNipExists(nimNip: String, excludeId: Long? = null): Boolean {
        val db = readableDatabase
        val selection: String
        val selectionArgs: Array<String>

        if (excludeId != null && excludeId > 0) {
            selection = "$COL_NIM_NIP = ? AND $COL_ID != ?"
            selectionArgs = arrayOf(nimNip.trim(), excludeId.toString())
        } else {
            selection = "$COL_NIM_NIP = ?"
            selectionArgs = arrayOf(nimNip.trim())
        }

        val cursor = db.query(
            TABLE_MEMBERS,
            arrayOf(COL_ID),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    private fun cursorToMember(cursor: Cursor): Member {
        return Member(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)),
            fullName = cursor.getString(cursor.getColumnIndexOrThrow(COL_FULL_NAME)),
            nimNip = cursor.getString(cursor.getColumnIndexOrThrow(COL_NIM_NIP)),
            studyProgram = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDY_PROGRAM)),
            address = cursor.getString(cursor.getColumnIndexOrThrow(COL_ADDRESS)),
            phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
            email = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)),
            membershipType = cursor.getString(cursor.getColumnIndexOrThrow(COL_MEMBERSHIP_TYPE)),
            createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(COL_CREATED_AT))
        )
    }
}
