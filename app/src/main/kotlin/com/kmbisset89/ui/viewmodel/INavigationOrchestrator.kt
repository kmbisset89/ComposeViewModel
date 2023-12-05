package com.kmbisset89.ui.viewmodel

import kotlinx.coroutines.flow.StateFlow

/**
 * An interface that orchestrates the overall navigation within the application.
 *
 * This interface serves as a central coordinator for navigation-related activities,
 * managing the process of screen transitions, creation, and tracking the current screen in focus.
 * It interacts with the screen navigator and screen factory to ensure smooth and consistent
 * navigation experiences.
 *
 * @property currentScreen A [StateFlow] of [INavigationScreen] representing the current screen in the navigation flow.
 * @property screenNavigator The [IScreenNavigator] responsible for executing direct navigation actions.
 * @property screenFactory The [IScreenFactory] responsible for creating instances of screens as required by the navigation logic.
 */
interface INavigationOrchestrator {

    /**
     * Represents a reactive stream of the current screen being displayed.
     *
     * This [StateFlow] of [INavigationScreen] is dynamically updated to reflect the current screen
     * in focus within the application. It allows for reactive UI updates based on the current navigation state,
     * facilitating a more responsive and user-centric navigation experience.
     */
    val currentScreen: StateFlow<INavigationScreen>

    /**
     * The navigator component tasked with managing direct navigation actions.
     *
     * This [IScreenNavigator] instance handles the core navigation logic, such as moving to new screens
     * or handling back navigation. It is a crucial component in executing the navigation decisions made by the orchestrator.
     */
    val screenNavigator: IScreenNavigator

    /**
     * The factory component responsible for creating specific screen instances.
     *
     * This [IScreenFactory] instance offers the functionality to create screens dynamically based on
     * navigation requirements. It plays a key role in instantiating screens with appropriate context,
     * state, and UI elements, ensuring that each screen is suitable for the current navigation context.
     */
    val screenFactory: IScreenFactory
}