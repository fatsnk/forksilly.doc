package expo.modules.fileprocess

import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

fun convertJpgToPng(inputPath: String, outputPath: String) {
    val cleanInputPath = if (inputPath.startsWith("file://")) inputPath.substring(7) else inputPath
    val cleanOutputPath = if (outputPath.startsWith("file://")) outputPath.substring(7) else outputPath

    val bitmap = BitmapFactory.decodeFile(cleanInputPath)
    if (bitmap == null) {
        throw Exception("Failed to decode input file: $cleanInputPath")
    }
    
    val outputStream = FileOutputStream(File(cleanOutputPath))
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.close()
}