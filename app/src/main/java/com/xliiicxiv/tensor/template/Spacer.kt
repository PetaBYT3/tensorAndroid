package com.xliiicxiv.tensor.template

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VerticalSpacer() {
    Spacer(modifier = Modifier.height(generalPadding))
}

@Composable
fun HorizontalSpacer() {
    Spacer(modifier = Modifier.width(generalPadding))
}