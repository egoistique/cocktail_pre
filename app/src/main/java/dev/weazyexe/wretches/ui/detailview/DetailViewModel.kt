package dev.weazyexe.wretches.ui.detailview

import android.app.Application
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import dev.weazyexe.wretches.R
import dev.weazyexe.wretches.app.App
import dev.weazyexe.wretches.entity.Crime
import dev.weazyexe.wretches.ui.common.BaseViewModel
import dev.weazyexe.wretches.ui.newcrime.NewCrimeActivity

class DetailViewModel (
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<DetailState, DetailEffect>(application) {
    override val initialState: DetailState = DetailState()
    private val crimesStorage = (application as App).cocktailsStorage

    init {
        savedStateHandle.get<Crime>(NewCrimeActivity.EXTRA_CRIME)?.let { setCrime(it) }
    }

    fun updateTitle(title: String) {
        setState { copy(title = title) }
    }

    fun updateDescription(description: String) {
        setState { copy(description = description) }
    }

    fun addPhoto(photo: Uri) {
        setState {
            copy(photos = photos + photo)
        }
    }

    fun removePhoto(photo: Uri) {
        setState {
            copy(photos = photos - photo)
        }
    }

    private fun setCrime(crime: Crime) {
        setState {
            copy(
                toolbarTitleRes = R.string.new_crime_edit_text,
                id = crime.id,
                title = crime.title,
                description = crime.description,
                photos = crime.photos
            )
        }
    }

}