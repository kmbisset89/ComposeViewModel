package com.kmbisset89.ui.viewmodel

/**
 * An abstract class representing a Composable ViewModel that requires state retention.
 *
 * This class provides the necessary infrastructure for Composable ViewModels that need
 * to retain their state across Android lifecycle events, such as configuration changes.
 * It utilizes a provided [IPrimitiveDataStoreProvider] to obtain its initial state
 * and an [IComposableStateHandler] to register and unregister itself for state retention.
 *
 * Implementing classes are expected to provide the logic for retaining their state
 * by implementing the [retainState] function.
 *
 * @param primitiveDataStoreProvider A provider interface that aids in fetching
 *                                      the initial state for the ViewModel.
 * @property retainer An interface that provides mechanisms to register and unregister
 *                    the ViewModel for state retention.
 * @property id A unique identifier for this ViewModel, ensuring distinct state storage.
 *
 * @see IPrimitiveDataStoreProvider
 * @see IComposableStateHandler
 */
abstract class StatedNavComposeViewModel(
    primitiveDataStoreProvider: IPrimitiveDataStoreProvider,
    navOrchestrator: INavigationOrchestrator,
    private val retainer: IComposableStateHandler,
    private val id: String,
) : ComposeViewModel(), INavigationOrchestrator by navOrchestrator {

    /**
     * Retrieves the initial state for this ViewModel using the provided [IPrimitiveDataStoreProvider].
     */
    protected val primitiveData = primitiveDataStoreProvider.getPrimitiveDataStore(id)

    init {
        retainer.registerForStateRetention(id) {
            retainState()
        }
    }

    /**
     * Abstract function to be implemented by subclasses, which defines how the ViewModel's
     * state should be retained. This function will be invoked when the state needs to be saved.
     *
     * @return The state to be retained in the form of a [PrimitiveDataStore].
     */
    abstract fun retainState(): PrimitiveDataStore?

    /**
     * Lifecycle-aware function which gets triggered when the ViewModel is being cleared.
     * Here, the ViewModel is unregistered from state retention, ensuring that its state
     * is no longer gathered post-clear.
     */
    override suspend fun onClear() {
        retainer.unregisterForStateRetention(id)
    }
}