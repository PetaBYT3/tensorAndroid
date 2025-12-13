package com.xliiicxiv.tensor.extension

import java.util.Locale

fun String.capitalizeEachWord(): String {
    if (this.isEmpty()) return ""

    return this.split(" ").joinToString(" ") { word ->
        word.lowercase().replaceFirstChar { firstChar ->
            if (firstChar.isLowerCase()) firstChar.titlecase(Locale.getDefault()) else firstChar.toString()
        }
    }
}