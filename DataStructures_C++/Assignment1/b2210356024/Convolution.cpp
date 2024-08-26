#include <iostream>

#include "Convolution.h"

// Default constructor 
Convolution::Convolution() {
}

// Parametrized constructor for custom kernel and other parameters
Convolution::Convolution(double** customKernel, int kh, int kw, int stride_val, bool pad) : kHeight(kh), kWidth(kw), stride(stride_val), padding(pad) {
    kernel = new double*[kh];
    for (int i = 0; i < kh; ++i) {
        kernel[i] = new double[kw];
        for (int j = 0; j < kw; j++) {
            kernel[i][j] = customKernel[i][j];
        }
    }
}

// Destructor
Convolution::~Convolution() {
    if (kernel != nullptr) {
        for (int i = 0; i < kHeight; ++i) {
            delete[] kernel[i];
        }
        delete[] kernel;
    }
}

// Copy constructor
Convolution::Convolution(const Convolution &other){

    kHeight = other.kHeight; 
    kWidth = other. kWidth; 
    stride = other.stride; 
    padding = other.padding;
    
    kernel = new double*[kHeight];
    for (int i = 0; i < kHeight; ++i) {
        kernel[i] = new double[kWidth];
        for (int j = 0; j < kWidth; j++) {
            kernel[i][j] = other.kernel[i][j];
        }
    }
}

// Copy assignment operator
Convolution& Convolution::operator=(const Convolution &other) {
    if (this == &other) { // Check for self-assignment
        return *this;
    }

    // Deallocate current data
    if (kernel != nullptr) {
         for (int i = 0; i < kHeight; ++i) {
            delete[] kernel[i];
        }
        delete[] kernel;   
    }
    
    kHeight = other.kHeight; 
    kWidth = other. kWidth; 
    stride = other.stride; 
    padding = other.padding;
    
    kernel = new double*[kHeight];
    for (int i = 0; i < kHeight; ++i) {
        kernel[i] = new double[kWidth];
        for (int j = 0; j < kWidth; j++) {
            kernel[i][j] = other.kernel[i][j];
        }
    }
    
    return *this;   
}


ImageMatrix Convolution::convolve(const ImageMatrix& input_image) const {
    int height = input_image.get_height();
    int width = input_image.get_width();

    int paddedHeight = height;
    int paddedWidth = width;
    int padding_int = 0;
    
    // If padding is true, update the padded dimensions
    if (padding) {
        paddedHeight = height + 2;
        paddedWidth = width + 2;
        padding_int = 1; // To calculate result_image's height and width. Line 112 and 113
    }

    // Allocate memory for the padded image
    double** padded_image = new double*[paddedHeight];
    for (int i = 0; i < paddedHeight; ++i) {
        padded_image[i] = new double[paddedWidth];
        for (int j = 0; j < paddedWidth; ++j) {
            if (padding) {
                if (i >= 1 && i <= height && j >= 1 && j <= width) {
                    padded_image[i][j] = input_image.get_data(i - 1, j - 1);
                } else {
                    padded_image[i][j] = 0.0;
                }
            } else {
                if (i < height && j < width) {
                    padded_image[i][j] = input_image.get_data(i, j);
                }
            }
        }
    }

    int newHeight = ((height - kHeight) + (2 * padding_int)) / stride + 1;
    int newWidth = ((width - kWidth) + (2 * padding_int)) / stride + 1;

    double** result_image = new double*[newHeight];
    for (int i = 0; i < newHeight; ++i) {
        result_image[i] = new double[newWidth];
        for (int j = 0; j < newWidth; ++j) {
            double sum = 0;
            for (int ki = 0; ki < kHeight; ki++) {
                for (int kj = 0; kj < kWidth; kj++) {
                    int img_i = i * stride + ki;
                    int img_j = j * stride + kj;
                    sum += padded_image[img_i][img_j] * kernel[ki][kj];
                }
            }
            result_image[i][j] = sum;
        }
    }

    ImageMatrix result(result_image, newHeight, newWidth);

    // Deallocate the memory when you're done
    for (int i = 0; i < paddedHeight; ++i) {
        delete[] padded_image[i];
    }
    delete[] padded_image;

    for (int i = 0; i < newHeight; ++i) {
        delete[] result_image[i];
    }
    delete[] result_image;

    return result;
}


