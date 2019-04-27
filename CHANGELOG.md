# Changelog

## Pre-releases

### 0.3.0

Changes:
- `PathParam.regex` now works.

### 0.2.0

Changes:
- TypeHandlers now use `InputStream`s and `OutputStream`s instead of paths.
- Changed the behavior of optionals, an empty value now maps to an empty
  file, not to a non-existing file.
- Added `itemize` method to `ListingTypeHandler`.
- Now JFM generates a virtual file system for reverse proxy.
- Various bug fixes and implementations of missing features.

### 0.1.0

Initial release.
