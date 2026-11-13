# 0010 · Android önce, iOS GitHub Actions ile

- **Durum:** Kabul
- **Tarih:** 2026-11-13

## Bağlam

Geliştirme makinesi Windows; Mac yok. iOS derlemesi ve imzalama macOS ister.

## Karar

1.0 yalnızca Android. Ortak kod baştan iOS hedefini içerir (`iosArm64`, `iosSimulatorArm64`) ve her PR'da en azından **derlenir** (`compileKotlinIosArm64`, Linux'ta çalışır). iOS uygulaması 1.1'de `macos` runner'da `ios-build.yml` ile derlenir, TestFlight ile dağıtılır.

## Sonuçlar

- iOS'a özel hatalar geç görülür; `expect/actual` yüzeyi küçük tutularak risk azaltılır.
- macOS runner dakikaları Linux'un 10 katı ücretlendirilir; iOS iş akışı PR'da değil, elle ve etikette çalışır.

## Değerlendirilen alternatifler

- Bulut Mac kiralama: ihtiyaç olursa Faz 5'te kısa süreli.
- iOS'u ertelemeyip paralel yapmak: doğrulama imkanı olmadan kod yazmak israf.
