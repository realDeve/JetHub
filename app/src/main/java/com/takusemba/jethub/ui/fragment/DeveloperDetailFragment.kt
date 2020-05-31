package com.takusemba.jethub.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.squareup.picasso.Picasso
import com.takusemba.jethub.R
import com.takusemba.jethub.databinding.FragmentDeveloperDetailBinding
import com.takusemba.jethub.ui.item.DeveloperDetailSection
import com.takusemba.jethub.viewmodel.DeveloperDetailViewModel
import com.takusemba.jethub.viewmodel.UserViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeveloperDetailFragment : Fragment(R.layout.fragment_developer_detail) {

  private val developerDetailViewModel: DeveloperDetailViewModel by viewModels()

  private val userViewModel: UserViewModel by activityViewModels()

  private val developerDetailSection: DeveloperDetailSection by lazy {
    DeveloperDetailSection(this, developerDetailViewModel, userViewModel)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val binding = FragmentDeveloperDetailBinding.bind(view)

    val linearLayoutManager = LinearLayoutManager(context)
    val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
      add(developerDetailSection)
    }
    binding.recyclerView.layoutManager = linearLayoutManager
    binding.recyclerView.adapter = groupAdapter

    developerDetailViewModel.developer.observe(viewLifecycleOwner) { developer ->
      Picasso.get().load(developer.avatarUrl).into(binding.icon)
      binding.name.text = developer.login
      binding.description.text = developer.bio
      binding.toolbarTitle.text = developer.login
      binding.repositoriesCount.text =
        requireContext().getString(R.string.followers_count, developer.publicRepositoriesCount)
      binding.gistsCount.text =
        requireContext().getString(R.string.gists_count, developer.publicGistsCount)
      binding.followersCount.text =
        requireContext().getString(R.string.followers_count, developer.followersCount)
      binding.followingsCount.text =
        requireContext().getString(R.string.followings_count, developer.followingCount)
    }

    binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
      if (binding.collapsingToolbar.height + offset < 2 * ViewCompat.getMinimumHeight(
          binding.collapsingToolbar)) {
        binding.toolbarTitle.animate().alpha(1f).setDuration(100).start()
      } else {
        binding.toolbarTitle.animate().alpha(0f).setDuration(100).start()
      }
    })

    binding.backArrow.setOnClickListener {
      view.findNavController().popBackStack()
    }

    if (savedInstanceState == null) {
      // TODO want to inject developerName into developerDetailViewModel's constructor
      val developerName = DeveloperDetailFragmentArgs.fromBundle(requireArguments()).developerName
      developerDetailViewModel.load(developerName)
    }
  }
}