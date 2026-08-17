#!/usr/bin/env python3
"""Composite the Robolectric captures into docs/assets/ marketing images.

Inputs are the PNGs written by ShowcaseScreenshotTest, so the pipeline is:

    ./gradlew :components:testAndroidHostTest --tests "*ShowcaseScreenshotTest*"
    python docs/gen-marketing-assets.py

Outputs: the README hero (light/dark side by side, plus its animated toggle), the Material 3
comparison, and the 1280x640 GitHub social preview. Labels are set in the real brand font
(Public Sans, bundled in :core) rather than a system fallback, so the assets match the library
they advertise.

Requires Pillow.
"""
import sys
from pathlib import Path

from PIL import Image, ImageDraw, ImageFont

repo = Path(sys.argv[1]) if len(sys.argv) > 1 else Path(__file__).resolve().parent.parent
SRC = repo / "components" / "build" / "screenshots"
OUT = repo / "docs" / "assets"
FONT_DIR = repo / "core" / "src" / "commonMain" / "composeResources" / "font"

CANVAS = (250, 249, 245)   # #FAF9F5
INK = (20, 20, 19)         # #141413
MUTED = (122, 114, 105)
# The panels sit on a warmer, darker field than the cream canvas: FormaUI's own background IS
# #FAF9F5, so matching the two would leave the FormaUI panel edgeless next to a visibly bounded
# Material 3 one — an artefact of the backdrop, not a real difference between the libraries.
BACKDROP = (232, 228, 220)
HAIRLINE = (214, 208, 198)

OUT.mkdir(parents=True, exist_ok=True)


def font(weight: str, size: int) -> ImageFont.FreeTypeFont:
    return ImageFont.truetype(str(FONT_DIR / f"public_sans_{weight}.ttf"), size)


def load(name: str) -> Image.Image:
    path = SRC / f"{name}.png"
    if not path.exists():
        sys.exit(
            f"missing {path}\n"
            'run: ./gradlew :components:testAndroidHostTest --tests "*ShowcaseScreenshotTest*"'
        )
    return Image.open(path).convert("RGB")


def rounded(img: Image.Image, radius: int, border: bool = True) -> Image.Image:
    """Round the corners of a screenshot so it reads as a device surface, not a raw crop."""
    mask = Image.new("L", img.size, 0)
    ImageDraw.Draw(mask).rounded_rectangle([0, 0, img.width - 1, img.height - 1], radius, fill=255)
    out = Image.new("RGBA", img.size, (0, 0, 0, 0))
    out.paste(img, (0, 0), mask)
    if border:
        # A hairline keeps each panel bounded whatever its own background happens to be.
        ImageDraw.Draw(out).rounded_rectangle(
            [0, 0, img.width - 1, img.height - 1], radius, outline=HAIRLINE + (255,), width=2
        )
    return out


def wrap(draw, text, fnt, max_width):
    """Greedy word wrap measured against the actual font, not an assumed character width."""
    lines, line = [], ""
    for word in text.split():
        trial = f"{line} {word}".strip()
        if draw.textlength(trial, font=fnt) <= max_width:
            line = trial
        else:
            if line:
                lines.append(line)
            line = word
    if line:
        lines.append(line)
    return lines


def side_by_side(images, labels, out_name, pad=48, gap=40, label_h=64, bg=BACKDROP):
    """Lay panels out in a row on a common baseline, each captioned above."""
    height = max(im.height for im in images)
    panels = []
    for im in images:
        if im.height < height:
            # Pad shorter panels with their own background colour so the fill is invisible.
            filler = Image.new("RGB", (im.width, height),
                               im.getpixel((im.width // 2, im.height - 2)))
            filler.paste(im, (0, 0))
            im = filler
        panels.append(rounded(im, 28))

    width = pad * 2 + sum(p.width for p in panels) + gap * (len(panels) - 1)
    canvas = Image.new("RGB", (width, pad * 2 + label_h + height), bg)
    draw = ImageDraw.Draw(canvas)
    label_font = font("semibold", 30)

    x = pad
    for panel, label in zip(panels, labels):
        if label:
            draw.text((x + panel.width // 2, pad + label_h // 2), label,
                      font=label_font, fill=INK, anchor="mm")
        canvas.paste(panel, (x, pad + label_h), panel)
        x += panel.width + gap

    path = OUT / out_name
    canvas.save(str(path), "PNG", optimize=True)
    print(f"{out_name:32} {canvas.width}x{canvas.height}  {path.stat().st_size // 1024}KB")


def social_preview(light, dark):
    """GitHub's social card: 1280x640, solid background, safe margins."""
    width, height = 1280, 640
    margin, panel_x = 72, 700
    text_width = panel_x - margin - 48   # keep the copy clear of the phone crops

    canvas = Image.new("RGB", (width, height), CANVAS)
    draw = ImageDraw.Draw(canvas)

    # Panels first, so the text layer is never overprinted by them.
    crop_h = 470
    for img, x, y in ((dark, 940, 96), (light, panel_x, 140)):
        scaled = img.resize((int(img.width * crop_h / img.height), crop_h), Image.LANCZOS)
        panel = rounded(scaled, 22)
        canvas.paste(panel, (x, y), panel)

    y = 170
    draw.text((margin, y), "FormaUI", font=font("bold", 92), fill=INK)
    y += 126

    tagline = font("semibold", 36)
    for line in wrap(draw, "Material 3, with better defaults.", tagline, text_width):
        draw.text((margin, y), line, font=tagline, fill=INK)
        y += 46
    y += 12

    body = font("regular", 27)
    blurb = "40 opinionated Jetpack Compose components for Android."
    for line in wrap(draw, blurb, body, text_width):
        draw.text((margin, y), line, font=body, fill=MUTED)
        y += 36
    y += 14

    draw.text((margin, y), "formaui.dev", font=font("semibold", 28), fill=(204, 120, 92))

    path = OUT / "formaui-social-preview.png"
    canvas.save(str(path), "PNG", optimize=True)
    size_kb = path.stat().st_size // 1024
    assert size_kb < 1024, f"social preview is {size_kb}KB — GitHub's limit is 1MB"
    print(f"{'formaui-social-preview.png':32} {width}x{height}  {size_kb}KB")


def toggle_gif(light, dark, width=420, fade_frames=6, colors=128):
    """Light <-> dark crossfade — the theme-toggle motion the README copy describes.

    Sized for a README hero that autoplays on every page view: 420px wide with a 6-frame fade
    and a 128-colour palette lands under 400KB, where the full-width 255-colour version was
    650KB. The crossfade reads as a theme toggle at this length; more frames mostly add bytes.
    """
    def scale(im):
        return im.resize((width, int(im.height * width / im.width)), Image.LANCZOS)

    a, b = scale(light), scale(dark)
    frames, durations = [], []

    for start, end in ((a, b), (b, a)):
        frames.append(start)
        durations.append(1400)          # hold long enough to actually read the theme
        for i in range(1, fade_frames + 1):
            frames.append(Image.blend(start, end, i / (fade_frames + 1)))
            durations.append(60)

    # Per-frame adaptive palettes; a single global palette bands the chart's area-fill gradient.
    paletted = [f.convert("P", palette=Image.ADAPTIVE, colors=colors) for f in frames]
    path = OUT / "formaui-showcase.gif"
    paletted[0].save(str(path), save_all=True, append_images=paletted[1:],
                     duration=durations, loop=0, optimize=True, disposal=2)
    print(f"{'formaui-showcase.gif':32} {width}x{paletted[0].height}  "
          f"{path.stat().st_size // 1024}KB  {len(frames)} frames")


light_shot, dark_shot = load("showcase-light"), load("showcase-dark")
material_shot, forma_shot = load("compare-material3"), load("compare-formaui")

side_by_side([light_shot, dark_shot], ["Light", "Dark"], "formaui-showcase.png")
side_by_side([material_shot, forma_shot], ["Stock Material 3", "FormaUI"],
             "formaui-vs-material3.png")
social_preview(light_shot, dark_shot)
toggle_gif(light_shot, dark_shot)
