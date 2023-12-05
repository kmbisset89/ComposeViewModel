package com.kmbisset89.ui.viewmodel

/**
 * An interface defining the contract for creating screen instances within the application.
 *
 * This interface is designed to abstract the creation of screens, allowing for a modular and
 * dynamic approach to constructing UI components based on navigation requirements. Implementations
 * of `IScreenFactory` are responsible for instantiating screens with the appropriate state and
 * context, based on the provided parameters.
 */
interface IScreenFactory {

    /**
     * Creates and returns an instance of a screen corresponding to the specified screen name.
     *
     * This method is a key part of the navigation system, enabling the dynamic creation of screens
     * as users navigate through the application. It allows for the construction of screens with
     * specific state or data, ensuring that each screen is tailored to the current context and user
     * interaction.
     *
     * @param screenName The name of the screen to be created, typically used to identify the type
     *                   or class of screen required.
     * @param data An optional [PrimitiveDataStore] containing state or additional data necessary for
     *             initializing the screen.
     * @return An instance of [INavigationScreen] that represents the newly created screen.
     */
    fun createScreen(screenName: String, data: PrimitiveDataStore? = null): INavigationScreen
}