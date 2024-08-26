#include "Network.h"
#include <fstream>
#include <sstream>
#include <iomanip>

Network::Network() {

}

void Network::process_commands(vector<Client> &clients, vector<string> &commands, int message_limit,
                               const string &sender_port, const string &receiver_port) {
    // TODO: Execute the commands given as a vector of strings while utilizing the remaining arguments.
    /* Don't use any static variables, assume this method will be called over and over during testing.
     Don't forget to update the necessary member variables after processing each command. For example,
     after the MESSAGE command, the outgoing queue of the sender must have the expected frames ready to send. */

    for(const auto& command : commands) {

        // Get first parts of commands
        std::istringstream iss(command);
        std::string commandType;
        iss >> commandType;
//        std::cout << commandType << std::endl;

        // Generate dashed line dynamically based on the length of the command
        std::string dashedLine(command.size() + 9, '-');

        // Print the command before processing
        std::cout << dashedLine << std::endl;
        std::cout << "Command: " << command << std::endl;
        std::cout << dashedLine << std::endl;

        if (commandType == "MESSAGE") {
            // Extract the next three parts (sender_id, final_destination_id, message)
            std::string sender_id, final_destination_id, message;
            iss >> sender_id >> final_destination_id;

            // Get the remainder of the string as message
            std::getline(iss >> std::ws, message);

            // Find the position of the first non-# character
            size_t firstNonHash = message.find_first_not_of('#');

            // Find the position of the last non-# character
            size_t lastNonHash = message.find_last_not_of('#');

            // Check if there are non-# characters in the string
            if (firstNonHash != std::string::npos && lastNonHash != std::string::npos) {
                // Extract the substring without leading and trailing #
                message = message.substr(firstNonHash, lastNonHash - firstNonHash + 1);
            } else {
                // The string is either empty or contains only #
                message.clear();
            }


//            std::cout << "SENDER: " << sender_id << std::endl;
//            std::cout << "FINAL RECEIVER: " << final_destination_id << std::endl;
//            std::cout << "MESSAGE: " << message << std::endl;

            std::cout << "Message to be sent: " << "\"" << message << "\"" << std::endl << std::endl;

            // Split the message into chunks based on message_limit
            std::vector<std::string> chunks;
            for (size_t i = 0; i < message.length(); i += message_limit) {
                chunks.push_back(message.substr(i, message_limit));
            }

            // Prepare frames for each chunk
            for (size_t i = 0; i < chunks.size(); ++i) {

                auto* app_packet = new ApplicationLayerPacket(0, sender_id, final_destination_id, chunks.at(i));
                app_packet->frame_count = i + 1;

                auto* transport_packet = new TransportLayerPacket(1, sender_port, receiver_port);

                auto* network_packet = new NetworkLayerPacket(2, getClientIP(clients, sender_id), getClientIP(clients, final_destination_id));

//                string nextHopID = getClient(clients, sender_id).getNextHopID(final_destination_id);
                string nextHopID;
                for(auto& client : clients) {
                    if(client.client_id == sender_id) {
                        nextHopID = client.getNextHopID(final_destination_id);
                    }
                }
                auto* physical_packet = new PhysicalLayerPacket(3, getClientMac(clients, sender_id), getClientMac(clients, nextHopID));
//                PhysicalLayerPacket* physical_packet = new PhysicalLayerPacket(3, getClientMac(clients, sender_id), getClientMac(clients, final_destination_id));

                // Create a stack from the packets
                std::stack<Packet*> frame;
                frame.push(app_packet);
                frame.push(transport_packet);
                frame.push(network_packet);
                frame.push(physical_packet);

                std::cout << "Frame #" << app_packet->frame_count << std::endl;
                physical_packet->print();
                network_packet->print();
                transport_packet->print();
                app_packet->print();
                std::cout << "Message chuck carried: " << "\"" << chunks.at(i) << "\"" << std::endl;
                std::cout << "Number of hops so far: " << app_packet->hop_count << std::endl;
                std::cout << "--------" << std::endl;

//                getClient(clients, sender_id).outgoing_queue.push(frame);
                for(auto& client : clients) {
                    if (client.client_id == sender_id) {
                        client.outgoing_queue.push(frame);
                        if(i == chunks.size() - 1) {
                            client.log_entries.push_back(Log(
                                    [](){time_t t = time(0); struct tm* now = localtime(&t); stringstream ss; ss << put_time(now, "%Y-%m-%d %H:%M:%S"); return ss.str();}(),
                                    message,
                                    app_packet->frame_count,
                                    app_packet->hop_count,
                                    sender_id,
                                    final_destination_id,
                                    true,
                                    ActivityType::MESSAGE_SENT
                                    ));
                        }
                    }
                }

//                for(auto& client : clients) {
//                    if (client.client_id == sender_id) {
//                        for(const auto& entry: client.log_entries) {
//                            std::cout << entry.timestamp << std::endl;
//                            std::cout << entry.message_content << std::endl;
//                            std::cout << entry.number_of_frames << std::endl;
//                            std::cout << entry.number_of_hops << std::endl;
//                            std::cout << entry.sender_id << std::endl;
//                            std::cout << entry.receiver_id << std::endl;
//                            std::cout << (entry.success_status == true ? "Yes" : "No") << std::endl;
//                            std::cout << (entry.activity_type == ActivityType::MESSAGE_SENT ? "sent" : "did not send") << std::endl;
//                        }
//                    }
//                }

            }

        } else if (commandType == "SHOW_FRAME_INFO") {

            std::string client_id, queue_selection, frame_number;
            iss >> client_id >> queue_selection >> frame_number;

            for(auto& client : clients) {
                if (client.client_id == client_id) {
                    if (queue_selection == "out") {
                        int frame_num = std::stoi(frame_number);
                        if (frame_num >= 1 && frame_num <= client.outgoing_queue.size()) {
                            // Get the stack of packets at the specified frame number
                            std::stack<Packet *> frame = client.outgoing_queue.front();
                            for (int i = 1; i < frame_num; ++i) {
                                client.outgoing_queue.push(client.outgoing_queue.front());
                                client.outgoing_queue.pop();
                                frame = client.outgoing_queue.front();
                            }

                            std::cout << "Current Frame #" << frame_num << " on the outgoing queue of client " << client_id << std::endl;

                            // Reverse the order of packets in the frame stack
                            std::stack<Packet *> reversedFrame;
                            while (!frame.empty()) {
                                reversedFrame.push(frame.top());
                                frame.pop();
                            }

                            int hop_count;
                            while (!reversedFrame.empty()) {
                                Packet *packet = reversedFrame.top();

                                // Use dynamic_cast to get ApplicationLayerPacket
                                if (auto *appPacket = dynamic_cast<ApplicationLayerPacket *>(packet)) {
                                    // Handle ApplicationLayerPacket specific information
                                    std::cout << "Carried message: " << "\"" << appPacket->message_data << "\""
                                              << std::endl;
                                    hop_count = appPacket->hop_count;
                                }
                                std::cout << "Layer " << packet->layer_ID << " info: ";
                                packet->print();
                                reversedFrame.pop();
                                if (packet->layer_ID == 3) {
                                    std::cout << "Number of hops so far: " << hop_count << std::endl;
                                }
                            }
                        } else {
                            std::cout << "No such frame." << std::endl;
                        }
                    } else if (queue_selection == "in") {
                        int frame_num = std::stoi(frame_number);
                        if (frame_num >= 1 && frame_num <= client.incoming_queue.size()) {
                            // Get the stack of packets at the specified frame number
                            std::stack<Packet *> frame = client.incoming_queue.front();
                            for (int i = 1; i < frame_num; ++i) {
                                client.incoming_queue.push(client.incoming_queue.front());
                                client.incoming_queue.pop();
                                frame = client.incoming_queue.front();
                            }

                            std::cout << "Current Frame #" << frame_num << " on the incoming queue of client " << client_id << std::endl;

                            // Reverse the order of packets in the frame stack
                            std::stack<Packet *> reversedFrame;
                            while (!frame.empty()) {
                                reversedFrame.push(frame.top());
                                frame.pop();
                            }

                            int hop_count;
                            while (!reversedFrame.empty()) {
                                Packet *packet = reversedFrame.top();

                                // Use dynamic_cast to get ApplicationLayerPacket
                                if (auto *appPacket = dynamic_cast<ApplicationLayerPacket *>(packet)) {
                                    // Handle ApplicationLayerPacket specific information
                                    std::cout << "Carried message: " << "\"" << appPacket->message_data << "\""
                                              << std::endl;
                                    hop_count = appPacket->hop_count;
                                }
                                std::cout << "Layer " << packet->layer_ID << " info: ";
                                packet->print();
                                reversedFrame.pop();
                                if (packet->layer_ID == 3) {
                                    std::cout << "Number of hops so far: " << hop_count << std::endl;
                                }
                            }
                        } else {
                            std::cout << "No such frame." << std::endl;
                        }
                    }
                }
            }
        } else if (commandType == "SHOW_Q_INFO") {

            std::string client_id, queue_selection;
            iss >> client_id >> queue_selection;

            for(auto& client : clients) {
                if (client.client_id == client_id) {
                    if(queue_selection == "out") {

                        std::cout << "Client " << client_id << " Outgoing Queue Status" << std::endl;
                        std::cout << "Current total number of frames: " << client.outgoing_queue.size() << std::endl;

                    } else if (queue_selection == "in") {

                        std::cout << "Client " << client_id << " Incoming Queue Status" << std::endl;
                        std::cout << "Current total number of frames: " << client.incoming_queue.size() << std::endl;

                    } else {
                        std::cout << "Invalid queue selection" << std::endl;
                    }
                }
            }
        } else if (commandType == "SEND") {

        } else if (commandType == "RECEIVE") {

        } else if (commandType == "PRINT_LOG") {

        } else { // Unknown command. Continue processing.
            std::cout << "Invalid command." << std::endl;
            continue;
        }
    }


}

vector<Client> Network::read_clients(const string &filename) {
    vector<Client> clients;
    // TODO: Read clients from the given input file and return a vector of Client instances.

    ifstream file(filename);

    if (!file.is_open()) {
        cerr << "Error opening file: " << filename << endl;
        return clients; // Return an empty vector if the file cannot be opened
    }

    int numClients;
    file >> numClients; // Read the number of clients from the first line

    for (int i = 0; i < numClients; ++i) {
        string id, ip, mac;
        file >> id >> ip >> mac;
        clients.emplace_back(id, ip, mac); // Create and add a new Client instance to the vector
    }

    file.close();

    return clients;
}

void Network::read_routing_tables(vector<Client> &clients, const string &filename) {
    // TODO: Read the routing tables from the given input file and populate the clients' routing_table member variable.

    ifstream file(filename);

    if (!file.is_open()) {
        cerr << "Error opening file: " << filename << endl;
        return;
    }

    string line;
    size_t clientIndex = 0;  // Index to keep track of which client's routing table is being read

    while (getline(file, line)) {
        if (line == "-") {
            // Move to the next client
            clientIndex++;
            continue;
        }

        istringstream iss(line);
        string receiverID, nextHopID;

        if (!(iss >> receiverID >> nextHopID)) {
            cerr << "Error reading routing table entry from file." << endl;
            break; // or return; depending on your error-handling strategy
        }

        // Assuming 'clients' is a vector of Client objects
        if (clientIndex < clients.size()) {
            // Populate the routing table for the current client
            clients[clientIndex].routing_table[receiverID] = nextHopID;
        } else {
            cerr << "Error: More routing tables found than clients." << endl;
            break; // or return; depending on your error-handling strategy
        }
    }

    file.close();
}

// Returns a list of token lists for each command
vector<string> Network::read_commands(const string &filename) {
    vector<string> commands;
    // TODO: Read commands from the given input file and return them as a vector of strings.

    // Open the file
    std::ifstream file(filename);

    // Check if the file is opened successfully
    if (!file.is_open()) {
        std::cerr << "Error opening file: " << filename << std::endl;
        return commands; // Return an empty vector in case of an error
    }

    // Read the number of commands from the first line
    int numCommands;
    file >> numCommands;
    file.ignore(); // Ignore the newline character after the number

    // Read each command line and store it in the vector
    std::string line;
    for (int i = 0; i < numCommands && std::getline(file, line); ++i) {
        commands.push_back(line);
    }

    // Close the file
    file.close();

    return commands;
}

Network::~Network() {
    // TODO: Free any dynamically allocated memory if necessary.
}

string Network::getClientIP(const vector<Client>& clients, const string& clientId) {
    for (const auto& client : clients) {
        if (client.client_id == clientId) {
            return client.client_ip;
        }
    }

    // If the client with the specified ID is not found, you may choose to return an empty string or handle it accordingly.
    // For example, you can throw an exception or return a default value.
    return ""; // or throw std::runtime_error("Client not found");
}

std::string Network::getClientMac(const std::vector<Client>& clients, const std::string& clientId) {
    for (const auto& client : clients) {
        if (client.client_id == clientId) {
            return client.client_mac;
        }
    }

    // If the client with the specified ID is not found, you may choose to return an empty string or handle it accordingly.
    // For example, you can throw an exception or return a default value.
    return ""; // or throw std::runtime_error("Client not found");
}

//Client Network::getClient(const vector<Client>& clients, const string& clientId) {
//    for (const auto& client : clients) {
//        if (client.client_id == clientId) {
//            return client;
//        }
//    }
//
//}
