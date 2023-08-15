package dev.weazyexe.wretches.ui.main
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dev.chrisbanes.insetter.applyInsetter
import dev.weazyexe.wretches.R
import dev.weazyexe.wretches.databinding.ActivityAllCocktailsBinding
import dev.weazyexe.wretches.ui.detailview.DetailViewActivity
import dev.weazyexe.wretches.ui.main.adapter.CrimeAdapter

import dev.weazyexe.wretches.ui.newcrime.NewCrimeActivity
import dev.weazyexe.wretches.ui.settings.SettingsActivity
import dev.weazyexe.wretches.utils.subscribe

/**
 * Главный экран
 */
class AllCocktailsActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAllCocktailsBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MainViewModel>()

//    private val adapter = CrimeAdapter {
//        openActivity<NewCrimeActivity>(NewCrimeActivity.composeParams(it))
//    }

    private val adapter = CrimeAdapter {
        openActivity<DetailViewActivity>(DetailViewActivity.composeParams(it))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initEdgeToEdge()
        initViews()
        initListeners()
        updateUi()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchCrimes()
    }

    /**
     * Инициализация edge-to-edge режима
     */
    private fun initEdgeToEdge() = with(binding) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        toolbar.applyInsetter {
            type(statusBars = true) { padding() }
        }
        cocktailsRv.applyInsetter {
            type(navigationBars = true) { padding() }
        }
        addCocktailsBt.applyInsetter {
            type(tappableElement = true) { margin() }
        }
    }

    private fun initViews() = with(binding) {
        cocktailsRv.adapter = adapter
        cocktailsRv.layoutManager = GridLayoutManager(this@AllCocktailsActivity, 2)
    }

    private fun initListeners() = with(binding) {
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        addCocktailsBt.setOnClickListener {
            openActivity<NewCrimeActivity>()
        }
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.settings) {
                openActivity<SettingsActivity>()
                true
            } else {
                false
            }
        }
    }

    /**
     * Подписываемся на обновление состояния из ViewModel
     */
    private fun updateUi() = with(binding) {
        subscribe(viewModel, onNewState = {
            val hasCrimes = it.crimes.isNotEmpty()
            cocktailsRv.isVisible = hasCrimes
            emptyLayout.root.isVisible = !hasCrimes
            adapter.submitList(it.crimes)
        })
    }

    /**
     * Открыть [Activity] типа [A] с параметрами из [bundle]
     */
    private inline fun <reified A : Activity> openActivity(bundle: Bundle = bundleOf()) {
        Intent(this@AllCocktailsActivity, A::class.java).apply {
            putExtras(bundle)
            startActivity(this)
        }
    }
}