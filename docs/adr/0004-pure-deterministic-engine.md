# 0004 · Saf ve deterministik oyun motoru

- **Durum:** Kabul
- **Tarih:** 2026-11-13

## Bağlam

Eski motor Android'e ve UI'a bağlıydı, test edilemiyordu. Günlük bulmaca her cihazda aynı olmalı; ipucu teknik öğretmeli; her bulmaca tek çözümlü olmalı.

## Karar

`:core:engine` yalnızca Kotlin stdlib ve `:core:model`'e bağlıdır. Tüm rastgelelik dışarıdan verilen `Random(seed)` ile; aynı girdi aynı çıktı. Teklik `countSolutions(limit = 2)` ile zorunlu. Zorluk, insan tekniği tabanlı grader ile belirlenir. Oyun kuralları saf `GameReducer` fonksiyonudur.

## Sonuçlar

- Motor JVM'de milisaniyelerde test edilir; kapsam hedefi ≥ 90 %.
- Günlük bulmaca sunucusuz çalışır.
- Grader'ın teknik kümesi genişledikçe eski seed'lerin derecesi değişebilir; günlük bulmaca satırı `difficulty` ve `seed` sakladığı için geçmiş bozulmaz.

## Değerlendirilen alternatifler

- Hazır bulmaca veri seti gömülmesi: çeşitlilik sınırlı, derece bilgisi dış kaynaklı; üretim yeterince hızlı.
- Sunucuda üretim: çevrimdışı ilkesine aykırı.
