package fr.vitesse.rh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.vitesse.rh.data.service.CandidateDetailService
import fr.vitesse.rh.ui.screen.CandidateScreen
import fr.vitesse.rh.ui.screen.HomeScreen
import fr.vitesse.rh.ui.screen.Screen
import fr.vitesse.rh.ui.state.CandidateUiState
import fr.vitesse.rh.ui.theme.VitesseRHTheme
import fr.vitesse.rh.ui.viewmodel.CandidateViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            VitesseRHTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val candidateViewModel: CandidateViewModel = hiltViewModel()
                    val candidateUiState by candidateViewModel.uiState.collectAsState()

                    val candidateDetailService = CandidateDetailService()
                    val navHostController = rememberNavController()

                    if (candidateUiState.isLoading) {
                        Loader()
                    } else {
                        CandidateNavHost(
                            modifier = Modifier.padding(innerPadding),
                            candidateDetailService,
                            navHostController,
                            candidateUiState
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CandidateNavHost(
    modifier: Modifier,
    candidateDetailService: CandidateDetailService,
    navHostController: NavHostController,
    candidateUiState: CandidateUiState,
    candidateViewModel: CandidateViewModel = hiltViewModel()
) {
    NavHost(
        navHostController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                modifier = modifier,
                navHostController,
                candidateDetailService,
                candidateUiState,
                onCandidateClick = {
                    navHostController.navigate(
                        Screen.DetailCandidate.createRoute(
                            candidateId = it.id.toString()
                        )
                    )
                },
                onCreateUpdateClick = {
                    navHostController.navigate(Screen.CreateOrUpdateCandidate.route)
                },
                candidateViewModel = candidateViewModel
            )
        }

        composable(
            route = Screen.DetailCandidate.route,
            arguments = Screen.DetailCandidate.navArguments
        ) {
            val candidateId = it.arguments?.getString("candidateId") ?: return@composable
            val candidate = candidateUiState.candidateList.firstOrNull { it.id.toString() == candidateId }
            if (candidate != null) {
                CandidateScreen(
                    candidate = candidate,
                    candidateDetailService = candidateDetailService,
                    candidateViewModel = candidateViewModel,
                    onBackClick = { navHostController.navigateUp() },
                    onCreateUpdateClick = {
                        navHostController.navigate(
                            Screen.CreateOrUpdateCandidate.createRoute(candidateId = candidate.id.toString())
                        )
                    }
                )
            } else {
                Text(text = "Candidate not found", modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun Loader() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.vitesse_hr_logo),
                contentDescription = stringResource(R.string.logo_hr_description),
                modifier = Modifier.width(160.dp)
            )
            Text(
                text = stringResource(R.string.loader_description),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
            CircularProgressIndicator(
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun NoDataMessage() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = stringResource(R.string.no_data))
    }
}