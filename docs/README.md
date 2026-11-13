# Pseudoku dokümanları

Bu klasör projenin tek doğruluk kaynağıdır. Kod bu dokümanlara göre yazılır; dokümanla kod ayrışırsa önce doküman güncellenir (ADR ile), sonra kod.

## Okuma sırası

1. [01 Vizyon ve kapsam](01-vision-and-scope.md), ne yapıyoruz, ne yapmıyoruz
2. [02 Mimari](02-architecture.md), modüller ve kurallar
3. [03 Alan modeli ve motor](03-domain-model.md), oyunun kalbi
4. [04 Veri modeli](04-data-model.md)
5. [05 Arayüz ve navigasyon](05-ui-and-navigation.md)
6. [09 Arayüz tasarım yönü](09-ui-design-direction.md)
7. [06 Test ve kalite](06-testing-and-quality.md)
8. [07 CI/CD ve sürüm](07-ci-cd-and-release.md)
9. [08 Yol haritası](08-roadmap.md)
10. [ADR'ler](adr/README.md), kararların gerekçeleri

## Konvansiyonlar

- Diyagramlar **Mermaid** ile yazılır; GitHub bunları doğrudan çizer. Harici araç gerekmez.
- Kod tanımlayıcıları (sınıf, modül, tablo adları) İngilizce; açıklama metni Türkçe.
- Her mimari karar bir ADR'dir: `docs/adr/NNNN-kisa-baslik.md`. Karar değişirse eski ADR "superseded" işaretlenir, yenisi yazılır; eski silinmez.
- Dokümanlardaki sürüm numaraları **minimum**dur; gerçek sürümler `gradle/libs.versions.toml` içinde sabitlenir.

## Durum etiketleri

| Etiket | Anlam |
|---|---|
| `Karar` | Verildi, ADR'si var, uygulanıyor |
| `Taslak` | Yön belli, ayrıntı kod yazılırken netleşecek |
| `Açık` | Kullanıcı kararı bekliyor |
