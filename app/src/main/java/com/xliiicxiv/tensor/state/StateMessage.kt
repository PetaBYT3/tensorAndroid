package com.xliiicxiv.tensor.state

import com.xliiicxiv.tensor.dataclass.DataClassMessage

data class StateMessage(

    val messagePerson: List<DataClassMessage> = emptyList()

)
