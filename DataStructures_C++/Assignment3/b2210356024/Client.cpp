//
// Created by alperen on 27.09.2023.
//

#include "Client.h"

Client::Client(string const& _id, string const& _ip, string const& _mac) {
    client_id = _id;
    client_ip = _ip;
    client_mac = _mac;
}

ostream &operator<<(ostream &os, const Client &client) {
    os << "client_id: " << client.client_id << " client_ip: " << client.client_ip << " client_mac: "
       << client.client_mac << endl;
    return os;
}

Client::~Client() {
    // TODO: Free any dynamically allocated memory if necessary.

    // Clean up dynamically allocated packets in the queues
    while (!incoming_queue.empty()) {
        stack<Packet*> packetStack = incoming_queue.front();
        while (!packetStack.empty()) {
            delete packetStack.top();
            packetStack.pop();
        }
        incoming_queue.pop();
    }

    while (!outgoing_queue.empty()) {
        stack<Packet*> packetStack = outgoing_queue.front();
        while (!packetStack.empty()) {
            delete packetStack.top();
            packetStack.pop();
        }
        outgoing_queue.pop();
    }
}

string Client::getNextHopID(const string& receiverID) const {
    // Find the next hop ID in the routing table
    auto it = routing_table.find(receiverID);

    // If the receiverID is in the routing table, return the corresponding next hop ID
    if (it != routing_table.end()) {
        return it->second;
    }

    // If the receiverID is not in the routing table, return an empty string or handle it accordingly
    // For example, you can throw an exception or return a default value.
    return "";  // or throw std::runtime_error("Receiver not found in the routing table");
}