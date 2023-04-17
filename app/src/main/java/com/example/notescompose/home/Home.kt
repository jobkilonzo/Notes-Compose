package com.example.notescompose.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notescompose.Utils
import com.example.notescompose.modal.Notes
import com.example.notescompose.repository.Resource
import com.example.notescompose.ui.theme.NotesComposeTheme
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.IconButton as IconButton1

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(
    homeVIewModel: HomeVIewModel?,
    onNoteClick: (id: String) -> Unit,
    onNavToDetailPage: () -> Unit,
    onNavToLoginPage: () -> Unit,
){
    val homeUiState = homeVIewModel?.homeUiState?: HomeUiState()

    var openDialog by remember {
        mutableStateOf(false)
    }
    var selectedNote: Notes? by remember {
        mutableStateOf(null)
    }
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()


    LaunchedEffect(key1 = Unit){
        homeVIewModel?.loadNotes()
    }
    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavToDetailPage.invoke()}) {
                Icon(imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
        },
        topBar = {
            TopAppBar(

                title = {Text(text = "Home")},
                navigationIcon = {},
                actions = {
                    IconButton1(onClick = {
                        homeVIewModel?.signOut()
                        onNavToLoginPage.invoke()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = null)
                    }
                }
            )
        }
    ) {
        padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            when(homeUiState.notesList){
                is Resource.Loading ->{
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(align = Alignment.Center)
                    )
                }
                is Resource.Success ->{
                    LazyVerticalGrid(columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp)
                    ){
                        items(homeUiState.notesList.data?: emptyList()){
                            note ->
                            run {
                                NoteItem(notes = note,
                                    onLongClick = {
                                        openDialog = true
                                        selectedNote = note
                                    }) {
                                    onNoteClick.invoke(note.documentId)
                                }
                            }
                        }
                    }
                    AnimatedVisibility(visible = openDialog) {
                        AlertDialog(
                            onDismissRequest = {
                                openDialog = false
                            },
                            title = { Text(text = "Delete Note?")},
                            confirmButton = {
                                Button(
                                    onClick = {
                                        selectedNote?.documentId?.let {
                                            homeVIewModel?.deleteNote(it)
                                        }
                                        openDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color.Red
                                    )
                                ) {
                                    Text(text = "Delete")
                                }
                            },
                            dismissButton = {
                                Button(onClick = { openDialog = false}) {
                                    Text(text = "Cancel")
                                }
                            }
                        )
                        
                    }
                }
                else ->{
                    Text(text = homeUiState
                        .notesList.throwable?.localizedMessage?: "Unknown error",
                        color = Color.Red
                    )
                }
            }
        }
    }
    LaunchedEffect(key1 = homeVIewModel?.hasUser ){
        if (homeVIewModel?.hasUser == false){
            onNavToLoginPage.invoke()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    notes: Notes,
    onLongClick: () -> Unit,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .combinedClickable(
                onLongClick = { onLongClick.invoke() },
                onClick = { onClick.invoke() }
            )
            .padding(8.dp)
            .fillMaxWidth(),
        backgroundColor = Utils.colors[notes.colorIndex]
    ) {
        Column {
            Text(
                text = notes.title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                modifier = Modifier.padding(4.dp)
            )

            Spacer(modifier = Modifier.size(4.dp))
            CompositionLocalProvider(
                LocalContentAlpha provides ContentAlpha.disabled) {
                Text(text = notes.description,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(4.dp),
                    maxLines = 4
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
            CompositionLocalProvider(
                LocalContentAlpha provides ContentAlpha.disabled) {
                Text(text = formatDate(notes.timestamp),
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.End),
                    maxLines = 4
                )
            }
        }

    }
}

private fun formatDate(timestamp: Timestamp) : String{
    val sdf = SimpleDateFormat("mm-dd-yy hh:mm", Locale.getDefault())

    return sdf.format(timestamp.toDate())
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewHome() {
    NotesComposeTheme {
        Home(homeVIewModel = null, onNoteClick = {}, onNavToDetailPage = {}, onNavToLoginPage = {})
    }
}
