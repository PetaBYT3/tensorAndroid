@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.xliiicxiv.tensor.page

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Dataset
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.xliiicxiv.tensor.action.ActionHome
import com.xliiicxiv.tensor.action.ActionProfile
import com.xliiicxiv.tensor.extension.capitalizeEachWord
import com.xliiicxiv.tensor.extension.toImageBitmap
import com.xliiicxiv.tensor.state.StateHome
import com.xliiicxiv.tensor.state.StateProfile
import com.xliiicxiv.tensor.template.CustomConfirmationBottomSheet
import com.xliiicxiv.tensor.template.CustomDangerButton
import com.xliiicxiv.tensor.template.CustomItemList
import com.xliiicxiv.tensor.template.CustomOutlinedButton
import com.xliiicxiv.tensor.template.HorizontalSpacer
import com.xliiicxiv.tensor.template.ImageCropDialog
import com.xliiicxiv.tensor.template.PolygonShape
import com.xliiicxiv.tensor.template.SimpleItemList
import com.xliiicxiv.tensor.template.VerticalSpacer
import com.xliiicxiv.tensor.template.generalPadding
import com.xliiicxiv.tensor.viewmodel.ViewModelProfile
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageProfileCore(
    backStack: NavBackStack<NavKey>,
    snackBarHostState: SnackbarHostState,
    viewModel: ViewModelProfile = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    val imagePicker = rememberFilePickerLauncher(
        title = "Pick Image For Profile Picture",
        type = FileKitType.Image,
        mode = FileKitMode.Single,
        onResult = { file: PlatformFile? ->
            onAction(ActionProfile.PickedImage(file))
            onAction(ActionProfile.BottomSheetChangeProfilePicture)
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
    ) { bitMap: Bitmap? ->
        onAction(ActionProfile.CapturedImage(bitMap))
        onAction(ActionProfile.BottomSheetChangeProfilePicture)
    }

    Scaffold(
        backStack = backStack,
        state = state,
        onAction = onAction
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { snackBarMessage ->
            snackBarHostState.showSnackbar(
                message = snackBarMessage,
                withDismissAction = true
            )
        }
    }

    if (state.bottomSheetChangeProfilePicture) {
        ModalBottomSheet(
            onDismissRequest = { onAction(ActionProfile.BottomSheetChangeProfilePicture) },
            content = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(generalPadding)
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        onClick = { cameraLauncher.launch(null) }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CameraAlt,
                            contentDescription = null
                        )
                    }
                    HorizontalSpacer()
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        onClick = { imagePicker.launch() }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Image,
                            contentDescription = null
                        )
                    }
                }
            }
        )
    }

    if (state.bottomSheetDeleteProfilePicture) {
        CustomConfirmationBottomSheet(
            icon = Icons.Rounded.Delete,
            message = "Are you sure you want to delete this profile picture ?",
            onConfirm = { onAction(ActionProfile.DeleteProfilePicture) },
            onCancel = { onAction(ActionProfile.BottomSheetDeleteProfilePicture) }
        )
    }

    if (state.imageBitmap != null) {
        ImageCropDialog(
            imageBitmap = state.imageBitmap!!,
            onConfirm = { onAction(ActionProfile.CroppedImage(it)) },
            onCancel = { onAction(ActionProfile.PickedImage(null)) }
        )
    }
}

@Composable
private fun Scaffold(
    backStack: NavBackStack<NavKey>,
    state: StateProfile,
    onAction: (ActionProfile) -> Unit
) {
    val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        state = rememberTopAppBarState()
    )

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehaviour.nestedScrollConnection)
            .imePadding(),
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        topBar = {
            TopBar(
                scrollBehavior = scrollBehaviour,
                state = state,
                onAction = onAction
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Content(
                    backStack = backStack,
                    state = state,
                    onAction = onAction
                )
            }
        }
    )
}

@Composable
private fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    state: StateProfile,
    onAction: (ActionProfile) -> Unit
) {
    LargeTopAppBar(
        title = { Text(text = "Profile") },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun Content(
    backStack: NavBackStack<NavKey>,
    state: StateProfile,
    onAction: (ActionProfile) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState, enabled = true)
            .padding(generalPadding)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(50)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        if (state.userData?.userProfilePicture != null) {
                            Image(
                                modifier = Modifier
                                    .fillMaxSize(),
                                bitmap = state.userData.userProfilePicture.toImageBitmap()!!,
                                contentDescription = null
                            )
                        } else {
                            Icon(
                                modifier = Modifier
                                    .size(150.dp)
                                    .align(Alignment.Center),
                                imageVector = Icons.Rounded.Person,
                                contentDescription = null
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Card(
                        onClick = { onAction(ActionProfile.BottomSheetChangeProfilePicture) }
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(10.dp),
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = null
                        )
                    }
                    VerticalSpacer()
                    Card(
                        onClick = { onAction(ActionProfile.BottomSheetDeleteProfilePicture) }
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(10.dp),
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = null
                        )
                    }
                }
            }
        }
        VerticalSpacer()
        val itemList = listOf(
            SimpleItemList(
                icon = Icons.Rounded.AlternateEmail,
                title = state.userData?.userName ?: "User Name Not Set Yet",
                onClick = {}
            ),
            SimpleItemList(
                icon = Icons.Rounded.Person,
                title = state.userData?.displayName ?: "Display Name Not Set Yet",
                onClick = {}
            ),
            SimpleItemList(
                icon = Icons.Rounded.Mail,
                title = state.firebaseUser?.email ?: "?",
                onClick = {}
            ),
            SimpleItemList(
                icon = Icons.Rounded.Phone,
                title = state.firebaseUser?.phoneNumber ?: "No Phone Number",
                onClick = {}
            ),
            SimpleItemList(
                icon = Icons.Rounded.Dataset,
                title = state.firebaseUser?.providerId?.capitalizeEachWord() ?: "?",
                onClick = {}
            ),
        )
        CustomItemList(
            itemList = itemList
        )
        VerticalSpacer()
        CustomOutlinedButton(
            text = "Change Password",
            isLoading = false,
            onClick = { onAction(ActionProfile.ChangePassword) }
        )
        VerticalSpacer()
        CustomDangerButton(
            text = "Sign Out",
            isLoading = false,
            onClick = { onAction(ActionProfile.SignOut) }
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {

    val snackBarHostState = remember { SnackbarHostState() }
    val backStack = rememberNavBackStack()

    Content(
        backStack = backStack,
        state = StateProfile(),
        onAction = {}
    )
}