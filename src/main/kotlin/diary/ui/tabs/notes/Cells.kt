package diary.ui.tabs.notes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import diary.ui.TabManager
import diary.ui.UIComponent
import diary.ui.tabs.pdf.PdfTab
import kotlinx.coroutines.runBlocking
import org.apache.pdfbox.pdmodel.PDDocument
import java.io.File
import java.nio.file.Path
import kotlin.io.path.writeText

class CellName(val name: String)

interface Cell : UIComponent {
    var name: String
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

class TextCell(
    private var _text: String = "",
    override var name: String = ""
) : AbstractCell() {

    val text: String get() = _text

    override fun save(path: Path) {
        path.writeText(text)
    }

    @Composable
    override operator fun invoke() = cell {
        var text by remember { mutableStateOf(_text) }
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

class RenderedTextCell(
    val text: String = "",
    override var name: String = "",
    val scrollState: LazyListState,
    val cells: SnapshotStateList<Cell>,
//    private val notesPath: Path,
    private val tabManager: TabManager
) : AbstractCell() {

    override fun save(path: Path) {
        path.writeText(text)
    }

    // TODO refactor
    @Composable
    override operator fun invoke() = cell {
        val linkRegex = Regex("""#[a-zA-Z]+\S""")
        val matches = linkRegex.findAll(text)

        val linkedText = buildAnnotatedString {
            var cursor = 0
            for (match in matches) {
                append(text.substring(cursor until match.range.first))

                pushStringAnnotation(tag = match.value, annotation = match.value)
                withStyle(style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
                    append(match.value)
                }
                cursor = match.range.last + 1
            }
            append(text.substring(cursor))
        }

        Column {
            ClickableText(
                modifier = Modifier.fillMaxWidth(),
                text = linkedText
            ) { offset ->
                val linkAnnotation = linkedText
                    .getStringAnnotations(start = offset, end = offset)
                    .lastOrNull()?.item
                val targetName = linkAnnotation?.substring(1)
                val i = cells.indexOfFirst { it.name == targetName }
                if (i > -1) runBlocking { scrollState.scrollToItem(i) }
            }
        }
    }
}

class SketchCell(
    private val initName: String = "",
    override var name: String = "",
    val backgroundImage: String?
) : AbstractCell() {
    override fun save(path: Path) {
        println("Save sketch $path") // TODO
    }

    @Composable
    override operator fun invoke() = cell {
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }
        var action by remember { mutableStateOf<Offset?>(null) }
        val path = androidx.compose.ui.graphics.Path()

//        if (backgroundImage != File("null")) { }
//            var imageBitmap = remember {
//                org.jetbrains.skija.Image.makeFromEncoded(backgroundImage.readBytes()).asImageBitmap()
//            }
//            Image(imageBitmap, "background")
//        } else println("No image")

        Canvas(
            modifier = Modifier
                .clipToBounds()
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
            if (backgroundImage != "null") {
                drawImage(org.jetbrains.skija.Image.makeFromEncoded(File(backgroundImage.toString()).readBytes()).asImageBitmap())
            }

//            drawRect(
//                color=Color.Red,
//                topLeft = Offset(0f,0f),
//                size = Size(width=100f, height = 100f)
//            )

            action?.let {
                drawPath(
                    path = path,
                    color = Color.Magenta,
                    alpha = 1f,
                    style = Stroke(3f)
                )
            }
        }

//        mainCanvas

//        if (backgroundImage != File("null")) {
//        }

        // TODO resize cell bar appearing on focus #9
        // TODO clickable link: click -> jump #11
    }
}
