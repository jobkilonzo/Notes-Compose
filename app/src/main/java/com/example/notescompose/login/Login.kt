package com.example.notescompose.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notescompose.ui.theme.NotesComposeTheme

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel? = null,
    onNavToHomePage: () -> Unit,
    onNavToSignUpPage: () -> Unit
) {
    val loginUiState = loginViewModel?.loginUiState
    val isError = loginUiState?.loginError != null
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment =  Alignment.CenterHorizontally
    ) {
        Text(
            text = "Notes App",
            fontSize = 30.sp,
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(30.dp)
        )
        Spacer(modifier = Modifier.size(30.dp))
        Text(
            text =  "Login",
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colors.primary
        )
        if (isError){
            Text(text = loginUiState?.loginError ?: "Unknown error",
            color = Color.Red
            )
        }
        Spacer(modifier = Modifier.size(30.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = loginUiState?.username ?: "",
            onValueChange = {loginViewModel?.onUserNameChange(it)},
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person,
                    contentDescription = null)
            },
            label = {
                Text(text = "Email")
            },
            isError = isError
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = loginUiState?.password ?: "",
            onValueChange = {loginViewModel?.onPasswordNameChange(it)},
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock,
                    contentDescription = null)
            },
            label = {
                Text(text = "Password")
            },
            visualTransformation = PasswordVisualTransformation(),
            isError = isError
        )
        
        Button(onClick = { loginViewModel?.loginUser(context)}) {
            Text(text = "Sign in")
        }
        Spacer(modifier = Modifier.size(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =  Arrangement.Center
        ) {
            Text(text = "Don't have an account?")
            Spacer(modifier = Modifier.size(8.dp))
            TextButton(onClick = { onNavToSignUpPage.invoke() }) {
                Text(text = "SignUp")
            }
        }

        if (loginUiState?.isLoading == null){
            CircularProgressIndicator()
        }
        LaunchedEffect(key1 = loginViewModel?.hasUser ){
            if (loginViewModel?.hasUser == true){
                onNavToHomePage.invoke()
            }
        }
    }
}

@Composable
fun SignUpScreen(
    loginViewModel: LoginViewModel? = null,
    onNavToHomePage: () -> Unit,
    onNavToLoginPage: () -> Unit
) {
    val loginUiState = loginViewModel?.loginUiState
    val isError = loginUiState?.signUpError != null
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment =  Alignment.CenterHorizontally
    ) {
        Text(
            text = "Notes App",
            fontSize = 30.sp,
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(30.dp)
        )
        Spacer(modifier = Modifier.size(30.dp))
        Text(
            text =  "Sign Up",
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colors.primary
        )
        if (isError){
            Text(text = loginUiState?.signUpError ?: "Unknown error",
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.size(30.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = loginUiState?.userNameSignUp ?: "",
            onValueChange = {loginViewModel?.onUsernameChangeSignUp(it)},
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person,
                    contentDescription = null)
            },
            label = {
                Text(text = "Email")
            },
            isError = isError
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = loginUiState?.passwordSignUp ?: "",
            onValueChange = {loginViewModel?.onPasswordChangeSignUp(it)},
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock,
                    contentDescription = null)
            },
            label = {
                Text(text = "Password")
            },
            visualTransformation = PasswordVisualTransformation(),
            isError = isError
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = loginUiState?.confirmPasswordSignUp ?: "",
            onValueChange = {loginViewModel?.onConfirmPasswordChange(it)},
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock,
                    contentDescription = null)
            },
            label = {
                Text(text = "Confirm Password")
            },
            visualTransformation = PasswordVisualTransformation(),
            isError = isError
        )
        Button(onClick = { loginViewModel?.createUser(context)}) {
            Text(text = "Sign up")
        }
        Spacer(modifier = Modifier.size(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =  Arrangement.Center
        ) {
            Text(text = "Already have an account?")
            Spacer(modifier = Modifier.size(8.dp))
            TextButton(onClick = { onNavToLoginPage.invoke() }) {
                Text(text = "Sign In")
            }
        }

        if (loginUiState?.isLoading == null){
            CircularProgressIndicator()
        }
        LaunchedEffect(key1 = loginViewModel?.hasUser ){
            if (loginViewModel?.hasUser == true){
                onNavToHomePage.invoke()
            }
        }
    }
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginScreen(){
    NotesComposeTheme {
        LoginScreen(onNavToHomePage = { /*TODO*/ }) {
            
        }
    }
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SignUpScreen(){
    NotesComposeTheme {
        SignUpScreen(onNavToHomePage = { /*TODO*/ }) {

        }
    }
}