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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.vitesse.rh.R
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.ui.viewmodel.CandidateViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUpdateScreen(
    modifier: Modifier = Modifier,
    candidate: Candidate? = null,
    onBackClick: () -> Unit,
    onCreateUpdateClick: () -> Unit = {},
    viewModel: CandidateViewModel
) {
    val screenTitle = if (candidate == null) "Sauvegarder" else "Mettre à jour"
    var firstName by remember { mutableStateOf(candidate?.firstName ?: "") }
    var lastName by remember { mutableStateOf(candidate?.lastName ?: "") }
    var phone by remember { mutableStateOf(candidate?.phoneNumber ?: "") }
    var email by remember { mutableStateOf(candidate?.emailAddress ?: "") }
    var dateOfBirth by remember { mutableStateOf(candidate?.dateOfBirth ?: "") }
    var salary by remember { mutableStateOf(candidate?.salaryExpectations ?: "") }
    var note by remember { mutableStateOf(candidate?.note ?: "") }

    var firstNameError by remember { mutableStateOf("") }
    var lastNameError by remember { mutableStateOf("") }
    var dateOfBirthError by remember { mutableStateOf("") }
    var salaryError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }

    fun validateAndSave() {
        firstNameError = ""
        lastNameError = ""
        phoneError = ""
        emailError = ""
        dateOfBirthError = ""
        salaryError = ""

        var isValid = true
        val mandatoryError = "Champ obligatoire"

        if (firstName.isEmpty()) {
            firstNameError = mandatoryError
            isValid = false
        }

        if (lastName.isEmpty()) {
            lastNameError = mandatoryError
            isValid = false
        }

        if (phone.isEmpty()) {
            phoneError = mandatoryError
            isValid = false
        }

        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
        if (email.isEmpty() || !email.matches(emailRegex)) {
            emailError = "L'adresse e-mail est invalide."
            isValid = false
        }

        if (dateOfBirth.isEmpty()) {
            dateOfBirthError = mandatoryError
            isValid = false
        }

        if (salary.isEmpty()) {
            salaryError = mandatoryError
            isValid = false
        }
        if (isValid) {
            val nonNullDateOfBirth = dateOfBirth
            val nonNullSalary = salary

            val updatedCandidate = candidate?.copy(
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phone,
                emailAddress = email,
                dateOfBirth = nonNullDateOfBirth,
                salaryExpectations = nonNullSalary,
                note = note,
                isFavorite = false,
                profilePicture = null,
                id = candidate?.id ?: 0
            ) ?: Candidate(
                id = 0,
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phone,
                emailAddress = email,
                dateOfBirth = nonNullDateOfBirth,
                note = note,
                isFavorite = false,
                profilePicture = null,
                salaryExpectations = nonNullSalary
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
            UpdateTopBar(
                onBackClick = onBackClick,
                screenTitle = screenTitle
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

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "First Name Icon",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        OutlinedTextField(
                            value = firstName,
                            isError = firstNameError.isNotEmpty(),
                            onValueChange = { firstName = it },
                            label = { Text("Prénom") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (firstNameError.isNotEmpty()) {
                        Row {
                            Spacer(modifier = Modifier.width(32.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = firstNameError,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.width(32.dp))
                        OutlinedTextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = { Text("Nom de famille") },
                            isError = lastNameError.isNotEmpty(),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                    }

                    if (lastNameError.isNotEmpty()) {
                        Row {
                            Spacer(modifier = Modifier.width(32.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = lastNameError,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = "Phone Icon",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Téléphone") },
                            singleLine = true,
                            isError = phoneError.isNotEmpty(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth()
                        )

                    }
                    if (phoneError.isNotEmpty()) {
                        Row {
                            Spacer(modifier = Modifier.width(32.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = phoneError,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = "Email Icon",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        EmailInputField(
                            email = email,
                            onEmailChange = { newEmail ->
                                email = newEmail
                                emailError = if (newEmail.isNotEmpty() && !newEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$".toRegex())) {
                                    "Format d'e-mail invalide."
                                } else {
                                    ""
                                }
                            },
                            emailError = emailError
                        )
                    }

                    Row {
                        AnniversaryInput(
                            dateOfBirth = dateOfBirth,
                            onDateOfBirthChange = { newDateOfBirth ->
                                dateOfBirth = newDateOfBirth
                            }
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AccountBox,
                            contentDescription = "Salary Icon",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        OutlinedTextField(
                            value = salary,
                            onValueChange = { salary = it },
                            label = { Text("Prétention salariale") },
                            isError = salaryError.isNotEmpty(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }


                    if (salaryError.isNotEmpty()) {
                        Row {
                            Spacer(modifier = Modifier.width(32.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = salaryError,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Create,
                            contentDescription = "Note Icon",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            label = { Text("Note") },
                            singleLine = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 150.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(64.dp))

                }

                FloatingActionButton(
                    onClick = {
                        validateAndSave()
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .fillMaxWidth()
                        .padding(32.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Text(text = if (candidate == null) "Sauvegarder" else "Mettre à jour")
                }




            }
        }
    )
}

private fun parseDate(dateString: String): Date? {
    return try {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.parse(dateString)
    } catch (e: Exception) {
        null
    }
}


@Composable
fun AnniversaryInput(
    dateOfBirth: String,
    onDateOfBirthChange: (String) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Sélectionner une date",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Entrez une date",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DateRange,
                        contentDescription = "Date Picker Icon"
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                thickness = 1.dp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = dateOfBirth.toString(),
                    onValueChange = { onDateOfBirthChange(dateOfBirth) },
                    label = { Text("Date") },
                    placeholder = { Text("jj/mm/aaaa") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    readOnly = true
                )
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { selectedDate ->
                onDateOfBirthChange(selectedDate)
                showDatePicker = false
            }
        )
    }
}


@Composable
fun DatePickerDialog(onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth)
                set(Calendar.DAY_OF_MONTH, selectedDay)
            }
            val selectedDate = selectedCalendar.time

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate)

            onDateSelected(formattedDate)
        },
        year, month, day
    )

    LaunchedEffect(context) {
        datePickerDialog.show()
    }
}
@Composable
fun EmailInputField(
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = email,
            onValueChange = {
                onEmailChange(it)
            },
            label = { Text("Adresse e-mail") },
            singleLine = true,
            isError = emailError.isNotEmpty(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (emailError.isNotEmpty()) {
            Text(
                text = emailError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateTopBar(
    onBackClick: () -> Unit,
    screenTitle: String
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }

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
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}