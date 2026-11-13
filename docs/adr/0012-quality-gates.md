# 0012 · Kalite kapıları

- **Durum:** Kabul
- **Tarih:** 2026-11-13

## Bağlam

Eski projede test, lint ve CI yoktu. "Enterprise seviye" hedefi ölçülebilir olmalı.

## Karar

Her PR'da zorunlu: detekt, ktlint (spotless), Android Lint (`warningsAsErrors`), tüm JVM testleri, Kover eşikleri (motor ≥ 90 %, reducer 100 %, genel ≥ 60 %), `:core:engine` bağımlılık denetimi, Koin `verify()`. `main` korumalı; CI kırmızıyken merge yok.

## Sonuçlar

- PR süresi birkaç dakika uzar; kalite geriye gitmez.
- Eşikler başlangıçta düşük modüllerde (`designsystem`) muaf tutulur, Faz 3'te eklenir.

## Değerlendirilen alternatifler

- Yalnızca derleme kontrolü: eski projenin tekrarı.
