package com.kmbisset89.ui.viewmodel

import androidx.compose.runtime.Composable

/**
 * An interface representing a screen within the application's navigation framework.
 *
 * This interface defines the core structure of a navigable screen within the app. It encapsulates
 * a Composable function, allowing the screen to be rendered with the necessary UI elements and state.
 * Implementations of `INavigationScreen` should provide the Composable logic that defines the
 * appearance and behavior of the screen.
 */
interface INavigationScreen {

    /**
     * A Composable function representing the visual and interactive elements of the screen.
     *
     * This property should contain the Composable content that makes up the screen. It can
     * optionally accept a [PrimitiveDataStore] containing any state or data required to render
     * the screen appropriately. This allows screens to be dynamic and responsive to the changing
     * state of the application.
     *
     * @param PrimitiveDataStore An optional data store containing state or data relevant to the screen.
     *                           Implementations can use this to initialize or update the screen's content.
     */
    val view : @Composable (PrimitiveDataStore?) -> Unit
}