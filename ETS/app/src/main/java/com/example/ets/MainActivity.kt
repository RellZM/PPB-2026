package com.example.ets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ets.data.LibraryDatabaseHelper
import com.example.ets.data.MemberRepository
import com.example.ets.ui.navigation.Screen
import com.example.ets.ui.screens.HomeScreen
import com.example.ets.ui.screens.MemberDetailScreen
import com.example.ets.ui.screens.MemberEditScreen
import com.example.ets.ui.screens.MemberListScreen
import com.example.ets.ui.screens.RegistrationFormScreen
import com.example.ets.ui.screens.RegistrationSuccessScreen
import com.example.ets.ui.theme.ETSTheme
import com.example.ets.ui.viewmodel.LibraryViewModel

class MainActivity : ComponentActivity() {

    private lateinit var dbHelper: LibraryDatabaseHelper
    private lateinit var repository: MemberRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        dbHelper = LibraryDatabaseHelper(applicationContext)
        repository = MemberRepository(dbHelper)

        setContent {
            ETSTheme {
                val viewModel: LibraryViewModel = viewModel(
                    factory = LibraryViewModel.Factory(repository)
                )

                Surface(modifier = Modifier.fillMaxSize()) {
                    LibraryRegApp(viewModel = viewModel)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}

@Composable
fun LibraryRegApp(viewModel: LibraryViewModel) {
    // Backstack management
    val backStack = remember { mutableStateListOf<Screen>(Screen.Home) }
    val currentScreen = backStack.lastOrNull() ?: Screen.Home

    val members by viewModel.members.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedMember by viewModel.selectedMember.collectAsState()
    val formState by viewModel.formState.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()

    fun navigateTo(screen: Screen) {
        backStack.add(screen)
    }

    fun popBack() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.size - 1)
        }
    }

    fun popToHome() {
        backStack.clear()
        backStack.add(Screen.Home)
    }

    // Android physical/gesture back button handling
    BackHandler(enabled = backStack.size > 1) {
        popBack()
    }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "ScreenTransition"
    ) { screen ->
        when (screen) {
            is Screen.Home -> {
                HomeScreen(
                    members = members,
                    onNavigateToRegister = {
                        viewModel.prepareNewRegistration()
                        navigateTo(Screen.Register)
                    },
                    onNavigateToMemberList = { filterQuery ->
                        viewModel.onSearchQueryChanged(filterQuery)
                        navigateTo(Screen.MemberList)
                    }
                )
            }

            is Screen.Register -> {
                RegistrationFormScreen(
                    formState = formState,
                    onFullNameChange = viewModel::onFullNameChange,
                    onNimNipChange = viewModel::onNimNipChange,
                    onStudyProgramChange = viewModel::onStudyProgramChange,
                    onAddressChange = viewModel::onAddressChange,
                    onPhoneChange = viewModel::onPhoneChange,
                    onEmailChange = viewModel::onEmailChange,
                    onMembershipTypeChange = viewModel::onMembershipTypeChange,
                    onSubmit = {
                        viewModel.submitRegistration { registeredMember ->
                            // Replace current register screen with success screen
                            backStack.removeAt(backStack.size - 1)
                            backStack.add(Screen.Success(registeredMember))
                        }
                    },
                    onNavigateBack = { popBack() }
                )
            }

            is Screen.Success -> {
                RegistrationSuccessScreen(
                    member = screen.member,
                    onBackToHome = { popToHome() },
                    onViewInList = {
                        popToHome()
                        viewModel.loadMembers()
                        navigateTo(Screen.MemberList)
                    }
                )
            }

            is Screen.MemberList -> {
                MemberListScreen(
                    members = members,
                    searchQuery = searchQuery,
                    onSearchQueryChange = viewModel::onSearchQueryChanged,
                    onClearSearch = viewModel::clearSearch,
                    onMemberClick = { memberId ->
                        viewModel.selectMember(memberId)
                        navigateTo(Screen.MemberDetail(memberId))
                    },
                    onNavigateToRegister = {
                        viewModel.prepareNewRegistration()
                        navigateTo(Screen.Register)
                    },
                    onNavigateBack = { popBack() },
                    snackbarMessage = snackbarMessage,
                    onClearSnackbar = viewModel::clearSnackbar
                )
            }

            is Screen.MemberDetail -> {
                MemberDetailScreen(
                    member = selectedMember,
                    onEditClick = { memberId ->
                        selectedMember?.let { viewModel.prepareEdit(it) }
                        navigateTo(Screen.MemberEdit(memberId))
                    },
                    onDeleteConfirm = { memberId ->
                        viewModel.deleteMember(memberId) {
                            popBack() // return to MemberList
                        }
                    },
                    onNavigateBack = { popBack() }
                )
            }

            is Screen.MemberEdit -> {
                MemberEditScreen(
                    formState = formState,
                    onFullNameChange = viewModel::onFullNameChange,
                    onNimNipChange = viewModel::onNimNipChange,
                    onStudyProgramChange = viewModel::onStudyProgramChange,
                    onAddressChange = viewModel::onAddressChange,
                    onPhoneChange = viewModel::onPhoneChange,
                    onEmailChange = viewModel::onEmailChange,
                    onMembershipTypeChange = viewModel::onMembershipTypeChange,
                    onSubmit = {
                        viewModel.submitEdit {
                            popBack() // return to MemberDetail
                        }
                    },
                    onNavigateBack = { popBack() }
                )
            }
        }
    }
}