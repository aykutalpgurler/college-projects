`timescale 1us / 1ps

module ARTAU(
    input radar_echo,
    input scan_for_target,
    input [31:0] jet_speed,
    input [31:0] max_safe_distance,
    input RST,
    input CLK,
    output reg radar_pulse_trigger,
    output reg [31:0] distance_to_target,
    output reg threat_detected,
    output reg [1:0] ARTAU_state
);

// Internal signals
reg [31:0] distance1;  // Previous distance to target
reg [31:0] distance2;  // Current distance to target
reg [31:0] elapsed_time;

// Constants
parameter c0 = 300000000;  // Speed of light in m/s
parameter radar_pulse_duration = 300;  // Duration of radar pulse in us
parameter max_listen_time = 2000;  // Maximum time to listen for echo in us
parameter assess_timeout = 3000;  // Timeout for ASSESS state in us

// State machine
always @(posedge CLK or posedge RST) begin
    if (RST) begin
        // Reset state and outputs
        ARTAU_state <= 2'b00;  // IDLE state
        radar_pulse_trigger <= 0;
        distance_to_target <= 0;
        threat_detected <= 0;
        distance1 <= 0;
        elapsed_time <= 0;
    end else begin
        case (ARTAU_state)
            2'b00:  // IDLE state
                if (scan_for_target) begin
                    radar_pulse_trigger <= 1;
                    ARTAU_state <= 2'b01;  // Transition to EMIT state
                end
            2'b01:  // EMIT state
                if (elapsed_time < radar_pulse_duration) begin
                    radar_pulse_trigger <= 1;
                    elapsed_time <= elapsed_time + 100;  // Increment elapsed time on each CLK cycle
                end else begin
                    radar_pulse_trigger <= 0;
                    elapsed_time <= 0;
                    ARTAU_state <= 2'b10;  // Transition to LISTEN state
                end
            2'b10:  // LISTEN state
                if (elapsed_time < max_listen_time) begin
                    if (radar_echo) begin
                        distance2 <= (c0 * elapsed_time) / 2000;
                        ARTAU_state <= 2'b01;  // Transition to EMIT state for second scan
                    end
                    elapsed_time <= elapsed_time + 100;
                end else begin
                    // No echo received, return to IDLE state
                    ARTAU_state <= 2'b00;
                    radar_pulse_trigger <= 0;
                    distance_to_target <= 0;
                    threat_detected <= 0;
                    elapsed_time <= 0;
                end
            2'b11:  // ASSESS state
                if (elapsed_time < assess_timeout) begin
                    distance_to_target <= distance2;
                    threat_detected <= ((distance2 + jet_speed * (elapsed_time / 1000)) - distance1) < 0 && distance2 < max_safe_distance;
                    elapsed_time <= elapsed_time + 100;
                    if (scan_for_target) begin
                        ARTAU_state <= 2'b01;  // Transition to EMIT state for refreshing target information
                    end
                end else begin
                    // Timeout reached, return to IDLE state
                    ARTAU_state <= 2'b00;
                    radar_pulse_trigger <= 0;
                    distance_to_target <= 0;
                    threat_detected <= 0;
                    elapsed_time <= 0;
                end
        endcase
    end
end

endmodule
