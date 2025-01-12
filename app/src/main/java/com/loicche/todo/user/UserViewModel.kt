package com.loicche.todo.user

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.loicche.todo.data.Api
import com.loicche.todo.data.User
import com.loicche.todo.data.UserUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserViewModel: ViewModel() {
    private val userService = Api.userWebService

    fun getUser(scope: CoroutineScope): User? {
        var user: User? = null
        scope.launch{
            user = userService.fetchUser().body()
        }
        return user
    }

    fun setUser(name: String, scope: CoroutineScope) {
        scope.launch {
            userService.update(UserUpdate.getFromName(name))
        }
    }



    fun publishPicture(uri: Uri?, scope: CoroutineScope, contentResolver: ContentResolver) {
        uri?.let {
            scope.launch{
                userService.updateAvatar(it.toRequestBody(contentResolver))
            }
        }
    }

    private fun Uri.toRequestBody(contentResolver: ContentResolver): MultipartBody.Part {
        val fileInputStream = contentResolver.openInputStream(this)!!
        val fileBody = fileInputStream.readBytes().toRequestBody()
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "avatar.jpg",
            body = fileBody
        )
    }
}