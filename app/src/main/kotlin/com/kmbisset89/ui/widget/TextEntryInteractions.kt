package com.kmbisset89.ui.widget

/**
 * Represents a set of possible interactions or events that can occur within a text entry component.
 *
 * As a sealed class, it provides a limited set of concrete implementations, making it easier to handle
 * all possible interaction types without missing any.
 */
sealed class TextEntryInteractions {

    /**
     * Represents an interaction where the text within the text entry has changed.
     *
     * @param text The new text content after the change.
     */
    data class TextChanged(val text: String) : TextEntryInteractions()
}