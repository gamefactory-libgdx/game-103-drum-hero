# Drum Hero — Figma AI Design Brief

---

## 1. Art Style & Color Palette

**Art Style:** Modern flat design with subtle depth through gradients, soft shadows, and layered geometric shapes. The aesthetic is clean and contemporary, avoiding skeuomorphism while maintaining tactile button feedback through highlight and shadow states. Rounded corners (12–16px) dominate UI elements for a friendly, approachable feel that contrasts with the high-energy rhythm gameplay.

**Primary Color Palette:**
- **Primary Brand Color:** #FF6B35 (vibrant orange-red, used for CTAs and drum solo highlights)
- **Dark Base:** #1A1A2E (near-black, used for backgrounds and text contrast)
- **Accent Neutral:** #FFFFFF (pure white for text, dividers, and clarity)
- **Secondary Accent:** #00D4FF (electric cyan, used for jazz/syncopation visual cues and energy effects)

**Accent Colors:**
- **Rock Style Accent:** #8B0000 (deep crimson for rock band UI elements and streak indicators)
- **Jazz Style Accent:** #2E8B57 (forest green for jazz complexity and syncopation feedback)
- **Metal Style Accent:** #DC143C (bright crimson for metal intensity and danger feedback)

**Font Mood & Weight:** Use geometric sans-serif typeface (e.g., Inter, Rubik, or Outfit). Headlines in **Bold (700)** weight for impact and readability at small mobile sizes; body text in **Regular (400)** for clarity. All text is left-aligned unless centered for emphasis (e.g., game titles, scores). Font sizes scale from 12px (fine print) to 48px (screen titles) with consistent line-height of 1.4x for legibility.

---

## 2. App Icon — icon_512.png (512×512px)

**Canvas & Background:**
A radial gradient background transitioning from #FF6B35 (center) to #1A1A2E (outer edge), creating a dynamic energy core. The gradient is subtle but noticeable, giving the icon depth and a sense of rhythm pulsing outward.

**Central Symbol:**
Four vertical drum pads arranged in a 2×2 grid, centered within a 380×380px zone. Each pad is a rounded square (80×80px with 12px corner radius) separated by 20px gaps. Pad colors (left to right, top to bottom): **#8B0000** (rock red), **#2E8B57** (jazz green), **#00D4FF** (electric cyan), **#DC143C** (metal crimson). Each pad has a white downward-pointing arrow or tap symbol (20px icon, centered) to communicate the core mechanic of tapping in rhythm. The four pads together form an instantly recognizable "drum kit" silhouette.

**Depth & Effects:**
- Inner shadow on each drum pad (offset 0px, blur 8px, color #000000 at 20% opacity) to create a recessed, tappable feel.
- Subtle white glow around the outer edge of the entire icon (blur 12px, color #FFFFFF at 15% opacity) for a polished, premium appearance.
- A thin white ring (2px stroke, #FFFFFF at 40% opacity) around the perimeter of each pad to emphasize tappability.

**Overall Mood:**
High-energy, modern, and immediately communicates rhythm gameplay. The four distinct colors signal variety (three band styles plus visual richness), while the downward arrows hint at the scrolling mechanic. No text or branding text appears anywhere on the icon.

---

## 3. UI Screens (480×854 portrait)

### MainMenuScreen — main_menu.png (480×854)

**Background:** A dark gradient transitioning from #1A1A2E (top) to #0F0F1E (bottom), with a subtle animated noise texture overlay (not visible in static image, but described for context). No image background; purely color-based for performance.

**Header:**
"DRUM HERO" title is centered at the top (y ≈ 80px), rendered in **Bold 48px** white (#FFFFFF). Subtitle "Rhythm Awaits" appears 20px below in **Regular 20px** #00D4FF (cyan accent), reinforcing the energy and excitement.

**Primary Content Area (center):**
Three large branded buttons stacked vertically, each 320×64px with 16px corner radius, separated by 20px vertical spacing. Buttons are:
1. **"START GAME"** — background #FF6B35, text white, centered text in **Bold 24px**
2. **"LEADERBOARD"** — background #2E8B57, text white, centered text in **Bold 18px**
3. **"STATS"** — background #00D4FF, text #1A1A2E (dark), centered text in **Bold 18px**

All three buttons positioned at y ≈ 320px (centered vertically in the play area).

**Bottom Navigation (footer):**
Two small icon-buttons at the bottom (y ≈ 780px), each 60×60px with 12px corner radius, separated by spacing to left/right edges (20px margin):
- **Left button (Settings):** labeled "⚙ SETTINGS" in **Bold 12px** white on background #1A1A2E with #FF6B35 border (3px)
- **Right button (Tutorial):** labeled "? TUTORIAL" in **Bold 12px** white on background #1A1A2E with #00D4FF border (3px)

**Visual Polish:**
Subtle animated pulse on the "START GAME" button (not captured in static image) to guide player attention. A thin horizontal divider line (1px, #FF6B35 at 30% opacity) separates the header from the button area.

---

### BandSelectScreen — band_select.png (480×854)

**Background:** Same dark gradient as MainMenuScreen (#1A1A2E to #0F0F1E).

**Header:**
"SELECT YOUR BAND" title centered at top (y ≈ 60px), **Bold 32px** white (#FFFFFF). Subtitle "Choose your difficulty" below in **Regular 14px** #00D4FF.

**Band Selection Cards (center):**
Three large card buttons, each 360×140px with 16px corner radius, stacked vertically with 24px spacing, centered horizontally. Cards are positioned at y ≈ 200px. Each card has:
- **Left side (40% width):** Background color matching band (Rock: #8B0000, Jazz: #2E8B57, Metal: #DC143C). Centered band icon (drum stick, musical note, flame silhouette — simple flat shapes, 64×64px, white).
- **Right side (60% width):** Background #1A1A2E with slight transparency/overlay. Band name in **Bold 24px** white at top (e.g., "ROCK"). Difficulty label in **Regular 12px** #00D4FF below (e.g., "Low–Mid Density"). Short descriptor in **Regular 11px** light gray (#CCCCCC) at bottom (e.g., "Steady beats & power chords").

**Navigation:**
"BACK" button at bottom-left (y ≈ 790px), 100×50px, background #1A1A2E with white border (2px), text **Bold 14px** white, centered.

---

### RockScreen — game_rock.png (480×854)

**Background:** A dark gradient from #1A1A2E (top) to #0F0F1E (bottom) with a subtle rock-themed texture overlay (diagonal lines or subtle grain, very faint). The visual feel is grounded and steady, matching the "steady beat" of rock.

**Top HUD Bar (y = 0–50px):**
- **Left:** Score display "SCORE: 0" in **Bold 18px** white (#FFFFFF), positioned top-left with 12px margin.
- **Center:** Streak counter "STREAK: 0" in **Bold 18px** #FF6B35, centered.
- **Right:** Combo multiplier indicator "2x DRUM SOLO" (only visible when active) in **Bold 16px** #FF6B35 with glow effect, positioned top-right with 12px margin.

**Gameplay Area (y = 50–650px):**
Four vertical drum pad lanes (columns) span the full width (480px), each lane 120px wide. Lanes are separated by thin dividers (1px, #FF6B35 at 20% opacity). Each lane has a distinct color:
- **Lane 1 (left):** Background gradient from #2A1A1A to #1A1A2E
- **Lane 2:** Background gradient from #1A2A1A to #1A1A2E
- **Lane 3:** Background gradient from #1A1A3A to #1A1A2E
- **Lane 4 (right):** Background gradient from #2A1A2A to #1A1A2E

Descending note markers (circles or rectangles, 80×20px, corner radius 6px) scroll downward through the lanes. Colors match lane assignments (lane 1 = red #FF6B35, lane 2 = green #2E8B57, lane 3 = cyan #00D4FF, lane 4 = crimson #DC143C). Each note has a faint white outline (1px) for clarity.

**Impact Zone (y = 620–660px):**
A subtle horizontal band across all four lanes indicating the "perfect hit" zone. Background color is semi-transparent white (#FFFFFF at 5% opacity) with a thin border (2px, #FF6B35) to mark the timing window clearly.

**Bottom HUD Bar (y = 650–854px):**
- **Tap Target Areas:** Four rounded-square buttons (100×100px, corner radius 16px) positioned at the bottom of each lane, matching lane colors but with darker backgrounds (e.g., lane 1 = #8B0000 with inner shadow). Each button shows a simple tap indicator icon (downward chevron or "TAP" text in small **Bold 12px** white).
- **Pause Button:** Small circular button (48×48px, corner radius 50%) in the top-right corner of the gameplay area (y ≈ 55px, x ≈ 420px), background #1A1A2E with #FF6B35 border, containing a pause icon (||). Text "PAUSE" in **Regular 10px** white below.
- **Crowd Reaction Indicator (optional visual):** Subtle emoji or icon (16×16px) above the impact zone showing crowd mood (happy face for streak, sad for break). Updates dynamically.

**Visual Polish:**
Falling notes have a slight glow (blur 4px, color matching lane) for visibility. Perfect hits trigger a white flash bloom (full-screen brief opacity spike) combined with an upward "PERFECT" text pop (green #2E8B57, **Bold 20px**, fades upward).

---

### JazzScreen — game_jazz.png (480×854)

**Background:** A dark gradient from #1A1A2E (top) to #0F0F1E (bottom) with a subtle syncopated pattern overlay (diagonal dashes or a jazz-club haze effect, very faint, perhaps a semi-transparent overlay at 5% opacity showing geometric shapes suggesting improvisation).

**Top HUD Bar (y = 0–50px):**
Identical to RockScreen (Score, Streak, Drum Solo indicators in same positions and colors).

**Gameplay Area (y = 50–650px):**
Four vertical drum pad lanes with same structure as RockScreen. However, Jazz notes are more frequent and densely packed, creating a visually busier scroll. Note colors:
- **Lane 1 (left):** #2E8B57 (green)
- **Lane 2:** #00D4FF (cyan)
- **Lane 3:** #FF6B35 (orange-red)
- **Lane 4 (right):** #8B0000 (deep red)

Lanes have subtle gradient backgrounds reflecting jazz complexity (slightly more vibrant than Rock):
- Lane 1: gradient from #1A3A2A to #1A1A2E
- Lane 2: gradient from #1A2A3A to #1A1A2E
- Lane 3: gradient from #3A1A1A to #1A1A2E
- Lane 4: gradient from #2A1A1A to #1A1A2E

**Impact Zone (y = 620–660px):**
Same as RockScreen but with a green (#2E8B57) border accent to reinforce the jazz theme.

**Bottom HUD Bar (y = 650–854px):**
Four tap target buttons (100×100px) matching lane colors but with darker, more saturated backgrounds (e.g., lane 1 = #2E8B57 with inner shadow, lane 2 = #00B8D4, etc.). Layout and pause button identical to RockScreen.

**Visual Polish:**
Jazz notes descend faster and pack tighter, requiring quicker reflexes. Perfect hits trigger a green flash bloom (#2E8B57) with "GROOVE!" text pop in **Bold 20px** green, fading upward.

---

### MetalScreen — game_metal.png (480×854)

**Background:** A dark gradient from #1A1A2E (top) to #000000 (bottom, pure black for intensity) with an aggressive texture overlay (subtle metallic grain or red static crackle at 3% opacity, suggesting raw power and danger).

**Top HUD Bar (y = 0–50px):**
Identical to RockScreen and JazzScreen in layout. However, Drum Solo indicator text is more aggressive: "2x DRUM SOLO" in **Bold 16px** #DC143C (bright crimson) with a red glow effect (blur 6px, opacity 40%).

**Gameplay Area (y = 50–650px):**
Four vertical drum pad lanes with highly aggressive visual design. Metal notes are extremely dense and frequent, sometimes appearing in rapid double-tap patterns (two notes in quick succession). Note colors:
- **Lane 1 (left):** #DC143C (bright crimson)
- **Lane 2:** #FF6B35 (orange-red)
- **Lane 3:** #8B0000 (deep red)
- **Lane 4 (right):** #FF1493 (hot pink/magenta for extreme intensity)

Lanes have darker, more saturated gradients with red/crimson undertones:
- Lane 1: gradient from #3A1A1A to #1A0000
- Lane 2: gradient from #3A2A1A to #1A0000
- Lane 3: gradient from #2A1A1A to #1A0000
- Lane 4: gradient from #3A1A2A to #1A0000

**Impact Zone (y = 620–660px):**
Bright crimson border (#DC143C) with semi-transparent red background overlay (#DC143C at 8% opacity) for visual intensity. Pulses subtly with game tempo.

**Bottom HUD Bar (y = 650–854px):**
Four tap target buttons (100×100px) with highly saturated, aggressive colors matching lane assignments (e.g., lane 1 = #DC143C with glow, lane 2 = #FF6B35 with glow). Each button has a subtle outer glow (blur 8px, color matching lane at 30% opacity) to emphasize intensity. Pause button styled with crimson border (#DC143C) instead of orange.

**Visual Polish:**
Metal notes descend at the fastest speed with double-bass patterns (rapid successive notes). Perfect hits trigger a red/pink flash bloom (full-screen brief opacity spike in #DC143C) with "SHRED!" text pop in **Bold 24px** #FF1493, fading upward with a slight shake effect (subtle screen vibration for intensity).

---

### GameOverScreen — game_over.png (480×854)

**Background:** A dark gradient from #1A1A2E (top) to #0F0F1E (bottom), with a semi-transparent overlay (black at 40% opacity) darkening the entire screen for focus.

**Main Content Area (centered):**
- **Title:** "GAME OVER" in **Bold 48px** white (#FFFFFF), positioned at y ≈ 80px, centered.
- **Band Name:** The selected band (e.g., "ROCK") displayed below title in **Regular 20px** #FF6B35, centered.

**Score Card (y ≈ 180px):**
A rounded card (360×320px, corner radius 16px) with background #1A1A2E and border (2px, #FF6B35). Inside:
- **Final Score:** "FINAL SCORE" label in **Regular 12px** #00D4FF above. Large score number in **Bold 56px** white (e.g., "45,320"), centered.
- **Stats Grid (below score):**
  - Row 1: "Max Streak:" (left, **Regular 12px** white) | value (right, **Bold 16px** #FF6B35)
  - Row 2: "Accuracy:" (left, **Regular 12px** white) | value (right, **Bold 16px** #00D4FF)
  - Row 3: "Perfect Hits:" (left, **Regular 12px** white) | value (right, **Bold 16px** #2E8B57)
  - Each row separated by thin divider (1px, #FF6B35 at 20% opacity).

**Action Buttons (y ≈ 520px):**
Two large buttons stacked, each 320×64px with corner radius 16px, separated by 16px:
- **"PLAY AGAIN"** — background #FF6B35, text white **Bold 20px**, centered
- **"BACK TO MENU"** — background #00D4FF, text #1A1A2E **Bold 20px**, centered

**Visual Polish:**
The score card slides in from the bottom with a subtle entrance animation. The final score number pulses briefly when the screen appears. A thin horizontal accent line (#FF6B35 at 30% opacity) separates the title area from the score card.

---

### LeaderboardScreen — leaderboard.png (480×854)

**Background:** Same dark gradient as MainMenuScreen (#1A1A2E to #0F0F1E).

**Header:**
"LEADERBOARD" title centered at top (y ≈ 60px), **Bold 32px** white (#FFFFFF). Subtitle "Local High Scores" in **Regular 14px** #00D4FF below.

**Filter Buttons (y ≈ 110px):**
Three small button tabs (100×40px, corner radius 8px), arranged horizontally, centered, separated by 12px:
- **"ALL TIME"** — active state: background #FF6B35, text white; inactive: background #1A1A2E, border #FF6B35, text #FF6B35
- **"ROCK"** — active: background #8B0000, text white; inactive: background #1A1A2E, border #8B0000, text #8B0000
- **"JAZZ"** — active: background #2E8B57, text white; inactive: background #1A1A2E, border #2E8B57, text #2E8B57
- **"METAL"** — active: background #DC143C, text white; inactive: background #1A1A2E, border #DC143C, text #DC143C

Text in each tab: **Bold 11px**.

**Leaderboard List (y ≈ 160–750px):**
Scrollable list of 10 rank entries. Each entry is a row (440×60px) with background #1A1A2E and border-bottom (1px, #FF6B35 at 20% opacity). Layout per row:
- **Left (12px margin):** Rank number (1–10) in **Bold 20px** white, followed by a medal icon for top 3 (gold/silver/bronze emoji or icon, 16×16px).
- **Center-left:** Player name in **Regular 14px** white (simulated as "Player X" for placeholder).
- **Right (12px margin):** Score in **Bold 16px** #FF6B35, right-aligned.
- **Top-right corner (8px margin):** Band icon (small colored dot, 8×8px) indicating which band the score was achieved on (red = Rock, green = Jazz, crimson = Metal).

**Top 3 Visual Emphasis:**
Rank 1 entry has a subtle gold glow (blur 4px, #FFD700 at 20% opacity).
Rank 2 entry has a subtle silver glow (blur 4px, #C0C0C0 at 20% opacity).
Rank 3 entry has a subtle bronze glow (blur 4px, #CD7F32 at 20% opacity).

**Navigation:**
"BACK" button at bottom-left (y ≈ 790px), 100×50px, background #1A1A2E with white border (2px), text **Bold 14px** white, centered.

---

### StatsScreen — stats.png (480×854)

**Background:** Same dark gradient as MainMenuScreen (#1A1A2E to #0F0F1E).

**Header:**
"YOUR STATS" title centered at top (y ≈ 60px), **Bold 32px** white (#FFFFFF). Subtitle "Lifetime Performance" in **Regular 14px** #00D4FF below.

**Stats Cards Grid (y ≈ 130px):**
Four large stat cards arranged in a 2×2 grid, each 200×120px with corner radius 12px, separated by 16px spacing, centered within the screen. Each card has background #1A1A2E and border (2px) matching the stat's accent color. Layout per card:
- **Card 1 (top-left):** Border #FF6B35. Stat label "Total Plays" in **Regular 12px** #FF6B35 at top. Large stat value (e.g., "42") in **Bold 32px** white, centered. Smaller unit text (e.g., "games") in **Regular 10px** light gray below.
- **Card 2 (top-right):** Border #00D4FF. Stat label "Total Score" in **Regular 12px** #00D4FF. Large stat value (e.g., "1,234,567") in **Bold 28px** white, centered. Unit: "points" in **Regular 10px** light gray.
- **Card 3 (bottom-left):** Border #2E8B57. Stat label "Best Streak" in **Regular 12px** #2E8B57. Large stat value (e.g., "87") in **Bold 32px** white. Unit: "hits" in **Regular 10px** light gray.
- **Card 4 (bottom-right):** Border #DC143C. Stat label "Accuracy" in **Regular 12px** #DC143C. Large stat value (e.g., "92.5") in **Bold 32px** white. Unit: "%" in **Regular 10px** light gray.

**Band Breakdown (y ≈ 400px):**
Subheading "BY BAND" in **Bold 14px** white (#FFFFFF), left-aligned with 16px margin.

Three horizontal stat rows (y ≈ 440–580px), each 440×70px, background #1A1A2E, border (2px) matching band color, corner radius 8px. Rows separated by 12px:
- **Rock Row:** Border #8B0000. Left side: band name "ROCK" in **Bold 14px** #8B0000. Middle section: stats in small text (**Regular 10px** white) — "Plays: X | Best Score: Y | Accuracy: Z%". Right side: small progress bar (60×8px, background #0F0F1E, fill #8B0000) showing relative mastery.
- **Jazz Row:** Border #2E8B57, text #2E8B57, progress bar fill #2E8B57.
- **Metal Row:** Border #DC143C, text #DC143C, progress bar fill #DC143C.

**Navigation:**
"BACK" button at bottom-left (y ≈ 790px), 100×50px, background #1A1A2E with white border (2px), text **Bold 14px** white, centered.

---

### SettingsScreen — settings.png (480×854)

**Background:** Same dark gradient as MainMenuScreen (#1A1A2E to #0F0F1E).

**Header:**
"SETTINGS" title centered at top (y ≈ 60px), **Bold 32px** white (#FFFFFF).

**Settings List (y ≈ 120–700px):**
Scrollable list of setting rows. Each row is 440×64px, background #1A1A2E, border-bottom (1px, #FF6B35 at 20% opacity). Layout per row:
- **Left (16px margin):** Setting label in **Regular 14px** white (e.g., "Music Volume").
- **Right (16px margin):** Control element:
  - **Sliders** (for volume, brightness): horizontal bar (140×6px, background #0F0F1E, fill #FF6B35), with circular draggable thumb (16×16px, background white). Value percentage displayed to the right in **Regular 10px** #FF6B35 (e.g., "75%").
  - **Toggles** (for vibration, color-blind mode): circular toggle switch (48×24px, background #0F0F1E when off, #FF6B35 when on), with sliding circle indicator (20×20px, white).

**Settings Included:**
1. **Music Volume** — slider, 0–100%
2. **SFX Volume** — slider, 0–100%
3. **Vibration** — toggle on/off
4. **Difficulty Preset** — dropdown showing "Easy | Normal | Hard" (simulated as a select row with text value displayed)
5. **Color-Blind Mode** — toggle on/off (when enabled, drum lane colors shift to a deuteranopia-friendly palette: blues, yellows, purples)

**Additional Info (y ≈ 710px):**
App version and build number displayed in **Regular 10px** light gray (#CCCCCC), centered (e.g., "Drum Hero v1.0.0 (Build 42)").

**Navigation:**
"BACK" button at bottom-left (y ≈ 790px), 100×50px, background #1A1A2E with white border (2px), text **Bold 14px** white, centered.

---

### TutorialScreen — tutorial.png (480×854)

**Background:** Same dark gradient as MainMenuScreen (#1A1A2E to #0F0F1E), with a subtle tutorial-themed overlay (large geometric shapes or faint circles, at 2% opacity, suggesting progression or learning).

**Header:**
"HOW TO PLAY" title centered at top (y ≈ 50px), **Bold 32px** white (#FFFFFF).

**Tutorial Carousel / Scrollable Content (y ≈ 110–750px):**
A vertically scrollable guide divided into 4 sections, each ~150px tall, separated by 12px:

**Section 1: The Mechanic**
- Subheading "Tap the Pads" in **Bold 16px** #FF6B35, left-aligned (16px margin).
- Instructional text "Four drum pads scroll downward. Tap each pad when the note reaches the timing line." in **Regular 12px** white, left-aligned, wrapped (16px margins).
- Visual: Simplified diagram showing four vertical lanes with a descending note and the impact zone highlighted (illustration, 360×60px, center-aligned). Colors: lanes in muted versions of their band colors, note in #FF6B35, impact zone in bright white.

**Section 2: Timing & Scoring**
- Subheading "Perfect, Good, Miss" in **Bold 16px** #00D4FF, left-aligned.
- Text: "Hit the note in the timing window for **Perfect** (2 points). Just outside = **Good** (1 point). Missed = no points." in **Regular 12px** white.
- Visual: Three small timeline diagrams (each 120×40px, center-aligned as a row) showing timing windows for Perfect (green zone, wide), Good (yellow zone, narrower), Miss (red zone).

**Section 3: Drum Solo Power**
- Subheading "Charge Your Power" in **Bold 16px** #2E8B57, left-aligned.
- Text: "Land 10 consecutive Perfect hits to activate **Drum Solo**. For 5 seconds, all points are doubled (2x)!" in **Regular 12px** white.
- Visual: A charging bar illustration (280×20px, center-aligned) showing 10 small circles (filled and empty) representing the charge meter, with a glowing effect on the filled ones.

**Section 4: Crowd Reactions**
- Subheading "Listen to the Crowd" in **Bold 16px** #DC143C, left-aligned.
- Text: "Build your streak and the crowd cheers! Break it, and they react with disappointment. Their energy affects your momentum." in **Regular 12px** white.
- Visual: Two small emoji faces (happy and sad, 32×32px each, center-aligned side-by-side) or simple icon illustrations representing crowd mood.

**Navigation:**
"GOT IT!" button at bottom (y ≈ 790px), 200×50px, background #FF6B35, text white **Bold 18px**, centered. Alternatively, "< PREV" (left, 80×50px) and "NEXT >" (right, 80×50px) if the tutorial is a paginated carousel instead of scrollable.

---

## 4. Export Checklist

- icon_512.png (512×512)
- main_menu.png (480×854)
- band_select.png (480×854)
- game_rock.png (480×854)
- game_jazz.png (480×854)
- game_metal.png (480×854)
- game_over.png (480×854)
- leaderboard.png (480×854)
- stats.png (480×854)
- settings.png (480×854)
- tutorial.png (480×854)

**Total Files: 11**

---

**End of Design Brief**
