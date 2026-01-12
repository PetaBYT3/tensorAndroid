@file:OptIn(ExperimentalMaterial3Api::class)

package com.xliiicxiv.tensor.template

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun CustomConfirmationBottomSheet(
    icon: ImageVector,
    message: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onCancel.invoke() },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(generalPadding)
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(75.dp),
                    imageVector = icon,
                    contentDescription = null
                )
                VerticalSpacer()
                Text(text = message)
                VerticalSpacer()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedButton(
                        modifier = Modifier
                            .height(45.dp),
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                                onCancel.invoke()
                            }
                        }
                    ) {
                        Text(text = "No")
                    }
                    HorizontalSpacer()
                    Button(
                        modifier = Modifier
                            .height(45.dp),
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                onConfirm.invoke()
                                onCancel.invoke()
                            }
                        }
                    ) {
                        Text(text = "Yes")
                    }
                }
            }
        }
    )
}