package com.kmbisset89.ui.person

import androidx.compose.runtime.mutableStateOf
import com.kmbisset89.ui.viewmodel.ComposeViewModel

/**
 * A view model for managing the form inputs of a person.
 *
 * This view model is responsible for managing the first name and last name states of a person
 * in a Compose UI. The internal states are exposed as [mutableStateOf] properties which can be
 * observed and modified by the UI components.
 */
class PersonFormViewModel : ComposeViewModel() {

    /**
     * Represents the first name of the person. It can be observed and modified by the UI components.
     */
    internal val personFirstName = mutableStateOf("")

    /**
     * Represents the last name of the person. It can be observed and modified by the UI components.
     */
    internal val personLastName = mutableStateOf("")

    /**
     * A cleanup method when the view model is cleared.
     *
     * This method can be overridden if there are specific resources or tasks to be
     * canceled or cleaned up when the view model is no longer in use.
     */
    override suspend fun onClear() {
        // No implementation needed.
    }
}