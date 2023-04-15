package com.example.notescompose.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.notescompose.modal.Notes
import com.example.notescompose.repository.StorageRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser

class DetailViewModel(private val repository: StorageRepository = StorageRepository()): ViewModel() {
    var detailUIState by mutableStateOf(DetailUIState())
        private set

    private val hasUser: Boolean
        get() = repository.hasUser()

    private val user: FirebaseUser?
        get() = repository.user()

    fun onColorChange(colorIndex: Int){
        detailUIState = detailUIState.copy(colorIndex = colorIndex)
    }

    fun onTitleChane(title: String){
        detailUIState = detailUIState.copy(title = title)
    }

    fun onNoteChange(note: String){
        detailUIState = detailUIState.copy(note = note)
    }

    fun addNote(){
        if (hasUser){
            repository.addNote(
                userId = user!!.uid,
                title = detailUIState.title,
                description = detailUIState.note,
                color = detailUIState.colorIndex,
                timestamp = Timestamp.now()
            ){
                detailUIState = detailUIState.copy(noteAddedStatus =  it)
            }

        }
    }

    fun setEditFields(notes: Notes){
        detailUIState = detailUIState.copy(
            colorIndex = notes.colorIndex,
            title = notes.title,
            note = notes.description
        )
    }

    fun getNote(noteId: String){
        repository.getNotes(
            noteId = noteId,
            onError = {}
        ){
            detailUIState = detailUIState.copy(selectedNote = it)
            detailUIState.selectedNote?.let { it1 -> setEditFields(it1) }
        }
    }

    fun updateNote(
        noteId: String
    ){
        repository.updateNote(
            title = detailUIState.title,
            note = detailUIState.note,
            noteId = noteId,
            color = detailUIState.colorIndex
        ){
            detailUIState = detailUIState.copy(updateNoteStatus = it)
        }
    }

    fun resetNoteAddedStatus(){
        detailUIState = detailUIState.copy(
            noteAddedStatus = false,
            updateNoteStatus = false
        )
    }

    fun resetState(){
        detailUIState = DetailUIState()
    }


}
data class DetailUIState(
    val colorIndex: Int = 0,
    val title:String = "",
    val note: String= "",
    val noteAddedStatus: Boolean = false,
    val updateNoteStatus: Boolean = false,
    val selectedNote: Notes? = null
)