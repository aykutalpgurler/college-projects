module ECSU(
    input CLK,
    input RST,
    input thunderstorm,
    input [5:0] wind,
    input [1:0] visibility,
    input signed [7:0] temperature,
    output reg severe_weather,
    output reg emergency_landing_alert,
    output reg [1:0] ECSU_state
);

    // Define states
    parameter ALL_CLEAR = 2'b00;
    parameter CAUTION = 2'b01;
    parameter HIGH_ALERT = 2'b10;
    parameter EMERGENCY = 2'b11;

    // Internal state register
    reg [1:0] current_state, next_state;

    always @(posedge CLK or posedge RST) begin
        if (RST) begin
            // Reset to default state
            current_state <= ALL_CLEAR;
        end else begin
            // State transitions
            current_state <= next_state;
        end
    end

    // Asynchronous output logic (Mealy outputs)
    always @(*) begin
        // Determine outputs based on current state and inputs
        // This allows outputs to change as soon as inputs change
        severe_weather = (current_state == HIGH_ALERT) || (current_state == EMERGENCY);
        emergency_landing_alert = (current_state == EMERGENCY);
        ECSU_state = current_state;
    end

    always @(*) begin
        // Default next state is current state
        next_state = current_state;

        // Logic for state transitions and outputs
        case (current_state)
            ALL_CLEAR: begin
                if (wind >= 11 && wind <= 15) // Wind threshold
                    next_state = CAUTION;
                else if (visibility == 2'b01) // Limited visibility
                    next_state = CAUTION;
                else if (thunderstorm || wind > 15 || temperature > 35 || temperature < -35 || visibility == 2'b11) begin
                    next_state = HIGH_ALERT;
                    severe_weather = 1;
                end
            end

            CAUTION: begin
                if (wind <= 10 && visibility == 2'b00) // Clear conditions
                    next_state = ALL_CLEAR;
                else if (thunderstorm || wind > 15 || temperature > 35 || temperature < -35 || visibility == 2'b11) begin
                    next_state = HIGH_ALERT;
                    severe_weather = 1;
                end
            end

            HIGH_ALERT: begin
                severe_weather = 1; // Severe weather alert
                if (temperature < -40 || temperature > 40 || wind > 20) begin
                    next_state = EMERGENCY;
                    emergency_landing_alert = 1;
                end else if (!thunderstorm && wind <= 10 && temperature >= -35 && temperature <= 35 && visibility != 2'b11) begin
                    next_state = CAUTION;
                    severe_weather = 0; // Deactivate severe weather alert
                end
            end

            EMERGENCY: begin
                // Define emergency state behavior if needed
            end
        endcase
    end

    always @(posedge CLK) begin
        // Output state
        ECSU_state <= current_state;
    end

endmodule
