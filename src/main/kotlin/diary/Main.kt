package diary

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import diary.ui.TabManager
import diary.ui.TopMenu
import diary.ui.WorkSpace

fun main() = application {
    Window(
        title = "Notes",
        state = rememberWindowState(
            size = WindowSize(
                width = 1400.dp,
                height = 900.dp,
            ),
            position = WindowPosition(Alignment.Center)
        ),
        onCloseRequest = ::exitApplication
    ) {
        // TODO drug-n-drop cells from one screen to another #16
        val tm = TabManager()
        DesktopMaterialTheme {
            Column {
                TopMenu(tm)
                WorkSpace(tm)
            }
        }
    }
}
