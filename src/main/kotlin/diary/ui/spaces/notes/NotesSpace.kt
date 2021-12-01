package diary.ui.spaces.notes

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import diary.ui.UIComponent
import diary.ui.WorkSpace
import diary.utils.callFileExplorer
import diary.utils.removeIfExists
import java.awt.FileDialog
import java.nio.file.Files.createDirectory
import java.nio.file.Path
import kotlin.io.path.createFile

class NotesSpace(
    private val cells: SnapshotStateList<Cell> = mutableStateListOf(),
    private val workSpace: WorkSpace
) : UIComponent {

    @Composable
    override operator fun invoke() {
        Box(modifier = Modifier.fillMaxSize().padding(10.dp)) {
            val state = rememberLazyListState()
            if (cells.isEmpty()) {
                cells += TextCell()
            }
            Column {
                Button(
                    onClick = {
                        val path = callFileExplorer(
                            title = "Select File Path",
                            mode = FileDialog.SAVE
                        )?.let { path ->
                            save(path)
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
    private fun CellBox(iCell: Int, cell: Cell, block: @Composable () -> Unit) {
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
                            cells.add(iCell, RenderedTextCell(cell.text))
                        }
                        is RenderedTextCell -> CellButton("Edit") {
                            cells.removeAt(iCell)
                            cells.add(iCell, TextCell(cell.text))
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
        fun from(path: Path, workSpace: WorkSpace) = NotesSpace(
            buildList {
                path.toFile().walk().filter { it.isFile }.sortedBy { it.name }.forEach { file ->
                    // TODO determine cell type
                    add(TextCell(file.readText()))
                }
            }.toMutableStateList(),
            workSpace = workSpace
        )
    }
}
