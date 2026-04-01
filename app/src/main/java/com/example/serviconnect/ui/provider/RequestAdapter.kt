package com.example.serviconnect.ui.provider


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.serviconnect.data.model.ServiceRequest
import com.example.serviconnect.databinding.ItemServiceRequestBinding

class RequestAdapter(
    private val requests: List<ServiceRequest>,
    private val onActionClick: (requestId: String, status: String) -> Unit
) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    class RequestViewHolder(val binding: ItemServiceRequestBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val binding = ItemServiceRequestBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = requests[position]
        with(holder.binding) {
            tvRequestSeekerName.text = "From: ${request.seekerName}"
            tvRequestDescription.text = request.description
            tvStatusLabel.text = "Status: ${request.status}"

            // Hide buttons if already responded
            llActions.visibility = if (request.status == "PENDING") View.VISIBLE else View.GONE

            btnAccept.setOnClickListener { onActionClick(request.id, "ACCEPTED") }
            btnReject.setOnClickListener { onActionClick(request.id, "REJECTED") }
        }
    }

    override fun getItemCount() = requests.size
}