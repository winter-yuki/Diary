package diary.utils

import java.awt.FileDialog
import java.awt.Frame
import java.nio.file.Path
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists
import kotlin.io.path.extension
import kotlin.io.path.isDirectory

typealias FileDialogMode = Int

fun callFileExplorer(title: String, mode: FileDialogMode = FileDialog.LOAD): Path? =
    FileDialog(Frame(), title).apply {
        this.mode = mode
        isVisible = true
    }.run {
        if (directory == null || file == null) null
        else Path.of(directory, file)
    }

enum class JFileChooserMode {
    Open, Save
}

fun callJFileChooser(title: String, mode: JFileChooserMode = JFileChooserMode.Open): Path? {
    val chooser = JFileChooser().apply {
        dialogTitle = title
        fileFilter = when (mode) {
            JFileChooserMode.Open -> FileNameExtensionFilter(
                "Diary & PDF Files", "diary", "pdf"
            )
            JFileChooserMode.Save -> FileNameExtensionFilter(
                "Diary Files", "diary"
            )
        }
        fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
    }
    return chooser.run {
        val rc = when (mode) {
            JFileChooserMode.Open -> showOpenDialog(null)
            JFileChooserMode.Save -> showSaveDialog(null)
        }
        if (rc != JFileChooser.APPROVE_OPTION) null
        else selectedFile.toPath()
    }
}

fun callJImageChooser(title: String, mode: JFileChooserMode = JFileChooserMode.Open): Path? {
    val chooser = JFileChooser().apply {
        dialogTitle = title

        if (mode == JFileChooserMode.Open) {
            fileFilter = FileNameExtensionFilter("png", "jpeg", "jpg")
        }

//        fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
    }
    return chooser.run {
        val rc = when (mode) {
            JFileChooserMode.Open -> showOpenDialog(null)
            JFileChooserMode.Save -> showSaveDialog(null)
        }
        if (rc != JFileChooser.APPROVE_OPTION) null
        else selectedFile.toPath()
    }
}

enum class FileType {
    Unknown, Diary, Pdf;

    companion object {
        fun of(path: Path): FileType = when (path.extension) {
            "diary" -> Diary
            "pdf" -> Pdf
            else -> Unknown
        }
    }
}

fun Path.removeIfExists() {
    if (exists()) {
        if (isDirectory()) {
            removeAll { true }
        }
        deleteExisting()
    }
}
