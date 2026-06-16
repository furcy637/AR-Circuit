package com.example.ardevre

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ardevre.databinding.ItemElemanBinding

class ElemanAdapter(
    private val elemanlar: List<Eleman>,
    private val onElemanSecildi: (Eleman) -> Unit
) : RecyclerView.Adapter<ElemanAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemElemanBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemElemanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eleman = elemanlar[position]
        holder.binding.tvElemanAdi.text = eleman.ad
        
        // Elemana tıklandığında seçilen elemanı geri döndürür
        holder.itemView.setOnClickListener {
            onElemanSecildi(eleman)
        }
    }

    override fun getItemCount() = elemanlar.size
}
