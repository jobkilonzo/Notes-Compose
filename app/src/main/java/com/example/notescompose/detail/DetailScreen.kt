package com.example.notescompose.detail

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notescompose.Utils
import com.example.notescompose.ui.theme.NotesComposeTheme
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DetailScreen(
    detailViewMdel: DetailViewModel?,
    noteId: String,
    onNavigate: () -> Unit
) {
    val detailUIState =detailViewMdel?.detailUIState?: DetailUIState()

    val isFormsNotBlank = detailUIState.note.isNotBlank() &&
            detailUIState.title.isNotBlank()
    
    val selectedColor by animateColorAsState(
        targetValue = Utils.colors[detailUIState.colorIndex]
    )

    val isNoteIDNotBlank = noteId.isNotBlank()

    val icon = if (isNoteIDNotBlank) Icons.Default.Refresh
    else Icons.Default.Check

    LaunchedEffect(key1 = Unit){
        if (isNoteIDNotBlank){
            detailViewMdel?.getNote(noteId)
        }else{
            detailViewMdel?.resetState()
        }
    }

    val scope = rememberCoroutineScope()

    val scaffoldState = rememberScaffoldState()
    
    Scaffold(scaffoldState = scaffoldState,
        floatingActionButton = {
           AnimatedVisibility(visible = isFormsNotBlank) {
               FloatingActionButton(
                   onClick = {
                       if (isNoteIDNotBlank){
                           detailViewMdel?.updateNote(noteId)
                       }else{
                           detailViewMdel?.addNote()
                       }
                   }) {
                   Icon(imageVector = icon, contentDescription = null)

               }
           }
        }
    ) {
        padding -> 
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = selectedColor)
                .padding(padding)
        ) {
            if (detailUIState.noteAddedStatus){
                scope.launch {
                    scaffoldState.snackbarHostState
                        .showSnackbar("Added note successfully")
                    detailViewMdel?.resetNoteAddedStatus()
                    onNavigate.invoke()
                }
            }
            if (detailUIState.updateNoteStatus){
                scope.launch {
                    scaffoldState.snackbarHostState
                        .showSnackbar("Note updated success fully")
                    detailViewMdel?.resetNoteAddedStatus()
                    onNavigate.invoke()
                }
            }
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                contentPadding = PaddingValues(
                    vertical = 16.dp,
                    horizontal = 8.dp
                )
            ){
                itemsIndexed(Utils.colors){
                    colorIndex, color ->
                    ColorItem(color =color) {
                        detailViewMdel?.onColorChange(colorIndex)
                    }
                }
            }
            OutlinedTextField(value = detailUIState.title,
                onValueChange = {
                    detailViewMdel?.onTitleChane(it)
                },
                label = {Text(text = "Title")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            OutlinedTextField(value = detailUIState.note,
                onValueChange = {detailViewMdel?.onNoteChange(it)},
                label = { Text(text = "Notes")},
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)

            )
        }

        
    }
}


@Composable
fun ColorItem(
    color: Color,
    onClick: () -> Unit
){
    Surface(
        color = color,
        shape = CircleShape,
        modifier = Modifier
            .padding(8.dp)
            .size(36.dp)
            .clickable {
                onClick.invoke()
            },
        border = BorderStroke(2.dp, Color.Black)
    ) {

    }
}

@Preview
@Composable
fun PreviewDetail(){
    NotesComposeTheme {
        DetailScreen(detailViewMdel = null, noteId = "") {
            
        }
    }
}