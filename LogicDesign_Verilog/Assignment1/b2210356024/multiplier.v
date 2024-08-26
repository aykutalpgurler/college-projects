`timescale 1ns/10ps

module multiplier (
    input [2:0] A, B,
    output [5:0] P
);

	// Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!
    
    and gate00(P[0], A[0], B[0]);

    wire AND_10, AND_01, C_HA1;
    and gate10(AND_10, A[1], B[0]);
    and gate01(AND_01, A[0], B[1]);
    half_adder ha1(.A(AND_10), .B(AND_01), .S(P[1]), .C(C_HA1));

    wire AND_20, AND_11, S_HA2, C_HA2, AND_02, Cout_FA1;
    and gate20(AND_20, A[2], B[0]);
    and gate11(AND_11, A[1], B[1]);
    half_adder ha2(.A(AND_11), .B(AND_20), .S(S_HA2), .C(C_HA2));
    and gate02(AND_02, A[0], B[2]);
    full_adder fa1(.A(AND_02), .B(S_HA2), .Cin(C_HA1), .S(P[2]), .Cout(Cout_FA1));

    wire AND_21, AND_12, S_FA2, Cout_FA2, C_HA3;
    and gate21(AND_21, A[2], B[1]);
    and gate12(AND_12, A[1], B[2]);
    full_adder fa2(.A(AND_12), .B(AND_21), .Cin(C_HA2), .S(S_FA2), .Cout(Cout_FA2));
    half_adder ha3(.A(S_FA2), .B(Cout_FA1), .S(P[3]), .C(C_HA3));

    wire AND_22;
    and gate22(AND_22, A[2], B[2]);
    full_adder fa3(.A(AND_22), .B(Cout_FA2), .Cin(C_HA3), .S(P[4]), .Cout(P[5]));

endmodule
