package fr.vitesse.rh.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import fr.vitesse.rh.R
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.data.service.CandidateInformationService
import fr.vitesse.rh.ui.common.CandidateContactButton
import fr.vitesse.rh.ui.common.CandidateInformation
import fr.vitesse.rh.ui.state.CandidateUiState
import fr.vitesse.rh.ui.viewmodel.CandidateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateScreen(
    candidate: Candidate,
    candidateInformationService: CandidateInformationService,
    onBackClick: () -> Unit,
    candidateViewModel: CandidateViewModel,
    onCreateUpdateClick: (Candidate) -> Unit
) {
    val uiState by candidateViewModel.uiState.collectAsState()

    LaunchedEffect(candidate.salaryExpectations) {
        candidateViewModel.getConvertedCurrencies(candidate.salaryExpectations)
    }

    val updatedCandidate = uiState.candidateList.find { it.id == candidate.id }

    if (updatedCandidate == null) {
        return
    }

    Scaffold(
        topBar = {
            CandidateTopBar(
                candidate = updatedCandidate,
                onBackClick = onBackClick,
                onFavoriteClick = { candidateViewModel.toggleFavorite(updatedCandidate) },
                onCreateUpdateClick = { onCreateUpdateClick(updatedCandidate) },
                onDeleteClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        candidateViewModel.deleteCandidate(updatedCandidate, onBackClick)
                    }
                }
            )
        },
        content = { padding ->
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CandidateInformationList(
                        modifier = Modifier.padding(padding),
                        candidate = updatedCandidate,
                        candidateInformationService = candidateInformationService,
                        uiState = uiState
                    )
                }
            }
        }
    )
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateTopBar(
    candidate: Candidate,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onCreateUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = "${candidate.firstName} ${candidate.lastName}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons. AutoMirrored. Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (candidate.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (candidate.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onCreateUpdateClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit"
                )
            }
            IconButton(onClick = { showDeleteConfirmation = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    )

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text(stringResource(R.string.deletion_title)) },
            text = { Text(stringResource(R.string.deletion_description)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                        onDeleteClick()
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmation = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun CandidateInformationList(
    modifier: Modifier,
    candidate: Candidate,
    candidateInformationService: CandidateInformationService,
    uiState: CandidateUiState
) {
    val context = LocalContext.current

    Column(modifier = modifier.padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.default_candidate),
                contentDescription = stringResource(R.string.profile_picture_description),
                modifier = Modifier
                    .height(175.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CandidateContactButton(
                icon = Icons.Default.Call,
                label = stringResource(R.string.phone_description),
                value = candidate.phoneNumber
            ) { phoneNumber ->
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                }
                context.startActivity(intent)
            }

            CandidateContactButton(
                icon = Icons.Default.Phone,
                label = stringResource(R.string.sms_description),
                value = candidate.phoneNumber
            ) { phoneNumber ->
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("smsto:$phoneNumber")
                }
                context.startActivity(intent)
            }

            CandidateContactButton(
                icon = Icons.Default.Email,
                label = stringResource(R.string.e_mail_description),
                value = candidate.emailAddress
            ) { email ->
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$email")
                }
                context.startActivity(intent)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CandidateInformation(
            title = stringResource(R.string.about_title),
            body = candidateInformationService.getFormattedBirthday(candidate, context),
            label = stringResource(R.string.about_description)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CandidateInformation(
            title = stringResource(R.string.salary_expectations_title),
            body = candidateInformationService.getSalaryExpectation(candidate),
            label = "soit " + uiState.convertedGbpSalary
        )

        Spacer(modifier = Modifier.height(16.dp))

        CandidateInformation(
            title = stringResource(R.string.notes_title),
            body = candidate.note,
            label = ""
        )
    }
}
