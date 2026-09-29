package com.example.kuis_01

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StudentViewModel : ViewModel() {
    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun addStudent(nim: String, name: String, programStudi: String) {
        _students.update { currentList ->
            currentList + Student(nim = nim, name = name, programStudi = programStudi)
        }
    }

    fun updateStudent(id: String, nim: String, name: String, programStudi: String) {
        _students.update { currentList ->
            currentList.map {
                if (it.id == id) {
                    it.copy(nim = nim, name = name, programStudi = programStudi)
                } else {
                    it
                }
            }
        }
    }

    fun deleteStudent(id: String) {
        _students.update { currentList ->
            currentList.filter { it.id != id }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getStudentById(id: String): Student? {
        return _students.value.find { it.id == id }
    }
}
