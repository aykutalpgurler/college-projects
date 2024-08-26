#include "ImageSharpening.h"


// Default constructor
ImageSharpening::ImageSharpening() : height(3), width(3) {
    kernel = new double*[3];
    for (int i = 0; i < 3; ++i) {
        kernel[i] = new double[3];
        for (int j = 0; j < 3; ++j) {
            kernel[i][j] = 1.0 / 9.0; // Ensure the division is done with doubles
        }
    }
}

ImageSharpening::~ImageSharpening(){
    if (kernel != nullptr) {
        for (int i = 0; i < height; ++i) {
            delete[] kernel[i];
        }
        delete[] kernel;
        kernel = nullptr;
    }
}

ImageMatrix ImageSharpening::sharpen(const ImageMatrix& input_image, double k) {
    
    ImageMatrix blurring_image;
    Convolution blurring_kernel(kernel, 3, 3, 1, true);
    blurring_image = blurring_kernel.convolve(input_image);
    
    ImageMatrix sharpened_image;
    sharpened_image = (input_image - blurring_image) * k + input_image;    
    
    for(int i = 0; i < sharpened_image.get_height(); i++) {
        for(int j = 0; j < sharpened_image.get_width(); j++) {
            if(sharpened_image.get_data(i, j) > 255) {
                sharpened_image.set_data(i, j, 255);
            } else if(sharpened_image.get_data(i, j) < 0) {
                sharpened_image.set_data(i, j, 0);
            }
        }
    }
    return sharpened_image;
}
