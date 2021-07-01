
# OCR in Java
## _General Info_
Simple OCR project created with Java to read text from image.

## _Installation_
To create a simple ocr software, download the tesseract
library and install it from
[github](https://github.com/UB-Mannheim/tesseract/wiki).

Then lets create a new maven project and add in pom.xml 
two dependecies. The first for tesseract library, and the 
seconde for creating a pdf document using iText.

```bash 
 <dependency>
    <groupId>net.sourceforge.tess4j</groupId>
    <artifactId>tess4j</artifactId>
    <version>4.5.4</version>
 </dependency>
```
```bash 
 <dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itextpdf</artifactId>
    <version>5.5.13.2 </version>
 </dependency>
```
## _About the code_
The source code is written in 6 classes:
```
  - src
   |
   - main
    |
    - java
     |
     - orc
      |     
      - Main.java
      - Image.java
      - Process.java
      - Fentre.java
      - Imgtext.java
      - TextPdf.java
    - resources
     |
     - images
      - ...
```

#
The code is relatively simple, we first open picture in which we are interested in. and we use some digital image processing (binarization: means to convert a colored image into a black and white) to improve the text recognition efficiency
 of the tesseract library.
 
Then in order to show the new image in a window we are going to use JFrame and Jpanel objects in the package javax.swing.
The result should be like this:

![App Screenshot](https://raw.githubusercontent.com/imenebaraitame/OCR-in-java/master/bufferedImage.jpg)

Comparing to the original image:
![App Screenshot](https://raw.githubusercontent.com/imenebaraitame/OCR-in-java/master/test2.jpg)
we can see that our original image with colored background converted into a binary image.

#
 After that we jump to Imgtext.java class, we are creating 
 a new instance of tesseract. we indicate where we have installed
 the library and then we do a text recognition using the doOCR() method.
#
 The last thing left is adding this text to Pdf document created from scratch based on iText.
 Below you will find a screenshot of the final result of the code:

 ![App Screenshot](https://raw.githubusercontent.com/imenebaraitame/OCR-in-java/master/ocrDemo.png)


 

 









