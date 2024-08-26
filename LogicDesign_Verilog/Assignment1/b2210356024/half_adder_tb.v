// half_adder_tb.v
`timescale 1ns/10ps

module half_adder_tb;

    // Inputs
    reg A, B;

    // Outputs
    wire S, C;

    // Instantiate the half_adder module
    half_adder uut(
        .A(A),
        .B(B),
        .S(S),
        .C(C)
    );

    // Testbench stimulus
    initial begin
        // Test case 1: A=0, B=0
        A = 0; B = 0;
        #10 $display("A=%b, B=%b, S=%b, C=%b", A, B, S, C);

        // Test case 2: A=0, B=1
        A = 0; B = 1;
        #10 $display("A=%b, B=%b, S=%b, C=%b", A, B, S, C);

        // Test case 3: A=1, B=0
        A = 1; B = 0;
        #10 $display("A=%b, B=%b, S=%b, C=%b", A, B, S, C);

        // Test case 4: A=1, B=1
        A = 1; B = 1;
        #10 $display("A=%b, B=%b, S=%b, C=%b", A, B, S, C);

        // End simulation
        #10 $finish;
    end

endmodule
