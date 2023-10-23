package com.kmbisset89.ui.person

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.mutableStateOf
import com.kmbisset89.ui.viewmodel.IComposableStateHandler
import com.kmbisset89.ui.viewmodel.IPrimitiveDataStoreProvider
import com.kmbisset89.ui.viewmodel.PrimitiveDataStore
import com.kmbisset89.ui.viewmodel.StatedComposeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
/**
 * A ViewModel responsible for managing the state and interactions related to the list of persons.
 *
 * This ViewModel retains and restores its state during configuration changes like orientation swaps
 * using [iPrimitiveDataStoreProvider] and [iComposableStateHandler]. State retention is powered by
 * the [PrimitiveDataStore] which provides a granular mechanism for persisting and restoring state.
 *
 * @param iPrimitiveDataStoreProvider The provider for accessing the [PrimitiveDataStore].
 * @param iComposableStateHandler The handler responsible for managing the ViewModel's lifecycle and state retention.
 * @property composeScope The coroutine scope in which ViewModel's asynchronous operations are executed.
 *
 * @see StatedComposeViewModel for more about stateful ViewModels designed for Compose.
 * @see PrimitiveDataStore for the data structure responsible for state retention.
 */
class PersonListViewModel(
    iPrimitiveDataStoreProvider: IPrimitiveDataStoreProvider,
    iComposableStateHandler: IComposableStateHandler,
    private val composeScope: CoroutineScope,
) : StatedComposeViewModel(iPrimitiveDataStoreProvider, iComposableStateHandler, id = "personList") {

    companion object {
        private const val KEY_BOTTOM_SHEET_STATE = "bottomSheetState"
    }

    internal val bottomSheetState = SheetState(
        skipPartiallyExpanded = true,
        initialValue = SheetValue.values()[primitiveData?.getInt(
            KEY_BOTTOM_SHEET_STATE,
        ) ?: SheetValue.Hidden.ordinal],
        skipHiddenState = false
    )

    internal val personList = mutableStateOf<List<Person>>(emptyList())

    internal val currentPersonEdited = mutableStateOf(
        if (primitiveData?.getInt(
                KEY_BOTTOM_SHEET_STATE,
            ) == SheetValue.Expanded.ordinal
        ) {
            Person("", "")
        } else {
            null
        }
    )

    override fun retainState(): PrimitiveDataStore {
        return PrimitiveDataStore().apply {
            putInt(KEY_BOTTOM_SHEET_STATE, bottomSheetState.currentValue.ordinal)
        }
    }

    internal fun onInteraction(interactions: PersonListInteractions) {
        when (interactions) {
            is PersonListInteractions.ShowAddPersonForm -> {
                composeScope.launch {
                    currentPersonEdited.value = Person("", "")
                    bottomSheetState.expand()
                }
            }

            is PersonListInteractions.PerformFormFinished -> {
                when (interactions.formResult) {
                    is PersonFormResult.FormFinished -> {
                        personList.value += interactions.formResult.person
                    }

                    PersonFormResult.Cancelled -> {
                        // No implementation needed.
                    }
                }

                composeScope.launch {
                    bottomSheetState.hide()
                    currentPersonEdited.value = null
                }
            }
        }
    }
}