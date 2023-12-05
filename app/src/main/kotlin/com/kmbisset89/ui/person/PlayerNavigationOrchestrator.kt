package com.kmbisset89.ui.person

import com.kmbisset89.PlayerListView
import com.kmbisset89.ui.viewmodel.INavigationOrchestrator
import com.kmbisset89.ui.viewmodel.INavigationScreen
import com.kmbisset89.ui.viewmodel.IScreenFactory
import com.kmbisset89.ui.viewmodel.IScreenNavigator
import com.kmbisset89.ui.viewmodel.PrimitiveDataStore
import com.kmbisset89.ui.viewmodel.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerNavigationOrchestrator(initialScreen: Screen) : INavigationOrchestrator {

    private val navigationStack = ArrayDeque<Screen>().addFirst(initialScreen)



    override val screenFactory: IScreenFactory = object : IScreenFactory {
        override fun createScreen(screenName: String, data: PrimitiveDataStore?): INavigationScreen {
            return when (screenName) {
                "PlayerList" -> PlayerListView()
                "PlayerForm" -> PlayerFormScreen(data)
                else -> throw IllegalArgumentException("Unknown screen name: $screenName")
            }
        }
    }

    override val screenNavigator: IScreenNavigator = object : IScreenNavigator {
        override fun navigateTo(screen: Screen, currentScreenState: PrimitiveDataStore?) {
            _currentScreen.value = screenFactory.createScreen(screen.name, currentScreenState)
        }

        override fun navigateBack() {
            TODO()
        }
    }

    private val _currentScreen = MutableStateFlow(screenFactory.createScreen(initialScreen.name, initialScreen.data))

    override val currentScreen: StateFlow<INavigationScreen> = _currentScreen.asStateFlow()
}