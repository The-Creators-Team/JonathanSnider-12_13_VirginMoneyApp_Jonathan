package com.example.jonathansnidervirginmoney.ui.userlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jonathansnidervirginmoney.databinding.FragmentUserRecyclerListBinding


class UserRecyclerListFragment : Fragment() {

    private var _binding: FragmentUserRecyclerListBinding? = null

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
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.recyclerViewUsers.apply {
            layoutManager= LinearLayoutManager(context)
            //adapter=UserAdapter()

        }

    }


}