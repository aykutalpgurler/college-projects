// half_adder.v
`timescale 1ns/10ps

module half_adder(
    input A, B,
    output S, C
);

    // Boolean equations for sum (S) and carry (C)
    assign S = A ^ B;  // XOR operation for sum
    assign C = A & B;  // AND operation for carry

endmodule
