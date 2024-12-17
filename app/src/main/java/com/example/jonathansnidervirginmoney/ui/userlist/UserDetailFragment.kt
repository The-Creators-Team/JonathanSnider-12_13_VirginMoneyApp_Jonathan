package com.example.jonathansnidervirginmoney.ui.userlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.jonathansnidervirginmoney.R
import com.example.jonathansnidervirginmoney.databinding.FragmentUserDetailBinding
import com.example.jonathansnidervirginmoney.viewmodel.SingleUserViewModel
import com.example.jonathansnidervirginmoney.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailFragment : Fragment() {

    private var _binding: FragmentUserDetailBinding? = null
    private lateinit var singleUserViewModel: SingleUserViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        //arguments is a special variable a fragment has access to.
        //Return the arguments supplied when the fragment was instantiated, if any.
        val personId = arguments?.getString("userId")
        singleUserViewModel = ViewModelProvider(this).get(SingleUserViewModel::class.java)

        singleUserViewModel.user.observe(viewLifecycleOwner) {
            binding.singleUserFirstName.text=it.firstName
            binding.singleUserLastName.text=it.lastName
            binding.singleUserEmail.text=it.email
            binding.singleUserJobTitle.text=it.jobtitle

            Glide.with(binding.singleUserImage.context)
                .load(it.avatar)
                .placeholder(R.drawable.ic_gear)
                .into(binding.singleUserImage)
        }

        if (personId != null) {
            singleUserViewModel.getUser(personId)
        }
        return binding.root

    }
}