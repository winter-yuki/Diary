import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.nio.file.Path


class Notes(private val cells: SnapshotStateList<Cell>) {

    constructor(vararg cells: Cell) : this(cells.toMutableList())
    constructor(cells: Iterable<Cell>) : this(cells.toMutableList())
    constructor(cells: MutableList<Cell>) : this(cells.toMutableStateList())

    @Composable
    operator fun invoke() {

        Box(modifier = Modifier.fillMaxSize().padding(10.dp))
        {
            val state = rememberLazyListState()

            Column() {
                LazyColumn(Modifier.fillMaxSize().padding(end = 12.dp), state) {
                    itemsIndexed(cells) { i, cell -> CellBox(i) { cell() } }
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
            Column(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(0.75F)) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(1.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Button(
                        modifier = Modifier
                            .size(width = 40.dp, height = 40.dp)
                            .align(Alignment.End)
                            .padding(1.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Red,
                            contentColor = Color.Black),
                        onClick = { cells.removeAt(iCell) }
                    ) {
                        Text("X", fontSize = 10.sp)
                    }
                }
                block()
                Row(
                ) {
                    Button(
                        modifier = Modifier
                            .wrapContentHeight()
                            .wrapContentWidth()
                            .fillMaxWidth(0.5f)
                            .height(30.dp)
                            .padding(1.dp),
                        onClick = { cells += TextCell() },
                    ) {
                        Text("Add text", fontSize = 10.sp)
                    }
                    Button(
                        modifier = Modifier
                            .wrapContentHeight()
                            .wrapContentWidth()
                            .fillMaxWidth()
                            .height(30.dp)
                            .padding(1.dp),
                        onClick = { cells += SketchCell() },
                    ) {
                        Text("Add scratch", fontSize = 10.sp)
                    }
                }
            }
        }
    }

    fun save(path: Path) {
        TODO() // #12
    }

    companion object {
        fun from(path: Path): Notes {
            TODO()
        }
    }
//
//    @Composable
//    private fun AddCellBox(block: @Composable () -> Unit) {
//
////        Column(
////            horizontalAlignment = Alignment.CenterHorizontally,
////            modifier = Modifier.fillMaxWidth().fillMaxHeight()
////        ) {
////            Column(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(0.75F)) {
//////                Row() {
////                    Button(
////                        modifier = Modifier
////                            .wrapContentHeight()
////                            .wrapContentWidth()
////                            .height(50.dp)
////                            .padding(10.dp),
////                        onClick = { cells += TextCell() },
////                    ) {
////                        Text("Add text")
////                    }
////                    Button(
////                        modifier = Modifier
////                            .wrapContentHeight()
////                            .wrapContentWidth()
////                            .height(50.dp)
////                            .padding(10.dp),
////                        onClick = { cells += SketchCell() },
////                    ) {
////                        Text("Add scratch")
////                    }
//////                }
////            }
////        }
//
//    }
}