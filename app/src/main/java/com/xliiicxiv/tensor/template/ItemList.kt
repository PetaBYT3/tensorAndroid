@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.xliiicxiv.tensor.template

import android.os.Message
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

data class MessageItemList(
    val profilePicture: String,
    val userDisplay: String,
    val lastMessage: String,
)

@Composable
fun CustomMessageList(
    modifier: Modifier = Modifier,
    itemList: List<MessageItemList>,
    onClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
    ) {
        items(
            items = itemList,
            key = { itemList -> itemList.profilePicture },
        ) { messageList ->
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                onClick = { onClick.invoke(messageList.userDisplay) }
            ) {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.Black)
                    ) {}
                    HorizontalSpacer()
                    Column() {
                        CustomTextTitle(text = messageList.userDisplay)
                        VerticalSpacer()
                        CustomTextContent(text = messageList.lastMessage)
                    }
                }
            }
            Spacer(modifier = Modifier.height(2.5.dp))
        }
    }
}