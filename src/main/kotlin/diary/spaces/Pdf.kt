package diary.spaces

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeBitmap
import androidx.compose.ui.unit.dp
import diary.ui.UIElem
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import java.nio.file.Path

class Pdf(doc: PDDocument) : UIElem {

    private val renderer: PDFRenderer = PDFRenderer(doc)
    private var currPage: Int = 0
    private val nPages: Int = doc.numberOfPages

    @Composable
    override fun invoke() {
        Box(modifier = Modifier.fillMaxSize().padding(10.dp)) {
            var image by remember { mutableStateOf(render()) }
            Column(modifier = Modifier.fillMaxSize()) {
                Image(
                    image,
                    contentDescription = "Page $currPage",
                    modifier = Modifier
                        .fillMaxHeight(0.95F)
                        .align(Alignment.CenterHorizontally)
                )
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    TextButton(
                        onClick = {
                            if (currPage > 0) {
                                currPage--
                                image = render()
                            }
                        },
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Text("Prev")
                    }
                    TextButton(
                        onClick = {
                            if (currPage + 1 < nPages) {
                                currPage++
                                image = render()
                            }
                        },
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Text("Next")
                    }
                }
            }
        }
        // TODO
    }

    // TODO remove borders
    private fun render(): ImageBitmap =
        renderer.renderImage(currPage).toComposeBitmap()

    companion object {
        fun from(path: Path) = Pdf(PDDocument.load(path.toFile()))
    }
}
