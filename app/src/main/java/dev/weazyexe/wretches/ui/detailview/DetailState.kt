package dev.weazyexe.wretches.ui.detailview

import android.net.Uri
import androidx.annotation.StringRes
import dev.weazyexe.wretches.R
import dev.weazyexe.wretches.ui.newcrime.NewCrimeActivity


/**
 * Состояние экрана [NewCrimeActivity]
 */
data class DetailState(
    @StringRes val toolbarTitleRes: Int = R.string.new_crime_title_text,
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    val isSolved: Boolean = false,
    val photos: List<Uri> = emptyList()
)

/**
 * Сайд эффекты экрана [NewCrimeActivity]
 */
sealed class DetailEffect {
    object GoBack : DetailEffect()
    data class SetTitleError(@StringRes val resId: Int) : DetailEffect()
    data class SetDescriptionError(@StringRes val resId: Int) : DetailEffect()
}