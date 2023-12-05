package com.kmbisset89.ui.person

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.kmbisset89.dal.person.Area
import com.kmbisset89.ui.viewmodel.IComposableStateHandler
import com.kmbisset89.ui.viewmodel.IPrimitiveDataStoreProvider
import com.kmbisset89.ui.viewmodel.PrimitiveDataStore
import com.kmbisset89.ui.viewmodel.StatedComposeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.json.Json

/**
 * A ViewModel tailored for managing the state and interactions related to the person form.
 *
 * This ViewModel manages the first name and last name fields of the form, and is responsible for
 * saving and restoring its state across lifecycle events. Its primary function is to hold onto the
 * data entered into the form, ensuring this data persists through configurations changes and other
 * interruptions.
 *
 * @param iPrimitiveDataStoreProvider The provider giving access to the [PrimitiveDataStore].
 * @param iComposableStateHandler The handler taking care of the ViewModel's lifecycle and state retention.
 *
 * @see StatedComposeViewModel for insights into stateful ViewModels tailored for Compose.
 * @see PrimitiveDataStore for understanding the data structure employed for state retention.
 */
class PlayerFormViewModel(
    iPrimitiveDataStoreProvider: IPrimitiveDataStoreProvider,
    iComposableStateHandler: IComposableStateHandler,
) : StatedComposeViewModel(
    iPrimitiveDataStoreProvider,
    iComposableStateHandler,
    id = "personForm"
) {
    companion object {
        private const val KEY_FIRST_NAME = "firstName"
        private const val KEY_LAST_NAME = "lastName"
        private const val KEY_SELECTED_AREAS = "selectedAreas"
    }

    /**
     * Represents the first name of the person. It can be observed and modified by the UI components.
     */
    internal val personFirstName = mutableStateOf(primitiveData?.getString(KEY_FIRST_NAME) ?: "")

    /**
     * Represents the last name of the person. It can be observed and modified by the UI components.
     */
    internal val personLastName = mutableStateOf(primitiveData?.getString(KEY_LAST_NAME) ?: "")

    internal val selectedAreas = MutableStateFlow(primitiveData?.getString(KEY_SELECTED_AREAS)?.let {
        Json.decodeFromString(SetSerializer(Area.serializer()), it)
    } ?: mutableSetOf())

    override fun retainState(): PrimitiveDataStore {
        return PrimitiveDataStore().apply {
            putString(KEY_FIRST_NAME, personFirstName.value)
            putString(KEY_LAST_NAME, personLastName.value)
            putString(KEY_SELECTED_AREAS, Json.encodeToString(SetSerializer(Area.serializer()), selectedAreas.value))
        }
    }


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