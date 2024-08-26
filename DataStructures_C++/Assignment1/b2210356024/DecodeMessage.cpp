// DecodeMessage.cpp

#include "DecodeMessage.h"
#include "ImageMatrix.h"
#include <iostream>

// Default constructor
DecodeMessage::DecodeMessage() {
    // Nothing specific to initialize here
}

// Destructor
DecodeMessage::~DecodeMessage() {
    // Nothing specific to clean up
}


std::string DecodeMessage::decodeFromImage(const ImageMatrix& image, const std::vector<std::pair<int, int>>& edgePixels) {
    
    std::string binary_string;
    
    for (const auto& pair : edgePixels) {
        double pixel_value = image.get_data(pair.first, pair.second);
        int int_pixel_value = static_cast<int>(pixel_value); // Casting to int

        // Extract the LSB of the pixel value
        int lsb = int_pixel_value & 1;
        
        // Append the LSB to the binary string
        binary_string += std::to_string(lsb);
    }
    
    // Padding the binary string to form complete 7-bit bytes
    while (binary_string.length() % 7 != 0) {
        binary_string = '0' + binary_string; // Add leading zeros
    }
    
    std::string decoded_message;
    for (size_t i = 0; i < binary_string.length(); i += 7) {
        std::string byte = binary_string.substr(i, 7); // Take 7 bits
        int ascii_value = std::stoi(byte, nullptr, 2); // Convert the 7 bits to decimal

        // Adjust ASCII value for printable characters
        if (ascii_value <= 32) {
            ascii_value += 33;
        } else if (ascii_value == 127) {
            ascii_value = 126;
        }

        char decoded_char = static_cast<char>(ascii_value); // Convert to ASCII character
        decoded_message += decoded_char;
    }

    return decoded_message;
}

