import sys
from pathlib import Path

PNG_COLOR_TYPES = {
    0: 'Grayscale',
    2: 'Truecolor (RGB)',
    3: 'Indexed-color (palette)',
    4: 'Grayscale with alpha',
    6: 'Truecolor with alpha (RGBA)'
}

p = Path(sys.argv[1])
if not p.exists():
    print('MISSING')
    sys.exit(2)

b = p.read_bytes()
# PNG signature 8 bytes
if b[:8] != b"\x89PNG\r\n\x1a\n":
    print('NOT_PNG')
    sys.exit(3)
# IHDR chunk starts at byte 8: length(4) 'IHDR'(4) then data
# IHDR data: width(4), height(4), bitdepth(1), colortype(1), comp(1), filt(1), interlace(1)
ihdr_index = 8 + 4  # skip length
if b[ihdr_index:ihdr_index+4] != b'IHDR':
    # try search
    idx = b.find(b'IHDR')
    if idx == -1:
        print('NO_IHDR')
        sys.exit(4)
    ihdr_index = idx

data_index = ihdr_index + 4
# bit depth at offset 8 within IHDR data (after width(4)+height(4))
bitdepth = b[data_index + 8]
colortype = b[data_index + 9]
print('bitdepth', bitdepth)
print('colortype', colortype, PNG_COLOR_TYPES.get(colortype, 'UNKNOWN'))
print('has_alpha', colortype in (4,6))
print('width', int.from_bytes(b[data_index:data_index+4], 'big'))
print('height', int.from_bytes(b[data_index+4:data_index+8], 'big'))

