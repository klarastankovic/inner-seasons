package hr.ferit.klarastankovic.innerseasons.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.core.os.HeapProfileRequestBuilder
import hr.ferit.klarastankovic.innerseasons.data.model.CycleLog
import java.io.File
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.String

object CSVExporter {
    @RequiresApi(Build.VERSION_CODES.O)
    fun exportToCSV(context: Context, logs: List<CycleLog>): Boolean {
        return try {
            val timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
            val fileName = "inner_seasons_export_$timestamp.csv"

            val downloadDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            )

            val file = File(downloadDir, fileName)

            FileWriter(file).use { writer ->
                writer.append("Date,Period,Mood,Sleep Hours,Pain Level,Water Intake (ml),Season\n")

                logs.forEach { log ->
                    writer.append(log.date).append(",")
                    writer.append(if (log.isPeriod) "Yes" else "No").append(",")
                    writer.append(log.mood.toString()).append(",")
                    writer.append(String.format("%.1f h", log.sleepHours)).append(",")
                    writer.append(log.painLevel.toString()).append(",")
                    writer.append(log.waterIntakeMl.toString()).append(",")
                    writer.append(log.season).append("\n")
                }

                writer.flush()
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getExportPath(fileName: String): String {
        val downloadDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )
        return "${downloadDir.absolutePath}/$fileName"
    }

    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
}