# 0007 · Kalıcılık için Room KMP ve DataStore

- **Durum:** Kabul
- **Tarih:** 2026-11-13

## Bağlam

Oyun durumu her hamlede kalıcı olmalı; istatistik sorguları gerekiyor; ayarlar basit anahtar-değer.

## Karar

Room KMP (2.7+) + `BundledSQLiteDriver`; şema repoda, migrasyon testleri CI'da. Ayarlar DataStore Preferences (KMP). JSON alanları kotlinx.serialization ile.

## Sonuçlar

- Ekip Room'u biliyor (eski projede vardı); KMP sürümü aynı DAO modeli.
- Aynı sürücü JVM testlerinde çalışır; Android emulator gerekmez.

## Değerlendirilen alternatifler

- SQLDelight: olgun ve KMP-native; SQL-önce yaklaşımı ekibin alışkanlığına ters, Room KMP artık stabil.
- Yalnızca dosya/JSON: istatistik sorguları ve eşzamanlı yazım için yetersiz.
