# Pseudoku

Kotlin Multiplatform ile geliştirilen, çevrimdışı çalışan, oyuncuya teknik öğreten bir Sudoku oyunu. Android önce, iOS sonra; tek kod tabanı (Compose Multiplatform).

> **Durum:** yeniden yazım aşamasında. `main` dalındaki eski native Android kodu (2024-2025) yalnızca referans olarak duruyor; yeni KMP yapısı aşağıdaki dokümanlardaki mimariye göre kuruluyor. Plan için bkz. [yol haritası](docs/08-roadmap.md).

## Dokümantasyon

| Doküman | İçerik |
|---|---|
| [Doküman haritası](docs/README.md) | Okuma sırası, konvansiyonlar, durum etiketleri |
| [01 Vizyon ve kapsam](docs/01-vision-and-scope.md) | Ürün hedefi, sürüm kapsamları, kalite çıtası |
| [02 Mimari](docs/02-architecture.md) | Modüller, katmanlar, bağımlılık kuralları, teknoloji yığını |
| [03 Alan modeli ve motor](docs/03-domain-model.md) | Sınıf diyagramları, üretim ve derecelendirme algoritmaları, oyun durum makinesi |
| [04 Veri modeli](docs/04-data-model.md) | ER diyagramı, tablolar, ayar anahtarları, migrasyon politikası |
| [05 Arayüz ve navigasyon](docs/05-ui-and-navigation.md) | Ekran envanteri, navigasyon grafı, UI durumları, erişilebilirlik, yerelleştirme |
| [06 Test ve kalite](docs/06-testing-and-quality.md) | Test piramidi, statik analiz, kapsam eşikleri, performans bütçeleri |
| [07 CI/CD ve sürüm](docs/07-ci-cd-and-release.md) | GitHub Actions hatları, imzalama, mağaza dağıtımı, iOS planı |
| [08 Yol haritası](docs/08-roadmap.md) | Fazlar, teslimatlar, çıkış kriterleri |
| [09 Arayüz tasarım yönü](docs/09-ui-design-direction.md) | Araştırma özeti, tasarım yönleri, token'lar |
| [ADR'ler](docs/adr/README.md) | Mimari karar kayıtları |

## Teknoloji (özet)

Kotlin Multiplatform · Compose Multiplatform · Koin · Room KMP · DataStore · kotlinx.coroutines / serialization / datetime · Navigation Compose (JetBrains) · GitHub Actions

## Geliştirme

Kurulum ve komutlar Faz 0 (proje iskeleti) birleştiğinde buraya eklenecek.

## Lisans

Apache-2.0, bkz. [LICENSE](LICENSE).
