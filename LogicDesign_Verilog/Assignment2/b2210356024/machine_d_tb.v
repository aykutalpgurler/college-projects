`timescale 1ns / 1ps

module machine_d_tb;

    // Inputs
    reg x;
    reg CLK;
    reg RESET;

    // Outputs
    wire F;
    wire [2:0] S;

    // Instantiate the module under test
    machine_d dut (
        .x(x),
        .CLK(CLK),
        .RESET(RESET),
        .F(F),
        .S(S)
    );

    // Clock generation
    always begin
        #5 CLK = ~CLK; // Assuming a 10ns clock period
    end

    // Initial stimulus
    initial begin
        // Initialize inputs
        x = 0;
        CLK = 0;
        RESET = 1;

        // Apply reset
        #10 RESET = 0;

        // Provide stimulus
        #10 x = 1;
        #10 x = 0;
        #10 x = 1;

        // Add more stimulus as needed

        // Finish simulation after some time
        #100 $finish;
    end

    // Monitor outputs
    always @(posedge CLK) begin
        $display("Time=%0t x=%b F=%b S=%b", $time, x, F, S);
    end

endmodule
