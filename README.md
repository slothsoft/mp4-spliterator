# MP4 Spliterator

[![Build Status](https://travis-ci.com/slothsoft/mp4-spliterator.svg?branch=master)](https://travis-ci.com/slothsoft/mp4-spliterator)

An application for splitting MP4 files into chapters. 


**Content of this ReadMe:**

- [Third Party Libraries](#third-party-libraries)
    - [Icons](#icons)
    - [MP4](#mp4)
- [Versions](#versions)
- [Features](#features)
- [How to](#how-to)
- [License](#license)


## Third Party Libraries

### Icons

Icons are from [icons8](https://icons8.de/).


### MP4

To get the meta data from MP4 files, [mp4parser](https://github.com/sannies/mp4parser) is used. Since this library doesn't support reading chapter files (yet), I tried implementing it with the help of the [official spec](https://www.adobe.com/content/dam/acom/en/devnet/flv/video_file_format_spec_v10.pdf) ("chpl" box, page 30).

Example files are from [here](http://techslides.com/sample-webm-ogg-and-mp4-video-files-for-html5).



##  Versions


| Version       | Issues | Changes       | Download      |
| ------------- | ------ | ------------- | ------------- |
| Future | [Issues](https://github.com/slothsoft/mp4-spliterator/issues) | | |
| 1.0.0 | [Issues](https://github.com/slothsoft/mp4-spliterator/milestone/1?closed=1) | first prototype | [Download](https://github.com/slothsoft/mp4-spliterator/releases/tag/1.0.0)



##  Features

If something is missing, request it via [a new issue](https://github.com/slothsoft/mp4-spliterator/issues/new).


## How to


## License

This project is licensed under the MIT License - see the [MIT license](LICENSE) for details.
