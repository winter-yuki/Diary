import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.awt.FileDialog
import java.awt.Frame
import java.nio.file.Path
import kotlin.io.path.extension

interface UIElem {
    @Composable
    operator fun invoke()
}

@Composable
fun <T> Iterable<T>.forEachIndexedCo(block: @Composable (Int, T) -> Unit) {
    for ((i, t) in this.withIndex()) {
        block(i, t)
    }
}

@Composable
fun <T> Iterable<T>.forEachCo(block: @Composable (T) -> Unit) {
    forEachIndexedCo { _, t ->
        block(t)
    }
}

@Composable
fun <T> forEachCo(vararg ts: T, block: @Composable (T) -> Unit) {
    for (t in ts) {
        block(t)
    }
}

typealias FileDialogMode = Int

fun callFileExplorer(title: String, mode: FileDialogMode = FileDialog.LOAD): Path? =
    FileDialog(Frame(), title).apply {
        this.mode = mode
        isVisible = true
    }.run {
        if (directory == null || file == null) null
        else Path.of(directory, file)
    }

enum class FileType {
    Unknown, Diary, Pdf;

    companion object {
        fun of(path: Path): FileType = when (path.extension) {
            "diary" -> Diary
            "pdf" -> Pdf
            else -> Unknown
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun makeErrorDialog(title: String, text: String): MutableState<Boolean> {
    val delegate = remember { mutableStateOf(false) }
    var open by delegate
    if (open) {
        AlertDialog(
            onDismissRequest = { open = false },
            title = {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Text(text, fontSize = 13.sp)
            },
            buttons = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TextButton(
                        onClick = { open = false },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Cancel")
                    }
                }
            },
            modifier = Modifier.width(200.dp)
        )
    }
    return delegate
}