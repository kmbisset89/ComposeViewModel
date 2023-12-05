package com.kmbisset89.ui.person

sealed class PlayerListInteractions {

    object ShowAddPlayerForm : PlayerListInteractions()

    data class PerformFormFinished(val formResult: PlayerFormResult) : PlayerListInteractions()

    data class SelectTab(val index: Int) : PlayerListInteractions()
}