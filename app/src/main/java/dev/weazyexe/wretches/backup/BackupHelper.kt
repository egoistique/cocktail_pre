package dev.weazyexe.wretches.backup

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import androidx.core.net.toUri
import dev.weazyexe.wretches.entity.Crime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

/**
 * Сущность-помощник для бэкапов данных приложения
 * @param context контекст приложения
 */
class BackupHelper(context: Context) {

    private val contentResolver = context.contentResolver
    private val filesDir = context.filesDir

    /**
     * Сохранить преступления [crimes] в файл [uri]
     * @return успешно ли данные забэкапились
     */
    suspend fun backup(uri: Uri, crimes: List<Crime>): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val json = crimesAsJson(crimes)
            contentResolver.openOutputStream(uri)?.use {
                it.write(json.toByteArray(Charsets.UTF_8))
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Восстановить данные из файла [uri]
     * @return восстановленный список преступлений
     */
    suspend fun restore(uri: Uri): List<Crime> = withContext(Dispatchers.IO) {
        return@withContext try {
            contentResolver.openInputStream(uri)?.use {
                val json = it.readBytes().toString(Charsets.UTF_8)
                return@withContext jsonAsCrimes(json)
            }
            emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Конвертация списка преступлений [crimes] в формат JSON
     * @return JSON с преступлениями
     */
    private fun crimesAsJson(crimes: List<Crime>): String {
        val jsonEntities = crimes.asJsonEntities()
        return Json.encodeToString(jsonEntities)
    }

    /**
     * Конвертация JSON-строки [json] в список преступлений
     * @return список с преступлениями [Crime]
     */
    private fun jsonAsCrimes(json: String): List<Crime> {
        val jsonEntities = Json.decodeFromString<List<CocktailJson>>(json)
        return jsonEntities.asAppEntities()
    }

    /**
     * Конвертация сущностей преступлений [Crime] в формат хранения в файле JSON
     *
     * Создан для того, потому что мы не можем хранить локальные URI в для бэкапа,
     * поэтому конвертируем картинки по адресу URI в картинки формата Base64
     */
    private fun List<Crime>.asJsonEntities(): List<CocktailJson> = map {
        CocktailJson(
            id = it.id,
            title = it.title,
            description = it.description,
            isSolved = it.isSolved,
            photos = it.photos.asBase64Strings(),
            //ingredients = it.ingredients
        )
    }

    /**
     * Конвертация картинки по адресу [Uri] в изображение формата Base64
     */
    private fun List<Uri>.asBase64Strings(): List<String> = mapNotNull { uri ->
        val bitmap = uri.toBitmap()
        val baos = bitmap.compress()
        return@mapNotNull Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
    }

    /**
     * Сжатие [Bitmap]
     */
    private fun Bitmap.compress(): ByteArrayOutputStream {
        val baos = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, baos)
        return baos
    }

    /**
     * Получение [Bitmap] по [Uri] картинки
     */
    private fun Uri.toBitmap(): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, this)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, this)
        }
    }

    /**
     * Конвертация JSON сущностей [CocktailJson] в сущности приложения [Crime]
     * @return список с сущностями приложения [Crime]
     */
    private fun List<CocktailJson>.asAppEntities(): List<Crime> = map {
        Crime(
            id = it.id,
            title = it.title,
            description = it.description,
            isSolved = it.isSolved,
            photos = it.photos.saveImagesToInternalStorage(),
            //ingredients = it.ingredients
        )
    }

    /**
     * Сохраняет список Base64-изображений во внутреннее хранилище приложения
     * @return список с сохраненными ссылками на изображения [Uri]
     */
    private fun List<String>.saveImagesToInternalStorage(): List<Uri> = map {
        val file = File(filesDir, UUID.randomUUID().toString())
        file.writeBytes(Base64.decode(it, Base64.DEFAULT))
        file.toUri()
    }
}