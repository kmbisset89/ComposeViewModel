package com.kmbisset89.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember

/**
 * A composable function that provides a convenient way to instantiate and manage a [ComposeViewModel]
 * within the composable hierarchy. The [ComposeViewModel] instance created by this function is scoped
 * to the current composition, and its lifetime is bound to the composable's lifecycle.
 *
 * Usage of this function ensures that the [ComposeViewModel] and its associated resources are cleared
 * and disposed of properly when the composable is removed from the hierarchy.
 *
 * @param T The type of the [ComposeViewModel] being managed.
 * @param factory A lambda expression that produces an instance of [T]. This expression will be invoked
 * to create a new [ComposeViewModel] instance whenever a recomposition occurs that requires a new model.
 * @param key1 An optional key to associate with the [ComposeViewModel] instance. Providing a key can
 * be useful to control the identity and therefore the lifecycle of the ViewModel, especially across
 * recompositions.
 * @param content A [Composable] lambda expression that will be invoked with the created [ComposeViewModel]
 * instance as its argument. This lambda is the consumer of the ViewModel, and is where the ViewModel
 * should be used to obtain data and interact with the rest of the composable hierarchy.
 */
@Composable
fun <T : ComposeViewModel> ComposableViewModel(
    factory: (() -> T),
    key1: Any? = null,
    content: @Composable (T) -> Unit
) {
    val viewModel: T = remember(key1) { factory() }

    /**
     * Manages the disposal process of the [ComposeViewModel] and its associated resources. When the
     * composable associated with this ViewModel is removed from the hierarchy, or when there is a
     * change in the provided key, this effect will trigger the [ComposeViewModel.clearViewModel] method
     * to clean up resources.
     *
     * @see DisposableEffect
     */
    DisposableEffect(key1 = true) {
        onDispose {
            viewModel.clearViewModel()
        }
    }

    // Invokes the consumer [content] lambda with the created [ComposeViewModel] instance.
    content(viewModel)
}


/**
 * A [Composable] function that provides a [StatedComposeViewModel] and manages its lifecycle within a Composable hierarchy.
 *
 * This function creates a [StatedComposeViewModel] and ensures its proper lifecycle management.
 * It is responsible for creating the ViewModel based on the provided factory function, and
 * for clearing its resources when the associated composable is removed from the hierarchy.
 *
 * It's recommended to use this function when you need state retention in your Composables
 * using the [StatedComposeViewModel].
 *
 * @param [T] The type of the [StatedComposeViewModel].
 * @param primitiveDataStoreProvider The provider interface to fetch the initial state for the ViewModel.
 * @param composableStateHandler The interface providing mechanisms for registering and unregistering the ViewModel for state retention.
 * @param factory A lambda to create an instance of [T]. Takes in a [IPrimitiveDataStoreProvider] and [IComposableStateHandler].
 * @param key1 An optional key to control the recomposition of the Composable.
 *             If the key changes, the ViewModel will be cleared and recreated.
 * @param content A [Composable] lambda that gets the created ViewModel instance as a parameter and allows its utilization within Composable content.
 *
 * @sample
 * ```
 * StatedComposableViewModel(
 *     primitiveDataStoreProvider = myProvider,
 *     composableStateHandler = myHandler,
 *     factory = { provider, handler -> MyViewModel(provider, handler) }
 * ) { viewModel ->
 *     // Use the viewModel in Composable content
 * }
 * ```
 *
 * @see StatedComposeViewModel
 * @see DisposableEffect
 */
@Composable
fun <T : StatedComposeViewModel> StatedComposableViewModel(
    primitiveDataStoreProvider: IPrimitiveDataStoreProvider,
    composableStateHandler: IComposableStateHandler,
    factory: ((IPrimitiveDataStoreProvider, IComposableStateHandler) -> T),
    key1: Any? = null,
    content: @Composable (T) -> Unit
) {
    val viewModel: T = remember(key1) { factory(primitiveDataStoreProvider, composableStateHandler) }

    /**
     * Manages the disposal process of the [ComposeViewModel] and its associated resources. When the
     * composable associated with this ViewModel is removed from the hierarchy, or when there is a
     * change in the provided key, this effect will trigger the [ComposeViewModel.clearViewModel] method
     * to clean up resources.
     *
     * @see DisposableEffect
     */
    DisposableEffect(key1 = true) {
        onDispose {
            viewModel.clearViewModel()
        }
    }

    // Invokes the consumer [content] lambda with the created [ComposeViewModel] instance.
    content(viewModel)
}