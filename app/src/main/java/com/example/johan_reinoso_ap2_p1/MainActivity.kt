package com.example.johan_reinoso_ap2_p1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.johan_reinoso_ap2_p1.Presentation.edit.EditEntradaScreen
import com.example.johan_reinoso_ap2_p1.Presentation.list.EntradaListScreen
import com.example.johan_reinoso_ap2_p1.Presentation.list.ListEntradaViewModel
import com.example.johan_reinoso_ap2_p1.ui.theme.Johan_Reinoso_AP2_P1Theme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Johan_Reinoso_AP2_P1Theme {
                val navController = rememberNavController()
                val listEntradaViewModel: ListEntradaViewModel = hiltViewModel()
                val entradaState by listEntradaViewModel.state.collectAsStateWithLifecycle()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    IconButton(
                        onClick = {
                            scope.launch { drawerState.open() }
                        },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                    ) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menú")
                    }

                    NavHost(
                        navController = navController,
                        startDestination = "entradaList",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable("entradaList") {
                            EntradaListScreen(
                                onNavigateToEdit = { id ->
                                    navController.navigate("editEntrada/$id")
                                },
                                onNavigateToCreate = {
                                    navController.navigate("editEntrada/0")
                                },
                            )
                        }
                        composable("editEntrada/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                            EditEntradaScreen(
                                EntradaId = id,
                                navController = navController,
                                viewModel = hiltViewModel()
                            )
                        }
                    }
                }
            }
        }
    }
}
