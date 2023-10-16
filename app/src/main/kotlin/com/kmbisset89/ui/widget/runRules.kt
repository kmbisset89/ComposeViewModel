package com.kmbisset89.ui.widget

fun Set<ValidationRule>.runRules(value: String): ValidationResult {
    val validationResult = ValidationResult()
    forEach { rule ->
        when (rule) {
            is ValidationRule.MinMaxRule -> {
                var minimumInRange = false
                rule.minimumValue?.let {
                    minimumInRange =
                        if (value.isNotBlank() && value.toDoubleOrNull() != null) value.toDouble() >= it.toDouble() else false
                }
                var maximumInRange = false
                rule.maximumValue?.let {
                    maximumInRange =
                        if (value.isNotBlank() && value.toDoubleOrNull() != null) value.toDouble() <= it.toDouble() else false
                }
                val meetsWholeNumberCriteria = if (rule.wholeNumberOnly) {
                    value.toIntOrNull() != null
                } else {
                    true
                }

                if (!(maximumInRange && minimumInRange && meetsWholeNumberCriteria)) {
                    validationResult.addError("$value does not fall between Minimum : ${rule.minimumValue} and Maximum : ${rule.maximumValue} rule.")
                }
            }

            is ValidationRule.IPValidationRule -> {
                if (!value.matches(rule.ipv4Regex)) {
                    validationResult.addError("$value is not a valid IP Address")
                }
                if (validationResult.isValid()) {
                    val parts = value.split(".")
                    parts.forEach {
                        if (it.toInt() > 255) {
                            validationResult.addError("$value is not a valid IP Address")
                        }
                    }
                }
            }

            is ValidationRule.MaxCharactersRule -> if (value.length > rule.maximumCharacters) {
                validationResult.addError("$value has more characters than Maximum Length: ${rule.maximumCharacters}")
            }

            is ValidationRule.MinCharactersRule -> if (value.length < rule.minimumCharacters) {
                validationResult.addError("$value has less characters than Minimum Length: ${rule.minimumCharacters}")
            }

            is ValidationRule.RegexRule -> {
                if (!value.matches(rule.regex)) {
                    validationResult.addError("$value does not match regex. Regex: ${rule.regex}")
                }
            }

            ValidationRule.NotEmptyRule -> {
                if (value.isBlank()) {
                    validationResult.addError("Should not be empty")
                }
            }

            is ValidationRule.ConditionalRule-> {
                if (!rule.condition.invoke(value)) {
                    validationResult.addError("Should pass conditional rule")
                }
            }

            is ValidationRule.InvalidCharactersRule -> {
                if (value.length != 1 || rule.invalid.contains(value.first())) {
                    validationResult.addError("$value has an invalid Character")
                }
            }
        }
    }
    return validationResult
}