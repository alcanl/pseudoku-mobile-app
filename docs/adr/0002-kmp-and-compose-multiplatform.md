# 0002 · Kotlin Multiplatform + Compose Multiplatform

- **Durum:** Kabul
- **Tarih:** 2026-11-13

## Bağlam

Android ve iOS'ta tek kod tabanı isteniyor. Ekip Kotlin biliyor. UI büyük ölçüde özel çizim (tahta) ve standart form/listelerden oluşuyor; platforma özgü karmaşık bileşen yok.

## Karar

Mantık **ve** UI paylaşılır: Compose Multiplatform ile tek arayüz. iOS tarafında SwiftUI yalnızca host (`UIViewController` sarmalayıcı) ve platform servisleri için.

## Sonuçlar

- Tek UI kod tabanı; tasarım sistemi bir kez yazılır.
- iOS'ta Compose metin ve kaydırma davranışı native'den küçük farklar taşıyabilir; tahta Canvas tabanlı olduğu için etki düşük.
- iOS derlemesi için macOS gerekir (ADR-0010 ile CI'da çözülür).

## Değerlendirilen alternatifler

- KMP mantık + native UI (Compose Android, SwiftUI iOS): iki UI, iki kat iş; proje ölçeğinde gereksiz.
- Flutter / React Native: ekip uzmanlığı Kotlin; motorun saf Kotlin kalması isteniyor.
