package com.example.ets.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ets.data.Member
import com.example.ets.data.MemberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FormState(
    val fullName: String = "",
    val nimNip: String = "",
    val studyProgram: String = "",
    val address: String = "",
    val phone: String = "",
    val email: String = "",
    val membershipType: String = Member.MEMBERSHIP_TYPES.first(),
    val fullNameError: String? = null,
    val nimNipError: String? = null,
    val studyProgramError: String? = null,
    val addressError: String? = null,
    val phoneError: String? = null,
    val emailError: String? = null,
    val editingMemberId: Long? = null
) {
    val isValid: Boolean
        get() = fullName.isNotBlank() && nimNip.isNotBlank() && studyProgram.isNotBlank() &&
                address.isNotBlank() && phone.isNotBlank() && email.isNotBlank() &&
                fullNameError == null && nimNipError == null && studyProgramError == null &&
                addressError == null && phoneError == null && emailError == null
}

class LibraryViewModel(private val repository: MemberRepository) : ViewModel() {

    private val _members = MutableStateFlow<List<Member>>(emptyList())
    val members: StateFlow<List<Member>> = _members.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedMember = MutableStateFlow<Member?>(null)
    val selectedMember: StateFlow<Member?> = _selectedMember.asStateFlow()

    private val _formState = MutableStateFlow(FormState())
    val formState: StateFlow<FormState> = _formState.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    init {
        loadMembers()
    }

    fun loadMembers() {
        viewModelScope.launch {
            _members.value = repository.getAllMembers()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _members.value = repository.searchMembers(query)
        }
    }

    fun clearSearch() {
        onSearchQueryChanged("")
    }

    fun selectMember(id: Long) {
        viewModelScope.launch {
            _selectedMember.value = repository.getMemberById(id)
        }
    }

    fun prepareNewRegistration() {
        _formState.value = FormState()
    }

    fun prepareEdit(member: Member) {
        _formState.value = FormState(
            fullName = member.fullName,
            nimNip = member.nimNip,
            studyProgram = member.studyProgram,
            address = member.address,
            phone = member.phone,
            email = member.email,
            membershipType = member.membershipType,
            editingMemberId = member.id
        )
    }

    fun onFullNameChange(value: String) {
        _formState.update { it.copy(fullName = value, fullNameError = if (value.isBlank()) "Nama lengkap wajib diisi" else null) }
    }

    fun onNimNipChange(value: String) {
        val error = when {
            value.isBlank() -> "NIM/NIP wajib diisi"
            !value.all { it.isDigit() } -> "NIM/NIP harus berupa angka"
            else -> null
        }
        _formState.update { it.copy(nimNip = value, nimNipError = error) }
    }

    fun onStudyProgramChange(value: String) {
        _formState.update { it.copy(studyProgram = value, studyProgramError = if (value.isBlank()) "Program studi wajib diisi" else null) }
    }

    fun onAddressChange(value: String) {
        _formState.update { it.copy(address = value, addressError = if (value.isBlank()) "Alamat wajib diisi" else null) }
    }

    fun onPhoneChange(value: String) {
        val error = when {
            value.isBlank() -> "Nomor telepon wajib diisi"
            !value.all { it.isDigit() } -> "Nomor telepon harus berupa angka"
            else -> null
        }
        _formState.update { it.copy(phone = value, phoneError = error) }
    }

    fun onEmailChange(value: String) {
        val error = when {
            value.isBlank() -> "Email wajib diisi"
            !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> "Format email salah"
            else -> null
        }
        _formState.update { it.copy(email = value, emailError = error) }
    }

    fun onMembershipTypeChange(value: String) {
        _formState.update { it.copy(membershipType = value) }
    }

    fun validateForm(isEdit: Boolean = false, onSuccess: suspend () -> Unit) {
        val state = _formState.value
        
        onFullNameChange(state.fullName)
        onNimNipChange(state.nimNip)
        onStudyProgramChange(state.studyProgram)
        onAddressChange(state.address)
        onPhoneChange(state.phone)
        onEmailChange(state.email)

        val updatedState = _formState.value
        if (updatedState.isValid) {
            viewModelScope.launch {
                val exists = repository.isNimNipExists(updatedState.nimNip, updatedState.editingMemberId)
                if (exists) {
                    _formState.update { it.copy(nimNipError = "NIM/NIP sudah terdaftar") }
                } else {
                    onSuccess()
                }
            }
        }
    }

    fun submitRegistration(onSuccess: (Member) -> Unit) {
        validateForm {
            val state = _formState.value
            val member = Member(
                fullName = state.fullName,
                nimNip = state.nimNip,
                studyProgram = state.studyProgram,
                address = state.address,
                phone = state.phone,
                email = state.email,
                membershipType = state.membershipType
            )
            val id = repository.insertMember(member)
            val newMember = member.copy(id = id)
            loadMembers() 
            onSuccess(newMember)
        }
    }

    fun submitEdit(onSuccess: () -> Unit) {
        validateForm(isEdit = true) {
            val state = _formState.value
            val id = state.editingMemberId ?: return@validateForm
            val member = Member(
                id = id,
                fullName = state.fullName,
                nimNip = state.nimNip,
                studyProgram = state.studyProgram,
                address = state.address,
                phone = state.phone,
                email = state.email,
                membershipType = state.membershipType
            )
            repository.updateMember(member)
            _selectedMember.value = member
            loadMembers() 
            _snackbarMessage.value = "Anggota berhasil diperbarui"
            onSuccess()
        }
    }

    fun deleteMember(id: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deleteMember(id)
            loadMembers()
            _snackbarMessage.value = "Anggota dihapus"
            onSuccess()
        }
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    class Factory(private val repository: MemberRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
                return LibraryViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}