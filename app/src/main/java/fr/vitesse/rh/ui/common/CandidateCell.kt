package fr.vitesse.rh.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.vitesse.rh.R
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.data.service.CandidateService
import fr.vitesse.rh.data.service.RandomService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CandidateCell(
    candidateService: CandidateService,
    candidate: Candidate,
    onCandidateClick: (Candidate) -> Unit,
) {
    val profilePicture: Int = candidateService.getProfilePicture(candidate)
    Row(
        modifier = Modifier
            .clickable {
                onCandidateClick(candidate)
            }
            .padding(start = 32.dp, end = 32.dp, top = 16.dp, bottom = 16.dp)
            .height(72.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .fillMaxHeight()
                .padding(4.dp),
            painter = painterResource(profilePicture),
            contentDescription = stringResource(id = R.string.profile_picture_description)
        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
        ) {
            Row(){
                Text(
                    text = candidate.lastName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiary
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = candidate.firstName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
            Row() {
                Text(
                    text = candidate.note,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CandidateCellPreview() {
    val randomService = RandomService()
    var randomCandidate: Candidate? = null
    LaunchedEffect(Unit) {
        launch(Dispatchers.IO) {
            randomCandidate = randomService.generateCandidates(1).getOrNull()?.first()!!
        }
    }

    MaterialTheme {
        randomCandidate?.let {
            CandidateCell(
                candidateService = CandidateService(),
                candidate = it,
                onCandidateClick = {  }
            )
        }
    }
}
