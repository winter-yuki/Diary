import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(title = "Notes", onCloseRequest = ::exitApplication) {
        DesktopMaterialTheme {
            // TODO context menu: save notes, load notes #13
            // TODO main menu #14
            // TODO double notes screen  #15
            // TODO drug-n-drop cells from one screen to another #16
            Notes(
                TextCell(),
                SketchCell(),
                TextCell()
            )()
        }
    }
}
