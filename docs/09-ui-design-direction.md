# 09 · Arayüz tasarım yönü

`Açık`: üç yönden biri seçilecek. Mockup'lar tasarım tuvalinde: https://claude.ai/artifact/2fC5o1Dn5BCUCg4aYk5TJF (özel bağlantı, yalnızca proje sahibi açabilir). Seçim yapılınca bu doküman `Karar`a döner ve token'lar `:core:designsystem` içine aynen işlenir.

## Araştırma özeti (13 Kasım 2026)

Kaynaklar: Good Sudoku (Zach Gage, Jack Schlesinger) incelemeleri ve geliştirici röportajları; NYT Games tasarım dili ve Pips/Sudoku yardım sayfaları; Sudoku.com, Brainium, Andoku 3, Conceptis, Logify mağaza sayfaları ve kılavuzları; Apple Design Awards 2021-2026 bulmaca finalistleri; "yapay zeka üretimi arayüz" klişeleri üzerine tasarımcı yazıları; WCAG 1.4.1 ve Android 14 doğrusal olmayan yazı ölçekleme notları. Bağlantılar dokümanın sonunda.

### Oyuncuların beklediği standart kalıplar (1.0'da hepsi var)

- 1-9 rakam pedi altta, hücre-önce giriş varsayılan; rakam-önce giriş ayar olarak (Conceptis "Lock Number").
- Not / kalem modu; hücre dolunca komşu notların otomatik temizlenmesi (Sudoku.com, Brainium, NYT "Auto-Candidate").
- Her rakamın kalan adedi pedde; biten rakam pasifleşir (Sudoku 9x9, Logify).
- Seçili hücre + satır/sütun/kutu + aynı rakam vurgusu, her biri ayrı kapatılabilir (NYT ayarları).
- Hata gösterimi: NYT küçük kırmızı nokta veya çizgi kullanır, tam kırmızı dolgu kullanmaz; Sudoku.com 3 hata limiti (kapatılabilir).
- Sınırsız geri al, sil, ipucu; en iyi uygulamalar ipucunu **bir sonraki mantıklı adım ve açıklaması** olarak verir (Andoku, Brainium, Conceptis Assistant; Good Sudoku asla cevabı söylemez).
- İstatistik (kazanılan, en iyi/ortalama süre, hatasız), günlük bulmaca ve seri, açık/koyu tema.
- Erişilebilirlik şikayeti: Sudoku.com'da ped tuşları ve not düğmesi küçük; motor beceri sorunu olan oyuncular için büyük hedefler.

### En iyi uygulamaların farkı

- **Good Sudoku**: "anything you can take off someone's plate is going to help them think better" ilkesi; sayma işini uygulama yapar, oyuncu yapıyı görür. Rakama dokununca gidemeyeceği hücreler işaretlenir. İki anlam rengi (turuncu = yerleşen, mavi = not). Ses ve titreşim tasarımı özellikle övülüyor; palet kimi kullanıcıyı bölüyor.
- **NYT Games**: "direct, clean, and effective, yet playful"; "avoid fussy gimmicks"; oyun kâğıtta da var olabilirmiş gibi hissettirmeli; ses efektleri gerçek nesnelerden kaydedilmiş; tek anlam rengi çifti (mavi = doğrulanmış, kırmızı nokta = hata). 2024 Apple Design Award (Delight and Fun).
- **ADA finalistleri** (Rytmos, Railbound, stitch.): onboarding oyunu anında anlaşılır kılar; mekanik "kazara keşfedilebilecek" kadar basit; kolay geri alma; sıcak ve yaklaşılabilir; erişilebilirlik seçenekleri (büyük rakamlar, rakam çerçeveleri, renk körlüğü deseni).

### Kaçınılacaklar ("yapay zeka arayüzü" işaretleri)

Mor-mavi gradyanlar (Tailwind indigo-500 varsayılanı), her şey için Inter/Roboto, gradyan başlık metni, derinliği olmayan cam efekti, her kartta gölge ve parlama, kart içinde kart, her yerde aynı köşe yarıçapı, üçlü eşit kart ızgaraları, her şey ortalanmış, geniş aralıklı büyük harf etiketler, emoji ikon, karışık ikon setleri, yan şeritli kartlar, her öğede fade-up ve 600 ms geçişler, dolgu metinler. Yerine: tek çıpa renk, karakterli bir başlık yazı tipi + gövde yazı tipi çifti, asimetrik yerleşim, role göre değişen yarıçap, yalnızca durum değişiminde ve 100-150 ms hareket.

## Üç yön

Her yön için Oyun ve Ana ekran mockup'ı tuvalde; Gazete için Sonuç ekranı da var. Üçü de aynı bilgi mimarisini ve aynı bileşen setini kullanır; fark token'lardadır. Bu yüzden seçilmeyen yönler ileride **palet** olarak eklenebilir (aynı tipografi ve yerleşim, farklı renk ve yüzey).

### 1 · Gazete (önerilen)

NYT Games soyu: kâğıt, mürekkep, tek vurgu rengi, ince kurallar. Bulmaca sayfası hissi; ciddi ama soğuk değil.

| Token | Açık | Koyu |
|---|---|---|
| `surface` | `#F5F2EB` | `#141412` |
| `onSurface` | `#1B1B1B` | `#EDE9E0` |
| `onSurfaceMuted` | `#6B6B66` | `#9A978F` |
| `boardLine` | `#C9C5BC` | `#3A3934` |
| `boardBoxLine` | `#1B1B1B` | `#EDE9E0` |
| `cellPeer` | `#ECE8DE` | `#1F1F1C` |
| `cellSameDigit` | `#DCE4F7` | `#1E2A48` |
| `cellSelected` | `#C9D6F5` | `#28386A` |
| `accent` (seçim halkası, oyuncu rakamı) | `#1F4FD6` | `#6F8FF0` |
| `error` (köşe noktası, rakam) | `#D83A1E` | `#FF7A5C` |
| `digitGiven` | `onSurface` 600 | |
| `digitUser` | `accent` 500 | |
| `note` | `onSurfaceMuted` 500 | |

- Tipografi: başlık **Zilla Slab** 700 (zorluk adı, "Çözüldü.", ana ekran logosu); arayüz ve rakamlar **Libre Franklin** (Franklin Gothic soyu, tabular rakamlar). Tahta rakamı 22 sp, ped 27 sp, notlar 9 sp.
- Biçim: köşe yarıçapı 2-3 dp (neredeyse köşeli), tahta 0; kart yerine 1.5 dp mürekkep çerçeve; gölge yok.
- Hata: tam dolgu yok; köşede 5 dp nokta + rakam kırmızı. Renk körlüğü için nokta şekil ipucu.
- Hareket: doğru girişte 120 ms ölçek, tamamlanan birimde 200 ms dalga; başka animasyon yok.
- Ses: kâğıt ve kalem sesleri (NYT yaklaşımı), varsayılan kapalı.
- Risk: fazla sade bulunabilir. Azaltma: sıcak kâğıt tonu, slab başlıklar, kazanma anında tahta dolgusu.

### 2 · Atölye

Good Sudoku soyu: sıcak kum zemin, krem tahta kartı, kalın mürekkep çerçeveler, basılı tuşlar (2-3 dp alt gölge), iki anlam rengi.

| Token | Değer |
|---|---|
| `surface` | `#F2E3BE` |
| `card` | `#FBF6EA` |
| `onSurface` | `#2A2117` |
| `onSurfaceMuted` | `#7A6B4F` |
| `placed` (oyuncu rakamı, aynı rakam, ilerleme) | `#E5702A` |
| `note` (not rakamları, not modu, günlük) | `#2F6FE0` |
| `cellPeer` / `cellSameDigit` / `cellSelected` | `#F4EAD2` / `#FFE3C9` / `#FFD2AE` |
| `error` | `#B8321C` + üstü çizili rakam |

- Tipografi: başlık **Bricolage Grotesque** 800; arayüz ve rakamlar **Rubik** 500-700.
- Biçim: 12-18 dp yarıçap, 2-2.5 dp mürekkep çerçeve, tuşlarda `0 3dp 0` katı gölge (bulanık değil). Hata sayacı üç küçük kare.
- Hareket: tuş basımında 2 dp aşağı kayma, kazanınca konfeti yerine tahta hücrelerinin sırayla turuncuya dönmesi.
- Koyu tema: `#221B14` zemin, krem metin, aynı iki anlam rengi.
- Risk: palet bölücü olabilir (Good Sudoku eleştirisi); çocuksu kayabilir. Azaltma: doygunluğu düşük tutmak, tek başlık yazı tipi.

### 3 · Alet

Fiziksel alet metaforu: grafit gövde, kabartma ızgara, hesap makinesi tuşları, tek amber vurgu, mono rakamlar. Koyu öncelikli.

| Token | Koyu | Açık |
|---|---|---|
| `surface` | `#15171A` | `#E4E2DD` |
| `panel` | `#1E2125` | `#F2F1ED` |
| `key` | `#2A2E33` | `#FFFFFF` |
| `onSurface` | `#EDE9E1` | `#111214` |
| `onSurfaceMuted` | `#8B9097` | `#6B6F75` |
| `boardLine` / `boardBoxLine` | `#2B3036` / `#3A3F46` | `#C9C8C3` / `#8A8984` |
| `cellPeer` / `cellSameDigit` / `cellSelected` | `#22262B` / `#2E2A1F` / `#3A3220` | `#E9E8E3` / `#F6E7C8` / `#F3D9A3` |
| `accent` | `#F2A33A` | `#B9761F` |
| `error` | `#FF6B57` + alt çizgi | `#C8381F` |

- Tipografi: rakamlar **IBM Plex Mono** 500, arayüz **IBM Plex Sans**; küçük etiketler mono.
- Biçim: 8 dp yarıçap; tuşlarda `inset 0 1 0 beyaz %8` üst ışık + `inset 0 -3 0 koyu` alt gölge; ızgara çizgileri iki tonlu.
- Hareket: yalnızca tuş basımı (gölge 3 dp → 1 dp, 80 ms) ve rakamın hücreye "düşmesi" (100 ms).
- Risk: koyu-öncelikli tasarımın açık sürümü zayıf kalabilir; mono rakamlar küçük notlarda okunaklılık ister.

## Ortak kurallar (yönden bağımsız)

- Tahta ekran genişliğinden türetilir (kenar 14-16 dp), hücre ≥ 38 dp; ped tuşları ≥ 48 dp yükseklik; aksiyon düğmeleri ≥ 54 dp.
- Sahte durum çubuğu yok; üst güvenli alan 56-58 dp.
- İkonlar tek set, çizgi kalınlığı sabit (1.8-2.2 dp); emoji yok.
- Metin kontrastı ≥ 4.5:1, vurgulu hücre zeminleri dahil.
- Yerleşim asimetrik: başlık sola, sayaç sağa; ortalanmış tek şey tahta.
- Yalnızca durum değişiminde hareket; 80-200 ms; `reduce_motion` açıkken anında.

## Karar için öneri

**Gazete** varsayılan yön: mağaza kalabalığından ayrışır, "profesyonel" algısı en yüksek, açık/koyu dengeli, erişilebilirlik en kolay. Atölye ve Alet, aynı bileşenler üzerinde **palet** olarak 1.1'de sunulabilir ("Kâğıt", "Atölye", "Grafit").

## Kaynaklar

- Good Sudoku: MacStories incelemesi (macstories.net/reviews/game-day-good-sudoku), hey.gg Zach Gage söyleşisi, 9to5mac lansman haberi, TapSmart ve Daring Fireball yazıları, Vice "making me good at sudoku".
- NYT Games: TechCrunch 2025 uygulama yenilemesi, NYT Sudoku yardım sayfası (helpjuice), Creative Boom Pips haberi.
- Ana akım: Sudoku.com ve Conceptis App Store sayfaları, brainium.com/sudoku, andoku.com/apps/andoku3, Android Authority "best sudoku apps", sudoku9x9.com/manual, Logify App Store sayfası.
- Ödüller: developer.apple.com/design/awards (2021-2026), Google Play Best of 2025.
- Yapay zeka arayüzü klişeleri: prg.sh "purple gradient", dev.to "indigo-500", noqta.tn "AI design slop 2026", Laith0003 ux-skill anti-slop listesi.
- Erişilebilirlik: WCAG 2.2 "Use of Color", Android 14 non-linear font scaling, Android haptics principles, Apple Art of Fauna yazısı.
