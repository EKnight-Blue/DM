package com.loicche.todo.user

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.viewModels
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.rememberCoroutineScope
import coil3.compose.AsyncImage
import com.loicche.todo.data.User
import com.loicche.todo.list.Task
import java.util.UUID

class UserActivity : ComponentActivity() {
    private val userModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val composeScope = rememberCoroutineScope()
            val bitmap: Bitmap? by remember { mutableStateOf(null) }
            var uri: Uri? by remember { mutableStateOf(null) }


            val pickPicture = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
                uri = it
                userModel.publishPicture(uri, composeScope, contentResolver)
            }
            val requestPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) pickPicture.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {success ->
                if (success) {
                    uri = captureUri
                }
            }
            var user by remember { mutableStateOf(userModel.getUser(composeScope)) }


            Column {
                AsyncImage(
                    modifier = Modifier.fillMaxHeight(.2f),
                    model = bitmap ?: uri,
                    contentDescription = null
                )
                OutlinedTextField(
                    user?.name ?: "USER",
                    {newName ->
                        user = user?.copy(name = newName) ?: User("", newName, null)
                    }
                )
                Button(
                    onClick = {
                        takePicture.launch(captureUri!!)
                    },
                    content = { Text("Take picture") }
                )
                Button(
                    onClick = {
//                        if (Build.VERSION.SDK_INT < 29) {
//                            requestPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                        } else {
                            pickPicture.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//                        }
                    },
                    content = { Text("Pick photo") }
                )
                Button(
                    onClick = {
                        uri?.let { userModel.publishPicture(it, composeScope, contentResolver) }
                        user?.let { userModel.setUser(it.name, composeScope)}
                        finish()
                    },
                    content = { Text("Validate") }
                )
            }
        }
    }
    private val captureUri by lazy {
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
    }
}
