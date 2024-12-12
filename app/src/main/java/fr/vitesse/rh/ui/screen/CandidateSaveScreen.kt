package fr.vitesse.rh.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.vitesse.rh.R
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.ui.common.BirthdateField
import fr.vitesse.rh.ui.common.CandidateField
import fr.vitesse.rh.ui.viewmodel.CandidateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSaveScreen(
    modifier: Modifier = Modifier,
    candidate: Candidate? = null,
    onBackClick: () -> Unit,
    viewModel: CandidateViewModel
) {
    val screenTitle = if (candidate == null) stringResource(R.string.action_save) else stringResource(R.string.action_update)
    var firstName = remember { mutableStateOf(candidate?.firstName ?: "") }
    var lastName = remember { mutableStateOf(candidate?.lastName ?: "") }
    var phone = remember { mutableStateOf(candidate?.phoneNumber ?: "") }
    var email = remember { mutableStateOf(candidate?.emailAddress ?: "") }
    var dateOfBirth by remember { mutableStateOf(candidate?.dateOfBirth ?: "") }
    var salary = remember { mutableStateOf(candidate?.salaryExpectations ?: "") }
    var note = remember { mutableStateOf(candidate?.note ?: "") }

    var firstNameError = remember { mutableStateOf("") }
    var lastNameError = remember { mutableStateOf("") }
    var dateOfBirthError = remember { mutableStateOf("") }
    var salaryError = remember { mutableStateOf("") }
    var phoneError = remember { mutableStateOf("") }
    var emailError = remember { mutableStateOf("") }
    var noteError = remember { mutableStateOf("") }

    fun validateAndSave() {
        val errorMap = mutableMapOf<String, String>()
        val mandatoryError = "Champ obligatoire"
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()

        val validations = listOf(
            Pair(firstName.value.isEmpty()) { errorMap["firstName"] = mandatoryError },
            Pair(lastName.value.isEmpty()) { errorMap["lastName"] = mandatoryError },
            Pair(phone.value.isEmpty()) { errorMap["phone"] = mandatoryError },
            Pair(email.value.isEmpty()) { errorMap["email"] = mandatoryError },
            Pair(!email.value.matches(emailRegex)) { errorMap["email"] = "L'adresse e-mail est invalide." },
            Pair(dateOfBirth.isEmpty()) { errorMap["dateOfBirth"] = mandatoryError },
            Pair(salary.value.isEmpty()) { errorMap["salary"] = mandatoryError }
        )

        validations.forEach { (condition, action) ->
            if (condition) action()
        }

        firstNameError.value = errorMap["firstName"] ?: ""
        lastNameError.value  = errorMap["lastName"] ?: ""
        phoneError.value  = errorMap["phone"] ?: ""
        emailError.value  = errorMap["email"] ?: ""
        dateOfBirthError.value  = errorMap["dateOfBirth"] ?: ""
        salaryError.value  = errorMap["salary"] ?: ""

        if (errorMap.isEmpty()) {
            val updatedCandidate = candidate?.copy(
                firstName = firstName.value,
                lastName = lastName.value,
                phoneNumber = phone.value,
                emailAddress = email.value,
                dateOfBirth = dateOfBirth,
                salaryExpectations = salary.value,
                note = note.value,
                isFavorite = false,
                profilePicture = null,
                id = candidate.id
            ) ?: Candidate(
                id = 0,
                firstName = firstName.value,
                lastName = lastName.value,
                phoneNumber = phone.value,
                emailAddress = email.value,
                dateOfBirth = dateOfBirth,
                note = note.value,
                isFavorite = false,
                profilePicture = null,
                salaryExpectations = salary.value
            )

            if (candidate == null) {
                viewModel.insertCandidate(updatedCandidate, onBackClick)
            } else {
                viewModel.updateCandidate(updatedCandidate, onBackClick)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = screenTitle,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons. AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { padding ->

            Box {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.default_candidate),
                        contentDescription = "Default Profile Picture",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )

                    CandidateField(
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Outlined.Person,
                        input = firstName,
                        inputError = firstNameError,
                        placeholder = stringResource(R.string.first_name_placeholder),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )

                    CandidateField(
                        modifier = Modifier.fillMaxWidth(),
                        icon = null,
                        input = lastName,
                        inputError = lastNameError,
                        placeholder = stringResource(R.string.last_name_placeholder),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )

                    CandidateField(
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Outlined.Phone,
                        input = phone,
                        inputError = phoneError,
                        placeholder = stringResource(R.string.phone_placeholder),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )

                    CandidateField(
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Outlined.Email,
                        input = email,
                        inputError = emailError,
                        placeholder = stringResource(R.string.email_placeholder),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )

                    Row {
                        BirthdateField(
                            dateOfBirth = dateOfBirth,
                            dateOfBirthError = dateOfBirthError.value,
                            onDateOfBirthChange = { newDateOfBirth ->
                                dateOfBirth = newDateOfBirth
                            }
                        )
                    }

                    CandidateField(
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        placeholder = stringResource(R.string.salary_expectation_placeholder),
                        icon = Icons.Outlined.AccountBox,
                        input = salary,
                        inputError = salaryError
                    )

                    CandidateField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 150.dp),
                        icon = Icons.Outlined.Create,
                        input = note,
                        inputError = noteError,
                        placeholder = stringResource(R.string.note_placeholder),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )

                    Spacer(modifier = Modifier.height(64.dp))

                }

                FloatingActionButton(
                    onClick = {
                        validateAndSave()
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .fillMaxWidth()
                        .padding(32.dp)
                        .semantics { contentDescription = "Save Button" },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Text(text = if (candidate == null) stringResource(R.string.action_save) else stringResource(R.string.action_update))
                }
            }
        }
    )
}
