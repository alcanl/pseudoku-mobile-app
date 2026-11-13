# 0006 · Bağımlılık enjeksiyonu için Koin

- **Durum:** Kabul
- **Tarih:** 2026-11-13

## Bağlam

Hilt Android'e özeldir, KMP'de çalışmaz. Derleme zamanı doğrulamalı KMP seçenekleri (kotlin-inject, Metro) daha yeni ve daha az yaygın.

## Karar

Koin 4.x; modül başına `module { }`, ViewModel'ler `viewModel { }` ile, platform servisleri `androidModule` / `iosModule`. Koin'in `verify()` testi CI'da çalışır, eksik bağımlılık PR'da yakalanır.

## Sonuçlar

- Runtime çözümleme; `verify()` ile derleme-zamanı güvencesine yakın sonuç.
- Compose MP ile `koinViewModel()` hazır.

## Değerlendirilen alternatifler

- kotlin-inject: derleme zamanı güvenli ama KSP yapılandırması ve topluluk desteği daha dar.
- Elle DI: modül sayısı büyüdükçe sürdürülemez.
