package diary

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import diary.ui.TopMenu
import diary.ui.WorkSpace
import diary.ui.rememberTabManager

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
        val tabManager = rememberTabManager()
        DesktopMaterialTheme {
            Column {
                TopMenu(tabManager)
                WorkSpace(tabManager)
            }
        }
    }
}
