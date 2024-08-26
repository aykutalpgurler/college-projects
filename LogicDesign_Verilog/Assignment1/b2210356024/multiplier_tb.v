`timescale 1ns/10ps

module multiplier_tb;

    reg [2:0] A, B;
    wire [5:0] P;

    multiplier uut (
        .A(A),
        .B(B),
        .P(P)
    );

    initial begin
        $display("Testing 3-bit Multiplier");

        // Iterate through all possible input combinations
        for (A = 0; A <= 7; A = A + 1) begin
            for (B = 0; B <= 7; B = B + 1) begin
                #1;  // Wait for 1 time unit

                $display("A = %b, B = %b, P = %b", A, B, P);

                // Add your expected output checks if needed
            end
        end

        $stop;
    end

endmodule
