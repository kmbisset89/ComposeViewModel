package com.kmbisset89

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.kmbisset89.ui.person.Person
import com.kmbisset89.ui.person.PersonFormResult
import com.kmbisset89.ui.person.PersonFormViewModel
import com.kmbisset89.ui.person.PersonListInteractions
import com.kmbisset89.ui.person.PersonListViewModel
import com.kmbisset89.ui.theme.MyApplicationTheme
import com.kmbisset89.ui.viewmodel.AndroidStateHandler
import com.kmbisset89.ui.viewmodel.IComposableStateHandler
import com.kmbisset89.ui.viewmodel.IPrimitiveDataStoreProvider
import com.kmbisset89.ui.viewmodel.StatedComposableViewModel
import com.kmbisset89.ui.viewmodel.StatedComposeViewModel
import com.kmbisset89.ui.widget.TextEntry

/**
 * The main entry point for the application.
 *
 * [MainActivity] is responsible for rendering the UI associated with managing a list of persons.
 * The UI is defined in [PersonListUi]. It utilizes the [StatedComposableViewModel] composable function
 * to create and provide the [PersonListViewModel] instance, which in turn handles the state and interactions
 * of the list of persons. This ViewModel is designed to retain and restore its state during lifecycle changes,
 * like orientation swaps, using [AndroidStateHandler].
 *
 * @see StatedComposableViewModel for more about stateful ViewModels designed for Compose.
 * @see AndroidStateHandler for state retention and restoration mechanism.
 * @see PersonListViewModel for managing state and interactions of the person list.
 * @see PersonListUi for the visual representation of the person list.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                val coroutineScope = rememberCoroutineScope()
                StatedComposableViewModel(
                    primitiveDataStoreProvider = AndroidStateHandler,
                    composableStateHandler = AndroidStateHandler,
                    factory = { iPrimitiveDataStoreProvider: IPrimitiveDataStoreProvider, iComposableStateHandler: IComposableStateHandler ->
                        PersonListViewModel(iPrimitiveDataStoreProvider, iComposableStateHandler, coroutineScope)
                    }) { vm ->
                    PersonListUi(vm)
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
 * The UI and its interactions are driven by the provided [PersonListViewModel] (referred as `vm` in the code).
 *
 * - If there's an ongoing person edit (as reflected by `vm.currentPersonEdited`), the bottom sheet with the
 *   form UI will be displayed.
 * - The list of persons (`vm.personList`) is displayed using a [LazyColumn] with individual [Card] elements.
 *
 * @param vm The [PersonListViewModel] instance responsible for managing the state and interactions of the person list.
 *
 * @see PersonFormUi for the UI representation of the person form inside the bottom sheet.
 * @see PersonFormViewModel for managing state and interactions of the person form.
 */
@Composable
private fun PersonListUi(vm: PersonListViewModel) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton({
                vm.onInteraction(PersonListInteractions.ShowAddPersonForm)
            }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Person")
            }
        },

    ) { contentPadding ->

        Column(modifier = Modifier.padding(contentPadding)) {
            if (vm.currentPersonEdited.value != null) {
                ModalBottomSheet(
                    onDismissRequest = {
                        vm.onInteraction(PersonListInteractions.PerformFormFinished(PersonFormResult.Cancelled))
                    },
                    sheetState = vm.bottomSheetState,
                    windowInsets = WindowInsets(0)
                ) {
                    StatedComposableViewModel(
                        primitiveDataStoreProvider = AndroidStateHandler,
                        composableStateHandler = AndroidStateHandler,
                        factory = { iPrimitiveDataStoreProvider: IPrimitiveDataStoreProvider, iComposableStateHandler: IComposableStateHandler ->
                            PersonFormViewModel(
                                iPrimitiveDataStoreProvider,
                                iComposableStateHandler,
                            )
                        },
                        key1 = vm.currentPersonEdited
                    ) { formViewModel ->
                        PersonFormUi(formViewModel, vm)
                    }
                }
            }

            LazyColumn {
                vm.personList.value.forEach { person ->
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
 * - [vmForm]: [PersonFormViewModel] manages the form's state, specifically the inputs for first name and last name.
 * - [vmList]: [PersonListViewModel] is responsible for adding the new person to the list or cancelling the operation.
 *
 * @param vmForm The [PersonFormViewModel] instance which drives the state for the form inputs.
 * @param vmList The [PersonListViewModel] instance used to interact with the list of persons.
 *
 * @see TextEntry for individual input fields representation.
 * @see PersonListUi for the overall person list UI.
 */
@Composable
private fun PersonFormUi(vmForm: PersonFormViewModel, vmList: PersonListViewModel) {
    Column(modifier = Modifier.fillMaxWidth().padding(
       bottom =  100.dp
    )) {
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
        Row {
            FilledTonalButton(onClick = {
                vmList.onInteraction(PersonListInteractions.PerformFormFinished(PersonFormResult.Cancelled))
            }, modifier = Modifier.weight(1f)) {
                Text(text = "Cancel")
            }
            Button(onClick = {
                vmList.onInteraction(
                    PersonListInteractions.PerformFormFinished(
                        PersonFormResult.FormFinished(
                            Person(
                                vmForm.personFirstName.value,
                                vmForm.personLastName.value
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


