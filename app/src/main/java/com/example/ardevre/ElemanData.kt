package com.example.ardevre

object ElemanData {
    // Uygulama içinde kullanılacak devre elemanlarının listesi
    val liste = listOf(
        Eleman("Pil", "Devreye elektrik enerjisi sağlar. (Birim: Volt)", "pil.glb"),
        Eleman("Direnç", "Akımı sınırlar ve parçaları korur. (Birim: Ohm)", "direnc.glb"),
        Eleman("LED", "Elektrik enerjisini ışığa çevirir, tek yönde akım geçirir.", "led.glb"),
        Eleman("Kondansatör", "Enerjiyi kısa süreliğine depolar ve filtreleme yapar. (Birim: Farad)", "kondansator.glb"),
        Eleman("Transistör", "Küçük sinyallerle büyük akımları kontroll eder, anahtarlama yapar.", "transistor.glb"),
        Eleman("Bobin", "Manyetik alanda enerji depolar, ani akım değişimlerine direnir. (Birim: Henry)", "bobin.glb"),
        Eleman("Diyot", "Akımın sadece tek bir yönde akmasına izin verir.", "diyot.glb"),
        Eleman("Buton", "Akımın yolunu fiziksel olarak açıp kapatır.", "buton.glb")
    )
}
