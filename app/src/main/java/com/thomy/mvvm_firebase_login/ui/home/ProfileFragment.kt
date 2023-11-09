package com.thomy.mvvm_firebase_login.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.thomy.mvvm_firebase_login.R
import com.thomy.mvvm_firebase_login.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    val TAG: String = "ProfileFragment"
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.userProfileLiveData.observe(viewLifecycleOwner) { userProfile ->
            if (userProfile != null) {
                binding.userTextView.text = " ${userProfile.email}"
            }
        }


        profileViewModel.loadUserProfile()




        binding.btnCloseSession.setOnClickListener {
            profileViewModel.logout {
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }
        }


    }

}

