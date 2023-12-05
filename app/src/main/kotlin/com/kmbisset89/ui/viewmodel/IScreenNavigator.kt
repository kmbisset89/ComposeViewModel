package com.kmbisset89.ui.viewmodel

/**
 * An interface defining the contract for navigating between different screens within the application.
 *
 * This interface abstracts the navigation logic, providing a streamlined way to handle screen transitions
 * and manage the state associated with those transitions. Implementations of `IScreenNavigator` should
 * facilitate moving to a new screen and handling back navigation in a consistent and state-aware manner.
 */
interface IScreenNavigator {

    /**
     * Navigates to the specified screen, optionally providing a state to be passed.
     *
     * Implementations should use this method to handle the transition to different screens
     * within the application, managing the underlying logic for switching contexts and preserving state.
     *
     * @param screen The [Screen] instance representing the target screen to navigate to.
     * @param currentScreenState An optional [PrimitiveDataStore] containing the state of the current screen.
     *                           This can be used to preserve and pass the current state to the next screen.
     */
    fun navigateTo(screen: Screen, currentScreenState: PrimitiveDataStore? = null)

    /**
     * Handles the back navigation action.
     *
     * Implementations should use this method to manage the logic for navigating back
     * in the application's navigation stack, including state management and transition animations.
     */
    fun navigateBack()
}