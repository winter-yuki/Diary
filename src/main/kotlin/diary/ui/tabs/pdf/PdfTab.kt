package diary.ui.tabs.pdf

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.mouseClickable
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.pointer.isSecondaryPressed
import diary.ui.Link
import diary.ui.TabManager
import diary.ui.tabs.Tab
import diary.utils.ui.DrawCanvas
import diary.utils.ui.makeAlertDialogStateful
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import java.nio.file.Path as FilePath

class PdfTab(
    doc: PDDocument,
    private val tabManager: TabManager,
    private val path: FilePath,
    currPage: Int = 0
) : Tab {

    private val renderer: PDFRenderer = PDFRenderer(doc)
    private var currPage by mutableStateOf(currPage)
    private val nPages: Int = doc.numberOfPages
    override val id: Tab.Id by lazy { Tab.Id(path) }
    private val pageDrawPaths =
        mutableMapOf<Int, MutableState<Path>>().withDefault {
            mutableStateOf(Path())
        }

    override fun navigate(link: Link) {
        require(link is PdfLink)
        // TODO
//        _currPage.value = link.page ?: 0
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun invoke() {
        var linkCreatedDialog by makeAlertDialogStateful(
            title = "Link created!",
            text = "Link created and now you can add it to your notes"
        )
        val image = remember { mutableStateOf(render()) }
        Column(modifier = Modifier.fillMaxSize()) {
            DrawCanvas(
                pageDrawPaths.getValue(currPage),
                bitmap = image.value,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxHeight(0.8F)
                    .mouseClickable {
                        if (buttons.isSecondaryPressed) {
                            tabManager.linkBuffer.link = PdfLink(path, currPage)
                            linkCreatedDialog = true
                        }
                    }
            )
            NavigationBar(image)
        }
    }

    @Composable
    private fun ColumnScope.NavigationBar(bitmapState: MutableState<ImageBitmap>) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            var bitmap by bitmapState
            NavigationButton("Prev") {
                if (currPage > 0) {
                    currPage--
                    bitmap = render()
                }
            }
            Text(
                "~ ${currPage + 1} ~",
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterVertically)
            )
            NavigationButton("Next") {
                if (currPage + 1 < nPages) {
                    currPage++
                    bitmap = render()
                }
            }
        }
    }

    @Composable
    private fun RowScope.NavigationButton(text: String, onClick: () -> Unit) {
        TextButton(
            onClick = onClick,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterVertically)
        ) {
            Text(text)
        }
    }

    private fun render(): ImageBitmap =
        renderer.renderImage(currPage).toComposeImageBitmap()

    companion object {
        fun from(path: FilePath, tabManager: TabManager, currPage: Int = 0) =
            PdfTab(
                PDDocument.load(path.toFile()),
                tabManager = tabManager,
                currPage = currPage,
                path = path
            )
    }
}
