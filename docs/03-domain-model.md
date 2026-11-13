# 03 · Alan modeli ve motor

`Karar` (ADR-0004)

Motor `:core:engine` içinde, tipler `:core:model` içinde yaşar. İkisi de platform ve UI bilmez. Her şey **deterministiktir**: aynı `seed` aynı bulmacayı üretir (günlük bulmaca buna dayanır).

## Temel tipler

```mermaid
classDiagram
  direction LR
  class Position {
    +Int index
    +Int row
    +Int col
    +Int box
    +peers() Set~Position~
  }
  class Grid {
    -IntArray cells
    +get(Position) Int
    +with(Position, Int) Grid
    +isEmpty(Position) Boolean
    +filledCount() Int
    +toText() String
  }
  class NotesGrid {
    -IntArray masks
    +has(Position, Int) Boolean
    +toggle(Position, Int) NotesGrid
    +clear(Position) NotesGrid
    +removeFromPeers(Position, Int) NotesGrid
  }
  class Difficulty {
    <<enumeration>>
    EASY
    MEDIUM
    HARD
    EXPERT
    MASTER
  }
  class Puzzle {
    +Long seed
    +Difficulty difficulty
    +Grid givens
    +Grid solution
    +Rating rating
  }
  class Rating {
    +Int score
    +Technique hardest
    +Map~Technique,Int~ usage
  }
  Puzzle --> Grid
  Puzzle --> Difficulty
  Puzzle --> Rating
```

- `Position` bir `value class`; `index` 0..80. Eski projedeki `satır*10+sütun` kodlaması terk edildi.
- `Grid` 81 elemanlı `IntArray` sarmalar, 0 = boş. Immutable; `with` yeni kopya döner (81 int kopyalamak ucuzdur).
- `NotesGrid` her hücre için 9 bitlik maske (bit n = rakam n+1).
- `Grid.toText()` 81 karakterlik `"530070000600195000..."` dizesi; veritabanı ve test fixture'ları bu biçimi kullanır.

## Motor bileşenleri

```mermaid
classDiagram
  class SudokuSolver {
    +solve(Grid) Grid
    +countSolutions(Grid, limit) Int
    +candidates(Grid) CandidateGrid
  }
  class SolutionBuilder {
    +build(Random) Grid
  }
  class Digger {
    +dig(Grid solution, DifficultyProfile, Random) Grid
  }
  class DifficultyGrader {
    +grade(Grid givens) Rating
  }
  class PuzzleGenerator {
    +generate(Difficulty, Long seed) Puzzle
  }
  class HintEngine {
    +nextHint(Grid cells, NotesGrid, Grid solution) Hint
  }
  class Hint {
    +Technique technique
    +Position target
    +Int digit
    +Set~Position~ highlights
    +String explanationKey
  }
  class Validator {
    +conflicts(Grid) Set~Position~
    +isSolved(Grid cells, Grid solution) Boolean
  }
  class Technique {
    <<enumeration>>
    NAKED_SINGLE
    HIDDEN_SINGLE
    NAKED_PAIR
    HIDDEN_PAIR
    POINTING_PAIR
    BOX_LINE_REDUCTION
    NAKED_TRIPLE
    X_WING
    Y_WING
    SWORDFISH
    XY_CHAIN
    BACKTRACK
  }
  PuzzleGenerator --> SolutionBuilder
  PuzzleGenerator --> Digger
  PuzzleGenerator --> DifficultyGrader
  Digger --> SudokuSolver
  DifficultyGrader --> Technique
  HintEngine --> DifficultyGrader
  Hint --> Technique
```

### SudokuSolver

Bit maskeli geri izleme (satır/sütun/kutu başına 9 bitlik kullanılmış-rakam maskesi, en az adaylı hücre önce). `countSolutions(limit = 2)` teklik denetimi için ikinci çözümü bulur bulmaz durur. Hedef: tipik bulmacada < 1 ms, en kötü durumda < 20 ms (JVM).

### SolutionBuilder

Boş tahtaya rastgele sıralı adaylarla geri izleme; her zaman geçerli tam bir çözüm üretir. `Random` dışarıdan gelir (determinizm).

### Digger

1. 81 hücre 180° dönme simetrisiyle 41 çifte ayrılır; çiftler `Random` ile karıştırılır.
2. Her çift için: değerleri kaldır, `countSolutions(limit = 2) == 1` ise kaldırma kalıcı, değilse geri koy.
3. Profilin hedef ipucu aralığına inince dur.

| Zorluk | Hedef ipucu (given) | Derece bandı (grader) |
|---|---|---|
| EASY | 38-45 | yalnızca singles |
| MEDIUM | 32-37 | + naked/hidden pair, pointing |
| HARD | 28-31 | + box-line, naked triple |
| EXPERT | 25-27 | + X-Wing, Y-Wing |
| MASTER | 22-25 | + Swordfish, XY-Chain veya backtrack gerektirir |

İpucu sayısı tek başına zorluk belirlemez; son söz grader'ındır.

### DifficultyGrader

İnsan stratejisi çözücüsü: teknikleri kolaydan zora sırayla dener, uygulanan her tekniği sayar, ilerleme durunca bir sonraki tekniğe geçer. Çıktı `Rating(score, hardest, usage)`. `score` her tekniğin ağırlığı × kullanım sayısı. Band dışına düşen bulmaca atılır; `PuzzleGenerator` alt-seed türeterek yeniden dener (üst sınır 40 deneme; aşılırsa bir alt bandın bulmacası döner ve bu durum loglanır).

### HintEngine

`DifficultyGrader` ile aynı teknik sırasını kullanır: mevcut tahtada uygulanabilen **ilk** tekniği bulur ve `Hint` döner. Hint iki aşamalıdır (UI kararı): önce teknik adı ve vurgulanan hücreler, oyuncu isterse rakam. Hiçbir teknik uygulanamıyorsa (oyuncu yanlış not bırakmışsa) `BACKTRACK` ile çözümden doğrudan hücre verir ve bunu söyler.

### Validator

- `conflicts`: aynı satır/sütun/kutuda aynı rakam olan hücreler (kural ihlali).
- `isSolved`: tüm hücreler dolu ve çözümle eşit.
- Hata tanımı (ayar): **çözüme göre** (varsayılan, mağaza standardı) veya **yalnızca kural çakışması**.

## Oyun durumu ve reducer

```mermaid
classDiagram
  class GameState {
    +Puzzle puzzle
    +Grid cells
    +NotesGrid notes
    +Position selected
    +InputMode inputMode
    +List~Move~ history
    +List~Move~ future
    +Int mistakes
    +Int hintsUsed
    +Int elapsedSeconds
    +GameStatus status
    +Set~Position~ conflicts
    +Set~Position~ wrongCells
  }
  class GameStatus {
    <<enumeration>>
    PLAYING
    PAUSED
    WON
    FAILED
  }
  class InputMode {
    <<enumeration>>
    DIGIT
    NOTE
  }
  class Move {
    +Position position
    +CellSnapshot before
    +CellSnapshot after
    +MoveKind kind
  }
  class CellSnapshot {
    +Int value
    +Int notesMask
  }
  class GameRules {
    +Int mistakeLimit
    +Int hintLimit
    +Boolean autoRemoveNotes
    +MistakePolicy mistakePolicy
  }
  class GameAction {
    <<sealed>>
  }
  class GameReducer {
    +reduce(GameState, GameAction, GameRules) GameState
  }
  GameState --> GameStatus
  GameState --> InputMode
  GameState --> Move
  Move --> CellSnapshot
  GameReducer ..> GameAction
  GameReducer ..> GameRules
```

`GameAction` alt tipleri: `Select(position)`, `Input(digit)`, `ToggleNote(digit)`, `Erase`, `Undo`, `Redo`, `ApplyHint(hint)`, `ToggleInputMode`, `Tick`, `Pause`, `Resume`, `Restart`.

Reducer kuralları:

- Verilen (given) hücreye giriş yok sayılır.
- `Input(d)`: hücre `d` ise temizler (toggle); değilse yazar, `autoRemoveNotes` açıksa komşu notlardan `d` silinir (bu da aynı `Move` içine kaydedilir, tek geri alma ile döner).
- Hata: `mistakePolicy == SOLUTION` ise `cells[p] != solution[p]` olduğunda `mistakes++`, hücre `wrongCells`'e girer; limit dolunca `FAILED`.
- `Undo` / `Redo`: `history` ve `future` yığınları; yeni hamle `future`'ı temizler.
- `ApplyHint`: hücreyi çözümle doldurur, `hintsUsed++`, `Move(kind = HINT)` olarak geçmişe girer.
- Son hücre dolup `isSolved` sağlanınca `WON`; sayaç durur.
- `Tick` yalnızca `PLAYING`'de `elapsedSeconds++`.

## Skor

`ScoreCalculator.score(state)`; yalnızca sonuç ekranında ve istatistikte gösterilir, oyun sırasında dikkat dağıtmaz.

```
base(difficulty)      EASY 500, MEDIUM 1000, HARD 1800, EXPERT 3000, MASTER 5000
timeBonus             max(0, base - elapsedSeconds * k(difficulty))
mistakePenalty        100 * mistakes
hintPenalty           150 * hintsUsed
score = base + timeBonus - mistakePenalty - hintPenalty   (min 0)
```

## Sıra diyagramları

### Yeni oyun

```mermaid
sequenceDiagram
  participant Home as HomeScreen
  participant VM as GameViewModel
  participant Pool as PuzzlePool
  participant Gen as PuzzleGenerator
  participant Repo as GameRepository
  Home->>VM: start(difficulty)
  VM->>Pool: take(difficulty)
  alt havuzda hazır
    Pool-->>VM: Puzzle
  else boş
    Pool->>Gen: generate(difficulty, seed)
    Gen-->>Pool: Puzzle
    Pool-->>VM: Puzzle
  end
  Pool-)Gen: generate(difficulty, nextSeed) (arka plan, havuzu doldur)
  VM->>Repo: createSavedGame(puzzle)
  Repo-->>VM: gameId
  VM-->>Home: UiState(PLAYING)
```

### Hamle

```mermaid
sequenceDiagram
  participant UI as GameScreen
  participant VM as GameViewModel
  participant R as GameReducer
  participant Repo as GameRepository
  UI->>VM: onEvent(DigitPressed(5))
  VM->>R: reduce(state, Input(5), rules)
  R-->>VM: newState
  VM-->>UI: UiState (vurgu, çakışma, hata sayacı)
  VM-)Repo: save(newState) (debounce 500 ms)
  opt newState.status == WON
    VM->>Repo: finish(gameId, outcome, score)
    VM-->>UI: UiEffect.ShowResult
  end
```

### İpucu

```mermaid
sequenceDiagram
  participant UI as GameScreen
  participant VM as GameViewModel
  participant H as HintEngine
  participant R as GameReducer
  UI->>VM: onEvent(HintPressed)
  VM->>H: nextHint(cells, notes, solution)
  H-->>VM: Hint(technique, target, highlights)
  VM-->>UI: UiState(hintPreview = teknik + vurgular)
  UI->>VM: onEvent(HintAccepted)
  VM->>R: reduce(state, ApplyHint(hint), rules)
  R-->>VM: newState (hintsUsed+1)
```

## Günlük bulmaca

`seed = hash64("pseudoku-daily", date.toString())`, zorluk haftanın gününe göre döner (Pzt EASY → Paz EXPERT, Cmt MASTER yerine HARD; tablo `DailyRules`'ta). Aynı tarih her cihazda aynı bulmacayı verir; sunucu gerekmez. Seri: ardışık günlerde `WON` sonuçlanan günlük bulmaca sayısı.

## Eski projeden taşınan bilgi

| Eski | Yeni | Not |
|---|---|---|
| 37 / 32 / 27 ipucu (EASY/MEDIUM/HARD) | Profil tablosu + grader | İpucu sayısı tek ölçüt değil |
| +50 / −100 puan | `ScoreCalculator` | Skor sonuçta, oyun içinde değil |
| 3 hata, 3 ipucu | `GameRules` varsayılanı | Ayarlardan değiştirilebilir |
| Tek haneli not | 9 bitlik `NotesGrid` | |
| Rastgele ipucu hücresi | Teknik tabanlı `HintEngine` | |
| Teklik kontrolü yok | `countSolutions(limit=2)` | |
