#include "EncodeMessage.h"
#include <cmath>
#include <bitset>
#include <iostream>
#include <ostream>




// Default Constructor
EncodeMessage::EncodeMessage() {

}

// Destructor
EncodeMessage::~EncodeMessage() {
    
}

// Function to encode a message into an image matrix
ImageMatrix EncodeMessage::encodeMessageToImage(const ImageMatrix &img, const std::string &message, const std::vector<std::pair<int, int>>& positions) {
    
    std::string transformed_message;
    std::string shifted_message;
    
    for(int i = 0; i < message.size(); i++) {
        char char_of_message = message[i]; // Initialize char_of_message with the current character
        if(is_prime(i)) {
            int ascii_val = static_cast<int>(message[i]);
            ascii_val += fibonacci(i);
            if (ascii_val <= 32) {
                ascii_val += 33;
            } else if (ascii_val >= 127) {
                ascii_val = 126;
            }
            char_of_message = static_cast<char>(ascii_val); // Update char_of_message with the transformed value
        }
        transformed_message += char_of_message;
    }
    
    shifted_message = right_circular_shift(transformed_message);
    
    std::string binary_string;

    for(int i = 0; i < shifted_message.size(); i++) {
        binary_string += std::bitset<7>(shifted_message[i]).to_string();
    }
    
    ImageMatrix embedded_image = img;
    
    for(int i = 0; i < binary_string.size(); i++) {
        if(i > positions.size() - 1) {
            break;
        }
        int pixel_value = static_cast<int>(img.get_data(positions[i].first, positions[i].second)); 
        std::string binary_pixel = std::bitset<7>(pixel_value).to_string(); 
        binary_pixel[binary_pixel.size() - 1] = binary_string[i];
        int new_pixel_decimal = std::bitset<7>(binary_pixel).to_ulong();
        double new_pixel_double = static_cast<double>(new_pixel_decimal);
        embedded_image.set_data(positions[i].first, positions[i].second, new_pixel_double);
    }
     
      
    return embedded_image;
}

int EncodeMessage::fibonacci(int n) {
    
    if (n <= 1) {
        return n;
    }
    
    return fibonacci(n - 1) + fibonacci(n - 2);
}

bool EncodeMessage::is_prime(int n) {
    if (n <= 1) {
        return false;
    }
    for (int i = 2; i * i <= n; i++) {
        if (n % i == 0) {
            return false;
        }
    }
    
    return true;
}

std::string EncodeMessage::right_circular_shift(const std::string& message) {
    int shift_amount = message.length() / 2; // Calculate the shift amount
    std::string shifted_message = message; // Make a copy of the original message

    // Perform right circular shift using a custom algorithm
    for (int i = 0; i < shift_amount; ++i) {
        char last = shifted_message.back(); // Store the last character
        for (int j = shifted_message.length() - 1; j > 0; --j) {
            shifted_message[j] = shifted_message[j - 1]; // Shift characters to the right
        }
        shifted_message[0] = last; // Set the first character to the last stored character
    }

    return shifted_message; // Return the shifted message
}
