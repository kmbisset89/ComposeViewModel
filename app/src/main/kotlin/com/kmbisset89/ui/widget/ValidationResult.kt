package com.kmbisset89.ui.widget

/**
 * Represents the result of a validation process, containing error messages mapped to their causes.
 * 
 * The data class provides utility functions to check the validity of the result, add errors,
 * and combine multiple validation results.
 *
 * @property errorMap A mutable map where the key represents the cause of the error and the value is a set of error messages.
 *                    Default is an empty map.
 */
data class ValidationResult(val errorMap: MutableMap<String, MutableSet<String>> = HashMap()) {
    
    companion object {
        /**
         * A general tag to categorize errors that don't belong to specific causes.
         */
        const val GENERAL_ERROR_TAG = "GENERAL"
    }

    /**
     * Checks if the current validation result is valid (i.e., no errors).
     *
     * @return `true` if the error map is empty or null, otherwise `false`.
     */
    fun isValid(): Boolean {
        return errorMap.isNullOrEmpty()
    }

    /**
     * Adds a general error to the validation result.
     *
     * @param error The error message to be added.
     */
    fun addError(error: String) {
        addError(GENERAL_ERROR_TAG, error)
    }

    /**
     * Adds an error with a specific cause to the validation result.
     *
     * @param errorCause The cause or category of the error.
     * @param errorInfo The error message describing the error.
     */
    fun addError(errorCause: String, errorInfo: String) {
        var list = errorMap[errorCause]

        if (list == null) {
            list = HashSet()
        }

        list.add(errorInfo)

        errorMap[errorCause] = list
    }

    /**
     * Merges another validation result with the current result.
     *
     * @param otherValidationResult Another validation result to be combined.
     */
    fun combine(otherValidationResult: ValidationResult) {
        combine(listOf(otherValidationResult))
    }

    /**
     * Merges a list of validation results with the current result.
     *
     * @param otherValidationResults A list of validation results to be combined.
     */
    fun combine(otherValidationResults: List<ValidationResult>) {
        otherValidationResults.forEach {
            if (!it.isValid()) {
                it.errorMap.forEach { mapItem ->
                    val listForKey = errorMap[mapItem.key]

                    if (listForKey.isNullOrEmpty()) {
                        errorMap[mapItem.key] = mapItem.value
                    } else {
                        listForKey.addAll(mapItem.value)
                    }
                }
            }
        }
    }
}