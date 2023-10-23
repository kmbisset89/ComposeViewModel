package com.kmbisset89.ui.viewmodel

/**
 * An interface defining the contract for providing [PrimitiveDataStore] instances.
 *
 * This interface is aimed at abstracting the retrieval of saved primitive data states
 * for Composable view models. Implementing classes should provide a mechanism to fetch
 * the saved state based on a unique identifier.
 *
 * @see PrimitiveDataStore
 */
interface IPrimitiveDataStoreProvider {

    /**
     * Retrieves the saved [PrimitiveDataStore] for a given identifier.
     *
     * This method is responsible for returning the previously saved state
     * corresponding to the provided unique identifier.
     *
     * @param id A unique identifier for the Composable ViewModel.
     * @return The saved state if available, or `null` if not found.
     */
    fun getPrimitiveDataStore(id: String): PrimitiveDataStore?
}