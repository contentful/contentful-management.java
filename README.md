Contentful Management SDK for Java
==================================

[![Build Status](https://travis-ci.org/contentful/contentful-management.java.svg)](https://travis-ci.org/contentful/contentful-management.java/builds#)
[![codecov](https://codecov.io/gh/contentful/contentful-management.java/branch/master/graph/badge.svg)](https://codecov.io/gh/contentful/contentful-management.java)

Java SDK for [Contentful's][1] Content Management API.

[Contentful][1] provides a content infrastructure for digital teams to power content in websites, apps, and devices. Unlike a CMS, Contentful was built to integrate with the modern software stack. It offers a central hub for structured content, powerful management and delivery APIs, and a customizable web app that enable developers and content creators to ship digital products faster.

Setup
=====

Grab via Maven:
```xml
<dependency>
  <groupId>com.contentful.java</groupId>
  <artifactId>cma-sdk</artifactId>
  <version>2.2.2</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.contentful.java:cma-sdk:2.2.2'
```

The SDK requires at minimum Java 6 or Android 2.3.

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].

### Default Client

The SDK uses [Retrofit][2] under the hood as a REST client, which detects [OkHttp][3] in your classpath and uses it if it's available, otherwise falls back to the default `HttpURLConnection`.

The recommended approach would be to add [OkHttp][3] as a dependency to your project, but that is completely optional.

You can also specify a custom client to be used, refer to the [official documentation][4] for instructions.

### ProGuard

```
-keepattributes Signature
-dontwarn rx.**
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keep class com.contentful.java.cma.** { *; }
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe { *; }
```

Usage
=====

### Client

In the beginning the API client instance should be created:

```java
final CMAClient client =
    new CMAClient
        .Builder()
        .setAccessToken("<access_token>")
        .build();
```

The access token can easily be obtained through the [management API documentation](https://www.contentful.com/developers/docs/references/authentication/#getting-a-personal-access-token).

A client can perform various operations on different types of resources (Assets, Content Types, Entries, Spaces, etc). Every type of resource is represented by a module in the `CMAClient` class, for example:

```java
client.spaces() // returns the Spaces module
client.entries() // returns the Entries module
client.assets() // returns the Assets module
…
```

Each module contains a set of methods which can be used to perform various operations on the specified resource type. Every method has a corresponding asynchronous extension which can be accessed through the `async()` method of the module, for example:

#### Retrieving all spaces (synchronously):

```java
final CMAArray<CMASpace> array =
    client
        .spaces()
        .fetchAll();
```

#### or asynchronously:

```java
final CMAArray<CMASpace> array =
    client
        .spaces()
        .async()
        .fetchAll(new CMACallback<CMAArray<CMASpace>>() {
  @Override protected void onSuccess(CMAArray<CMASpace> result) {
    // success
  }

  @Override protected void onFailure(RetrofitError retrofitError) {
    // failure
  }
});
```

Please note that the default `CMACallback` has an empty `onFailure()` implementation, which makes it optional to override it.

If you want to see the SDK in action, please check with [our CMA documentation][docs], which offers snippets for all the relevant and supported parts, just select an endpoint and select the language java.

Documentation
=============

[Javadoc reference documentation][4] is is available, but if you want to get more information, please refer to [our CMA documentation][docs]. In case of anything still unclear, please feel free to [open issues on this github repository](../../issues)).

License
=======

Copyright (c) 2018 Contentful GmbH. See [LICENSE.txt][5] for further details.


 [1]: https://www.contentful.com
 [2]: https://square.github.io/retrofit
 [3]: https://square.github.io/okhttp
 [4]: https://contentful.github.io/contentful-management.java
 [5]: LICENSE.txt
 [snap]: https://oss.sonatype.org/content/repositories/snapshots/
 [docs]: https://www.contentful.com/developers/docs/references/content-management-api/
