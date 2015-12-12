# Scalpel-Util
Utility class for great [Scalpel][Scalpel] library allowing attach scalpel at runtime

[![Build Status](https://travis-ci.org/jraska/Scalpel-Util.svg?branch=master)](https://travis-ci.org/jraska/Scalpel-Util)
[![Sample](https://img.shields.io/badge/Download-Sample-blue.svg)](https://drive.google.com/file/d/0B0T1YjC17C-rS0JzRVl5U2RkcXM/view?usp=sharing)
[![License](https://img.shields.io/badge/license-Apache%202.0-green.svg) ](https://github.com/jraska/Scalpel-Util/blob/master/LICENSE)
[![Download](https://api.bintray.com/packages/jraska/maven/com.jraska%3Ascalpel-util/images/download.svg) ](https://bintray.com/jraska/maven/com.jraska%3Ascalpel-util/_latestVersion)
[![License](https://img.shields.io/badge/API-10+-green.svg) ]()

## Usage

```java
// Dynamically add scalpel layout to your activity
ScalpelFrameLayout scalpel = ScalpelUtil.wrapWithScalpel(activity);
```

Then you can remove it by three fast clicks on layout.

## Download

Grab via Gradle: 
```groovy
compile 'com.jraska:scalpel-util:1.0.0'
```

## License

    Copyright 2015 Josef Raska

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

  [Scalpel]: https://github.com/JakeWharton/scalpel
