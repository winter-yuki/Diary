package diary

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import diary.ui.Theme
import diary.ui.TabManager
import diary.ui.TopMenu

fun main() = application {
    Window(
        title = "Notes",
        state = rememberWindowState(
            size = WindowSize(
                width = 1400.dp,
                height = 900.dp,
            ),
            position = WindowPosition(Alignment.Center),
        ),
        onCloseRequest = ::exitApplication,
    ) {
        val tabManager = TabManager()
        Theme {
//        MaterialTheme {
            Column {
                TopMenu(tabManager)
                tabManager()
            }
        }
    }
}
