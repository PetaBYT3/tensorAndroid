package com.xliiicxiv.tensor.state

import com.google.firebase.auth.FirebaseUser
import com.xliiicxiv.tensor.dataclass.DataClassUser

data class StateAddMessage(

    val currentUser: FirebaseUser? = null,

    val textFieldSearchUser: String = "",
    val searchUserResult: List<DataClassUser>? = null

)
