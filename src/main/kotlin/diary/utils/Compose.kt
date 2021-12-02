package diary.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <T> Iterable<T>.forEachIndexedCo(block: @Composable (Int, T) -> Unit) {
    for ((i, t) in this.withIndex()) {
        block(i, t)
    }
}

@Composable
fun <T> Iterable<T>.forEachCo(block: @Composable (T) -> Unit) {
    forEachIndexedCo { _, t ->
        block(t)
    }
}

@Composable
fun <T> forEachCo(vararg ts: T, block: @Composable (T) -> Unit) {
    for (t in ts) {
        block(t)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlertDialog(
    title: String, text: String,
    open: MutableState<Boolean>
) {
    var isOpen by open
    if (!isOpen) return
    AlertDialog(
        onDismissRequest = { isOpen = false },
        title = {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        text = {
            Text(text, fontSize = 13.sp)
        },
        buttons = {
            CancelButton { isOpen = false }
        },
        modifier = Modifier.width(350.dp).wrapContentHeight()
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun makeAlertDialogStateful(
    title: String, text: String
): MutableState<Boolean> {
    val open = remember { mutableStateOf(false) }
    AlertDialog(title = title, text = text, open = open)
    return open
}

@Composable
fun CancelButton(onCancel: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TextButton(
            onClick = onCancel,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Cancel")
        }
    }
}
