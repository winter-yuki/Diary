package diary.ui.tabs.pdf

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
import diary.ui.Link
import diary.ui.TabManager
import diary.ui.tabs.Tab
import diary.utils.makeAlertDialogStateful
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import java.nio.file.Path

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
        Box(modifier = Modifier.fillMaxSize().padding(10.dp)) {
            var linkCreatedDialog by makeAlertDialogStateful(
                title = "Link created!",
                text = "Link created and now you can add it to your notes"
            )
            var image by remember { mutableStateOf(render()) }
            var currPage by remember { _currPage }
            Column(modifier = Modifier.fillMaxSize()) {
                Image(
                    image,
                    contentDescription = "Page $currPage",
                    modifier = Modifier
                        .fillMaxHeight(0.95F)
                        .align(Alignment.CenterHorizontally)
                        .mouseClickable {
                            if (buttons.isSecondaryPressed) {
                                tabManager.linkBuffer.link = PdfLink(path, currPage)
                                linkCreatedDialog = true
                            }
                        }
                )
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    TextButton(
                        onClick = {
                            if (currPage > 0) {
                                currPage--
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
                                currPage++
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
        fun from(path: Path, tabManager: TabManager, currPage: Int = 0) =
            PdfTab(
                PDDocument.load(path.toFile()),
                tabManager = tabManager,
                currPage = currPage,
                path = path
            )
    }
}
