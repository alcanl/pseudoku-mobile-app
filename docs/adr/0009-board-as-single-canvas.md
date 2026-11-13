# 0009 · Tahta tek Canvas composable

- **Durum:** Kabul
- **Tarih:** 2026-11-13

## Bağlam

Eski tahta 81 `FrameLayout` + 36 kenarlık drawable'ı ile 174 KB XML'di. Compose'da 81 composable da çalışır ama her seçimde 81 recomposition ve vurgulama mantığının dağılması anlamına gelir.

## Karar

`SudokuBoard` tek `Canvas`; `drawWithCache` ile metin ölçümleri saklanır; dokunma `pointerInput` ile hücreye çevrilir; erişilebilirlik için hücre başına `semantics` düğümü eklenir.

## Sonuçlar

- Seçim değişimi tek çizim; ölçüm/yerleşim yok.
- Animasyonlar (doğru giriş, tamamlanan birim) Canvas içinde `Animatable` ile; bileşen başına animasyon yönetimi gerekmez.
- Hücre başına `Modifier` tabanlı özelleştirme yok; tüm görsel karar token'lardan gelir.

## Değerlendirilen alternatifler

- `LazyVerticalGrid` / 81 `Box`: basit ama performans ve semantik tutarlılığı daha zayıf.
