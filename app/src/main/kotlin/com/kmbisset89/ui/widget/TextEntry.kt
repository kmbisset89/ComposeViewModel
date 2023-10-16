package com.kmbisset89.ui.widget

import androidx.compose.foundation.focusable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import com.kmbisset89.ui.viewmodel.ComposableViewModel

/**
 * A composable function that renders a customizable text entry field with validation capabilities.
 *
 * @param label The label to display above the text field.
 * @param text A mutable state holding the text entered by the user.
 * @param modifier Modifier to be applied to the text field.
 * @param error A mutable state indicating if there's an error in the text entry. Default is `false`.
 * @param allCaps If set to `true`, the text will be transformed to uppercase.
 * @param keyboardType The type of keyboard to show for this text field.
 * @param visualTransformation Transformation applied to the text content (e.g., password masking). Default is none.
 * @param isRequired Indicates if the field is mandatory to be filled. Default is `false`.
 * @param readOnly If `true`, the text cannot be edited by the user.
 * @param specificDisable If `true`, the text field will be disabled and won't respond to user input.
 * @param rules A set of validation rules to apply to the text. Uses the [ValidationRule] sealed class to specify the rules.
 * @param maxLines Defines how many lines the text field can display. Uses [MultilineHandling] to specify behavior.
 * @param onValueChange A callback that is triggered when the text value changes, providing the new text value.
 *                     This is separate from the `text` parameter to decouple the flow of text changes from validation flow.
 *                     This is useful when you may have multiple text fields that need to be validated together.
 * @param allowNext Determines the focus direction to the next focusable composable when the user presses the "Next" button on the keyboard.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextEntry(
    label: String,
    text: MutableState<String>,
    modifier: Modifier = Modifier,
    error: MutableState<Boolean> = remember {
        mutableStateOf(false)
    },
    allCaps: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isRequired: Boolean = false,
    readOnly: Boolean = false,
    specificDisable: Boolean = false,
    rules: Set<ValidationRule> = setOf(),
    maxLines: MultilineHandling = MultilineHandling.OneLine,
    onValueChange: ((String) -> Unit)? = null,
    allowNext: FocusDirection? = null,
) {
    val focusManager = LocalFocusManager.current

    ComposableViewModel({
        TextEntryViewModel(
            isRequired = isRequired,
            ruleSet = rules,
            text = text,
            isError = error,
        )
    }) { viewModel ->
        val keyboardController = LocalSoftwareKeyboardController.current

        OutlinedTextField(
            value = text.value,
            onValueChange = { string ->
                viewModel.onInteraction(TextEntryInteractions.TextChanged(string))
                onValueChange?.invoke(string)
            },
            visualTransformation = visualTransformation,
            label = { Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
            modifier = modifier.then(Modifier.focusable()),
            enabled = !specificDisable,
            readOnly = readOnly,
            isError = error.value,
            keyboardOptions = KeyboardOptions(
                capitalization = if (allCaps) KeyboardCapitalization.Characters else KeyboardCapitalization.Sentences,
                keyboardType = keyboardType,
                imeAction = if (allowNext != null) {
                    ImeAction.Next
                } else {
                    when (maxLines) {
                        is MultilineHandling.MaximumLine -> ImeAction.Default
                        MultilineHandling.OneLine -> ImeAction.Done
                        MultilineHandling.UnlimitedLine -> ImeAction.Default
                    }
                }
            ),
            maxLines = when (maxLines) {
                is MultilineHandling.MaximumLine -> maxLines.lineCount
                MultilineHandling.OneLine -> 1
                MultilineHandling.UnlimitedLine -> Int.MAX_VALUE
            },
            keyboardActions = if (allowNext != null) {
                KeyboardActions(
                    onNext = { focusManager.moveFocus(allowNext) }
                )
            } else {
                when (maxLines) {
                    is MultilineHandling.MaximumLine -> KeyboardActions.Default
                    MultilineHandling.OneLine -> {
                        KeyboardActions(
                            onDone = { keyboardController?.hide() }
                        )
                    }

                    MultilineHandling.UnlimitedLine -> KeyboardActions.Default
                }
            }
        )
    }
}