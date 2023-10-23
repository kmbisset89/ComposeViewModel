package com.kmbisset89.ui.viewmodel

/**
 * An interface defining the contract for handling the state retention of Composable view models.
 *
 * This interface facilitates the registration and deregistration mechanisms for Composables
 * that require state retention across Android lifecycle events, especially configuration changes.
 * It provides methods to register a state gatherer function that, when invoked, will return
 * the current state to be saved, and to unregister a previously registered state gatherer to
 * prevent further state gathering for that particular Composable ViewModel.
 *
 * Implementations of this interface are expected to efficiently manage the lifecycle of registered
 * state gatherers, ensuring that state is properly captured and restored when needed.
 *
 * @see PrimitiveDataStore
 */
interface IComposableStateHandler {

    /**
     * Registers a Composable ViewModel for state retention.
     *
     * This method allows a Composable ViewModel to declare its intent to save its state
     * across configuration changes. The provided state gatherer function will be invoked
     * to fetch the current state when required.
     *
     * @param id A unique identifier for the Composable ViewModel, ensuring distinct state storage.
     * @param stateGatherer A function that, when invoked, will return the current state
     *                      of the Composable ViewModel to be saved.
     */
    fun registerForStateRetention(id : String, stateGatherer: () -> PrimitiveDataStore?)

    /**
     * Unregisters a Composable ViewModel from state retention.
     *
     * This method allows a Composable ViewModel to declare that it no longer requires its
     * state to be saved. This is useful to prevent unnecessary state gathering or in scenarios
     * where the ViewModel's lifecycle has ended.
     *
     * @param id The unique identifier previously used to register the Composable ViewModel.
     */
    fun unregisterForStateRetention(id : String)
}