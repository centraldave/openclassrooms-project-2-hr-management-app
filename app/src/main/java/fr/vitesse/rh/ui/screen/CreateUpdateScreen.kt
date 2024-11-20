package fr.vitesse.rh.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.data.model.GenderEnum
import fr.vitesse.rh.ui.theme.VitesseRHTheme
import java.util.UUID

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
) {

}

@Preview(showBackground = true)
@Composable
private fun CandidateDetailPreview() {
    VitesseRHTheme() {
        CandidateDetail(
            candidate = Candidate(
                UUID.randomUUID(),
                null,
                GenderEnum.FEMALE,
                "Amélie",
                "Poulin",
                null,
                "",
                1000.00,
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                true
            )
        )
    }
}