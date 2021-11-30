import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import java.awt.FileDialog

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
        MenuButton("Save") {
            val path = callFileExplorer(
                title = "Select File Path",
                mode = FileDialog.SAVE
            ) ?: return@MenuButton
            println(path.toString())
            // TODO
        }
    }
}

@Composable
fun WorkSpace(tm: TabManager) {
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
//        DesktopMaterialTheme {
//            // TODO context menu: save notes, load notes #13
//            // TODO main menu #14
//            // TODO double notes screen  #15
//            // TODO drug-n-drop cells from one screen to another #16
//            Column(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Ctrl+Tab для переключения между ячейками вперед")
//                Text("Shift+Tab для переключения между ячейками назад")
//                Notes(
//                    List(3) { i ->
//                        if (i % 2 == 0) TextCell()
//                        else SketchCell()
//                    }
//                )()
//            }
        }
    }
}
