# 0005 · Tek yönlü veri akışı ve KMP ViewModel

- **Durum:** Kabul
- **Tarih:** 2026-11-13

## Bağlam

Eski projede durum Activity, Handler, Mutex ve DataBinding arasında dağılmıştı; yeni oyunda ekran eski nesneye bağlı kalıyordu.

## Karar

Her ekranın tek `StateFlow<UiState>`'i ve tek `onEvent(UiEvent)` girişi vardır. Durum geçişleri `androidx.lifecycle.ViewModel` (KMP) içinde; oyun kuralları `GameReducer`'da. Tek seferlik etkiler `Channel<UiEffect>`. UI hiçbir karar vermez, yalnızca çizer.

## Sonuçlar

- ViewModel testleri Turbine ile deterministik.
- Yapılandırma değişimi ve süreç ölümü: durum Room'da olduğu için `SavedStateHandle` yalnızca `gameId` taşır.
- Kayıp olmayan yerde onay diyaloğu yok (çıkış otomatik kaydeder).

## Değerlendirilen alternatifler

- Decompose / Voyager gibi üçüncü parti navigasyon+state: androidx ViewModel KMP'de stabil; ek bağımlılık gereksiz.
- Tam MVI kütüphanesi (Orbit, MVIKotlin): proje ölçeğinde fazla; aynı disiplin elle uygulanır.
