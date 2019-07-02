package cn.imrhj.pixellauncherhide.utils

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * Created by rhj on 24/02/2018.
 */
object FileUtils {
    @Throws(IOException::class)
    fun copyFile(`in`: InputStream, out: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        do {
            read = `in`.read(buffer)
            if (read == -1) {
                break
            }
            out.write(buffer, 0, read)
        } while (true)
    }
}