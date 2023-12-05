package com.kmbisset89.ui.person

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.kmbisset89.dal.person.Area
import com.kmbisset89.dal.person.Player
import com.kmbisset89.ui.viewmodel.IComposableStateHandler
import com.kmbisset89.ui.viewmodel.IPrimitiveDataStoreProvider
import com.kmbisset89.ui.viewmodel.PrimitiveDataStore
import com.kmbisset89.ui.viewmodel.StatedComposeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

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
class PlayerListViewModel(
    iPrimitiveDataStoreProvider: IPrimitiveDataStoreProvider,
    iComposableStateHandler: IComposableStateHandler,
    private val composeScope: CoroutineScope,
) : StatedComposeViewModel(iPrimitiveDataStoreProvider, iComposableStateHandler, id = "playerList") {

    companion object {
        private const val KEY_BOTTOM_SHEET_STATE = "bottomSheetState"
        private const val KEY_LIST = "list"
        private const val KEY_TAB_SELECTED = "tabSelected"
    }

    internal val tabs: List<String> = listOf("All", "Offense", "Defense", "Special Teams")

    internal val selectedTab = mutableIntStateOf(
        primitiveData?.getInt(
            KEY_TAB_SELECTED,
        ) ?: 0
    )

    internal val bottomSheetState = SheetState(
        skipPartiallyExpanded = true,
        initialValue = SheetValue.values()[primitiveData?.getInt(
            KEY_BOTTOM_SHEET_STATE,
        ) ?: SheetValue.Hidden.ordinal],
        skipHiddenState = false
    )

    private val allPlayers = ArrayList(primitiveData?.getString(KEY_LIST)?.let {
        Json.decodeFromString(ListSerializer(Player.serializer()), it)
    } ?: emptyList()
    )

    internal val playerList = mutableStateOf(determinePlayerList())

    internal val currentPlayerEdited = mutableStateOf(
        if (primitiveData?.getInt(
                KEY_BOTTOM_SHEET_STATE,
            ) == SheetValue.Expanded.ordinal
        ) {
            Player("", "", setOf())
        } else {
            null
        }
    )

    override fun retainState(): PrimitiveDataStore {
        return PrimitiveDataStore().apply {
            putInt(KEY_BOTTOM_SHEET_STATE, bottomSheetState.currentValue.ordinal)
            putString(KEY_LIST, Json.encodeToString(ListSerializer(Player.serializer()), allPlayers))
        }
    }

    internal fun onInteraction(interactions: PlayerListInteractions) {
        when (interactions) {
            is PlayerListInteractions.ShowAddPlayerForm -> {
                composeScope.launch {
                    currentPlayerEdited.value = Player("", "", setOf())
                    bottomSheetState.expand()
                }
            }

            is PlayerListInteractions.PerformFormFinished -> {
                when (interactions.formResult) {
                    is PlayerFormResult.FormFinished -> {
                        allPlayers.add(interactions.formResult.player)
                        playerList.value = determinePlayerList()
                    }

                    PlayerFormResult.Cancelled -> {
                        // No implementation needed.
                    }
                }

                composeScope.launch {
                    bottomSheetState.hide()
                    currentPlayerEdited.value = null
                }
            }

            is PlayerListInteractions.SelectTab -> {
                selectedTab.intValue = interactions.index
                playerList.value = determinePlayerList()
            }
        }
    }

    private fun determinePlayerList(): List<Player> {
        return when (selectedTab.intValue) {
            Area.Offense.ordinal + 1 -> {
                allPlayers.filter { it.areas.contains(Area.Offense) }
            }

            Area.Defense.ordinal + 1 -> {
                allPlayers.filter { it.areas.contains(Area.Defense) }
            }

            Area.SpecialTeams.ordinal + 1 -> {
                allPlayers.filter { it.areas.contains(Area.SpecialTeams) }
            }

            else -> {
                allPlayers
            }
        }
    }
}