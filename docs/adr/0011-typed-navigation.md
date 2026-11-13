# 0011 · JetBrains Navigation Compose ve tipli route'lar

- **Durum:** Kabul
- **Tarih:** 2026-11-13

## Bağlam

Beş ekran, bir overlay, iki sheet. Feature'lar birbirini görmemeli.

## Karar

`org.jetbrains.androidx.navigation:navigation-compose` ile `@Serializable` route sınıfları; graf `composeApp` içinde; her feature `NavGraphBuilder` genişletmesi sunar ve navigasyon isteğini callback ile dışarı verir.

## Sonuçlar

- String route ve argüman ayrıştırma yok; derleme zamanı tip güvenliği.
- Overlay ve sheet'ler navigasyon hedefi değil, ekran içi durumdur (`GameUiState.result`), geri tuşu ViewModel'de ele alınır.

## Değerlendirilen alternatifler

- Decompose, Voyager: güçlü ama ek kavram; androidx navigasyon KMP'de stabil ve ekip biliyor.
