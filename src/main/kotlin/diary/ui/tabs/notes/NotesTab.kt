package diary.ui.tabs.notes

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import diary.ui.Link
import diary.ui.TabManager
import diary.ui.tabs.Tab
import diary.ui.tabs.notes.cells.*
import diary.utils.JFileChooserMode
import diary.utils.callJFileChooser
import diary.utils.removeIfExists
import java.nio.file.Files.createDirectory
import java.nio.file.Path
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.io.path.createFile
import kotlin.io.path.extension

class NotesTab(
    private val cells: SnapshotStateList<Cell> = mutableStateListOf(),
    private val tabManager: TabManager,
    var path: Path = Path.of("")
) : Tab {

    // TODO change id to something better: new notes are the same
    override val id: Tab.Id get() = Tab.Id(path)

    override fun navigate(link: Link) {
        require(link is NotesLink)
        // TODO
    }

    @Composable
    override operator fun invoke() = Box(modifier = Modifier.fillMaxSize()) {
        if (cells.isEmpty()) {
            cells += TextCell()
        }
        Column {
            SaveButton()
            val state = rememberLazyListState()
            CellList(cells, state)
        }
    }

    @Composable
    private fun ColumnScope.SaveButton() {
        Button(
            modifier = Modifier
                .align(Alignment.End)
                .wrapContentSize()
                .padding(5.dp)
                .height(25.dp),
            onClick = {
                val path = callJFileChooser(
                    title = "Select File Path",
                    mode = JFileChooserMode.Save,
                    filter = FileNameExtensionFilter(
                        "Diary Files", "diary"
                    )
                )?.let { path ->
                    save(path)
                    this@NotesTab.path = path
                }
            },
        ) {
            Text("Save", fontSize = 8.sp)
        }
    }

    @Composable
    private fun CellList(cells: List<Cell>, state: LazyListState) {
        Box {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(end = 12.dp),
                state = state
            ) {
                itemsIndexed(cells) { i, cell ->
                    CellBox(i, cell, state)
                }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(
                    scrollState = state
                )
            )
        }
    }

    @Composable
    private fun CellBox(iCell: Int, cell: Cell, state: LazyListState) {
        CellContentAlignment {
            Row {
                CellNumber(iCell)
                Column {
                    CellAbove(iCell, cell)
                    cell()
                    CellBelowButtons(iCell, cell, state)
                }
            }
        }
    }

    @Composable
    private fun CellContentAlignment(block: @Composable () -> Unit) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(0.75F)
            ) {
                block()
            }
        }
    }

    @Composable
    private fun CellAbove(iCell: Int, cell: Cell) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(bottom = 5.dp, top = 10.dp).fillMaxWidth()
        ) {
            Row(modifier = Modifier.align(Alignment.Bottom)) {
                val text = remember { mutableStateOf(cell.name) }
                CellNameField(text)
            }
            Row {
                CellButtonAbove("⇧") {
                    if (iCell > 0) {
                        cells.removeAt(iCell)
                        cells.add(iCell - 1, cell)
                    }
                }
                CellButtonAbove("⇩") {
                    if (iCell + 1 < cells.size) {
                        cells.removeAt(iCell)
                        cells.add(iCell + 1, cell)
                    }
                }
                CloseCellButton(iCell)
            }
        }
    }

    @Composable
    private fun RowScope.CellNumber(iCell: Int) {
        Text(
            text = (iCell + 1).toString() + ". ",
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }

    @Composable
    private fun CellNameField(name: MutableState<CellName>) {
        Row {
            Text(modifier = Modifier.align(alignment = Alignment.Bottom), text = "Name: ", fontSize = 12.sp)
            BasicTextField(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .align(Alignment.Bottom)
                    .width(100.dp)
                    .wrapContentSize(),
                value = name.value.name,
                textStyle = TextStyle(fontSize = 15.sp),
                singleLine = true,
                onValueChange = {
                    name.value = CellName(it)
                }
            )
        }
    }

    @Composable
    private fun CloseCellButton(iCell: Int) {
        OutlinedButton(
            modifier = Modifier
                .wrapContentSize()
                .padding(5.dp)
                .width(35.dp)
                .height(25.dp),
            onClick = { cells.removeAt(iCell) },
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("x", fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }

    @Composable
    private fun CellBelowButtons(iCell: Int, cell: Cell, state: LazyListState) = Row {
        CellButton("Add text") {
            cells.add(iCell + 1, TextCell())
        }
        CellButton("Add sketch") {
            cells.add(iCell + 1, SketchCell())
        }
        CellButton("Add image") {
            val path = callJFileChooser(
                "Select Image",
                filter = FileNameExtensionFilter(
                    "Image Files", "png", "jpeg", "jpg"
                )
            ) ?: return@CellButton
            cells.add(iCell + 1, SketchCell(backgroundImage = path))
        }
        when (cell) {
            is TextCell -> CellButton("Render") {
                cells.removeAt(iCell)
                cells.add(
                    iCell,
                    RenderedTextCell(
                        textCell = cell
                    )
                )
            }
            is RenderedTextCell -> CellButton("Edit") {
                cells.removeAt(iCell)
                cells.add(iCell, cell.textCell)
            }
        }
    }

    @Composable
    private fun CellButtonAbove(text: String, onClick: () -> Unit) {
        OutlinedButton(
            modifier = Modifier
                .wrapContentSize()
                .padding(5.dp)
                .width(35.dp)
                .height(25.dp),
            onClick = onClick,
            contentPadding = PaddingValues(0.dp),
            shape = MaterialTheme.shapes.small,
        ) {
            Text(text, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colors.primary)
        }
    }

    @Composable
    private fun CellButton(text: String, onClick: () -> Unit) {
        Button(
            modifier = Modifier
                .wrapContentSize()
                .padding(5.dp)
                .height(25.dp),
            onClick = onClick,
            shape = MaterialTheme.shapes.small,
        ) {
            Text(text, fontSize = 8.sp, color = Color.White)
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
                    add(TextCell(text = mutableStateOf(file.readText())))
                }
            }.toMutableStateList(),
            tabManager = tabManager
        )
    }
}
