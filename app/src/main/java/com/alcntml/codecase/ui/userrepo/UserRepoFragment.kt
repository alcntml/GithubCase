package com.alcntml.codecase.ui.userrepo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.alcntml.codecase.AppExecutors
import com.alcntml.codecase.testing.OpenForTesting
import com.alcntml.codecase.R
import com.alcntml.codecase.binding.FragmentDataBindingComponent
import com.alcntml.codecase.databinding.UserRepoFragmentBinding
import com.alcntml.codecase.di.Injectable
import com.alcntml.codecase.ui.common.BackCallback
import com.alcntml.codecase.ui.common.FavCallback
import com.alcntml.codecase.ui.common.RetryCallback
import com.alcntml.codecase.util.autoCleared
import com.alcntml.codecase.vo.UserRepo
import javax.inject.Inject

@OpenForTesting
final class UserRepoFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var userRepoViewModel: UserRepoViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var repo : UserRepo

    var binding by autoCleared<UserRepoFragmentBinding>()
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<UserRepoFragmentBinding>(
                inflater,
                R.layout.user_repo_fragment,
                container,
                false,
                dataBindingComponent
        )
        dataBinding.retryCallback = object : RetryCallback {
            override fun retry() {
                userRepoViewModel.retry()
            }
        }
        dataBinding.favCallback = object : FavCallback {
            override fun favorite() {
                binding.imgFav.isSelected = !binding.imgFav.isSelected
                repo.favorite = !repo.favorite
            }
        }
        dataBinding.backCallback = object : BackCallback {
            override fun back() {
                findNavController().popBackStack()
            }
        }
        binding = dataBinding
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(R.transition.move)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userRepoViewModel = ViewModelProvider(this, viewModelFactory)
                .get(UserRepoViewModel::class.java)
        val params = UserRepoFragmentArgs.fromBundle(arguments!!)
        repo = params.repo
        userRepoViewModel.setId(repo.owner.login, repo.name)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.userRepo = userRepoViewModel.repo
        binding.imgFav.isSelected = repo.favorite
        postponeEnterTransition()
        binding.description.viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }
    }
}
