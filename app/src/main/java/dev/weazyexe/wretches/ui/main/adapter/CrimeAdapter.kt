package dev.weazyexe.wretches.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.weazyexe.wretches.R
import dev.weazyexe.wretches.databinding.ItemCocktailBinding
import dev.weazyexe.wretches.entity.Crime

/**
 * Адаптер для списка преступлений
 */
class CrimeAdapter(private val onClick: (Crime) -> Unit) :
    ListAdapter<Crime, CrimeAdapter.Holder>(DiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemCocktailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class Holder(
        private val binding: ItemCocktailBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(crime: Crime) = with(binding) {
            if (crime.photos.isEmpty()) {
               photoIv.setImageResource(R.drawable.pink_lemonade)
            } else{
                val firstPhoto = crime.photos.firstOrNull()
                firstPhoto?.let {
                    photoIv.setImageURI(it)
                } ?: photoIv.setImageResource(R.drawable.pink_lemonade)
            }
            titleTv.text = crime.title
            root.setOnClickListener {
                onClick(crime)
            }
        }
    }

    private class DiffUtils : DiffUtil.ItemCallback<Crime>() {

        override fun areItemsTheSame(oldItem: Crime, newItem: Crime): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Crime, newItem: Crime): Boolean =
            oldItem == newItem
    }
}