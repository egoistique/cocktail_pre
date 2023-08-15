package dev.weazyexe.wretches.ui.detailview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.weazyexe.wretches.R
import dev.weazyexe.wretches.databinding.ActivityDetailViewBinding
import dev.weazyexe.wretches.databinding.ItemCocktailBinding
import dev.weazyexe.wretches.entity.Crime

class EditAdapter(private val onClick: (Crime) -> Unit) :
    ListAdapter<Crime, EditAdapter.Holder>(DiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ActivityDetailViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class Holder(
        private val binding: ActivityDetailViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(crime: Crime) = with(binding) {
            if (crime.photos.isEmpty()) {
                photoIv.setImageResource(R.drawable.pink_lemonade)
            } else{

            }
            editBt.setOnClickListener { onClick(crime) }
        }
    }

    private class DiffUtils : DiffUtil.ItemCallback<Crime>() {

        override fun areItemsTheSame(oldItem: Crime, newItem: Crime): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Crime, newItem: Crime): Boolean =
            oldItem == newItem
    }

}