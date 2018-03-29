# Change Log
All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).

## Version [3.0.0] - (TBD)
- Add: environments CRUD (Create/Read/Update/Delete).
- Add: environment aware entries, assets, locales, content types and editor_interfaces.
- Add: Object variant for Update/Delete of resources.
- Break: Remove `space id` from [`ModuleLocales`|`ModuleRoles`|`ModuleSpaceMemberships`].delete.
- Break: Use JDK 1.8 (LTS)
- Break: remove queries for locales
- Polish: Test resource generators to strip credentials and to include environments.
- Polish: Annotation placement in tests.

## Version [2.2.3] - 2018-03-08
- Fix: Parsing error in error response

## Version [2.2.2] - 2018-02-19
- Fix: make creating auto save webhooks work.
- Fix: add details to contentful error.
- Fix: Do not send default field in locales.

## Version [2.2.1] - 2017-10-18
- Fix: add version number of sdk on buildtime, not on runtime.
- Remove: Now obsolete mockito testing dependency.

## Version [2.2.0] - 2017-08-30
- Add: [Locales module](https://www.contentful.com/developers/docs/references/content-management-api/#/reference/locales).
- Fix: ImageMeta contains data now.

## Version [2.1.0]- 2017-07-19
- Add: Custom user agent header.
- Add: [`/users/me` module](https://www.contentful.com/developers/docs/references/content-management-api/#/reference/users).
- Add: [Personal Access Token](https://www.contentful.com/developers/docs/references/content-management-api/#/reference/personal-access-tokens).
- Add: [SpaceMembership module](https://www.contentful.com/developers/docs/references/content-management-api/#/reference/space-memberships).
- Add: [Roles module](https://www.contentful.com/developers/docs/references/content-management-api/#/reference/roles).
- Add: [Organizations module](https://www.contentful.com/developers/docs/references/content-management-api/#/reference/organizations).
- Add: [Api Keys module](https://www.contentful.com/developers/docs/references/content-management-api/#/reference/api-keys).
- Add: [Editor Interfaces](https://www.contentful.com/developers/docs/references/content-management-api/#/reference/editor-interface).
- Add: [UI Extensions](https://www.contentful.com/developers/docs/references/content-management-api/#/reference/ui-extensions).
- Add: [Snapshots](https://www.contentful.com/developers/docs/references/content-management-api/#/reference/snapshots/entry-snapshots-collection).
- Add: Query parameter for all collection endpoints. (skip and limit your responses)
- Add: Ability to track rate limit headers.
- Fix: Return HTTP Code of no content methods.
- Fix: Assets: Do not send `url` and `details` if `uploadUrl` or `uploadFrom` is changed.
- Fix: Serialize Links correctly.
- Polishing: Update dependencies
  - retrofit 2.3.0 (was 2.0.1)
  - commonsio 2.5 (was 2.4)
  - junit: 4.12 (was 4.11)
  - okhttp: 3.8.1 (was 3.2.0)
  - rxjava: 2.1.1 (was 1.0.13)
  - mockito: 2.8.47 (was 1.10.8)

## Version [2.0.0] - 2017-04-19
- New: [`Upload` module](https://www.contentful.com/developers/docs/references/content-management-api/#/reference/uploads).
- New: Add convenience `default call factory` method in client builder.
- New: `Fluid interface` for getting localized fields on entries and assets.
- New: `Replace hash maps` with accessors and fields in entries and assets.
- New: `Add rate limit headers` once limit is hit.
- New: Add `defaultLocale` accessors on CMASpace.
- Changed: Order of arguments on `CMAEntry.setField` takes the `key` first, _then_ the `locale` and lastly the value.
- Changed: Version numbers and version counters are of `Integer` type, not `Double`.
- Changed: CMASpace: `getSpaceId` returns the `id` of the current space (aka `getId`), not the id of the space the space belongs to.
- Removed: `StatefulResource` is now part of the `CMAResource`.

## Version [1.3.0] - 2016-08-17
- New: [`Webhooks` module](https://www.contentful.com/developers/docs/references/content-management-api/#/reference/webhooks).
- Fix: Send correct `Content-Type`-header on PUT/POST HTTP requests.
- Changed: Report response body from failed HTTP requests.

## Version [1.2.0] - 2016-07-21
- New: Add `omitted` field for content type fields.

## Version [1.1.1] - 2016-05-09
- Fix: Redirect input query on async `ModuleSpaces` fetches.
- Changed: Use `Map` instead of `HashMap` in query maps.

## Version [1.1.0] - 2016-05-09
- New: Add query maps for `fetchAll` and have `limit` default to `100`.

## Version [1.0.2] - 2016-05-02
- Fixed: QueryMap was set to null in Assets and Entries module.

## Version [1.0.1] - 2016-04-18
- Fixed: java.lang.IllegalArgumentException: Query map was null in ModuleContentTypes

## Version [1.0.0] - 2016-04-15
- Change: Use kotlin 1.0.1
- Change: Use okhttp 3.2.0
- Change: Use retrofit 2.0.1

## Version [0.10.0] - 2015-12-23
- New: Add localized field to FieldTypeAdapter
- Change: Use kotlin-1.0.0-beta-3595 for unit tests

## Version [0.9.10] - 2015-11-24
- Fixed: Correctly serialize Entry links.

## Version [0.9.9] - 2015-07-16
- Fixed: Support posting included resources as links.

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

[unreleased]: https://github.com/contentful/contentful-management.java/compare/cma-sdk-2.2.3...HEAD
[2.2.3]: https://github.com/contentful/contentful-management.java/compare/cma-sdk-2.2.2...cma-sdk-2.2.3
[2.2.2]: https://github.com/contentful/contentful-management.java/compare/cma-sdk-2.2.1...cma-sdk-2.2.2
[2.2.1]: https://github.com/contentful/contentful-management.java/compare/cma-sdk-2.2.0...cma-sdk-2.2.1
[2.2.0]: https://github.com/contentful/contentful-management.java/compare/cma-sdk-2.1.0...cma-sdk-2.2.0
[2.1.0]: https://github.com/contentful/contentful-management.java/compare/cma-sdk-2.0.0...cma-sdk-2.1.0
[2.0.0]: https://github.com/contentful/contentful-management.java/compare/cma-sdk-1.3.0...cma-sdk-2.0.0
[1.3.0]: https://github.com/contentful/contentful-management.java/compare/cma-sdk-1.2.0...cma-sdk-1.3.0
[1.2.0]: https://github.com/contentful/contentful-management.java/compare/cma-sdk-1.1.1...cma-sdk-1.2.0
[1.1.1]: https://github.com/contentful/contentful-management.java/compare/cma-sdk-1.1.0...cma-sdk-1.1.1
[1.1.0]: https://github.com/contentful/contentful-management.java/compare/cma-sdk-1.0.2...cma-sdk-1.1.0
[1.0.2]: https://github.com/contentful/contentful-management.java/compare/cma-sdk-1.0.1...cma-sdk-1.0.2
[1.0.1]: https://github.com/contentful/contentful-management.java/compare/cma-sdk-1.0.0...cma-sdk-1.0.1
[1.0.0]: https://github.com/contentful/contentful-management.java/compare/cma-sdk-0.10.0...cma-sdk-1.0.0
[0.10.0]: https://github.com/contentful/contentful-management.java/compare/cma-sdk-0.9.10...cma-sdk-0.10.0
[0.9.10]: https://github.com/contentful/contentful-management.java/compare/cma-sdk-0.9.9...cma-sdk-0.9.10
[0.9.9]: https://github.com/contentful/contentful-management.java/compare/cma-sdk-0.9.8...cma-sdk-0.9.9
[0.9.8]: https://github.com/contentful/contentful-management.java/compare/0.9.8...cma-sdk-0.9.8
[0.9.7]: https://github.com/contentful/contentful-management.java/compare/0.9.6...0.9.7
[0.9.6]: https://github.com/contentful/contentful-management.java/compare/0.9.5...0.9.6
[0.9.5]: https://github.com/contentful/contentful-management.java/compare/0.9.4...0.9.5
[0.9.4]: https://github.com/contentful/contentful-management.java/compare/0.9.3...0.9.4
[0.9.3]: https://github.com/contentful/contentful-management.java/compare/0.9.2...0.9.3
[0.9.2]: https://github.com/contentful/contentful-management.java/compare/0.9.1...0.9.2
[0.9.1]: https://github.com/contentful/contentful-management.java/compare/v0.9.0...0.9.1
