#include "Leaderboard.h"
#include <fstream>
#include <iostream>
#include <iomanip>

void Leaderboard::insert_new_entry(LeaderboardEntry* new_entry) {
    if (!head_leaderboard_entry || new_entry->score > head_leaderboard_entry->score) {
        new_entry->next_leaderboard_entry = head_leaderboard_entry;
        head_leaderboard_entry = new_entry;
    } else {
        LeaderboardEntry* current = head_leaderboard_entry;
        LeaderboardEntry* prev = nullptr;

        while (current && new_entry->score <= current->score) {
            prev = current;
            current = current->next_leaderboard_entry;
        }

        prev->next_leaderboard_entry = new_entry;
        new_entry->next_leaderboard_entry = current;
    }

    // Maintain the leaderboard size
    LeaderboardEntry* last = head_leaderboard_entry;
    int count = 1;
    while (last && count < MAX_LEADERBOARD_SIZE) {
        last = last->next_leaderboard_entry;
        count++;
    }

    if (last) {
        LeaderboardEntry* temp = last->next_leaderboard_entry;
        last->next_leaderboard_entry = nullptr;
        while (temp) {
            LeaderboardEntry* toDelete = temp;
            temp = temp->next_leaderboard_entry;
            delete toDelete;
        }
    }
}

void Leaderboard::write_to_file(const string& filename) {
    ofstream file(filename);
    LeaderboardEntry* current = head_leaderboard_entry;
    while (current) {
        file << current->score << " " << current->last_played << " " << current->player_name << "\n";
        current = current->next_leaderboard_entry;
    }
    file.close();
}

void Leaderboard::read_from_file(const string& filename) {
    ifstream file(filename);
    if (!file.is_open()) {
        cout << "No existing leaderboard file found.\n";
        return;
    }

    // Clear existing leaderboard
    LeaderboardEntry* current = head_leaderboard_entry;
    while (current) {
        LeaderboardEntry* toDelete = current;
        current = current->next_leaderboard_entry;
        delete toDelete;
    }
    head_leaderboard_entry = nullptr;

    // Read from file
    unsigned long score;
    time_t lastPlayed;
    string playerName;
    while (file >> score >> lastPlayed >> playerName) {
        LeaderboardEntry* new_entry = new LeaderboardEntry(score, lastPlayed, playerName);
        insert_new_entry(new_entry);
    }

    file.close();
}

void Leaderboard::print_leaderboard() {
    cout << "Leaderboard\n-----------\n";
    LeaderboardEntry* current = head_leaderboard_entry;
    int order = 1;

    while (current) {
        cout << order << ". " << current->player_name << " " << current->score << " ";
        cout << put_time(localtime(&(current->last_played)), "%T/%d.%m.%Y") << "\n";
        current = current->next_leaderboard_entry;
        order++;
    }
}

Leaderboard::~Leaderboard() {
    LeaderboardEntry* current = head_leaderboard_entry;
    while (current) {
        LeaderboardEntry* toDelete = current;
        current = current->next_leaderboard_entry;
        delete toDelete;
    }
}
