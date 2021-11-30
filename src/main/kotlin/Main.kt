import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(title = "Notes", onCloseRequest = ::exitApplication) {
        DesktopMaterialTheme {
            // TODO context menu: save notes, load notes #13
            // TODO main menu #14
            // TODO double notes screen  #15
            // TODO drug-n-drop cells from one screen to another #16

            //            var WinWidth = 500.dp
            //            Box (
            //                modifier = Modifier
            //                    .fillMaxSize()
            //                    .onSizeChanged {
            //                        WinWidth = it.toSize().width.dp
            //                        println("Window width: $WinWidth")
            //                    }
            //            ) {
            //                Box (
            //                    modifier = Modifier
            //                        .background(color = Color.Green)
            //                        .fillMaxHeight()
            //                        .width( WinWidth/2 )
            //                ) {}
            //            }

            Row () {
                Box (modifier = Modifier.fillMaxHeight().fillMaxWidth(0.2f)) {}
                Column (
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.75f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Ctrl+Tab для переключения между ячейками вперед")
                    Text("Shift+Tab для переключения между ячейками назад")
                    Notes(
                        TextCell(),
                        SketchCell(),
                        TextCell()
                    ) ()
                    Row() {
                        Button (
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .height(50.dp)
                                .padding(10.dp),
                            onClick = {},
                        ) {
                            Text("Add text cell")
                        }
                        Button (
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(10.dp),
                            onClick = {},
                        ) {
                            Text("Add scratch cell")
                        }
                    }
                }
                Box (modifier = Modifier.fillMaxHeight().fillMaxWidth()) {}
            }
        }
    }
}
