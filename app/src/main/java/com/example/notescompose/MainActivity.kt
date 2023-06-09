package com.example.notescompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notescompose.detail.DetailViewModel
import com.example.notescompose.home.HomeVIewModel
import com.example.notescompose.login.LoginScreen
import com.example.notescompose.login.LoginViewModel
import com.example.notescompose.ui.theme.NotesComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val loginViewModel = viewModel(modelClass = LoginViewModel::class.java)
            val homeVIewModel = viewModel(modelClass = HomeVIewModel::class.java)
            val detailViewModel = viewModel(modelClass = DetailViewModel::class.java)
            NotesComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Navigation(loginViewModel = loginViewModel, homeVIewModel = homeVIewModel, detailViewModel = detailViewModel)

                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val loginViewModel = viewModel(modelClass = LoginViewModel::class.java)
    val homeVIewModel = viewModel(modelClass = HomeVIewModel::class.java)
    val detailViewModel = viewModel(modelClass = DetailViewModel::class.java)
    NotesComposeTheme {
        Navigation(loginViewModel = loginViewModel, homeVIewModel = homeVIewModel, detailViewModel = detailViewModel)
    }
}