package com.kmbisset89.ui.person

import com.kmbisset89.dal.person.Player

/**
 * Represents the result of a person form operation.
 *
 * This sealed class provides a clear distinction between
 * the various results that can emerge from interacting with the form.
 *
 * @see FormFinished when the form is completed successfully and a [Player] is returned.
 * @see Cancelled when the form operation is cancelled without a successful submission.
 */
sealed class PlayerFormResult {

    /**
     * Indicates that the form was successfully completed.
     *
     * @property player The [Player] object created or modified by the form.
     */
    data class FormFinished(val player: Player) : PlayerFormResult()

    /**
     * Represents the scenario where the form operation was cancelled.
     */
    object Cancelled : PlayerFormResult()
}