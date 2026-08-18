#!/usr/bin/env python3
"""Rasterise the FormaUI mark into :sample's legacy launcher mipmaps.

    python docs/gen-launcher-icons.py

API 26+ gets the vector adaptive icon (sample/src/main/res/mipmap-anydpi-v26); these PNGs exist
only for API 24-25, which :sample still supports (minSdk 24). Geometry mirrors
docs/assets/formaui-mark.svg: three stadium bars on a 120-unit canvas, group-rotated -9 degrees
about the centre, translated by (8, 8).

Requires Pillow.
"""
import sys
from pathlib import Path

from PIL import Image, ImageDraw

repo = Path(sys.argv[1]) if len(sys.argv) > 1 else Path(__file__).resolve().parent.parent
RES = repo / "sample" / "src" / "main" / "res"

CANVAS = "#FAF9F5"   # cream canvas, matches FormaColorScheme light background
RUST = "#C84219"
AMBER = "#C9922E"
INK = "#26201D"

# (x, y, w, h) in the SVG's post-translate(8,8) space; rx is always min(w,h)/2 (stadium).
BARS = [
    ((0, 0, 26, 104), RUST),
    ((34, 0, 70, 26), AMBER),
    ((34, 39, 48, 26), INK),
]

SS = 8          # supersample factor, downsampled with LANCZOS at the end
MARK_FRAC = 0.62  # mark's share of the icon's width, leaving a comfortable margin

DENSITIES = {
    "mipmap-mdpi": 48,
    "mipmap-hdpi": 72,
    "mipmap-xhdpi": 96,
    "mipmap-xxhdpi": 144,
    "mipmap-xxxhdpi": 192,
}


def render_mark(size_px: int) -> Image.Image:
    """The three bars, rotated -9 degrees, on a transparent square of `size_px`."""
    big = size_px * SS
    layer = Image.new("RGBA", (big, big), (0, 0, 0, 0))
    draw = ImageDraw.Draw(layer)

    # The bars occupy 0..104 in SVG units; map that onto MARK_FRAC of the icon, centred.
    span = 104.0
    scale = (big * MARK_FRAC) / span
    offset = (big - span * scale) / 2.0

    for (x, y, w, h), color in BARS:
        x0 = offset + x * scale
        y0 = offset + y * scale
        x1 = offset + (x + w) * scale
        y1 = offset + (y + h) * scale
        draw.rounded_rectangle([x0, y0, x1, y1], radius=min(x1 - x0, y1 - y0) / 2, fill=color)

    # Expand-free rotation about the centre, matching the SVG's rotate(-9 60 60).
    return layer.rotate(-9, resample=Image.BICUBIC, expand=False)


def build(size_px: int, round_icon: bool) -> Image.Image:
    big = size_px * SS
    base = Image.new("RGBA", (big, big), (0, 0, 0, 0))
    bg = Image.new("RGBA", (big, big), CANVAS)

    if round_icon:
        mask = Image.new("L", (big, big), 0)
        ImageDraw.Draw(mask).ellipse([0, 0, big - 1, big - 1], fill=255)
    else:
        # Legacy square icons still want rounded corners; ~18% is the platform convention.
        mask = Image.new("L", (big, big), 0)
        ImageDraw.Draw(mask).rounded_rectangle(
            [0, 0, big - 1, big - 1], radius=int(big * 0.18), fill=255
        )

    base.paste(bg, (0, 0), mask)
    mark = render_mark(size_px)
    base.alpha_composite(mark)
    # Re-apply the mask so the rotated mark cannot bleed outside the icon silhouette.
    base.putalpha(Image.composite(base.getchannel("A"), Image.new("L", (big, big), 0), mask))
    return base.resize((size_px, size_px), Image.LANCZOS)


written = []
for folder, px in DENSITIES.items():
    target = RES / folder
    target.mkdir(parents=True, exist_ok=True)
    for name, is_round in (("ic_launcher", False), ("ic_launcher_round", True)):
        path = target / f"{name}.png"
        build(px, is_round).save(str(path), "PNG", optimize=True)
        written.append((path, px, path.stat().st_size))

for path, px, size in written:
    print(f"{px:>3}px  {size:>6}B  {path.relative_to(RES)}")
print(f"\n{len(written)} PNGs written")
