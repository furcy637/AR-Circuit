package com.example.ardevre

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.ardevre.databinding.ActivityMainBinding
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.light.intensity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var secilenEleman: Eleman? = null
    private var modelNode: ArModelNode? = null
    private val CAMERA_PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Kamera iznini kontrol et
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }

        setupRecyclerView()
        setupSceneView()
    }

    // RecyclerView (Alt Menü) Ayarları: Eleman listesini bağlar
    private fun setupRecyclerView() {
        val adapter = ElemanAdapter(ElemanData.liste) { eleman ->
            // Menüden bir eleman seçildiğinde çalışır
            secilenEleman = eleman
            Toast.makeText(this, "${eleman.ad} seçildi. Zemine dokunarak yerleştirin.", Toast.LENGTH_SHORT).show()
            
            // Yeni model hazırlığı (Seçilen elemanın .glb modelini yükle)
            hazirlaYeniModel(eleman.modelDosyaYolu)
        }
        binding.rvElemanlar.adapter = adapter
    }

    // SceneView (AR Görünümü) Ayarları: Zemin algılama ve tıklama
    private fun setupSceneView() {
        // Sceneview lifecycle yönetimi için activity lifecycle'ına observer olarak eklenir.
        this.lifecycle.addObserver(binding.sceneView)

        binding.sceneView.apply {
            // Zemin algılandığında noktaları (dotları) ve ızgarayı gösterir
            planeRenderer.isVisible = true

            // MODELLERİN KARANLIK OLMASINI ÖNLEMEK İÇİN IŞIK ŞİDDETİNİ ARTIRIYORUZ:
            // Ana ışık kaynağının şiddeti (Sceneview varsayılanı bazen düşük kalabilir)
            mainLight?.intensity = 100000f
            // Çevresel ışık şiddeti (Gölgelerin çok siyah olmasını engeller ve modeli her yönden aydınlatır)
            indirectLight?.intensity = 50000f

            // Zemin algılandığında veya tıklandığında çalışır
            onTapAr = { hitResult, _ ->
                // Zemine tıklandığında seçili bir model node'u varsa ve henüz sabitlenmemişse sabitle
                modelNode?.let { node ->
                    if (!node.isAnchored) {
                        node.anchor() // Modeli tıklanan noktaya sabitler
                        bilgiKartiniGoster() // Model yerleşince bilgi kartını aç
                    }
                }
            }
        }
    }

    // Seçilen elemana göre assets içinden 3D modeli yükler
    private fun hazirlaYeniModel(modelYolu: String) {
        // Eğer ekranda daha önceden kalan bir model varsa onu sahneden kaldır
        modelNode?.let { binding.sceneView.removeChild(it) }

        // Yeni bir AR Model Node oluştur (Yatay zeminler için)
        modelNode = ArModelNode(binding.sceneView.engine, PlacementMode.PLANE_HORIZONTAL).apply {
            loadModelGlbAsync(
                glbFileLocation = "models/$modelYolu", // Assets/models/ klasöründeki dosya
                autoAnimate = true,
                scaleToUnits = 0.5f // Modelin gerçek dünyadaki boyutunu ayarlar (0.5 metre gibi)
            ) {
                // Model başarıyla yüklendiğinde burası çalışır
            }
        }
        
        // Hazırlanan modeli AR sahnesine ekle
        modelNode?.let { binding.sceneView.addChild(it) }
        
        // Yeni eleman seçildiğinde eski bilgi kartını gizle
        binding.cardBilgi.isVisible = false
    }

    // Bilgi kartını seçili elemanın verileriyle doldurur ve gösterir
    private fun bilgiKartiniGoster() {
        secilenEleman?.let { eleman ->
            binding.tvKartBaslik.text = eleman.ad
            binding.tvKartAciklama.text = eleman.gorev
            binding.cardBilgi.visibility = View.VISIBLE
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "AR özellikleri için kamera izni gereklidir.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
