# Change Log
All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).

## Version [1.0.0][unreleased] - (in development)

## Version [0.9.8] - 2015-06-10
- New: Add support for search queries via fetchAll() methods.
- Changed: PUT methods now have an empty request body to avoid failure with OkHttp 2.4.
- Fixed: Add missing description attribute to `CMAContentType`.

## Version [0.9.7] - 2015-02-23
- Changed: RxJava v1.0.5
- Changed: Retrofit v1.9.0
- Changed: `update()` for resource that has no version now throws an exception.
- Fixed: NPE for entry with no fields.

## Version [0.9.6] - 2015-02-23
- Fixed: Add missing `CMAField` attributes.
- Changed: Use custom serializer for `CMAField` instances.

## Version [0.9.5] - 2015-02-20
- Fixed: Add missing `required` attribute for `CMAContentType`.

## Version [0.9.4] - 2015-02-20
- Changed: RxJava v1.0.3
- Fixed: Content type update no longer overrides validations.

## Version [0.9.3] - 2014-11-30
- New: Support custom callback executors.

## Version [0.9.2] - 2014-11-27
- Fixed: RxJava now defers to IO thread.

## Version [0.9.1] - 2014-11-24
- Fixed: Add missing headers when creating entries.

## Version 0.9.0 - 2014-11-18
Initial release.

[unreleased]: https://github.com/contentful/contentful-management.java/compare/0.9.7...HEAD
[0.9.7]: https://github.com/contentful/contentful-management.java/compare/0.9.6...0.9.7
[0.9.6]: https://github.com/contentful/contentful-management.java/compare/0.9.5...0.9.6
[0.9.5]: https://github.com/contentful/contentful-management.java/compare/0.9.4...0.9.5
[0.9.4]: https://github.com/contentful/contentful-management.java/compare/0.9.3...0.9.4
[0.9.3]: https://github.com/contentful/contentful-management.java/compare/0.9.2...0.9.3
[0.9.2]: https://github.com/contentful/contentful-management.java/compare/0.9.1...0.9.2
[0.9.1]: https://github.com/contentful/contentful-management.java/compare/v0.9.0...0.9.1
