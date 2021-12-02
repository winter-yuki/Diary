package diary.ui.tabs.pdf
import androidx.compose.foundation.ExperimentalDesktopApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.mouseClickable
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import diary.ui.Link
import diary.ui.TabManager
import diary.ui.tabs.Tab
import diary.utils.makeAlertDialog
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.jetbrains.skija.Bitmap
import org.jetbrains.skiko.toBitmap
import java.nio.file.Path
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput

class PdfTab(
    doc: PDDocument,
    private val tabManager: TabManager,
    private val path: Path,
    currPage: Int = 0
) : Tab {

    private val renderer: PDFRenderer = PDFRenderer(doc)
    private var _currPage = mutableStateOf(currPage)
    val currPage: Int get() = _currPage.value
    private val nPages: Int = doc.numberOfPages
    override val id: Tab.Id by lazy { Tab.Id(path) }

    override fun navigate(link: Link) {
        require(link is PdfLink)
        _currPage.value = link.page ?: 0
    }

    @OptIn(ExperimentalDesktopApi::class)
    @Composable
    override fun invoke() {
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }
        var action by remember { mutableStateOf<Offset?>(null) }
        val paintedPath = androidx.compose.ui.graphics.Path()

        Box(modifier = Modifier.fillMaxSize().padding(10.dp)) {
            var linkCreatedDialog by makeAlertDialog(
                title = "Link created!",
                text = "Link created and now you can add it to your notes"
            )
            var image by remember { mutableStateOf(render().asImageBitmap()) }
            var currPage by remember { _currPage }

            Column(modifier = Modifier.fillMaxSize()) {
                Canvas(
                    modifier = Modifier
                        .fillMaxHeight(0.95F)
                        .fillMaxWidth(image.width.toFloat())
                        .align(Alignment.CenterHorizontally)
                        .mouseClickable {
                            if (buttons.isSecondaryPressed) {
                                tabManager.linkBuffer.link = PdfLink(path, currPage)
                                linkCreatedDialog = true
                            }
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = {
                                    offsetX = it.x
                                    offsetY = it.y
                                    paintedPath.moveTo(it.x, it.y)
                                }
                            ) { _, dragAmount ->
                                offsetX += dragAmount.x
                                offsetY += dragAmount.y
                                paintedPath.lineTo(offsetX, offsetY)
                                action = Offset(offsetX, offsetY)
                            }
                        }
                ) {
                    drawImage(image)
                    action?.let {
                        drawPath(
                            path = paintedPath,
                            color = Color(0xFF37596D), // Color.Magenta,
                            alpha = 1f,
                            style = Stroke(3f)
                        )
                    }
                }

                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    TextButton(
                        onClick = {
                            if (currPage > 0) {
                                currPage--
                                image = render().asImageBitmap()
                            }
                        },
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.CenterVertically)
                    ) {
                        Text("Prev")
                    }
                    Text(
                        "~ ${currPage + 1} ~",
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.CenterVertically)
                    )
                    TextButton(
                        onClick = {
                            if (currPage + 1 < nPages) {
                                currPage++
                                image = render().asImageBitmap()
                            }
                        },
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.CenterVertically)
                    ) {
                        Text("Next")
                    }
                }
            }
        }
    }

    // TODO remove extra padding
//    private fun render(): ImageBitmap =
//        renderer.renderImage(currPage).toComposeBitmap()

    private fun render(): Bitmap =
        renderer.renderImage(currPage).toBitmap()

    companion object {
        fun from(path: Path, tabManager: TabManager, currPage: Int = 0) =
            PdfTab(
                PDDocument.load(path.toFile()),
                tabManager = tabManager,
                currPage = currPage,
                path = path
            )
    }
}
