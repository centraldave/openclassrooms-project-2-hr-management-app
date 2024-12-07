package fr.vitesse.rh.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.vitesse.rh.NoDataMessage
import fr.vitesse.rh.R
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.data.service.CandidateDetailService
import fr.vitesse.rh.ui.common.CandidateCell
import fr.vitesse.rh.ui.state.CandidateUiState
import fr.vitesse.rh.ui.viewmodel.CandidateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    candidateDetailService: CandidateDetailService,
    candidateUiState: CandidateUiState,
    candidateViewModel: CandidateViewModel,
    onCreateUpdateClick: () -> Unit = {},
    onCandidateClick: (Candidate) -> Unit = {},
) {
    val tabNameList = listOf(stringResource(R.string.all_tab), stringResource(R.string.favorite_tab))
    var searchQuery by remember { mutableStateOf("") }

    val filteredCandidates = when (candidateUiState.tabIndex.value) {
        0 -> candidateUiState.candidateList.filter {
            it.firstName.contains(searchQuery, ignoreCase = true) ||
                    it.lastName.contains(searchQuery, ignoreCase = true)
        }
        1 -> candidateUiState.candidateList.filter {
            it.isFavorite &&
                    (it.firstName.contains(searchQuery, ignoreCase = true) ||
                            it.lastName.contains(searchQuery, ignoreCase = true))
        }
        else -> emptyList()
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.vitesse_hr_logo),
                contentDescription = "Vitesse HR Logo",
                modifier = Modifier
                    .width(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )

            TextField(
                value = searchQuery,
                onValueChange = { newQuery -> searchQuery = newQuery },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_placeholder),
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search icon",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Clear search",
                                tint = Color.Gray
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            TabRow(
                selectedTabIndex = candidateUiState.tabIndex.value
            ) {
                tabNameList.forEachIndexed { index, title ->
                    Tab(
                        selected = candidateUiState.tabIndex.value == index,
                        onClick = { candidateUiState.tabIndex.value = index },
                        text = { Text(text = title) }
                    )
                }
            }

            if (filteredCandidates.isNotEmpty()) {
                LazyColumn {
                    items(filteredCandidates) { candidate ->
                        CandidateCell(
                            candidateDetailService = candidateDetailService,
                            candidate = candidate,
                            onCandidateClick = {
                                navHostController.navigate(
                                    Screen.DetailCandidate.createRoute(candidateId = candidate.id.toString())
                                )
                            }
                        )
                    }
                }
            } else {
                NoDataMessage()
            }
        }

        FloatingActionButton(
            onClick = onCreateUpdateClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Candidate",
                tint = Color.White
            )
        }
    }
}
