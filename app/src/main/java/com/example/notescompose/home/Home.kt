package com.example.notescompose.home

import android.annotation.SuppressLint
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.notescompose.ui.theme.NotesComposeTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(
    onNavToLoginPage: () -> Unit
){
//    Scaffold(
//        topBar = {
//            TopAppBar(){
//                Row() {
//                    Text(
//                        text = "Notes App",
//                        fontSize = 20.sp,
//                        style = MaterialTheme.typography.h3,
//                        color = Color.White,
//                        modifier = Modifier.padding(5.dp)
//                    )
//                    Spacer(modifier = Modifier.size(20.dp))
//                    Text(
//                        text = "Logout")
//                }
//            }
//        }
//    ) {
//        Text(text = "This is home Screen")
//    }
    Button(onClick = {

        Firebase.auth.signOut()
        onNavToLoginPage.invoke()
    }) {
        Text(text = "Logout")
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewHome(){
    NotesComposeTheme {
        //Home()
    }
}