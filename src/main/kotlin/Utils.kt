import androidx.compose.runtime.Composable

interface UIElem {
    @Composable
    operator fun invoke()
}

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
