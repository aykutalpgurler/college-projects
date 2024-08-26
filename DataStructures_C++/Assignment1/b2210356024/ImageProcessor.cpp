#include "ImageProcessor.h"

ImageProcessor::ImageProcessor() {

}

ImageProcessor::~ImageProcessor() {

}


std::string ImageProcessor::decodeHiddenMessage(const ImageMatrix &img) {
    
    ImageSharpening sharpener;
    ImageMatrix sharpened_image = sharpener.sharpen(img, 2);
    
    EdgeDetector detector;
    std::vector<std::pair<int, int>> edge_positions = detector.detectEdges(sharpened_image);
    
    DecodeMessage decoder;
    std::string alien_message = decoder.decodeFromImage(sharpened_image, edge_positions);
    
    return alien_message;
}

ImageMatrix ImageProcessor::encodeHiddenMessage(const ImageMatrix &img, const std::string &message) {
    
    ImageSharpening sharpener;
    ImageMatrix sharpened_image = sharpener.sharpen(img, 2);
    
    EdgeDetector detector;
    std::vector<std::pair<int, int>> edge_positions = detector.detectEdges(sharpened_image);
    
    EncodeMessage encoder;
    ImageMatrix encoded_image = encoder.encodeMessageToImage(img, message, edge_positions);
    
    return encoded_image;
}
