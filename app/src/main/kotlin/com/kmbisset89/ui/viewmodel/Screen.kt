package com.kmbisset89.ui.viewmodel

/**
 * Represents a screen within the application's navigation system.
 *
 * This data class encapsulates the information about a particular screen that is needed
 * for navigation actions. It includes the screen's name, which can be used to identify the screen,
 * and an optional data payload that can carry state or additional information required by the screen.
 *
 * @param name The unique name of the screen, used for identification in navigation actions.
 * @param data An optional [PrimitiveDataStore] carrying state or additional data for the screen.
 */
data class Screen(val name : String, val data : PrimitiveDataStore? = null)