# 0008 · Çevrimdışı öncelikli, 1.0'da hesap yok

- **Durum:** Kabul
- **Tarih:** 2026-11-13

## Bağlam

Eski proje yerel kullanıcı adı/parola ekranıyla açılıyor, parolayı düz metin saklıyordu ve sosyal giriş yarım kalmıştı. Tek kullanıcılı, çevrimdışı bir bulmaca oyununda hesap zorunluluğu kullanıcı kaybı ve güvenlik yükü demektir.

## Karar

1.0 hesapsızdır: açılış doğrudan ana ekran. Tüm veri cihazda. Hesap (Sign in with Apple / Google) ve bulut eşitleme 1.2 için ayrı ADR ile değerlendirilir; eklenirse de isteğe bağlı kalır.

## Sonuçlar

- Giriş ekranı, parola saklama, Facebook SDK ve ilgili manifest kayıtları yok.
- Cihaz değişince istatistik taşınmaz (Android yedeklemesi `allowBackup` ile kısmen çözer).

## Değerlendirilen alternatifler

- Firebase Auth ile isteğe bağlı giriş 1.0'da: 1.0 kapsamını ve takvimi büyütür; değeri bulut eşitleme olmadan yok.
