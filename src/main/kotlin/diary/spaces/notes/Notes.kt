package diary.spaces.notes

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import diary.ui.UIElem
import diary.utils.callFileExplorer
import java.awt.FileDialog
import java.nio.file.Path

class Notes(private val cells: SnapshotStateList<Cell>) : UIElem {

    constructor(vararg cells: Cell) : this(cells.toMutableList())
    constructor(cells: Iterable<Cell>) : this(cells.toMutableList())
    constructor(cells: MutableList<Cell>) : this(cells.toMutableStateList())

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
                        CellBox(i) { cell() }
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
    private fun CellBox(iCell: Int, block: @Composable () -> Unit) {
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
        // TODO add .diary
        println("Save notes $path")
//        val dir = createDirectory(path)
//        cells.forEachIndexed { i, cell ->
//            val file = Path.of(dir.toString(), "$i.cell").createFile()
//            cell.save(file)
//        }
        // TODO zip
    }

    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        fun from(path: Path) = Notes(buildList {
            println("read notes from $path")
//            path.toFile().walk().filter { it.isFile }.sortedBy { it.name }.forEach { file ->
//                println("file = $file")
//                add(TextCell(file.readText()))
//            }
        })
    }
}
