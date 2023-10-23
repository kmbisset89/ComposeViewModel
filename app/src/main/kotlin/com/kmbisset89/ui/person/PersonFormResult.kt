package com.kmbisset89.ui.person

/**
 * Represents the result of a person form operation.
 *
 * This sealed class provides a clear distinction between
 * the various results that can emerge from interacting with the form.
 *
 * @see FormFinished when the form is completed successfully and a [Person] is returned.
 * @see Cancelled when the form operation is cancelled without a successful submission.
 */
sealed class PersonFormResult {

    /**
     * Indicates that the form was successfully completed.
     *
     * @property person The [Person] object created or modified by the form.
     */
    data class FormFinished(val person: Person) : PersonFormResult()

    /**
     * Represents the scenario where the form operation was cancelled.
     */
    object Cancelled : PersonFormResult()
}