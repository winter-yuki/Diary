package diary.ui.spaces

import androidx.compose.foundation.ExperimentalDesktopApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.mouseClickable
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeBitmap
import androidx.compose.ui.unit.dp
import diary.ui.UIComponent
import diary.ui.WorkSpace
import diary.utils.makeAlertDialog
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import java.nio.file.Path

class PdfSpace(doc: PDDocument, private val workSpace: WorkSpace) : UIComponent {

    private val renderer: PDFRenderer = PDFRenderer(doc)
    private var _currPage: Int = 0
    val currPage: Int get() = _currPage
    private val nPages: Int = doc.numberOfPages

    @OptIn(ExperimentalDesktopApi::class)
    @Composable
    override fun invoke() {
        Box(modifier = Modifier.fillMaxSize().padding(10.dp)) {
            var linkCreatedDialog by makeAlertDialog(
                title = "Link created!",
                text = "Link created and now you can add it to your notes"
            )
            var image by remember { mutableStateOf(render()) }
            Column(modifier = Modifier.fillMaxSize()) {
                Image(
                    image,
                    contentDescription = "Page $currPage",
                    modifier = Modifier
                        .fillMaxHeight(0.95F)
                        .align(Alignment.CenterHorizontally)
                        .mouseClickable {
                            if (buttons.isSecondaryPressed) {
                                linkCreatedDialog = true
                                // TODO
                            }
                        }
                )
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    TextButton(
                        onClick = {
                            if (currPage > 0) {
                                _currPage--
                                image = render()
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
                                _currPage++
                                image = render()
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
    private fun render(): ImageBitmap =
        renderer.renderImage(currPage).toComposeBitmap()

    companion object {
        fun from(path: Path, workSpace: WorkSpace) =
            PdfSpace(PDDocument.load(path.toFile()), workSpace = workSpace)
    }
}
