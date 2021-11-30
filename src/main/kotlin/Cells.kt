import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.sun.jdi.Value
import java.nio.file.Path

interface Cell {
    fun save(path: Path)

    fun load(path: Path): Cell

    @Composable
    operator fun invoke()

    companion object {
        fun of(path: Path): Cell {
            TODO()
        }
    }
}

abstract class AbstractCell : Cell {
    @Composable
    protected fun cell(block: @Composable () -> Unit) {
        // TODO border rectangle #5
        // TODO remove cell button #2

        Surface(shape = MaterialTheme.shapes.large, elevation = 2.dp) {
            block()
        }
    }
}


class TextCell : AbstractCell() {

    var rendered = false

    override fun save(path: Path) {
        TODO("Not yet implemented") // #7
    }

    override fun load(path: Path): TextCell {
        TODO("Not yet implemented") // #7
    }

    @Composable
    override operator fun invoke() = cell {
        var rendered = false
        var RawText by remember { mutableStateOf("") }

//        var AnnostatedText = buildAnnotatedString {
//            append("Click ")
//            pushStringAnnotation(tag = "tag", annotation = "annotation")
//            withStyle(style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)
//            ) { append("here") }
//        }
//
//        ClickableText(
//            text = AnnostatedText,
//            onClick = { println("Clicked") }
//        )

        Canvas(
            modifier = Modifier
                .size(width = 500.dp, height = 500.dp)
                .background(color = Color.Green)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = { println("Double click") },
                        onTap = { println("Click") }
                    )
                }
        ) {
            if (rendered) {
                println("Oooops")
            } else {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = RawText,
                    singleLine = false,
                    onValueChange = { RawText = it },
                )
            }
        }
    }
}

class SketchCell : AbstractCell() {
    override fun save(path: Path) {
        TODO("Not yet implemented") // #8
    }

    override fun load(path: Path): SketchCell {
        TODO("Not yet implemented") // #8
    }

    @Composable
    override operator fun invoke() = cell {
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }
        var action by mutableStateOf<Offset?>(null)
        val path = androidx.compose.ui.graphics.Path()
        Canvas(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            offsetX = it.x
                            offsetY = it.y
                            path.moveTo(it.x, it.y)
                        }
                    ) { _, dragAmount ->
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                        path.lineTo(offsetX, offsetY)
                        action = Offset(offsetX, offsetY)
                    }
                }
        ) {
            action?.let {
                drawPath(
                    path = path,
                    color = Color.Magenta,
                    alpha = 1f,
                    style = Stroke(3f)
                )
            }
        }

        // TODO resize cell bar appearing on focus #9
        // TODO clickable link: click -> jump #11
    }
}