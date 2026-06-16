package com.example.ardevre

// Devre elemanlarını temsil eden veri sınıfı
data class Eleman(
    val ad: String,           // Elemanın adı
    val gorev: String,        // Elemanın teknik görevi
    val modelDosyaYolu: String // Assets klasöründeki .glb dosyasının adı
)
