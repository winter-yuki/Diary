import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

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
        DesktopMaterialTheme {
            // TODO context menu: save notes, load notes #13
            // TODO main menu #14
            // TODO double notes screen  #15
            // TODO drug-n-drop cells from one screen to another #16
            Notes(
                List(30) { i ->
                    if (i % 2 == 0) TextCell()
                    else SketchCell()
                }
            )()
        }
    }
}