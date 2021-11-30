import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import org.jetbrains.skija.paragraph.TextBox
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication

@Composable
fun AnnotatedClickableText() {
    val annotatedText = buildAnnotatedString {
        append("Click ")
        pushStringAnnotation(tag = "tag", annotation = "annotation")
        withStyle(style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)
        ) { append("here") }
    }

    ClickableText(
        text = annotatedText,
        onClick = { println("Clicked") }
    )
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
        DesktopMaterialTheme {
            // TODO context menu: save notes, load notes #13
            // TODO main menu #14
            // TODO double notes screen  #15
            // TODO drug-n-drop cells from one screen to another #16
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ctrl+Tab для переключения между ячейками вперед")
                Text("Shift+Tab для переключения между ячейками назад")
                Notes(
                    List(10) { i ->
                        if (i % 2 == 0) TextCell()
                        else SketchCell()
                    }
                )()
            }
        }
    }
}
