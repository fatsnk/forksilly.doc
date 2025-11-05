package expo.modules.fileprocess

import android.app.AlertDialog
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import java.net.URL
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import android.util.Base64
import java.util.zip.CRC32
 
 class FileprocessModule : Module() {
   // Each module class must implement the definition function. The definition consists of components
  // that describes the module's functionality and behavior.
  // See https://docs.expo.dev/modules/module-api for more details about available components.
  override fun definition() = ModuleDefinition {
    // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
    // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
    // The module will be accessible from `requireNativeModule('Fileprocess')` in JavaScript.
    Name("Fileprocess")

    // Defines constant property on the module.
    Constant("PI") {
      Math.PI
    }

    // Defines event names that the module can send to JavaScript.
    Events("onChange")

    // Defines a JavaScript synchronous function that runs the native code on the JavaScript thread.
    Function("hello") {
      "Hello world! 👋"
    }

    // Defines a JavaScript function that always returns a Promise and whose native code
    // is by default dispatched on the different thread than the JavaScript runtime runs on.
    AsyncFunction("setValueAsync") { value: String ->
      // Send an event to JavaScript.
      sendEvent("onChange", mapOf(
        "value" to value
      ))
    }

    AsyncFunction("showAlert") { title: String, message: String ->
      val activity = appContext.activityProvider?.currentActivity
      activity?.runOnUiThread {
        AlertDialog.Builder(activity)
          .setTitle(title)
          .setMessage(message)
          .setPositiveButton("OK", null)
          .show()
      }
    }

   // New function to parse character card
   AsyncFunction("parseCharacterCardPngAsync") { filePath: String ->
       val cleanPath = if (filePath.startsWith("file://")) filePath.substring(7) else filePath
       val file = File(cleanPath)
       if (!file.exists() || !file.isFile) {
           throw Exception("File does not exist or is not a regular file: $cleanPath")
       }

       FileInputStream(file).use { fis ->
           // Verify PNG signature
           val signature = ByteArray(8)
           if (fis.read(signature) != 8) {
               throw Exception("Cannot read PNG signature")
           }
           val expectedSignature = byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A)
           if (!signature.contentEquals(expectedSignature)) {
               throw Exception("Invalid PNG signature")
           }

           // Loop through chunks
           while (true) {
               val headerBytes = ByteArray(8)
               if (fis.read(headerBytes) != 8) {
                   break // End of file
               }

               val buffer = ByteBuffer.wrap(headerBytes).order(ByteOrder.BIG_ENDIAN)
               val length = buffer.int
               val type = ByteArray(4)
               buffer.get(type)
               val typeString = String(type, Charsets.US_ASCII)

               if (typeString == "tEXt") {
                   val chunkData = ByteArray(length)
                   if (fis.read(chunkData) != length) {
                       throw Exception("Could not read tEXt chunk data")
                   }

                   val nullIndex = chunkData.indexOf(0.toByte())
                   if (nullIndex != -1) {
                       val keyword = String(chunkData, 0, nullIndex, Charsets.US_ASCII)
                       if (keyword == "chara") {
                           // Found it!
                           val base64Data = String(chunkData, nullIndex + 1, chunkData.size - (nullIndex + 1), Charsets.UTF_8)
                           val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
                           val jsonString = String(decodedBytes, Charsets.UTF_8)
                           
                           // Skip the CRC for this chunk and return
                           fis.skip(4)
                           return@AsyncFunction jsonString
                       }
                   }
                   // If it was a tEXt chunk but not 'chara', we've already read its data.
                   // We just need to skip its CRC and continue to the next chunk.
                   fis.skip(4)
               } else if (typeString == "IEND") {
                   break // End of chunks
               } else {
                   // For any other chunk type, we haven't read its data yet.
                   // Skip both the chunk data and its CRC.
                   val bytesToSkip = length.toLong() + 4
                   if (fis.skip(bytesToSkip) != bytesToSkip) {
                       break // Could not skip, probably EOF
                   }
               }
           }
       }

       // If we reach here, 'chara' chunk was not found
       throw Exception("'chara' tEXt chunk not found in PNG")
   }

   AsyncFunction("saveCharacterCardPngAsync") { sourcePath: String, destinationPath: String, characterJson: String ->
       val cleanSourcePath = if (sourcePath.startsWith("file://")) sourcePath.substring(7) else sourcePath
       val cleanDestinationPath = if (destinationPath.startsWith("file://")) destinationPath.substring(7) else destinationPath

       val sourceFile = File(cleanSourcePath)
       if (!sourceFile.exists()) {
           throw Exception("Source file does not exist: $cleanSourcePath")
       }

       val newChunks = mutableListOf<Pair<String, ByteArray>>()
       var iendChunkData: ByteArray? = null

       FileInputStream(sourceFile).use { fis ->
           // Skip signature
           fis.skip(8)

           while (true) {
               val headerBytes = ByteArray(8)
               val bytesRead = fis.read(headerBytes)
               if (bytesRead < 8) break

               val buffer = ByteBuffer.wrap(headerBytes).order(ByteOrder.BIG_ENDIAN)
               val length = buffer.int
               val typeBytes = ByteArray(4)
               buffer.get(typeBytes)
               val type = String(typeBytes, Charsets.US_ASCII)

               val data = ByteArray(length)
               if (fis.read(data) != length) break

               // Skip CRC
               fis.skip(4)

               if (type == "IEND") {
                   iendChunkData = data
                   continue // Don't add to list yet, we'll add it at the end
               }

               var shouldKeepChunk = true
               if (type == "tEXt") {
                   // Unconditionally remove all tEXt chunks, same as the JS implementation.
                   shouldKeepChunk = false
               } else if (type == "zTXt" || type == "iTXt") {
                   // For zTXt and iTXt, check for the keyword.
                   val nullIndex = data.indexOf(0.toByte())
                   if (nullIndex != -1) {
                       val keyword = String(data, 0, nullIndex, Charsets.US_ASCII)
                       if (keyword == "chara" || keyword == "chara_compressed_json") {
                           shouldKeepChunk = false // Discard old character data.
                       }
                   }
               }

               if (shouldKeepChunk) {
                   newChunks.add(Pair(type, data))
               }
           }
       }

       // Prepare the new chara chunk
       val base64Data = Base64.encodeToString(characterJson.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
       val keywordBytes = "chara".toByteArray(Charsets.US_ASCII)
       val separatorByte = byteArrayOf(0)
       val textBytes = base64Data.toByteArray(Charsets.UTF_8)
       val charaChunkData = keywordBytes + separatorByte + textBytes

       newChunks.add(Pair("tEXt", charaChunkData))

       // Add IEND chunk at the end
       newChunks.add(Pair("IEND", iendChunkData ?: ByteArray(0)))

       // Write the new PNG file
       val destinationFile = File(cleanDestinationPath)
       FileOutputStream(destinationFile).use { fos ->
           // Write signature
           fos.write(byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A))

           val crc32 = CRC32()

           for ((type, data) in newChunks) {
               val typeBytes = type.toByteArray(Charsets.US_ASCII)

               // Write length
               val lengthBytes = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(data.size).array()
               fos.write(lengthBytes)

               // Write type
               fos.write(typeBytes)

               // Write data
               fos.write(data)

               // Calculate and write CRC
               crc32.reset()
               crc32.update(typeBytes)
               crc32.update(data)
               val crcBytes = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(crc32.value.toInt()).array()
               fos.write(crcBytes)
           }
       }
   }

  AsyncFunction("copyFileAsync") { sourcePath: String, destinationPath: String ->
      val cleanSourcePath = if (sourcePath.startsWith("file://")) sourcePath.substring(7) else sourcePath
      val cleanDestinationPath = if (destinationPath.startsWith("file://")) destinationPath.substring(7) else destinationPath

      val sourceFile = File(cleanSourcePath)
      if (!sourceFile.exists()) {
          throw Exception("Source file does not exist: $cleanSourcePath")
      }

      val destinationFile = File(cleanDestinationPath)

      try {
          FileInputStream(sourceFile).use { inStream ->
              FileOutputStream(destinationFile).use { outStream ->
                  inStream.copyTo(outStream)
              }
          }
      } catch (e: Exception) {
          throw Exception("Failed to copy file: ${e.message}")
      }
  }

  AsyncFunction("convertJpgToPngAsync") { inputPath: String, outputPath: String ->
       convertJpgToPng(inputPath, outputPath)
  }

    // Enables the module to be used as a native view. Definition components that are accepted as part of
    // the view definition: Prop, Events.
    View(FileprocessView::class) {
      // Defines a setter for the `url` prop.
      Prop("url") { view: FileprocessView, url: URL ->
        view.webView.loadUrl(url.toString())
      }
      // Defines an event that the view can send to JavaScript.
      Events("onLoad")
    }
  }
}
