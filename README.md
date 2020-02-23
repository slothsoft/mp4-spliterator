# Example Eclipse Application

[![Build Status](https://travis-ci.org/slothsoft/example-eclipse-application.svg?branch=master)](https://travis-ci.org/slothsoft/example-eclipse-application)

This example project shows how to develop and build an Eclipse based application using Tycho. 

It shows how to:

- build a product with Tycho
- use a target platform **file** both inside Tycho and Eclipse
- create a structure that can be used on multiple operation system (e.g. my OS is Windows, but this project is build by [Travis](https://travis-ci.org/slothsoft/example-eclipse-application) using Linux)
- use plain Maven dependencies


**Content of this ReadMe:**

- [Maven Modules](#maven-modules)
    - [de.slothsoft.example.build](#deslothsoftexamplebuild)
    - [de.slothsoft.example](#deslothsoftexample)
    - [de.slothsoft.example.it](#deslothsoftexampleit)
    - [de.slothsoft.example.feature](#deslothsoftexamplefeature)
    - [de.slothsoft.example.product](#deslothsoftexampleproduct)
    - [maven-p2](#maven-p2)
- [Developer Guide](#developer-guide)
- [How to](#how-to)
- [To Do List](#to-do-list)
- [License](#license)


## Maven Modules

- **[de.slothsoft.example.build](#deslothsoftexamplebuild)** - parent of the reactor
    - **[de.slothsoft.example](#deslothsoftexample)** - the plug-in with the usable code
    - **[de.slothsoft.example.it](#deslothsoftexampleit)** - the integration tests for above plug-in
    - **[de.slothsoft.example.feature](#deslothsoftexamplefeature)** - a feature for a feature-based product
    - **[de.slothsoft.example.product](#deslothsoftexampleproduct)** - the product file to start and build an application
- **[maven-p2](#maven-p2)** - the P2 repository for Maven dependencies


### de.slothsoft.example.build
  
This project is the parent of the reactor and contains common resources and configurations.

- **_platform.target_** - all the dependencies in one handy file; can be used by both Tycho and Eclipse
- **_pom.xml_** - most of the Maven and Tycho configuration
- **_README.md_** - a landing page for the repository


### de.slothsoft.example

This project is the plug-in with the usable code. It just contains the Eclipse template for a new application and the template for a view.


### de.slothsoft.example.it

This project tests the project `de.slothsoft.example`. In this example, it's tested that the view `ExampleView` can be opened without problems.


### de.slothsoft.example.feature

This project contains a feature for a feature-based product.


### de.slothsoft.example.product

This project has the product file to start build an application from Eclipse and build it from Tycho.


### maven-p2

This project converts plain Maven dependencies into a P2 repository and is able to start and stop said repository. It can handle JARs with and without OSGi information in their _Manifest.MF_.

Check out the awesome [p2-maven-plugin](https://github.com/reficio/p2-maven-plugin) to learn more.



## Developer Guide

To start this project in your Eclipse, do:

1. Clone this repository
1. Call `mvn install -Pstart` on _maven-p2/pom.xml_ to start a P2 repository locally
1. To start the application from **Eclipse** do the following:
    1. Open the file _platform.target_ and click on _"Set as Active Target Platform"_
    1. _de.slothsoft.example.product/ExampleApp.product_ and click on _"1. Synchronize"_ and then _"2. Launch Eclipse Application"_
1. To build the application with **Tycho** 
     - Run `mvn clean install` on the repository root
     - Or in Eclipse right click on the _pom.xml_ and use _"Run as..."_ → _"Maven install"_
     - The resulting EXE file is in _de.slothsoft.example.product/target/products/de.slothsoft.example.product-&lt;time>-&lt;os>.zip_ and / or the folder next to the ZIP file
1. when finished, call `mvn package -Pstop` to stop the P2 repository

To use this example as a template for your Eclipse based application you need to do the following:

1. check out this project (or use the button "Use this template" in GitHub)
1. rename my IDs to yours
    - "de.slothsoft" to your company's domain (including packages)
    - "example" to your project's name (the same for capitalized "Example")
    - "Slothsoft" to your company's name



## How to

### How to change Eclipse version? 

The Eclipse version is set inside the _platform.target_. Just change the repository location to your wanted Eclipse version and adjust the versions of the features (or use "0.0.0" to get the latest version).


### How to change the operating system? 

The file _platform.target_ should be able to accommodate every OS you could open it with. The _pom.xml_ file next to it however has information about the OS this application gets build for. Edit the `<environments>` tag of the `target-platform-configuration` plug-in to change this build to your liking, or add more environments to build the application for multiple OS.


### How to change Java version?

The file _pom.xml_ has a property `tycho.version` that can be changed easily.


### How to change the Java version?

The Java version is defined inside the _MANIFEST.MF_ for each plug-in (including the IT project). 


### How to use plain Maven dependencies?

I followed the tutorial for the Maven plug-in [p2-maven-plugin](https://github.com/reficio/p2-maven-plugin). Basically, the following is done:

1. call `mvn install -Pstart` on _maven-p2/pom.xml_ to create a P2 repository and then start it locally
1. now the _platform.target_ can access "http://localhost:8080/site/"
1. when finished, call `mvn package -Pstop` or `mvn jetty:stop` to stop the P2 repository

The entire configuration is contained in _maven-p2/_.



### How to remove use of plain Maven dependencies?

If you don't need the feature, you can remove it like this:

1. Delete the folder _maven-p2/_ from the repository
1. Remove the location "http://localhost:8080/site/" from _platform.target_
1. Remove the "before_install" and "after_script" part of the Travis configuration file _.travis.yml_



### How to add / update Maven dependencies?

Open the file _maven-p2/pom.xml_ and configure the artifacts of the `p2-maven-plugin`. I'd advise to only add one version for each artifact and use "0.0.0" in the target platform.



### How to do pomless builds?

The official release notes of [Tycho 0.24](https://wiki.eclipse.org/Tycho/Release_Notes/0.24) will show you how it's done.


### How to release?

This official [tutorial](https://wiki.eclipse.org/Tycho/Release_Workflow) shows how to release a Tycho project.



## To Do List

_(All open issues can be found [here](https://github.com/slothsoft/example-eclipse-application/issues).)_



## License

This project is licensed under the MIT License - see the [MIT license](LICENSE) for details.
