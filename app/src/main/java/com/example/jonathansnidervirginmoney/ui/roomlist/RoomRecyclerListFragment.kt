package com.example.jonathansnidervirginmoney.ui.roomlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.jonathansnidervirginmoney.R
import com.example.jonathansnidervirginmoney.data.model.Rooms
import com.example.jonathansnidervirginmoney.data.responsestate.RoomsResponseState
import com.example.jonathansnidervirginmoney.databinding.FragmentRoomRecyclerListBinding
import com.example.jonathansnidervirginmoney.viewmodel.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoomRecyclerListFragment : Fragment() {

    private var _binding: FragmentRoomRecyclerListBinding? = null
    private lateinit var roomViewModelInstance: RoomViewModel

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
        _binding = FragmentRoomRecyclerListBinding.inflate(inflater, container, false)

        roomViewModelInstance = ViewModelProvider(this).get(RoomViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {

            /*val roomResult= RetrofitClient.apiInstance.getRooms()
            val userResult=RetrofitClient.apiInstance.getUsers()

            println("THIS IS THE RESULT FOR THE MAX OCCUPANCY OF THE FIRST ROOM: ${roomResult.get(0).maxOccupancy}")
            println("THIS IS THE RESULT FOR THE FIRST NAME OF THE FIRST USER: ${userResult.get(0).firstName}")*/


            roomViewModelInstance.rooms.observe(viewLifecycleOwner) { value ->
                //THE RESULT MUST BE SENT TO A SEPARATE FUNCTION TO ENSURE THE RESULT FROM THE
                //ASYNC FUNCTION CALL IS ACTUALLY RECEIVED BEFORE SUPPLYING IT TO THE RECYCLE VIEW
                when (value) {
                    is RoomsResponseState.Loading -> updateRoomsListLoading()
                    is RoomsResponseState.Success -> updateRoomsListSuccess(value.result)
                    is RoomsResponseState.Fail -> updateRoomsListFail(value.failureString)


                }
            }
            roomViewModelInstance.getRooms()
        }

        binding.recyclerViewRooms.apply {
            layoutManager = GridLayoutManager(context, 2)
            //adapter=RoomsAdapter()
        }

    }

    private fun updateRoomsListLoading() {
        binding.apply {
            loadingImage.setImageResource(R.drawable.ic_gear)
            downloadText.text=("Loading...")
        }
    }

    private fun updateRoomsListSuccess(result: Rooms) {
        binding.apply {
            loadingImage.visibility= View.GONE
            downloadText.visibility=View.GONE
        }
        binding.recyclerViewRooms.apply {
            //vertical by default
            layoutManager = GridLayoutManager(context, 2)
            adapter = RoomsAdapter(result)
        }
    }

    private fun updateRoomsListFail(error: String){
        binding.apply {
            loadingImage.setImageResource(R.drawable.ic_cancel)
            downloadText.text=(error)
        }

    }

}