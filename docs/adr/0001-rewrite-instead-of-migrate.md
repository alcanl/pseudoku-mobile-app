# 0001 · Eski kodu taşımak yerine yeniden yazmak

- **Durum:** Kabul
- **Tarih:** 2026-11-13

## Bağlam

`main` dalındaki proje (2024-2025) tek modüllü native Android: XML + DataBinding, Hilt, Room, yarım Facebook girişi. 45 Kotlin dosyası, 174 KB elle yazılmış tahta layout'u. Motor 200 satır ve hatalı (üretici singleton grid'i sıfırlamıyor, ipucu kenar hücreleri seçmiyor, teklik denetimi yok). Hedef platform KMP (Android + iOS) ve Compose.

## Karar

Kod taşınmaz. Eski proje **şartname** olarak kullanılır (zorluk sabitleri, kurallar, ürün fikri); yeni proje sıfırdan, bu dokümanlardaki mimariyle kurulur. Eski `app/` modülü Faz 0 PR'ında silinir; tarih git'te kalır.

## Sonuçlar

- Hiçbir Android View / DataBinding / Hilt kodu taşınmadığı için KMP'ye uyarlama maliyeti yok.
- Motor düzgün tasarımla (teklik, derecelendirme, determinizm) yeniden yazılır; eski hatalar taşınmaz.
- Eski Room şeması ile uyumluluk aranmaz; uygulama hiç yayınlanmadı.

## Değerlendirilen alternatifler

- Kademeli taşıma (önce Compose, sonra KMP): UI'ın tamamı zaten değişeceğinden ara adımın değeri yok.
- Motoru olduğu gibi alıp düzeltmek: 200 satır, hatalı ve test yok; yeniden yazmak daha hızlı.
