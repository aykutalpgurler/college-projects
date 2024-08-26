// EdgeDetector.cpp

#include "EdgeDetector.h"
#include <cmath>

#include "EdgeDetector.h"
#include <cmath>

// Default constructor
EdgeDetector::EdgeDetector() {
    // Sobel Operator X for edge detection
    heightX = 3;
    widthX = 3;
    gX = new double*[heightX];
    for (int i = 0; i < heightX; ++i) {
        gX[i] = new double[widthX];
    }
    gX[0][0] = -1.0;
    gX[0][1] = 0.0;
    gX[0][2] = 1.0;
    gX[1][0] = -2.0;
    gX[1][1] = 0.0;
    gX[1][2] = 2.0;
    gX[2][0] = -1.0;   
    gX[2][1] = 0.0; 
    gX[2][2] = 1.0;

    // Sobel Operator Y for edge detection
    heightY = 3;
    widthY = 3;
    gY = new double*[heightY];
    for (int i = 0; i < heightY; ++i) {
        gY[i] = new double[widthY];
    }
    gY[0][0] = -1.0;
    gY[0][1] = -2.0;    
    gY[0][2] = -1.0;
    gY[1][0] = 0.0; 
    gY[1][1] = 0.0; 
    gY[1][2] = 0.0;
    gY[2][0] = 1.0; 
    gY[2][1] = 2.0; 
    gY[2][2] = 1.0;
}

// Destructor
EdgeDetector::~EdgeDetector() {
    // Free allocated memory for gX and gY
    for (int i = 0; i < heightX; ++i) {
        delete[] gX[i];
    }
    delete[] gX;

    for (int i = 0; i < heightY; ++i) {
        delete[] gY[i];
    }
    delete[] gY;
}

// Detect Edges using the given algorithm
std::vector<std::pair<int, int>> EdgeDetector::detectEdges(const ImageMatrix& input_image) {
    
    ImageMatrix iX;
    ImageMatrix iY;
    
    Convolution horizontal_edge(gX, heightX, widthX, 1, true);
    Convolution vertical_edge(gY, heightY, widthY, 1, true);
    
    iX = horizontal_edge.convolve(input_image);
    iY = vertical_edge.convolve(input_image);
    
    double threshold = 0.0;
    double sum = 0.0;
        
    for(int i = 0; i < iX.get_height(); i++) {
        for(int j = 0; j < iX.get_width(); j++) {
            double magnitude = sqrt(pow(iX.get_data(i, j), 2) + pow(iY.get_data(i, j), 2));
            sum += magnitude;
        }
    }
    
    threshold = sum / (iX.get_height() * iX.get_width()); // Average of magnitudes
    
    std::vector<std::pair<int, int>> EdgePixels; // List of edge pixels

    // Identify edge pixels above the threshold
    for (int i = 0; i < iX.get_height(); i++) {
        for (int j = 0; j < iX.get_width(); j++) {
            double magnitude = sqrt(pow(iX.get_data(i, j), 2) + pow(iY.get_data(i, j), 2));
            if (magnitude > threshold) {
                EdgePixels.push_back(std::make_pair(i, j));
            }
        }
    }
    
    return EdgePixels; // Return list of edge pixels
}

// To control iX 
ImageMatrix EdgeDetector::detectEdgesX(const ImageMatrix& input_image) {
    
    ImageMatrix iX;
    
    Convolution horizontal_edge(gX, 3, 3, 1, true);
    
    iX = horizontal_edge.convolve(input_image);
    
    return iX;
}

// To control iY 
ImageMatrix EdgeDetector::detectEdgesY(const ImageMatrix& input_image) {
    
    ImageMatrix iY;
    
    Convolution vertical_edge(gY, 3, 3, 1, true);
    
    iY = vertical_edge.convolve(input_image);
    
    return iY;
}

