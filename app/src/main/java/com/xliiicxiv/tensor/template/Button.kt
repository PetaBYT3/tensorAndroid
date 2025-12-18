@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.xliiicxiv.tensor.template

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CustomBasicButton(
    modifier: Modifier = Modifier,
    text: String,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp),
        enabled = isEnabled,
        onClick = { onClick.invoke() }
    ) {
        Text(text = text)
    }
}

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp),
        enabled = !isLoading,
        onClick = { onClick.invoke() }
    ) {
        Box() {
            if (isLoading) {
                LoadingIndicator(
                    modifier = Modifier
                )
            } else {
                Text(text = text)
            }
        }
    }
}

@Composable
fun CustomOutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp),
        enabled = !isLoading,
        onClick = { onClick.invoke() }
    ) {
        Box() {
            if (isLoading) {
                LoadingIndicator(
                    modifier = Modifier
                )
            } else {
                Text(text = text)
            }
        }
    }
}

@Composable
fun CustomDangerButton(
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp),
        enabled = !isLoading,
        onClick = { onClick.invoke() },
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.error
        )
    ) {
        Box() {
            if (isLoading) {
                LoadingIndicator(
                    modifier = Modifier
                )
            } else {
                Text(text = text)
            }
        }
    }
}

@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit
) {
    IconButton(
        onClick = { onClick.invoke() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
    }
}