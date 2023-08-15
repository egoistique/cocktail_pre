package dev.weazyexe.wretches.ui.detailview

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import dev.chrisbanes.insetter.applyInsetter
import dev.weazyexe.wretches.R
import dev.weazyexe.wretches.databinding.ActivityDetailViewBinding
import dev.weazyexe.wretches.databinding.ActivityNewCrimeBinding
import dev.weazyexe.wretches.entity.Crime
import dev.weazyexe.wretches.ui.detailview.adapter.EditAdapter
import dev.weazyexe.wretches.ui.main.adapter.CrimeAdapter
import dev.weazyexe.wretches.ui.newcrime.NewCrimeActivity
import dev.weazyexe.wretches.ui.newcrime.NewCrimeEffect.*
import dev.weazyexe.wretches.ui.newcrime.NewCrimeViewModel
import dev.weazyexe.wretches.ui.newcrime.adapter.PhotoAdapter
import dev.weazyexe.wretches.ui.newcrime.photopicker.PhotoPickerDialog
import dev.weazyexe.wretches.utils.subscribe
import dev.weazyexe.wretches.utils.updateIfNeeds

/**
 * Экран с добавлением/редактированием преступлений
 */
class DetailViewActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailViewBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<DetailViewModel>()

    private lateinit var current: Crime

    private val adapter = PhotoAdapter(
        onCloseClick = { viewModel.removePhoto(it) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        current = intent.getParcelableExtra(EXTRA_CRIME)
            ?: throw IllegalArgumentException("EXTRA_CRIME must not be null")

        initEdgeToEdge()
        initListeners()
        updateUi()
    }

    /**
     * Инициализация edge-to-edge режима
     */
    private fun initEdgeToEdge() = with(binding) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        toolbar.applyInsetter {
            type(statusBars = true) { padding() }
        }
        editBt.applyInsetter {
            type(tappableElement = true, ime = true) { margin() }
        }
    }

    private fun initListeners() = with(binding) {
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        titleTv.doOnTextChanged { text, _, _, _ ->
            titleTil.isErrorEnabled = false
            viewModel.updateTitle(text.toString())
        }
        descriptionTv.doOnTextChanged { text, _, _, _ ->
            descriptionTil.isErrorEnabled = false
            viewModel.updateDescription(text.toString())
        }

        editBt.setOnClickListener {
            openActivity<NewCrimeActivity>(NewCrimeActivity.composeParams(current))
        }
        supportFragmentManager.setFragmentResultListener(
            PhotoPickerDialog.PHOTO_PICKER_RESULT_KEY,
            this@DetailViewActivity
        ) { _, bundle ->
            val result = bundle.getParcelable<Uri?>(PhotoPickerDialog.PHOTO_PICKER_URI)
            if (result != null) {
                viewModel.addPhoto(result)
            }
        }
    }

    /**
     * Подписываемся на обновление состояния из ViewModel
     */
    private fun updateUi() = with(binding) {
        subscribe(
            viewModel,
            onNewState = {
                toolbar.title = getString(it.toolbarTitleRes)
                val firstPhoto = it.photos.firstOrNull()
                firstPhoto?.let {
                    photoIv.setImageURI(it)
                } ?: photoIv.setImageResource(R.drawable.pink_lemonade)
                titleTv.updateIfNeeds(it.title)
                descriptionTv.updateIfNeeds(it.description)
                adapter.submitList(it.photos)
            }
        )
    }

    /**
     * Открыть [Activity] типа [A] с параметрами из [bundle]
     */
    private inline fun <reified A : Activity> openActivity(bundle: Bundle = bundleOf()) {
        Intent(this@DetailViewActivity, A::class.java).apply {
            putExtras(bundle)
            startActivity(this)
        }
    }

    companion object {

        const val EXTRA_CRIME = "EXTRA_CRIME"

        fun composeParams(crime: Crime): Bundle = bundleOf(
            EXTRA_CRIME to crime
        )
    }
}