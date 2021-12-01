import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowSize
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
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
            )
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
