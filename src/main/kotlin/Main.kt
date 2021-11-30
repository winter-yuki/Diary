import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowSize
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

@Composable
fun Menu(tm: TabManager) {
    Row {
        val size = 13.sp
        val modifier = Modifier
            .wrapContentSize()
        forEachCo(
            "New" to {
                val notes = Notes(TextCell())
                tm.add(NotesTab(notes))
            },
            "Open" to { TODO() },
            "Save" to { TODO() }
        ) { (text, block) ->
            TextButton(
                onClick = block,
                modifier = modifier
            ) {
                Text(text, fontSize = size)
            }
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
        }
    }
}
