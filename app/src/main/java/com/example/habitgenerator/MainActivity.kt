package com.example.habitgenerator

import android.app.Activity
import android.app.ComponentCaller
import android.content.ContentResolver
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.DocumentsContract
import android.util.Log
import android.util.Log.e
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.net.toUri
import com.example.habitgenerator.ui.theme.HabitGeneratorTheme
import com.example.habitgenerator.presentation.EditHabitListViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.file.Files.createFile


val TAG = "--- main ---"
class MainActivity : ComponentActivity() {
    val getContent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d(TAG, "result launcher: $result")
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.also { uri ->
                Log.d(TAG, "got uri: $uri")
                // keep permission across app restarts
                val contentResolver = applicationContext.contentResolver
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                contentResolver.takePersistableUriPermission(uri, takeFlags)
                // add uri to shared preferences
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
                with(sharedPreferences.edit()) {
                    putString("uri", uri.toString())
                    apply()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // reset uri
//        with(sharedPreferences.edit()) {
//            putString("uri", null)
//            apply()
//        }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val uri = sharedPreferences.getString("uri",null)
        if (uri == null) {
            openFile(getContent)
        } else {
            Log.d(TAG, "onCreate: already have uri: $uri")
        }
        setContent {
            App()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        Log.d(TAG, "onActivityResult: ------------------------------ finished activity -----------------------------------------------")
        super.onActivityResult(requestCode, resultCode, data, caller)
    }
}
// Request code for creating a PDF document.
const val APP_JSON = "application/json"

private fun createFile(activityResultLauncher: ActivityResultLauncher<Intent>) {
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = APP_JSON
        putExtra(Intent.EXTRA_TITLE, "test.json")
    }
    activityResultLauncher.launch(intent)
}

fun openFile(activityResultLauncher: ActivityResultLauncher<Intent>) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = APP_JSON
    }
    activityResultLauncher.launch(intent)
}
private fun writeToUri(contentResolver: ContentResolver, uri: Uri, text: String) {
    try {
        contentResolver.openFileDescriptor(uri, "w")?.use {
            FileOutputStream(it.fileDescriptor).use {
                it.write( text.toByteArray() )
            }
        }
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

@Throws(IOException::class)
private fun readFromUri(contentResolver: ContentResolver, uri: Uri): String {
    val stringBuilder = StringBuilder()
    contentResolver.openInputStream(uri)?.use { inputStream ->
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            var line: String? = reader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = reader.readLine()
            }
        }
    }
    return stringBuilder.toString()
}
