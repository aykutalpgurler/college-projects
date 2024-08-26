module machine_d(
    input wire x,
    input wire CLK,
    input wire RESET,
    output wire F,
    output wire [2:0] S
);
    // Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!

    wire A, B, C;

    dff d0(.D((~A & B & ~x) | A), .CLK(CLK), .RESET(RESET), .Q(A));
    dff d1(.D((A & B) | ~(B ^ x)), .CLK(CLK), .RESET(RESET), .Q(B));
    dff d2(.D(C ^ x), .CLK(CLK), .RESET(RESET), .Q(C));
    assign F = (A & B & ~C);
    assign S = {A, B, C};

endmodule