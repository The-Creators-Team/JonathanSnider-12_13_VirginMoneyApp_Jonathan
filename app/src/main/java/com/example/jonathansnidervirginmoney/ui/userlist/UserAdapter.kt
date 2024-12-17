package com.example.jonathansnidervirginmoney.ui.userlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jonathansnidervirginmoney.R
import com.example.jonathansnidervirginmoney.data.model.Users
import com.example.jonathansnidervirginmoney.databinding.RecyclerViewRowUserBinding

class UserAdapter(
    private val users: Users
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = RecyclerViewRowUserBinding.bind(view)

        fun setupUI(
            firstName: String?,
            lastName: String?,
            jobTitle: String?,
            email: String?,
            userImage: String?,
            id: String?
        ) {
            binding.userFirstName.text = firstName
            binding.userLastName.text = lastName

            Glide.with(binding.root.context)
                .load(userImage)
                .placeholder(R.drawable.ic_launcher_background) //what to display while image is loading
                .error(R.drawable.ic_cancel) //what to display when the image is not successfully retrieved
                .into(binding.recycleViewImage)
            binding.moreInfoButton.setOnClickListener {

                val bundle = bundleOf(
                    "userId" to id
                )
                val navController = Navigation.findNavController(view)
                navController.navigate(
                    R.id.action_navigation_user_recycler_list_to_navigation_user_detail,
                    bundle
                )

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_row_user, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.setupUI(
            users.get(position).firstName,
            users.get(position).lastName,
            users.get(position).jobtitle,
            users.get(position).email,
            users.get(position).avatar,
            users.get(position).id
        )
    }
}