package diary.spaces.notes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
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
import diary.ui.UIElem
import java.nio.file.Path
import kotlin.io.path.writeText

interface Cell : UIElem {
    fun save(path: Path)

    companion object {
        fun of(path: Path): Cell {
            TODO()
        }
    }
}

abstract class AbstractCell : Cell {
    @Composable
    protected fun cell(block: @Composable () -> Unit) {
        Surface(shape = MaterialTheme.shapes.large, elevation = 2.dp) {
            block()
        }
    }

    companion object {
        fun of(path: Path): Cell {
            TODO()
        }
    }
}

class TextCell(private var _text: String = "") : AbstractCell() {

    val text: String get() = _text

    override fun save(path: Path) {
        path.writeText(text)
    }

    @Composable
    override operator fun invoke() = cell {
        var text by remember { mutableStateOf(this._text) }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            value = text,
            singleLine = false,
            onValueChange = {
                // TODO fix state management
                text = it
                _text = it
            }
        )
    }
}

class RenderedTextCell(val text: String = "") : AbstractCell() {

    override fun save(path: Path) {
        TODO("Not yet implemented") // #7
    }

    @Composable
    override operator fun invoke() = cell {
        val AnnostatedText = buildAnnotatedString {
            pushStringAnnotation(tag = "tag", annotation = "annotation")
            withStyle(
                style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)
            ) {
                append("(empty link)")
            }
        }
        Column {
            Text(text)
            ClickableText(
                text = AnnostatedText,
                onClick = { println("Clicked") },
            )
        }
    }
}

class SketchCell : AbstractCell() {
    override fun save(path: Path) {
        println("Save sketch $path") // TODO
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
