package com.kmbisset89

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kmbisset89.ui.person.PersonFormViewModel
import com.kmbisset89.ui.theme.MyApplicationTheme
import com.kmbisset89.ui.viewmodel.ComposableViewModel
import com.kmbisset89.ui.widget.TextEntry

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                ComposableViewModel({ PersonFormViewModel() }) { vm ->
                    // A surface container using the 'background' color from the theme
                    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                        Column {
                            TextEntry(
                                "First Name",
                                vm.personFirstName,
                                isRequired = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            TextEntry(
                                "Last Name",
                                vm.personLastName,
                                isRequired = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}