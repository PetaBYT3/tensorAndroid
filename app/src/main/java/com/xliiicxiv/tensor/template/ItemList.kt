@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.xliiicxiv.tensor.template

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class SimpleItemList(
    val icon: ImageVector,
    val title: String,
    val onClick: () -> Unit
)

@Composable
fun CustomItemList(
    modifier: Modifier = Modifier,
    itemList: List<SimpleItemList>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge),
        verticalArrangement = Arrangement.spacedBy(2.5.dp)
    ) {
        itemList.forEach { itemIndex ->
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                onClick = { itemIndex.onClick.invoke() }
            ) {
                Row(
                    modifier = Modifier
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = itemIndex.icon,
                        contentDescription = null
                    )
                    HorizontalSpacer()
                    Text(text = itemIndex.title)
                }
            }
        }
    }
}