package com.kmbisset89.ui.person

sealed class PersonListInteractions {

    object ShowAddPersonForm : PersonListInteractions()

    data class PerformFormFinished(val formResult: PersonFormResult) : PersonListInteractions()
}