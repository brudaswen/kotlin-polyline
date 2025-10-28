# Kotlin Multiplatform Polyline Encoding

[![Maven Central](https://img.shields.io/maven-central/v/de.brudaswen.kotlin.polyline/kotlin-polyline?style=flat-square)](https://search.maven.org/artifact/de.brudaswen.kotlin.polyline/kotlin-polyline)
[![Snapshot](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fde%2Fbrudaswen%2Fkotlin%2Fpolyline%2Fkotlin-polyline%2Fmaven-metadata.xml&style=flat-square&label=snapshot)](https://oss.sonatype.org/#nexus-search;gav~de.brudaswen.kotlin.polyline~kotlin-polyline~~~)
[![CI Status](https://img.shields.io/github/actions/workflow/status/brudaswen/kotlin-polyline/ci-main.yml?style=flat-square)](https://github.com/brudaswen/kotlin-polyline/actions/workflows/ci-main.yml)
[![Codecov](https://img.shields.io/codecov/c/github/brudaswen/kotlin-polyline?style=flat-square)](https://codecov.io/gh/brudaswen/kotlin-polyline)
[![License](https://img.shields.io/github/license/brudaswen/kotlin-polyline?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0)

Kotlin Multiplatform implementation for
[Google Polyline encoding algorithm](http://code.google.com/apis/maps/documentation/polylinealgorithm.html)
to convert `Polyline` to/from `String`.

## Gradle Dependencies

```toml
[versions]
kotlin-polyline = "1.0.0"

[libraries]
kotlin-polyline = { group = "de.brudaswen.kotlin.polyline", name = "kotlin-polyline", version.ref = "kotlin-polyline" }
```

```kotlin
// From Gradle version catalog
implementation(libs.kotlin.polyline)

// Manually
implementation("de.brudaswen.kotlin.polyline:kotlin-polyline:1.0.0")
```

## Usage

The library provides default implementations of `Polyline` and `Coordinate` used for encoding and
decoding.

```kotlin
val polyline = Polyline(
    coordinates = listOf(
        Coordinate(0.0, 0.0),
        Coordinate(1.0, 1.0),
    ),
)

val encoded = PolylineEncoding.encode(polyline)
val decoded = PolylineEncoding.decode(encoded)
```

### Custom `Polyline` or `Coordinate` implementation

Existing classes could implement the `Polyline` and/or `Coordinate` interface to directly encode
and decode from your existing classes.

```kotlin
data class CustomPolyline(
    val points: List<CustomCoordinate>,
) : Polyline {
    override val coordinates: List<CustomCoordinate>
        get() = points
}

data class CustomCoordinate(
    val latitude: Float,
    val longitude: Float,
) : Coordinate {
    override val lat: Double
        get() = latitude.toDouble()

    override val lon: Double
        get() = longitude.toDouble()
}


val polyline = CustomPolyline(
    points = listOf(
        CustomCoordinate(0.0f, 0.0f),
        CustomCoordinate(1.0f, 1.0f),
    ),
)

val encoded = PolylineEncoding.encode(polyline)
val decoded = PolylineEncoding.decode(
    polyline = encoded,
    polylineFactory = { coordinates ->
        CustomPolyline(
            points = coordinates.toList(),
        )
    },
    coordinateFactory = { lat, lon ->
        CustomCoordinate(
            latitude = lat.toFloat(),
            longitude = lon.toFloat(),
        )
    },
)
```

## License

```
MIT License

Copyright (c) 2025

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
