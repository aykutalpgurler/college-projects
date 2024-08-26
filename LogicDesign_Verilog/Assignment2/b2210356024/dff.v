module dff (
    input D,      // Data input
    input CLK,    // Clock input
    input RESET,  // Asynchronous reset, active high
    output reg Q  // Output
);
    // Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!
    always @(posedge CLK  or posedge RESET)
    begin
        if(RESET == 1'b1)
        Q <= 1'b0;
        else
        Q <= D;
    end

endmodule