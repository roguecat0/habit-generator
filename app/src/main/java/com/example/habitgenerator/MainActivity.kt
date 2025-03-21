package com.example.habitgenerator

import android.app.Activity
import android.app.ComponentCaller
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.DocumentsContract
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
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


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val uri = sharedPreferences.getString("uri", null)
        Log.d("main", "onCreate: $uri")
        if (uri == null) {
            createFile(this,"/".toUri())
        }
//        openFile(this,"/".toUri())
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
        super.onActivityResult(requestCode, resultCode, data, caller)
        if (requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri ->
                Log.d("main", "onActivityResult: $uri")
            }
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            with (sharedPreferences.edit()) {
                putString("uri", data?.data.toString())
                apply()
            }
        }
    }
}
// Request code for creating a PDF document.
const val CREATE_FILE = 1

private fun createFile(activity: Activity, pickerInitialUri: Uri) {
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "plain/text"
        putExtra(Intent.EXTRA_TITLE, "open.txt")

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker before your app creates the document.
        putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
    }
    startActivityForResult(activity, intent, CREATE_FILE, null)
}
// Request code for selecting a PDF document.
const val PICK_PDF_FILE = 2

fun openFile(activity: Activity, pickerInitialUri: Uri) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/pdf"

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
    }

    startActivityForResult(activity, intent, PICK_PDF_FILE, null)
}