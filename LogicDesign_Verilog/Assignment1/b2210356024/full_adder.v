// full_adder.v
`timescale 1ns/10ps

module full_adder(
    input A, B, Cin,
    output S, Cout
);

    // Instantiate two half adders and an OR gate
    wire H1_S, H1_C, H2_C;
    half_adder u1(.A(A), .B(B), .S(H1_S), .C(H1_C));
    half_adder u2(.A(H1_S), .B(Cin), .S(S), .C(H2_C));

    or gate1(Cout, H1_C, H2_C);

endmodule
