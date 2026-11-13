# 08 · Yol haritası

`Karar`; takvim hedefleri `Taslak`.

## Varsayımlar

- Uygulama kodunu büyük ölçüde Claude Code bulut oturumları yazar; her faz bir veya birkaç PR'dır. Fazlar bağımsız olduğunda paralel oturumlar açılır (örn. iskelet ve motor).
- Proje sahibi PR'ları inceler, gerçek cihazda test eder ve tasarım kararlarını verir. **Takvimi belirleyen darboğaz kod yazma hızı değil, inceleme ve cihaz testi temposudur.**
- Hafta içi her gün ilerleme varsayılır; tarihler hedeftir, söz değil.
- iOS için Mac yok; derleme GitHub Actions'ta, görsel doğrulama TestFlight ile.

## Fazlar

| Faz | Kapsam | Çıkış kriteri | Hedef |
|---|---|---|---|
| 0 · Temel | Dokümanlar (bu PR), KMP iskeleti, version catalog, convention plugin'ler, tüm modüller boş ama derlenir, Koin, navigasyon, tema kabuğu, CI yeşil, eski `app/` kaldırılır | `main`'de `assembleDebug` ve `allTests` CI'da yeşil; boş Home ekranı açılır | 2 iş günü |
| 1 · Motor | Solver, generator, digger, grader, hint engine, validator, reducer, skor, DailyRules; testler ve benchmark | Kover ≥ 90 %, teklik ve determinizm testleri yeşil | 2-3 iş günü (Faz 0 ile paralel) |
| 2 · Oyun | Tasarım sistemi token'ları (09'daki seçilen yön), `SudokuBoard`, `NumberPad`, Game ekranı tam işlevli, Pause/Hint/Result katmanları, Room + DataStore, otomatik kayıt/devam, Home ekranı | Cihazda baştan sona oyun; uygulama öldürülüp açılınca oyun aynı yerde | 4 iş günü |
| 3 · Ürün | Stats, Settings, günlük bulmaca, nasıl oynanır, onboarding, TR/EN, açık/koyu + paletler, titreşim/ses, Crashlytics, analitik, Play görselleri, adaptive icon, imzalama, release workflow | Release build internal track'te | 3 iş günü |
| 4 · Yayın | Cihaz QA, hata düzeltme, performans ölçümü, erişilebilirlik geçişi, `v1.0.0` | Yayın kontrol listesi tamam | 2-3 iş günü + Play test süresi |
| 5 · iOS (1.1) | Xcode projesi, SwiftUI host, safe area ve haptics actual'ları, `ios-build.yml`, TestFlight, App Store görselleri, başarımlar | TestFlight'ta oynanabilir; App Store gönderimi | 4-5 iş günü |
| 6 · Bulut (1.2) | Hesap, istatistik eşitleme, liderlik | Karar bekliyor | — |

Toplam: **Android 1.0 için yaklaşık 12-14 iş günü (3 hafta)**, iOS 1.1 için +1 hafta. Önceki 6-8 haftalık tahmin tek insan geliştiriciye göreydi; bu plan paralel oturumlar ve dokümanla önceden verilmiş kararlar sayesinde kısaltıldı. Kısaltılamayan kalemler: Play kapalı test zorunluluğu (yeni hesapta 14 gün), mağaza incelemesi, gerçek cihaz QA.

## Takvim (hedef)

```mermaid
gantt
  title Pseudoku 1.0 (Android) ve 1.1 (iOS) hedef takvimi
  dateFormat YYYY-MM-DD
  axisFormat %d %b
  section Faz 0-1
  Dokümanlar ve tasarım yönü      :done, d0, 2026-11-13, 1d
  İskelet, CI, DI, navigasyon       :f0, 2026-11-16, 2d
  Motor ve testler                   :f1, 2026-11-16, 3d
  section Faz 2
  Oyun ekranı, kalıcılık, Home      :f2, 2026-11-19, 4d
  section Faz 3
  Stats, Settings, günlük, TR/EN     :f3, 2026-11-25, 3d
  section Faz 4
  Cihaz QA, Play iç test, v1.0.0     :f4, 2026-11-30, 3d
  section Faz 5
  iOS: Xcode, Actions, TestFlight    :f5, 2026-12-03, 5d
```

## Paralel çalışma kuralları

- Aynı anda en fazla iki oturum; ikisi aynı modüle dokunmaz (örn. biri `core:engine`, diğeri `composeApp` + `build-logic`).
- Her oturum kendi dalında, PR ile biter; `main`'e doğrudan commit yok.
- Faz 2, Faz 0 ve 1 `main`'e girmeden başlamaz (tahta motor tiplerine bağlı).

## Riskler

| Risk | Etki | Azaltma |
|---|---|---|
| Compose MP iOS'ta metin ölçüm/klavye farkları | iOS cilası uzar | Tahta Canvas tabanlı, metin girdisi yok; risk düşük |
| Room KMP iOS'ta sürücü sorunları | Kalıcılık hatası | `BundledSQLiteDriver` her iki platformda aynı; JVM testleri aynı sürücüyü kullanır |
| Grader bantları çok dar → üretim yavaş | Yeni oyun gecikir | Havuz + bant genişletme + benchmark testi |
| Play yeni hesap kapalı test kuralı | Yayın 2 hafta kayar | Hesap durumu Faz 0'da kontrol edilir; testçi listesi erken hazırlanır |
| Mac olmaması | iOS hataları geç görülür | TestFlight + CI UI testleri; gerekirse kısa süreli bulut Mac |
