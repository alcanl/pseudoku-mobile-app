# 01 · Vizyon ve kapsam

## Vizyon

Pseudoku, telefonda **hızlı, sakin ve öğreten** bir Sudoku deneyimi sunar. Reklam duvarları, oyalayıcı animasyonlar ve giriş zorunluluğu yoktur. Oyuncu bir tahtayı açar, oynar, takıldığında ipucu yalnızca cevabı değil **tekniği** de gösterir. Uygulama kapanırsa oyun olduğu yerde kalır.

## Hedef kullanıcı

- Günde 10-30 dakika Sudoku çözen, mağaza uygulamalarının reklam yoğunluğundan sıkılmış oyuncular.
- Teknik öğrenmek isteyen başlangıç ve orta seviye oyuncular (ipucu açıklamaları, "nasıl oynanır").
- İstatistik ve seri takibi isteyen düzenli oyuncular (günlük bulmaca).

## Platformlar

| Platform | Sürüm | Not |
|---|---|---|
| Android | 1.0 | minSdk 26, targetSdk güncel. Önce bu. |
| iOS | 1.1 | iOS 16+. Derleme GitHub Actions macOS runner'ında (ADR-0010). |
| Masaüstü / Web | Kapsam dışı | Mimari engel yok; talep olursa `jvm` ve `wasmJs` hedefi eklenir. |

## Kapsam

### 1.0 (Android)

- Klasik 9×9 Sudoku; 5 zorluk: Easy, Medium, Hard, Expert, Master.
- Her bulmaca **tek çözümlü** ve teknik tabanlı derecelendirilmiş (bkz. 03).
- Giriş: rakam / not modu, 9 haneli kalem notları, otomatik aday temizliği (ayar), silme, geri al / ileri al (sınırsız geçmiş).
- Vurgulama: seçili hücre, satır-sütun-kutu, aynı rakamlar, çakışmalar (ayarlanabilir).
- Hata sayacı ve limit (varsayılan 3, kapatılabilir), ipucu (varsayılan 3, teknik açıklamalı).
- Sayaç, duraklat/devam, uygulama arka plana gidince otomatik duraklatma.
- **Otomatik kayıt ve devam**: her hamle kalıcı; "Devam et" kartı ana ekranda.
- Günlük bulmaca (tarihe göre deterministik), seri takibi.
- İstatistik: zorluk bazında oynanan/kazanılan, en iyi ve ortalama süre, hatasız oyun sayısı, günlük seri.
- Ayarlar: tema (sistem/açık/koyu), renk paleti, vurgulama seçenekleri, hata limiti, sayaç görünürlüğü, titreşim, ses, dil.
- Tema: açık ve koyu; 09'daki tasarım yönüne göre 2-3 palet.
- Yerelleştirme: Türkçe ve İngilizce.
- Nasıl oynanır: kurallar ve ilk teknikler (singles, pairs) kısa kartlar.
- Crashlytics ve temel kullanım analitiği (opt-out).

### 1.1 (iOS + ortak)

- iOS uygulaması, TestFlight ve App Store.
- Başarımlar.
- Ekran görüntüsü / sonuç paylaşımı.

### 1.2 (isteğe bağlı, kullanıcı kararı bekliyor, `Açık`)

- Hesap (Sign in with Apple / Google) ve istatistik bulut eşitleme.
- Liderlik tablosu (günlük bulmaca süreleri).

### Kapsam dışı (bilinçli)

- Eski projedeki yerel kullanıcı adı / parola ekranı. Çevrimdışı tek kullanıcılı oyunda gereksiz ve güvenlik yükü (ADR-0008).
- Facebook girişi.
- Varyantlar (Killer, Samurai, 16×16). Motor `size` parametresiyle büyümeye uygun ama 1.x'te yok.
- Çok oyunculu.

## Kalite çıtası ("enterprise" ne demek)

| Ölçüt | Hedef | Nerede doğrulanır |
|---|---|---|
| Motor test kapsamı | ≥ 90 % satır, reducer 100 % | Kover, CI eşiği |
| Genel test kapsamı | ≥ 60 % | Kover |
| Statik analiz | detekt + ktlint sıfır ihlal | CI |
| Çökme oranı | ≥ 99.5 % crash-free session | Crashlytics |
| Soğuk başlangıç | < 1.5 s (orta segment cihaz) | Macrobenchmark (Faz 4) |
| Oyun ekranı | Giriş sırasında kare düşürme yok (jank < 1 %) | Compose compiler metrics + manuel |
| Bulmaca üretimi | Hard için ortalama < 150 ms (JVM), kullanıcı hiç beklemez (havuz) | Motor benchmark testi |
| Erişilebilirlik | TalkBack ile oynanabilir, 4.5:1 kontrast, 44 dp dokunma alanı | Manuel + Compose semantics testleri |
| Yerelleştirme | Kodda sabit metin yok | Lint kuralı |
| Sürüm | Her PR CI yeşil, `main` korumalı, etiketle otomatik build | GitHub Actions |
