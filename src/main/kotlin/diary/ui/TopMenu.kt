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
import diary.ui.spaces.pdf.PdfSpace
import diary.ui.spaces.notes.NotesSpace
import diary.utils.FileType
import diary.utils.callJFileChooser
import diary.utils.makeAlertDialog

@Composable
fun TopMenu(workSpace: WorkSpace) {
    var wrongFileTypeDialog by makeAlertDialog(
        title = "Wrong file type",
        text = "Only PDF and Diary files are supported"
    )
    Row {
        MenuButton("New") {
            val notes = NotesSpace(workSpace = workSpace)
            workSpace.add(Tab(notes))
        }
        MenuButton("Open") {
            val path = callJFileChooser("Select File to Open") ?: return@MenuButton
            val space = when (FileType.of(path)) {
                FileType.Diary -> NotesSpace.from(path, workSpace)
                FileType.Pdf -> PdfSpace.from(path, workSpace)
                else -> {
                    wrongFileTypeDialog = true
                    return@MenuButton
                }
            }
            workSpace.add(Tab(space))
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
