# Katkı rehberi

## Dallar ve commit'ler

- `main` korumalıdır; her değişiklik PR ile girer.
- Dal adları: `feat/...`, `fix/...`, `docs/...`, `chore/...`, `refactor/...`, `test/...`.
- Commit mesajları Conventional Commits: `feat(engine): add hidden single technique`.
- Bir PR tek konudur; 400 satırı geçen PR bölünür.

## Bir PR ne zaman biter

[docs/06-testing-and-quality.md](docs/06-testing-and-quality.md) içindeki tanım geçerlidir: CI yeşil, test var, TR/EN metin, erişilebilirlik, gerekiyorsa ADR.

## Mimari kurallar

[docs/02-architecture.md](docs/02-architecture.md) bağımlılık kuralları bağlayıcıdır. Kural değiştirmek için önce ADR açılır.

## Yerel geliştirme

Komutlar Faz 0 ile eklenecek (`./gradlew check`, `./gradlew :androidApp:installDebug`).
