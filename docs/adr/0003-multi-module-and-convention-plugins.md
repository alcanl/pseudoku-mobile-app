# 0003 · Çok modüllü yapı ve convention plugin'ler

- **Durum:** Kabul
- **Tarih:** 2026-11-13

## Bağlam

Motorun UI'dan, feature'ların birbirinden bağımsız kalması ve paralel oturumların çakışmadan çalışması isteniyor.

## Karar

`core:*` / `feature:*` / `composeApp` / `androidApp` modül yapısı (bkz. 02). Gradle yapılandırması `build-logic/` içindeki convention plugin'lerde; modül build dosyaları yalnızca plugin uygular ve bağımlılık listeler. Sürümler tek `libs.versions.toml`.

## Sonuçlar

- Bağımlılık yönü derleyiciyle korunur; feature'lar birbirini göremez.
- İlk kurulum maliyeti (convention plugin'ler) bir gün; sonrasında yeni modül 5 satır.
- Derleme süresi modül paralelliğinden yararlanır.

## Değerlendirilen alternatifler

- Tek `shared` modül: hızlı başlar, 2-3 haftada örtük bağımlılıklar oluşur; "enterprise" hedefiyle uyumsuz.
