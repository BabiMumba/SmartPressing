package cd.cleanto.clean.Models

import com.google.firebase.Timestamp

data class User (
    val uid:String,
    val nom: String,
    val prenom: String,
    val email: String,
    val password: String,
    val date: Timestamp
)