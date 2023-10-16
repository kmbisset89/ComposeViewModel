package com.kmbisset89.ui.widget

import android.util.Log
import androidx.compose.runtime.MutableState
import com.kmbisset89.ui.viewmodel.ComposeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A view model for managing and validating text entry in a Compose UI.
 *
 * This view model is responsible for managing a text entry's state and its validation
 * using a predefined set of rules. Whenever the text changes, it automatically
 * triggers validation and updates the error state.
 *
 * The class utilizes a flow (`_textToValidate`) to track changes to the text and
 * validate them asynchronously using the provided rule set.
 *
 * @param isRequired Indicates if the text field is mandatory to be filled.
 *                   This will just attempt to add the [ValidationRule.NotEmptyRule].
 * @param ruleSet A set of validation rules ([ValidationRule]) against which the text will be validated.
 * @property text A [MutableState] holding the text input. This will be updated
 *             internally whenever a [TextEntryInteractions.TextChanged] event is received.
 * @property isError A [MutableState] that indicates if the current text violates
 *                any validation rules. It is updated based on validation results.
 *
 * @property _textToValidate A [MutableStateFlow] that tracks the latest text
 *                           that needs validation. This is separate from the `text`
 *                           parameter to decouple the flow of text changes from validation flow.
 *
 * @constructor Initializes the view model, sets up a flow collector to handle text validation,
 *              and updates the `isError` state based on validation results.
 */
class TextEntryViewModel(
    isRequired : Boolean,
    ruleSet: Set<ValidationRule>,
    private val text: MutableState<String>,
    private val isError: MutableState<Boolean>,
) : ComposeViewModel() {

    private val _textToValidate = MutableStateFlow(text.value)
    private val rules = if (isRequired) {
        ruleSet.plus(ValidationRule.NotEmptyRule)
    } else {
        ruleSet
    }

    init {
        viewModelScope.launch(Dispatchers.Default) {
            _textToValidate.collectLatest { textToValidate ->
                val isValid = validateText(textToValidate)
                withContext(Dispatchers.Main){
                    isError.value = !isValid
                }
            }
        }
    }

    /**
     * Handle interactions related to text entry.
     *
     * This method manages the interactions passed to it and updates the internal state
     * accordingly. For instance, when a [TextEntryInteractions.TextChanged] event is received,
     * it updates the `text` state and triggers validation.
     *
     * @param interactions A set of interactions related to text entry.
     */
    internal fun onInteraction(interactions: TextEntryInteractions){
        when(interactions){
            is TextEntryInteractions.TextChanged -> {
                text.value = interactions.text
                _textToValidate.value = interactions.text
            }
        }
    }

    /**
     * Validates the provided text against the predefined rule set.
     *
     * The method checks if the text violates any of the validation rules and returns the
     * validation result.
     *
     * @param textToValidate The text that needs to be validated.
     * @return Returns `true` if the text is valid according to the rule set; otherwise returns `false`.
     */
    private fun validateText(textToValidate: String): Boolean {
        val validationResult = rules.runRules(textToValidate)
        return validationResult.isValid()
    }

    /**
     * A cleanup method when the view model is cleared.
     *
     * This method can be overridden if there are specific resources or tasks to be
     * canceled or cleaned up when the view model is no longer in use.
     */
    override suspend fun onClear() {
        Log.d("TextEntryViewModel", "onClear called")
    }
}