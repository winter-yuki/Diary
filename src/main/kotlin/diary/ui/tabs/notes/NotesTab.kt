package diary.ui.tabs.notes

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
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
import java.nio.file.Files.createDirectory
import java.nio.file.Path
import kotlin.io.path.createFile
import kotlin.io.path.extension

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
                TextButton(
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
                        CellBox(i, cell, state) { cell() }
                    }
                }
//                runBlocking {
//                    println("Run blocking") // TODO
//                    navigateDstName.value?.let {
//                        state.scrollToItem(it, 0)
//                    }
//                }
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
    private fun CellBox(iCell: Int, cell: Cell, state: LazyListState, block: @Composable () -> Unit) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(0.75F)
            ) {
                TextButton(
                    modifier = Modifier
//                        .padding(5.dp)
                        .wrapContentSize()
                        .border(
                            BorderStroke(
                                1.dp, MaterialTheme.colors.primary.copy(alpha = 0.2f)
                            )
                        )
                        .align(Alignment.End),
                    onClick = { cells.removeAt(iCell) }
                ) {
                    Text("XO")
                }

                // TODO mb move to cell and make cell name immutable
                var text by remember { mutableStateOf(cell.name) }
                BasicTextField(
                    modifier = Modifier
//                        .fillMaxWidth(0.5f)
//                        .fillMaxHeight(0.1f)
                        .padding(2.dp)
                        .border(
                            BorderStroke(
                                1.dp, MaterialTheme.colors.primary.copy(alpha = 0.2f)
                            )
                        )
                        .wrapContentSize(),
//                        .background(color = Color.),
                    value = text,
                    textStyle = TextStyle(fontSize = 15.sp),
                    singleLine = true,
                    onValueChange = {
                        text = it
                        cell.name = it
                    },
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
                                RenderedTextCell(
                                    text = cell.text,
                                    name = cell.name,
                                    scrollState = state,
                                    cells = cells,
                                    tabManager = tabManager
                                )
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
                .padding(5.dp)
                .height(26.dp),
//                .border(
//                    BorderStroke(
//                        1.dp, MaterialTheme.colors.primary.copy(alpha = 0.2f)
//                    )
//                ),
            onClick = onClick,
            shape = MaterialTheme.shapes.small
        ) {
            Text(text, fontSize = 8.sp)
        }
    }

    private fun save(path: Path) {
        path.removeIfExists()
        val diaryPath = Path.of(
            // TODO make Path extension
            if (path.extension == "diary") path.toString()
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
