package com.example.jonathansnidervirginmoney.ui.roomlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jonathansnidervirginmoney.R
import com.example.jonathansnidervirginmoney.data.model.Rooms
import com.example.jonathansnidervirginmoney.databinding.RecyclerViewRowMeetingRoomBinding

class RoomsAdapter(
    private val rooms: Rooms
) : RecyclerView.Adapter<RoomsAdapter.RoomsViewHolder>() {

    class RoomsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding=RecyclerViewRowMeetingRoomBinding.bind(view)

        fun setupUI(roomID: String, roomStatusIsOccupied: Boolean, roomOccupancyMax: String){
            binding.roomIdNumber.text=roomID
            if(roomStatusIsOccupied) {
                binding.roomBoolean.text = "YES"
                //binding.roomBoolean.setTextColor(ContextCompat.getColor(this, R.color.virgin_color))
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
        return rooms.size
    }

    override fun onBindViewHolder(holder: RoomsViewHolder, position: Int) {
        holder.setupUI(
            rooms.get(position).id!!,
            rooms.get(position).isOccupied!!,
            rooms.get(position).maxOccupancy!!.toString()
            )
    }
}