package com.example.jonathansnidervirginmoney.ui.roomlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.jonathansnidervirginmoney.R
import com.example.jonathansnidervirginmoney.databinding.RecyclerViewRowMeetingRoomBinding

class RoomsAdapter(
    val roomList: List<String>
) : RecyclerView.Adapter<RoomsAdapter.RoomsViewHolder>() {

    class RoomsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding=RecyclerViewRowMeetingRoomBinding.bind(view)

        fun setupUI(roomStatusIsOccupied: Boolean, roomOccupancyMax: String){
            if(roomStatusIsOccupied) {
                binding.roomBoolean.text = "YES"
                //binding.roomBoolean.setTextColor(ContextCompat.getColor(, R.color.virgin_color))
            }
            else{
                binding.roomBoolean.text= "NO"
            }
            binding.roomOccupancyMaxNumber.text=roomOccupancyMax
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomsViewHolder {
        return RoomsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_row_meeting_room, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    override fun onBindViewHolder(holder: RoomsViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}