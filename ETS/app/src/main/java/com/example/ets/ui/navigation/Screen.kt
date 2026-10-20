package com.example.ets.ui.navigation

import com.example.ets.data.Member

sealed class Screen {
    object Home : Screen()
    object Register : Screen()
    data class Success(val member: Member) : Screen()
    object MemberList : Screen()
    data class MemberDetail(val memberId: Long) : Screen()
    data class MemberEdit(val memberId: Long) : Screen()
}