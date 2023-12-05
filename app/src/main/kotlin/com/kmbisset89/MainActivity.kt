package com.kmbisset89

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kmbisset89.dal.person.Area
import com.kmbisset89.dal.person.Player
import com.kmbisset89.ui.person.PlayerFormResult
import com.kmbisset89.ui.person.PlayerFormViewModel
import com.kmbisset89.ui.person.PlayerListInteractions
import com.kmbisset89.ui.person.PlayerListViewModel
import com.kmbisset89.ui.theme.MyApplicationTheme
import com.kmbisset89.ui.viewmodel.AndroidStateHandler
import com.kmbisset89.ui.viewmodel.IComposableStateHandler
import com.kmbisset89.ui.viewmodel.IPrimitiveDataStoreProvider
import com.kmbisset89.ui.viewmodel.StatedComposableViewModel
import com.kmbisset89.ui.viewmodel.StatedNavComposableViewModel
import com.kmbisset89.ui.widget.TextEntry

/**
 * The main entry point for the application.
 *
 * [MainActivity] is responsible for rendering the UI associated with managing a list of persons.
 * The UI is defined in [PlayerListView]. It utilizes the [StatedComposableViewModel] composable function
 * to create and provide the [PlayerListViewModel] instance, which in turn handles the state and interactions
 * of the list of persons. This ViewModel is designed to retain and restore its state during lifecycle changes,
 * like orientation swaps, using [AndroidStateHandler].
 *
 * @see StatedComposableViewModel for more about stateful ViewModels designed for Compose.
 * @see AndroidStateHandler for state retention and restoration mechanism.
 * @see PlayerListViewModel for managing state and interactions of the person list.
 * @see PlayerListView for the visual representation of the person list.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                StatedNavComposableViewModel()
                StatedComposableViewModel(
                    primitiveDataStoreProvider = AndroidStateHandler,
                    composableStateHandler = AndroidStateHandler,
                    factory = { iPrimitiveDataStoreProvider: IPrimitiveDataStoreProvider, iComposableStateHandler: IComposableStateHandler ->
                        PlayerListViewModel(iPrimitiveDataStoreProvider, iComposableStateHandler, coroutineScope)
                    }) { vm ->
                    PlayerListView(vm)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
/**
 * Renders the main UI for displaying a list of persons and adding new ones.
 *
 * This function creates a UI layout that showcases the list of persons using [LazyColumn]. It also provides
 * a floating action button for adding new persons. Clicking the floating action button will open up
 * a [ModalBottomSheet] with a form UI for creating a new person.
 *
 * The UI and its interactions are driven by the provided [PlayerListViewModel] (referred as `vm` in the code).
 *
 * - If there's an ongoing person edit (as reflected by `vm.currentPersonEdited`), the bottom sheet with the
 *   form UI will be displayed.
 * - The list of persons (`vm.personList`) is displayed using a [LazyColumn] with individual [Card] elements.
 *
 * @param vm The [PlayerListViewModel] instance responsible for managing the state and interactions of the person list.
 *
 * @see PersonFormUi for the UI representation of the person form inside the bottom sheet.
 * @see PlayerFormViewModel for managing state and interactions of the person form.
 */
@Composable
fun PlayerListView(vm: PlayerListViewModel) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton({
                vm.onInteraction(PlayerListInteractions.ShowAddPlayerForm)
            }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Person")
            }
        },

        ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {

            TabRow(
                selectedTabIndex = vm.selectedTab.intValue,
                modifier = Modifier.fillMaxWidth(),
                tabs = {
                    vm.tabs.forEach {
                        Tab(
                            text = { Text(text = it) },
                            selected = vm.selectedTab.intValue == vm.tabs.indexOf(it),
                            onClick = {
                                vm.onInteraction(PlayerListInteractions.SelectTab(vm.tabs.indexOf(it)))
                            }
                        )
                    }
                })

            if (vm.currentPlayerEdited.value != null) {
                ModalBottomSheet(
                    onDismissRequest = {
                        vm.onInteraction(PlayerListInteractions.PerformFormFinished(PlayerFormResult.Cancelled))
                    },
                    sheetState = vm.bottomSheetState,
                    windowInsets = WindowInsets(0)
                ) {
                    StatedComposableViewModel(
                        primitiveDataStoreProvider = AndroidStateHandler,
                        composableStateHandler = AndroidStateHandler,
                        factory = { iPrimitiveDataStoreProvider: IPrimitiveDataStoreProvider, iComposableStateHandler: IComposableStateHandler ->
                            PlayerFormViewModel(
                                iPrimitiveDataStoreProvider,
                                iComposableStateHandler,
                            )
                        },
                        key1 = vm.currentPlayerEdited
                    ) { formViewModel ->
                        PersonFormUi(formViewModel, vm)
                    }
                }
            }

            LazyColumn {
                vm.playerList.value.forEach { person ->
                    item {
                        Card(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = person.firstName,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                            Text(
                                text = person.lastName,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Renders the UI for the person form inside a modal bottom sheet.
 *
 * This function constructs a UI layout for inputting a person's first name and last name.
 * After filling in the details, users have the option to either save the data or cancel the process.
 *
 * The input fields are rendered using the `TextEntry` composable, and two action buttons (Save and Cancel)
 * are provided to determine the next steps.
 *
 * This UI is interactive and reacts based on the state and functions provided by two ViewModels:
 * - [vmForm]: [PlayerFormViewModel] manages the form's state, specifically the inputs for first name and last name.
 * - [vmList]: [PlayerListViewModel] is responsible for adding the new person to the list or cancelling the operation.
 *
 * @param vmForm The [PlayerFormViewModel] instance which drives the state for the form inputs.
 * @param vmList The [PlayerListViewModel] instance used to interact with the list of persons.
 *
 * @see TextEntry for individual input fields representation.
 * @see PlayerListView for the overall person list UI.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersonFormUi(vmForm: PlayerFormViewModel, vmList: PlayerListViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(
            bottom = 100.dp
        )
    ) {
        TextEntry(
            label = "First Name",
            text = vmForm.personFirstName,
            Modifier.fillMaxWidth().padding(8.dp)
        )
        TextEntry(
            label = "Last Name",
            text = vmForm.personLastName,
            Modifier.fillMaxWidth().padding(8.dp)
        )
        Area.values().forEach {
            FilterChip(
                label = { Text(it.name) },
                selected = vmForm.selectedAreas.collectAsState().value.contains(it),
                onClick = {
                    vmForm.selectedAreas.value = vmForm.selectedAreas.value.toMutableSet().apply {
                        if (it in this) {
                            remove(it)
                        } else {
                            add(it)
                        }
                    }
                },
                modifier = Modifier.padding(4.dp).fillMaxWidth()
            )
        }

        Row {
            FilledTonalButton(onClick = {
                vmList.onInteraction(PlayerListInteractions.PerformFormFinished(PlayerFormResult.Cancelled))
            }, modifier = Modifier.weight(1f)) {
                Text(text = "Cancel")
            }
            Button(onClick = {
                vmList.onInteraction(
                    PlayerListInteractions.PerformFormFinished(
                        PlayerFormResult.FormFinished(
                            Player(
                                vmForm.personFirstName.value,
                                vmForm.personLastName.value,
                                vmForm.selectedAreas.value
                            )
                        )
                    )
                )
            }, modifier = Modifier.weight(1f)) {
                Text(text = "Save")
            }
        }
        Spacer(Modifier.navigationBarsPadding())
    }
}


