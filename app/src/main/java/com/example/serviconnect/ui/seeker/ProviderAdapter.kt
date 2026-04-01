package com.example.serviconnect.ui.seeker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.serviconnect.data.model.User
import com.example.serviconnect.databinding.ItemProviderBinding

class ProviderAdapter(
    private val providers: List<User>,
    private val onProviderClick: (User) -> Unit
) : RecyclerView.Adapter<ProviderAdapter.ProviderViewHolder>() {

    class ProviderViewHolder(val binding: ItemProviderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProviderViewHolder {
        val binding = ItemProviderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProviderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProviderViewHolder, position: Int) {
        val provider = providers[position]

        holder.binding.tvProviderName.text = provider.fullName
        holder.binding.tvExperience.text = "${provider.experience ?: "0"} Years Experience"

        // Handle the "View Details" or Card Click
        holder.binding.btnViewProfile.setOnClickListener { onProviderClick(provider) }
        holder.itemView.setOnClickListener { onProviderClick(provider) }
    }

    override fun getItemCount() = providers.size
}