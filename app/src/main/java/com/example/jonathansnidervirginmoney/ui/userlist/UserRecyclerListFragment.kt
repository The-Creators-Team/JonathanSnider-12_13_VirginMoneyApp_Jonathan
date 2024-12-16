package com.example.jonathansnidervirginmoney.ui.userlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jonathansnidervirginmoney.R
import com.example.jonathansnidervirginmoney.data.model.Users
import com.example.jonathansnidervirginmoney.data.responsestate.UserResponseState
import com.example.jonathansnidervirginmoney.databinding.FragmentUserRecyclerListBinding
import com.example.jonathansnidervirginmoney.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserRecyclerListFragment : Fragment() {

    private var _binding: FragmentUserRecyclerListBinding? = null
    private lateinit var userViewModelInstance: UserViewModel

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
        _binding = FragmentUserRecyclerListBinding.inflate(inflater, container, false)

        userViewModelInstance = ViewModelProvider(this).get(UserViewModel::class.java)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            userViewModelInstance.users.observe(viewLifecycleOwner) { value ->
                //THE RESULT MUST BE SENT TO A SEPARATE FUNCTION TO ENSURE THE RESULT FROM THE
                //ASYNC FUNCTION CALL IS ACTUALLY RECEIVED BEFORE SUPPLYING IT TO THE RECYCLE VIEW
                when (value) {
                    is UserResponseState.Loading -> updateUserListLoading()
                    is UserResponseState.Success -> updateUserListSuccess(value.result)
                    is UserResponseState.Fail -> updateUserListFail(value.failureString)


                }
            }
            userViewModelInstance.getUsers()
        }

    }

    private fun updateUserListLoading() {
        binding.apply {
            loadingImage.visibility= View.GONE
            downloadText.visibility=View.GONE
        }
    }

    private fun updateUserListSuccess(result: Users) {
        binding.apply {
            loadingImage.visibility= View.GONE
            downloadText.visibility=View.GONE
        }
        binding.recyclerViewUsers.apply {
            //vertical by default
            layoutManager = LinearLayoutManager(context)
            adapter = UserAdapter(result)
        }
    }

    private fun updateUserListFail(error: String){
        binding.apply {
            loadingImage.setImageResource(R.drawable.ic_cancel)
            downloadText.text=(error)
        }
    }




}