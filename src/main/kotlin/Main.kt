import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(title = "Notes", onCloseRequest = ::exitApplication) {
        DesktopMaterialTheme {
            // TODO context menu: save notes, load notes
            // TODO main menu
            // TODO double notes screen
            // TODO drug-n-drop cells from one scree to another
            Notes(
                TextCell(),
                SketchCell(),
                TextCell()
            )()
        }
    }
}
