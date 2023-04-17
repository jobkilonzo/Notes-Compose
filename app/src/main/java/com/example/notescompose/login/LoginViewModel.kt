package com.example.notescompose.login

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notescompose.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
): ViewModel() {
    val currentUser = repository.currentUser

    val hasUser: Boolean
        get() = repository.hasUser()

    var loginUiState by mutableStateOf(LoginUiState())
        private set
    fun onUserNameChange(username: String){
        loginUiState = loginUiState.copy(username = username)
    }
    fun onPasswordNameChange(password: String){
        loginUiState = loginUiState.copy(password = password)
    }
    fun onUsernameChangeSignUp(username: String){
        loginUiState = loginUiState.copy(userNameSignUp = username)
    }
    fun onPasswordChangeSignUp(password: String){
        loginUiState = loginUiState.copy(passwordSignUp = password)
    }
    fun  onConfirmPasswordChange(password: String){
        loginUiState = loginUiState.copy(confirmPasswordSignUp = password)
    }

    private fun validateLoginForm() =
        loginUiState.username.isNotBlank() &&
            loginUiState.password.isNotBlank()
    private fun validateSignUpForm() =
        loginUiState.userNameSignUp.isNotBlank() &&
                loginUiState.passwordSignUp.isNotBlank() &&
                loginUiState.confirmPasswordSignUp.isNotBlank()


    fun createUser(context: Context) = viewModelScope.launch {
        try {
            if (!validateSignUpForm()){
                throw java.lang.IllegalArgumentException("email and password cannot be empty")
            }
            loginUiState = loginUiState.copy(isLoading = true)
            if (loginUiState.passwordSignUp !=loginUiState.confirmPasswordSignUp){
                throw java.lang.IllegalArgumentException("Password do not match")
            }
            loginUiState = loginUiState.copy(signUpError = null)
            repository.createUser(loginUiState.userNameSignUp, loginUiState.passwordSignUp){
                isSuccessful ->
                if (isSuccessful){
                    Toast.makeText(context, "Success login", Toast.LENGTH_SHORT).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = true)
                }else{
                    Toast.makeText(context, "Failed login", Toast.LENGTH_SHORT).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = false)
                }
            }
        }catch (e: java.lang.Exception){
            loginUiState = loginUiState.copy(signUpError = e.localizedMessage)
            e.printStackTrace()
        }finally {
            loginUiState =loginUiState.copy(isLoading = false)
        }
    }
    fun loginUser(context: Context) = viewModelScope.launch {
        try {
            if (!validateLoginForm()){
                throw java.lang.IllegalArgumentException("Email and Password cannot be empty")
            }
            loginUiState = loginUiState.copy(isLoading = true)

            loginUiState = loginUiState.copy(loginError = null)
            repository.login(
                loginUiState.username,
                loginUiState.password
            ){
                    isSuccessful ->
                loginUiState = if(isSuccessful){
                    Toast.makeText(context, "Success login", Toast.LENGTH_SHORT).show()
                    loginUiState.copy(isSuccessLogin = true)
                }else{
                    Toast.makeText(context, "Failed login", Toast.LENGTH_SHORT).show()
                    loginUiState.copy(isSuccessLogin = false)
                }
            }
        }catch (e: java.lang.Exception){
            loginUiState = loginUiState.copy(loginError = e.localizedMessage)
            e.printStackTrace()
        }finally {
            loginUiState =loginUiState.copy(isLoading = false)
        }
    }
}
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val userNameSignUp: String = "",
    val passwordSignUp: String = "",
    val confirmPasswordSignUp: String = "",
    val isLoading: Boolean = false,
    val isSuccessLogin: Boolean = false,
    val signUpError: String? = null,
    val loginError: String? = null

)