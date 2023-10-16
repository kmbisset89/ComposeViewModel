package com.kmbisset89.ui.widget

/**
 * Represents a collection of validation rules to apply on data. Each rule specifies a constraint and
 * provides an error message in case the constraint is not met.
 *
 * @property errorMessage A user-friendly message describing the validation error associated with the rule.
 */
sealed class ValidationRule {
    abstract val errorMessage: String

    /**
     * Validates a number by ensuring it falls between a specified minimum and/or maximum value.
     * Also offers a constraint to only allow whole numbers.
     */
    data class MinMaxRule(
        val minimumValue: Number? = null,
        val maximumValue: Number? = null,
        val wholeNumberOnly: Boolean = false
    ) : ValidationRule() {
        override val errorMessage: String = when {
            minimumValue != null && maximumValue != null -> {
                "Value needs to be between $minimumValue and $maximumValue"
            }

            minimumValue != null -> {
                "Value needs to be at least $minimumValue"
            }

            maximumValue != null -> {
                "Value needs to be no more than $maximumValue"
            }

            else -> {
                "No specific rule set"
            }
        }
    }

    /**
     * Validates a text field or data by ensuring it is not empty or blank.
     */
    object NotEmptyRule : ValidationRule() {
        override val errorMessage: String = "Cannot be left blank"
    }

    /**
     * Validates a text by ensuring its character count does not exceed the specified maximum.
     */
    data class MaxCharactersRule(val maximumCharacters: Int) : ValidationRule() {
        override val errorMessage: String = "Needs to be less than $maximumCharacters characters"
    }

    /**
     * Validates a text by ensuring its character count is above the specified minimum.
     */
    data class MinCharactersRule(val minimumCharacters: Int) : ValidationRule() {
        override val errorMessage: String = "Needs to be more than $minimumCharacters characters"
    }

    /**
     * Validates a text based on a custom condition specified by a lambda function.
     */
    data class ConditionalRule(val condition: (String) -> Boolean) : ValidationRule() {
        override val errorMessage: String = "Did not meet the specified condition"
    }

    /**
     * Validates a text by ensuring it does not contain any of the specified invalid characters.
     */
    data class InvalidCharactersRule(val invalid: Set<Char>) : ValidationRule() {
        override val errorMessage: String = "Contains an invalid character"
    }

    /**
     * Validates a text by ensuring it matches a specified regex pattern.
     */
    data class RegexRule(val regex: Regex) : ValidationRule() {
        override val errorMessage: String = "Does not match the specified pattern"
    }

    /**
     * Validates a text by ensuring it matches the IPv4 address format.
     */
    class IPValidationRule() : ValidationRule() {
        internal val ipv4Regex = Regex("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$")
        override val errorMessage: String = "Does not match IPv4 format"
    }
}