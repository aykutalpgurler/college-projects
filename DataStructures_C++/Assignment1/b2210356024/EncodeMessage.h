#ifndef ENCODE_MESSAGE_H
#define ENCODE_MESSAGE_H

#include <string>
#include <vector>
#include "ImageMatrix.h"

class EncodeMessage {
public:
    EncodeMessage();
    ~EncodeMessage();

    ImageMatrix encodeMessageToImage(const ImageMatrix &img, const std::string &message, const std::vector<std::pair<int, int>>& positions);


private:
    // Any private helper functions or variables if necessary
    int fibonacci(int n);
    bool is_prime(int n);
    std::string right_circular_shift(const std::string& message); 
};

#endif // ENCODE_MESSAGE_H
