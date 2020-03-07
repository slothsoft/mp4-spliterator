# MP4 Spliterator

[![MIT Licence](https://img.shields.io/github/license/jenkinsci/java-client-api.svg?label=License)](http://opensource.org/licenses/MIT) [![Build Status](https://travis-ci.com/slothsoft/mp4-spliterator.svg?branch=master)](https://travis-ci.com/slothsoft/mp4-spliterator)

- **Author:** [Stef Schulz](mailto:s.schulz@slothsoft.de)
- **Repository:** <https://github.com/slothsoft/mp4-spliterator>
- **Open Issues:** <https://github.com/slothsoft/mp4-spliterator/issues>
- **User Manual:** [Hosted on GitHub](https://slothsoft.github.io/mp4-spliterator/)

An application for splitting MP4 files into chapters: 

<img alt="Screenshot" src="docs/resources/images/screenshot.png" width="300"/>



**Content of this ReadMe:**

- [Getting Started](#getting-started)
- [Features](#features)
- [Versions](#versions)
- [Developer Guide](#developer-guide)
- [Third Party Libraries](#third-party-libraries)
    - [Icons](#icons)
    - [MP4](#mp4)
- [License](#license)



## Getting Started

### Prerequisites

You need at least an installed **Java 8** or above to run the code. You can download it from [java.com](https://www.java.com/download/).



### Installing

Just download the last release and extract it in a folder of your choice, then double click the EXE file.



### Using the Application


<p>Splitting an MP4 into chapters is an easy process which can be done in very 
little clicks:</p>

<ul>  
	<li>1st a) Double click on a video in the <b><img src="docs/resources/images/video-folder@2x.png" width="20"/> Video Folder</b>. </li>
	<li>1st b) <i>Or</i> open a video directly via the menu <i>File → <img src="docs/resources/images/file_video@2x.png" width="20"/> Open Video</i>. </li>
	<li>2nd The video will be opened in a separate section where you can check and uncheck the chapters.</li>
	<li>3rd Click <b><img src="docs/resources/images/export-split@2x.png" width="20"/> Export Chapters Separately</b> and choose a folder. The chapters will be exported into this folder.</li>
</ul>

*(More information can be found in the  [user manual](https://slothsoft.github.io/mp4-spliterator/).)*



##  Features

If something is missing, request it via [a new issue](https://github.com/slothsoft/mp4-spliterator/issues/new).



##  Versions


| Version       | Issues | Changes       | Download      |
| ------------- | ------ | ------------- | ------------- |
| Future | [Issues](https://github.com/slothsoft/mp4-spliterator/issues) | | |
| 1.1.0 | [Issues](https://github.com/slothsoft/mp4-spliterator/milestone/2?closed=1) | Chapter merging | [Download](https://github.com/slothsoft/mp4-spliterator/releases/tag/1.1.0)
| 1.0.0 | [Issues](https://github.com/slothsoft/mp4-spliterator/milestone/1?closed=1) | First prototype | [Download](https://github.com/slothsoft/mp4-spliterator/releases/tag/1.0.0)



## Developer Guide

To start this project in your Eclipse, do:

1. Clone this repository
1. Call `mvn install -Pstart` on _maven-p2/pom.xml_ (_Start P2 Repo.launch_) to start a P2 repository locally
1. To start the application from **Eclipse** do the following:
    1. Open the file _platform.target_ and click on _"Set as Active Target Platform"_
    1. _de.slothsoft.mp4spliterator.product/Mp4SpliteratorApp.product_ and click on _"1. Synchronize"_ and then _"2. Launch Eclipse Application"_
1. To build the application with **Tycho** 
     - Run `mvn clean install` on the repository root
     - Or in Eclipse right click on the _pom.xml_ and use _"Run as..."_ → _"Maven install"_
     - The resulting EXE file is in _de.slothsoft.mp4spliterator.product/target/products/de.slothsoft.mp4spliterator.product-&lt;time>-&lt;os>.zip_ and / or the folder next to the ZIP file
1. when finished, call `mvn package -Pstop` to stop the P2 repository


**Note:** This project is a copy of the template [example-eclipse-application](https://github.com/slothsoft/example-eclipse-application). So if you need more information or if you like this setup, check it out!



## Third Party Libraries

### Icons

Application icons are from [icons8](https://icons8.de/). The flags in the README are from [H. J. Nilsson](https://github.com/hjnilsson/country-flags).


### MP4

To get the meta data from MP4 files, [mp4parser](https://github.com/sannies/mp4parser) is used. Since this library doesn't support reading chapter files (yet), I tried implementing it with the help of the [official spec](https://www.adobe.com/content/dam/acom/en/devnet/flv/video_file_format_spec_v10.pdf) ("chpl" box, page 30).

Example files are from [here](http://techslides.com/sample-webm-ogg-and-mp4-video-files-for-html5).



## License

This project is licensed under the MIT License - see the [MIT license](LICENSE) for details.
