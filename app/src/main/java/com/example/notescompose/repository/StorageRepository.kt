package com.example.notescompose.repository

import com.example.notescompose.modal.Notes
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


const val NOTES_COLLECTION_REF = "notes"
class StorageRepository() {
    fun user() =  Firebase.auth.currentUser
    fun hasUser(): Boolean = Firebase.auth.currentUser != null
    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()

    private val notesRef: CollectionReference = Firebase
        .firestore.collection(NOTES_COLLECTION_REF)

    fun getUserNotes(
        userId: String
    ): Flow<Resource<List<Notes>>> = callbackFlow {
        var snapshotSTateListener: ListenerRegistration? =null
        try {
            snapshotSTateListener = notesRef
                .orderBy("timestamp")
                .whereEqualTo("userId", userId)
                .addSnapshotListener{
                    snapshot, e ->
                        val response = if (snapshot != null){
                            val notes = snapshot.toObjects(Notes::class.java)
                            Resource.Success(data = notes)
                        }else{
                            Resource.Error(throwable = e?.cause)
                        }
                    trySend(response)
                }
        }catch (e: Exception){
            trySend(Resource.Error(e?.cause))
            e.printStackTrace()
        }
        awaitClose{
            snapshotSTateListener?.remove()
        }
    }
    fun getNotes(
        noteId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (Notes?) -> Unit
    ){
       notesRef
           .document(noteId)
           .get()
           .addOnSuccessListener {
               onSuccess.invoke(it.toObject(Notes::class.java))
           }
           .addOnFailureListener {
               result ->
               onError.invoke(result.cause)
           }
    }

    fun addNote(
        userId: String,
        title:String,
        description:String ,
        timestamp: Timestamp,
        color:Int,
        onComplete: (Boolean) -> Unit
    ){
        val documentId = notesRef.document().id
        val note = Notes(
            userId,
            title,
            description,
            timestamp,
            colorIndex = color,
            documentId = documentId
        )
        notesRef.document(documentId)
            .set(note)
            .addOnCompleteListener {
                result -> onComplete.invoke(result.isSuccessful)
            }
    }

    fun deleteNote(noteId: String, onComplete: (Boolean) -> Unit){
        notesRef.document(noteId)
            .delete()
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
    }
    fun signOut() = Firebase.auth.signOut()

    fun updateNote(
        title: String,
        note: String,
        color: Int,
        noteId: String,
        onResult: (Boolean) -> Unit
    ){
        val updateData = hashMapOf<String, Any>(
            "colorIndex" to color,
            "description" to note,
            "title" to title
        )
        notesRef.document(noteId)
            .update(updateData)
            .addOnCompleteListener {
                onResult(it.isSuccessful)
            }
    }
}


sealed class Resource<T>(
    val data: T? = null,
    val throwable: Throwable? = null,
){
    class Loading<T>:Resource<T>()
    class Success<T>(data: T?): Resource<T>()
    class Error<T>(throwable: Throwable?): Resource<T>()
}