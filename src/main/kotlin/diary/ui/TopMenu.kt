package diary.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import diary.ui.tabs.notes.NotesTab
import diary.ui.tabs.pdf.PdfTab
import diary.utils.FileType
import diary.utils.callJFileChooser
import diary.utils.makeAlertDialog

@Composable
fun TopMenu(tabManager: TabManager) {
    var wrongFileTypeDialog by makeAlertDialog(
        title = "Wrong file type",
        text = "Only PDF and Diary files are supported"
    )
    Row {
        MenuButton("New") {
            val notes = NotesTab(tabManager = tabManager)
            tabManager.add(notes)
        }
        MenuButton("Open") {
            val path = callJFileChooser("Select File to Open") ?: return@MenuButton
            val space = when (FileType.of(path)) {
                FileType.Diary -> NotesTab.from(path, tabManager)
                FileType.Pdf -> PdfTab.from(path, tabManager)
                else -> {
                    wrongFileTypeDialog = true
                    return@MenuButton
                }
            }
            tabManager.add(space)
        }
    }
}

@Composable
private fun MenuButton(text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.wrapContentSize()
    ) {
        Text(text, fontSize = 13.sp)
    }
}
