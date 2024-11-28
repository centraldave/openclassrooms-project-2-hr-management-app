package fr.vitesse.rh.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.vitesse.rh.data.model.Candidate

@Composable
fun CreateUpdateScreen(
    modifier: Modifier = Modifier,
    candidate: Candidate?,
    onBackClick: () -> Unit,
    onCreateUpdateClick: () -> Unit = {}
) {
    candidate?.let {
        CandidateDetail(modifier, candidate)
    }
}

@Composable
private fun CandidateDetail(
    modifier: Modifier = Modifier,
    candidate: Candidate
) { }