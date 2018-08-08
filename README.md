<p align="center">
  <img src="assets/feature_graphic.png" alt="Contentful Java SDK"/><br/>
  <a href="https://www.contentful.com/slack/">
    <img src="https://img.shields.io/badge/-Join%20Community%20Slack-2AB27B.svg?logo=slack&maxAge=31557600" alt="Join Contentful Community Slack">
  </a>
  &nbsp;
  <a href="https://www.contentfulcommunity.com/">
    <img src="https://img.shields.io/badge/-Join%20Community%20Forum-3AB2E6.svg?logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA1MiA1OSI+CiAgPHBhdGggZmlsbD0iI0Y4RTQxOCIgZD0iTTE4IDQxYTE2IDE2IDAgMCAxIDAtMjMgNiA2IDAgMCAwLTktOSAyOSAyOSAwIDAgMCAwIDQxIDYgNiAwIDEgMCA5LTkiIG1hc2s9InVybCgjYikiLz4KICA8cGF0aCBmaWxsPSIjNTZBRUQyIiBkPSJNMTggMThhMTYgMTYgMCAwIDEgMjMgMCA2IDYgMCAxIDAgOS05QTI5IDI5IDAgMCAwIDkgOWE2IDYgMCAwIDAgOSA5Ii8+CiAgPHBhdGggZmlsbD0iI0UwNTM0RSIgZD0iTTQxIDQxYTE2IDE2IDAgMCAxLTIzIDAgNiA2IDAgMSAwLTkgOSAyOSAyOSAwIDAgMCA0MSAwIDYgNiAwIDAgMC05LTkiLz4KICA8cGF0aCBmaWxsPSIjMUQ3OEE0IiBkPSJNMTggMThhNiA2IDAgMSAxLTktOSA2IDYgMCAwIDEgOSA5Ii8+CiAgPHBhdGggZmlsbD0iI0JFNDMzQiIgZD0iTTE4IDUwYTYgNiAwIDEgMS05LTkgNiA2IDAgMCAxIDkgOSIvPgo8L3N2Zz4K&maxAge=31557600" alt="Join Contentful Community Forum"/>
  </a>
</p>

contentful-management.java - Contentful Java Management SDK
===="======================================================

[![Build Status](https://travis-ci.org/contentful/contentful-management.java.svg)](https://travis-ci.org/contentful/contentful-management.java/builds#) 
[![codecov](https://codecov.io/gh/contentful/contentful-management.java/branch/master/graph/badge.svg)](https://codecov.io/gh/contentful/contentful-management.java)

> Java SDK for [Content Management API](https://www.contentful.com/developers/docs/references/content-management-api/). It helps you to easily editing and creating your content stored in Contentful with your Java applications.


What is Contentful?
-------------------

[Contentful](https://www.contentful.com) provides a content infrastructure for digital teams to power content in websites, apps, and devices. Unlike a CMS, Contentful was built to integrate with the modern software stack. It offers a central hub for structured content, powerful management and delivery APIs, and a customizable web app that enable developers and content creators to ship digital products faster.

<details open>
  <summary>Table of contents</summary>
  <!-- TOC -->

- [Setup](#setup)
  - [Snapshots](#snapshots)
  - [Default HTTP Client](#default-http-client)
  - [Proguard](#proguard)
- [Usage](#usage)
  - [Client](#client)
  - [Modules](#modules)
  - [Environment Configuration](#environment-configuration)
- [Documentation](#documentation)
- [Licence](#licence)
  <!-- /TOC -->
</details>

Setup
=====

Install the dependency via 

* _Maven_
```xml
<dependency>
  <groupId>com.contentful.java</groupId>
  <artifactId>cma-sdk</artifactId>
  <version>3.1.0</version>
</dependency>
```

* _Gradle_
```groovy
compile 'com.contentful.java:cma-sdk:3.1.0'
```

The SDK requires at minimum Java 7 or Android 5.

Snapshots
---------

Snapshots of the development version are available through

* [Sonatype's `snapshots` repository][snap]

```groovy
maven { url 'https://oss.sonatype.org/content/repositories/snapshots' }
compile 'com.contentful.java:cma-sdk:3.1.0-SNAPSHOT'
```

* [jitpack.io][jitpack]:

```groovy
maven { url 'https://jitpack.io' }
compile 'com.github.contentful:contentful.java:cma-sdk-3.1.0-SNAPSHOT'
```

Default HTTP Client
-------------------

The SDK uses [Retrofit][2] as a REST client, which detects [OkHttp][3] in your classpath and uses it if it's available, otherwise falls back to the default `HttpURLConnection`.

The recommended approach would be to add [OkHttp][3] as a dependency to your project, but that is completely optional.

You can also specify a custom client to be used, refer to the [official documentation][4] for instructions.

ProGuard
--------

The following lines are the start of a `proguard` file used for minifying Android distributions.

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

Client
------

The CMA Client instance is created like so:

```java
final CMAClient client =
    new CMAClient
        .Builder()
        .setAccessToken("<access_token>")
        .build();
```

The _AccessToken_ can easily be obtained through the [management API documentation](https://www.contentful.com/developers/docs/references/authentication/#getting-a-personal-access-token).

Modules
-------

A client can perform various operations on different types of _Resources_ (such as _Assets_, _Content Types_, _Entries_, _Spaces_, etc). Every type of Resource is represented by a _Module_ in the `CMAClient` class, for example:

```java
client.spaces() // returns the Spaces Module
client.entries() // returns the Entries Module
client.assets() // returns the Assets Module
…
```

Each Module contains a set of methods which can be used to perform various operations on the specified Resource type. Every method has a corresponding asynchronous extension which can be accessed through the `async()` method of the Module, for example the following synchronous call

```java
final CMAArray<CMASpace> array =
    client
        .spaces()
        .fetchAll(
	    "space_id",
	    "environment_id"
	);
```

can be expressed by this asynchronous call:

```java
final CMAArray<CMASpace> array =
    client
        .spaces()
        .async()
        .fetchAll(
	    "space_id",
	    "environment_id",
	    new CMACallback<CMAArray<CMASpace>>() {
  @Override protected void onSuccess(CMAArray<CMASpace> result) {
    // success
  }

  @Override protected void onFailure(RetrofitError retrofitError) {
    // failure
  }
});
```

> Note: The default `CMACallback` has an empty `onFailure()` implementation. If failures are of interest, overriding this methods is immanent.

> Note: [Our CMA documentation][docs] offers more code snippets for all Modules.


Environment Configuration
-------------------------

Instead of repeating the _Space_ and _Environment _ids with every call like so

```java
final CMAArray<CMAEntry> array =
    client
        .entries()
        .fetchAll(
            "space_id",
            "environment_id",
        );
```

the client can be configured to use always use the same values:

```java
final CMAClient client =
    new CMAClient
        .Builder()
        .setAccessToken("<access_token>")
        .setSpaceId("<space_id>")
        .setEnvironmentId("<environment_id>")
        .build();
```

This changes the parameters Modules are using: 

```java
final CMAArray<CMAEntry> array =
    client
        .entries()
        .fetchAll();
```

*Words of warning*

The Modules `apiKeys`, `environments`, `roles`, `spaceMemberships`, `uiExtensions`, `uploads`, and `webhooks`, do not support Environments different to `master`. If the configuration explained is used with these Modules, they will throw an `exception`. Creation of a new client without specifying an Environment id is needed and repeating of the ids in the call is needed:

```
final CMAArray<CMAApiKey> array = 
    client
        .apiKeys()
        .fetchAll(
	   "spaceid"
	);
```

Documentation
=============

[Javadoc reference documentation][4] is is available, but if you want to get more information, please refer to [our CMA documentation][docs]. In case of anything still being unclear [issues can be opened on this github repository](../../issues)).

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
