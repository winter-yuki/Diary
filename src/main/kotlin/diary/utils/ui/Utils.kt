package diary.utils.ui

import androidx.compose.runtime.Composable

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
