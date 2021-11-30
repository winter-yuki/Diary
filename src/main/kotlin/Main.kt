import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowSize
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

@Composable
private fun MenuButton(text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.wrapContentSize()
    ) {
        Text(text, fontSize = 13.sp)
    }
}

@Composable
private fun Menu(tm: TabManager) {
    var wrongFileTypeDialog by makeErrorDialog(
        title = "Wrong file type",
        text = "Only PDF and Diary files are supported"
    )
    Row {
        MenuButton("New") {
            val notes = Notes(TextCell())
            tm.add(NotesTab(notes))
        }
        MenuButton("Open") {
            val path = callFileExplorer("Select File to Open") ?: return@MenuButton
            when (FileType.of(path)) {
                FileType.Diary -> NotesTab(Notes.from(path))
                else -> null.also {
                    wrongFileTypeDialog = true
                }
            }?.let { tab -> tm.add(tab) }
        }
    }
}

@Composable
private fun WorkSpace(tm: TabManager) {
    tm()
}

fun main() = application {
    Window(
        title = "Notes",
        state = rememberWindowState(
            size = WindowSize(
                width = 1400.dp,
                height = 900.dp,
            )
        ),
        onCloseRequest = ::exitApplication
    ) {
        // TODO drug-n-drop cells from one screen to another #16
        val tm = TabManager()
        DesktopMaterialTheme {
            Column {
                Menu(tm)
                WorkSpace(tm)
            }
        }
    }
}
