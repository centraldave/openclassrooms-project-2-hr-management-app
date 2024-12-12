package fr.vitesse.rh.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CandidateField(
    modifier: Modifier,
    keyboardOptions: KeyboardOptions?,
    icon: ImageVector?,
    input: MutableState<String>,
    inputError: MutableState<String>,
    placeholder: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = "First Name Icon",
                modifier = Modifier.padding(end = 8.dp)
            )
        } else {
            Spacer(modifier = Modifier.width(32.dp))
        }
        OutlinedTextField(
            value = input.value, // Use input.value to read the state
            isError = inputError.value.isNotEmpty(),
            onValueChange = {
                input.value = it
                if (it.isNotEmpty()) {
                    inputError.value = ""
                }
            },
            label = { Text(placeholder) },
            singleLine = true,
            modifier = modifier
        )
    }

    if (inputError.value.isNotEmpty()) {
        Row {
            Spacer(modifier = Modifier.width(32.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = inputError.value,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

