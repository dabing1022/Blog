//: ## New Playground Authoring Features
//: 
//: New playground files in Xcode 6.3 show results in-line within the playground document.  The new format also makes it easy to create gorgeous playgrounds with stylized text and embedded images. Playgrounds are perfect for documentation, tutorials, or samples you include with your projects.
//:
//: ----
//:
//: Using simple plain text markup you can quickly create new headings, lists, **bold** or *italic* text, and [links](http://developer.apple.com/swift/blog) from within playground comments.  Select the Editor -> Show Documentation as Rich Text menu option to see your playground rendered with styles enabled.

import Cocoa

// Let's play with mathematics and an in-line graph
for count in 0...100 {
    sin(1000.0 / Double(count))
}

//: Playgrounds contain their own resource bundle.  Just drag images or other assets into the Resources folder of the playground using Show Package Contents in Finder.

// Load a full color image directly from within the playground's Resources folder
var vacationImage = NSImage(named: "Tortolla.jpg")

//: ## Create an Image Filter
//: 
//: Next, we'll create a filter to apply to the image. For a full listing of filters [go here](https://developer.apple.com/library/mac/documentation/GraphicsImaging/Reference/CoreImageFilterReference/index.html#).

let monochromeFilter = CIFilter(name:"CIColorMonochrome")
let inputCIImage = CIImage(data:vacationImage?.TIFFRepresentation);

// Set some filter parameters.
monochromeFilter.setValue(inputCIImage, forKey:kCIInputImageKey)
monochromeFilter.setValue(CIColor(red: 0.5, green: 0.5, blue: 0.5), forKey:kCIInputColorKey)
monochromeFilter.setValue(1.0, forKey:kCIInputIntensityKey)

// Use the playground to peek at the image now
let outputCIImage = monochromeFilter.outputImage

//: ## Explore the Filter Settings
//: 
//: For the curious, you might we wondering what would happen if we tweaked that color we used to desaturate the image. In the loop below, you can see what happens to the image as we change the value of kCIInputColorKey from 0 to 1.

let numImagesToGenerate = 5
for i in 1...numImagesToGenerate {
    
    let colorChannelValue: CGFloat = (CGFloat(i)/CGFloat(numImagesToGenerate))
    let ciColor = CIColor(red: colorChannelValue, green: colorChannelValue, blue: colorChannelValue)
    monochromeFilter.setValue(ciColor, forKey:kCIInputColorKey)

    // now, look at the image!
    let outputCIImage = monochromeFilter.outputImage
}






// end.
