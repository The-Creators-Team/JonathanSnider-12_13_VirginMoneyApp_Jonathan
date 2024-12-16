package com.example.jonathansnidervirginmoney.ui.userlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jonathansnidervirginmoney.R
import com.example.jonathansnidervirginmoney.databinding.RecyclerViewRowUserBinding

class UserAdapter(
    val userList: List<String>
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RecyclerViewRowUserBinding.bind(view)

        fun setupUI(
            firstName: String,
            lastName: String,
            jobTitle: String,
            email: String,
            userImage: String
        ) {
            binding.userFirstName.text = firstName
            binding.userLastName.text = lastName
            binding.userTitle.text = jobTitle
            binding.userEmail.text = email

            Glide.with(binding.root.context)
                .load(userImage)
                .placeholder(R.drawable.ic_launcher_background) //what to display while image is loading
                .error(R.drawable.ic_cancel) //what to display when the image is not successfully retrieved
                .into(binding.recycleViewImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_row_user, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
    }
}