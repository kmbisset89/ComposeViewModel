package com.kmbisset89.ui.viewmodel

import android.app.Activity
import android.app.Application
import android.os.Bundle
import kotlinx.serialization.json.Json
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference


/**
 * A centralized handler to manage state retention for Composable views on Android.
 *
 * This handler utilizes Android's Activity lifecycle callbacks to determine the appropriate times
 * to save and restore state. It provides the ability to register and unregister Composable view models
 * for state retention, and gives a centralized mechanism to manage that retained state across configuration changes.
 *
 * @see IComposableStateHandler
 * @see IPrimitiveDataStoreProvider
 */
object AndroidStateHandler : Application.ActivityLifecycleCallbacks, IComposableStateHandler,
    IPrimitiveDataStoreProvider {

    private val viewModelStore: MutableMap<String, () -> PrimitiveDataStore?> = mutableMapOf()
    private val wasDestroyed: AtomicBoolean = AtomicBoolean(false)
    private val lastActivityName = AtomicReference<String?>(null)
    private val lastActivityBundle = AtomicReference<Bundle?>(null)
    private val temporaryMapOfData = mutableMapOf<String, PrimitiveDataStore>()

    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityPreCreated(activity, savedInstanceState)

        // Check to see if the activity was destroyed and needs to be restored.
        if (wasDestroyed.get()) {
            // Check to see if the activity is the same as the last activity that was destroyed.
            if (lastActivityName.get() == activity::class.java.name) {
                lastActivityBundle.set(savedInstanceState)
            }
        }
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        // No implementation needed
    }

    override fun onActivityStarted(p0: Activity) {
        // No implementation needed
    }

    override fun onActivityResumed(p0: Activity) {
        // Check to see if was destroyed and needs to be restored
        temporaryMapOfData.clear()
    }

    override fun onActivityPaused(p0: Activity) {
        // Gather-up all the data stores and save them
        viewModelStore.forEach { (id, primitiveData) ->
            // Save the data store to the bundle
            primitiveData.invoke()?.let {
                temporaryMapOfData[id] = it
            }
        }
    }

    override fun onActivityStopped(p0: Activity) {
        // No implementation needed
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        // Save the data stores to the bundle.
        temporaryMapOfData.forEach { (id, primitiveData) ->
            // Save the data store to the bundle
            p1.putString(id, Json.encodeToString(PrimitiveDataStore.serializer(), primitiveData))
        }
        temporaryMapOfData.clear()
    }

    override fun onActivityDestroyed(p0: Activity) {
        // Keep track that the activity was destroyed
        wasDestroyed.set(true)
        // Keep track of the last activity name
        lastActivityName.set(p0::class.java.name)
    }

    /**
     * Register a [PrimitiveDataStore] state gatherer.
     *
     * @param id A unique identifier for the Composable ViewModel.
     * @param stateGatherer A lambda function providing the current state to be saved.
     */
    override fun registerForStateRetention(id: String, stateGatherer: () -> PrimitiveDataStore?) {
        viewModelStore[id] = stateGatherer
    }

    /**
     * Unregister a [PrimitiveDataStore] state gatherer.
     *
     * @param id A unique identifier for the Composable ViewModel to be unregistered.
     */
    override fun unregisterForStateRetention(id: String) {
        viewModelStore.remove(id)
    }

    /**
     * Retrieves the saved [PrimitiveDataStore] for a given id and removes it from the bundle after retrieval.
     *
     * This mechanism ensures that the saved state is consumed only once and helps in avoiding
     * potential state inconsistencies or reapplications, especially during re-compositions.
     *
     * @param id A unique identifier for the Composable ViewModel.
     * @return The saved state if available, or `null` if not found.
     */
    override fun getPrimitiveDataStore(id: String): PrimitiveDataStore? {
        return lastActivityBundle.get()?.let { bundle ->
            bundle.getString(id)?.let {
                Json.decodeFromString(PrimitiveDataStore.serializer(), it)
            }.also {
                // Remove the data from the bundle because it's been retrieved and only needs to be retrieved once.
                bundle.remove(id)
            }
        }
    }
}