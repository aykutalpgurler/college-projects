// full_adder_tb.v
`timescale 1ns/10ps

module full_adder_tb;

    // Inputs
    reg A, B, Cin;

    // Outputs
    wire S, Cout;

    // Instantiate the full_adder module
    full_adder uut(
        .A(A),
        .B(B),
        .Cin(Cin),
        .S(S),
        .Cout(Cout)
    );

    // Testbench stimulus
    initial begin
        // Test for all 8 input combinations
        for (int i = 0; i < 8; i = i + 1) begin
            A = i & 1;
            B = (i >> 1) & 1;
            Cin = (i >> 2) & 1;

            #10 $display("A=%b, B=%b, Cin=%b, S=%b, Cout=%b", A, B, Cin, S, Cout);
        end

        // End simulation
        #10 $finish;
    end

endmodule
