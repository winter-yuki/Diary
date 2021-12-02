package diary.ui.tabs.notes

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import diary.ui.Link
import diary.ui.TabManager
import diary.ui.tabs.Tab
import diary.utils.JFileChooserMode
import diary.utils.callJFileChooser
import diary.utils.removeIfExists
import kotlinx.coroutines.runBlocking
import java.nio.file.Files.createDirectory
import java.nio.file.Path
import kotlin.io.path.createFile

class NotesTab(
    private val cells: SnapshotStateList<Cell> = mutableStateListOf(),
    private val tabManager: TabManager,
    var path: Path = Path.of("")
) : Tab {

    // TODO change id to something better: new notes are the same
    override val id: Tab.Id get() = Tab.Id(path)
    private val navigateDstName = mutableStateOf<Int?>(null)

    override fun navigate(link: Link) {
        require(link is NotesLink)
        val i = cells.indexOfFirst { it.name == link.cellName?.name }
        println("i = $i") // TODO
        if (i != -1) {
            navigateDstName.value = i
        }
    }

    @Composable
    override operator fun invoke() {
        Box(modifier = Modifier.fillMaxSize()) {
            val state = rememberLazyListState()
            if (cells.isEmpty()) {
                cells += TextCell()
            }
            Column {
                Button(
                    onClick = {
                        val path = callJFileChooser(
                            title = "Select File Path",
                            mode = JFileChooserMode.Save
                        )?.let { path ->
                            save(path)
                            this@NotesTab.path = path
                        }
                    },
                    modifier = Modifier.align(Alignment.End).wrapContentSize()
                ) {
                    Text("Save", fontSize = 10.sp)
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(end = 12.dp),
                    state = state
                ) {
                    itemsIndexed(cells) { i, cell ->
                        CellBox(i, cell) { cell() }
                    }
                }
                runBlocking {
                    println("Run blocking") // TODO
                    navigateDstName.value?.let {
                        state.scrollToItem(it, 0)
                    }
                }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(
                    scrollState = state
                )
            )
        }

        // TODO move cell up/down buttons #3
        // TODO show buttons only on focus #4
    }

    @Composable
    private fun CellBox(iCell: Int, cell: Cell, label: String = "", block: @Composable () -> Unit) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(0.75F)
            ) {
                Button(
                    modifier = Modifier
                        .padding(5.dp)
                        .size(width = 30.dp, height = 25.dp)
                        .align(Alignment.End),
                    onClick = { cells.removeAt(iCell) }
                ) {}

                // TODO mb move to cell and make cell name immutable
                var text by remember { mutableStateOf(label) }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.5f).fillMaxHeight(0.1f),
                    value = text,
                    textStyle = TextStyle(fontSize = 10.sp),
                    singleLine = true,
                    onValueChange = {
                        text = it
                        cell.name = it
                    }
                )

                block()

                Row {
                    CellButton("Add text") {
                        cells.add(iCell + 1, TextCell())
                    }
                    CellButton("Add sketch") {
                        cells.add(iCell + 1, SketchCell())
                    }
                    // TODO refactor
                    when (cell) {
                        is TextCell -> CellButton("Render") {
                            cells.removeAt(iCell)
                            cells.add(
                                iCell,
                                RenderedTextCell(text = cell.text, notesPath = path, tabManager = tabManager).apply {
                                    name = cell.name
                                }
                            )
                        }
                        is RenderedTextCell -> CellButton("Edit") {
                            cells.removeAt(iCell)
                            cells.add(
                                iCell,
                                TextCell(_text = cell.text).apply {
                                    name = cell.name
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun CellButton(text: String, onClick: () -> Unit) {
        Button(
            modifier = Modifier
                .wrapContentSize()
                .padding(10.dp),
            onClick = onClick,
        ) {
            Text(text, fontSize = 10.sp)
        }
    }

    private fun save(path: Path) {
        path.removeIfExists()
        val diaryPath = Path.of(
            // TODO make Path extension
            if (path.toFile().endsWith(".diary")) path.toString()
            else "$path.diary"
        )
        val dir = createDirectory(diaryPath)
        cells.forEachIndexed { i, cell ->
            val file = Path.of(dir.toString(), "$i.cell").createFile()
            cell.save(file)
        }
        // TODO make zip
    }

    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        fun from(path: Path, tabManager: TabManager) = NotesTab(
            buildList {
                path.toFile().walk().filter { it.isFile }.sortedBy { it.name }.forEach { file ->
                    // TODO determine cell type
                    add(TextCell(file.readText()))
                }
            }.toMutableStateList(),
            tabManager = tabManager
        )
    }
}
