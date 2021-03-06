package com.takusemba.jethub.core

import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.takusemba.jethub.core.databinding.ItemRepositoryBinding
import com.takusemba.jethub.model.Repository
import com.xwray.groupie.viewbinding.BindableItem

data class RepositoryItem(
  val repository: Repository,
  val userViewModel: UserViewModel
) : BindableItem<ItemRepositoryBinding>(
  repository.hashCode().toLong()
) {

  override fun initializeViewBinding(view: View): ItemRepositoryBinding {
    return ItemRepositoryBinding.bind(view)
  }

  override fun bind(binding: ItemRepositoryBinding, position: Int) {
    binding.title.text = repository.name
    binding.description.text = repository.description
    binding.description.visibility = if (repository.description.isBlank()) {
      View.GONE
    } else {
      View.VISIBLE
    }
    val language = Language.of(repository.language)
    binding.languageName.text = language.title
    binding.languageIcon.setImageResource(language.icon)
    binding.starCount.text = repository.starsCount.toString()
    binding.forkCount.text = repository.forksCount.toString()

    binding.root.setOnLongClickListener { v ->
      if (userViewModel.isPinned(repository)) {
        userViewModel.unpin(repository)
        Toast.makeText(v.context, R.string.unpinned_repository, LENGTH_SHORT).show()
      } else {
        userViewModel.pin(repository)
        Toast.makeText(v.context, R.string.pinned_repository, LENGTH_SHORT).show()
      }
      false
    }
  }

  override fun getLayout() = R.layout.item_repository
}
