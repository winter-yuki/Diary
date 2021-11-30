import androidx.compose.runtime.Composable

interface UIElem {
    @Composable
    operator fun invoke()
}

@Composable
fun <T> Iterable<T>.forEachCo(block: @Composable (T) -> Unit) {
    for (t in this) {
        block(t)
    }
}
