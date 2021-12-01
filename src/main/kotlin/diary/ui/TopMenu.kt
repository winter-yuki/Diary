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
import diary.spaces.Pdf
import diary.spaces.notes.Notes
import diary.spaces.notes.TextCell
import diary.utils.FileType
import diary.utils.callFileExplorer
import diary.utils.makeAlertDialog

@Composable
fun TopMenu(tm: TabManager) {
    var wrongFileTypeDialog by makeAlertDialog(
        title = "Wrong file type",
        text = "Only PDF and Diary files are supported"
    )
    Row {
        MenuButton("New") {
            val notes = Notes(TextCell())
            tm.add(Tab(notes))
        }
        MenuButton("Open") {
            val path = callFileExplorer("Select File to Open") ?: return@MenuButton
            val space = when (FileType.of(path)) {
                FileType.Diary -> Notes.from(path)
                FileType.Pdf -> Pdf.from(path)
                else -> {
                    wrongFileTypeDialog = true
                    return@MenuButton
                }
            }
            tm.add(Tab(space))
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
