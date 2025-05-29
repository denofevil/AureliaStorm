# AureliaStorm Community Changelog

## [2.0.0] - 2025-05-29

A huge thank you to the efforts done by Readock on Github and the work done to https://plugins.jetbrains.com/plugin/27000-aureliastorm-re
This release is a combination of the work done by Readock and the community contributions to AureliaStorm.

### Added

- Custom element / attribute recognition
- References gets resolved over <require from=""> tags
- Without require a class with matching @customElement will be taken (also works with name suffix)
- Developer Note: Having custom elements with the same name might not recognize correctly in some instances
- Require and import tag reference detection and navigation
- Detecting bindable HTML attributes and events
- Require and import tag support
- Custom element bindable property recognition (by @bindable)
- Suppress <template> and <require> element warnings
- Proper `repeat.for` detection and reference resolving
- Ignoring binding behaviors and value converts for code injection
- Suppressing of missing promise for aurelia js injected code
- `$this`, `$parent`, `$index`, `$event` support for code injections
- GoTo Declaration/definition (Default Alt+Ctrl+Shift+O)
- Custom element completion (ctrl+space)
- Property and custom attribute completion (ctrl+space)
- Description has now a gif showing some features
- Lifecycle method detection for all exported js classes
- Injection now enables as a default

### Fixed

- Reference detection getting called for files outside of aurelia

## [1.4.0] - 4/16/2025

- Update kotlin JVM plugin 1.x -> 2.x,
- Add in support for else custom attribute,
- Minimum IDE version updated from 2023.2 -> 2024.2 - This was causing the previous version not to upload due to the grade v2 update which is only supported in 2024.2 and beyond.,
- Adds support for 2025.1

## [1.3.3] - 4/9/2025
- Now uses the PackageJsonData class from com.intellij to determine dependencies which is much more performant than a direct PSI,
- Updates kotlin version,
- Updates to Gradle v2 version for intellij,
- Tons of prep work for the 2025.1 Release

## [1.2.1]

### Changed

- Support older versions

## [1.2.0]

### Added

- Support <let> element recognition
- Support the `else` attribute
- Adds support for `promise.bind` recognition

### Fixed

- Remove deprecated getDependencies Call
- Remove deprecated getBaseDir call

[Unreleased]: https://github.com/Readock/AureliaStormRe/compare/v2.3.1...HEAD

[2.3.1]: https://github.com/Readock/AureliaStormRe/compare/v2.3.0...v2.3.1

[2.3.0]: https://github.com/Readock/AureliaStormRe/compare/v2.2.0...v2.3.0

[2.2.0]: https://github.com/Readock/AureliaStormRe/compare/v2.1.0...v2.2.0

[2.1.0]: https://github.com/Readock/AureliaStormRe/compare/v2.0.1...v2.1.0

[2.0.1]: https://github.com/Readock/AureliaStormRe/compare/v2.0.0...v2.0.1

[2.0.0]: https://github.com/Readock/AureliaStormRe/compare/v1.2.1...v2.0.0

[1.2.1]: https://github.com/Readock/AureliaStormRe/compare/v1.2.0...v1.2.1

[1.2.0]: https://github.com/Readock/AureliaStormRe/commits/v1.2.0
