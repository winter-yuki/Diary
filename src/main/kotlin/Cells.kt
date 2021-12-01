import androidx.compose.foundation.Canvas
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
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
import androidx.compose.ui.unit.sp
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

class TextCell(public var RawText: String = "") : AbstractCell() {
    override fun save(path: Path) {
        TODO("Not yet implemented") // #7
    }

    override fun load(path: Path): TextCell {
        TODO("Not yet implemented") // #7
    }

    @Composable
    override operator fun invoke() = cell {
        var text by remember { mutableStateOf(RawText) }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            value = text,
            singleLine = false,
            onValueChange = { text = it; RawText = it },
        )
    }
}

class RenderedTextCell(public val RawText: String = "") : AbstractCell() {
    override fun save(path: Path) {
        TODO("Not yet implemented") // #7
    }

    override fun load(path: Path): TextCell {
        TODO("Not yet implemented") // #7
    }

    @Composable
    override operator fun invoke() = cell {
        val link_regex = Regex("""#[a-zA-Z]+\S""")
        val matches = link_regex.findAll(RawText)
        println(matches.map{ it.groupValues }.joinToString())

        val LinkedText = buildAnnotatedString {
            append(RawText)
            append(" ")
            pushStringAnnotation(tag = "link 1", annotation = "annotation 1")
            withStyle(style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
                append("link 1")
            }
            append(" ")
            pushStringAnnotation(tag = "link 2", annotation = "annotation 2")
            withStyle(style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
                append("link 2")
            }
        }

        Column() {
            ClickableText(
                text = LinkedText,
                onClick = {
                        offset ->
                    var annotation = LinkedText.getStringAnnotations(start = offset, end = offset).lastOrNull()?.item
                    println("Clicked ${annotation}")
                }
            )


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
