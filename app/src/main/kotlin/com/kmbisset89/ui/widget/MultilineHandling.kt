package com.kmbisset89.ui.widget

/**
 * Represents the strategies for handling multi-line text fields.
 *
 * This sealed class defines a set of possible strategies to control the behavior of text fields
 * with respect to the number of lines they can display.
 *
 * @property lineCount The maximum number of lines associated with the specific multiline handling strategy.
 */
sealed class MultilineHandling {

    abstract val lineCount: Int

    /**
     * Represents a text field that supports only a single line of text.
     *
     * Use this when you want to ensure the text remains on a single line, regardless of its length.
     * Overflowing content may be truncated or hidden.
     */
    object OneLine : MultilineHandling() {
        override val lineCount: Int = 1
    }

    /**
     * Represents a text field that has a specified maximum number of lines.
     *
     * This allows you to control and limit the number of lines for the text field, which can be useful
     * for situations where space is limited, but more than one line is necessary.
     *
     * @param lineCount The specified maximum number of lines. Must be greater than 1.
     */
    data class MaximumLine(override val lineCount: Int) : MultilineHandling() {
        init {
            require(lineCount > 1) { "lineCount for MaximumLine should be greater than 1." }
        }
    }

    /**
     * Represents a text field that can expand to accommodate an unlimited number of lines.
     *
     * Use this when there's no restriction on the vertical space the text can occupy. This is especially
     * useful when the amount of content is dynamic or unknown beforehand.
     */
    object UnlimitedLine : MultilineHandling() {
        override val lineCount: Int = Int.MAX_VALUE
    }
}