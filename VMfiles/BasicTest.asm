//PUSH constant 10
@10
D=A
@SP
A=M
M=D
@SP
M=M+1
//POP local 0
@0
D=A
@LCL
A=M+D
D=A
@SP
A=M
M=D
A=A-1
D=M
A=A+1
A=M
M=D
@SP
M=M-1
//PUSH constant 21
@21
D=A
@SP
A=M
M=D
@SP
M=M+1
//PUSH constant 22
@22
D=A
@SP
A=M
M=D
@SP
M=M+1
//POP argument 2
@2
D=A
@ARG
A=M+D
D=A
@SP
A=M
M=D
A=A-1
D=M
A=A+1
A=M
M=D
@SP
M=M-1
//POP argument 1
@1
D=A
@ARG
A=M+D
D=A
@SP
A=M
M=D
A=A-1
D=M
A=A+1
A=M
M=D
@SP
M=M-1
//PUSH constant 36
@36
D=A
@SP
A=M
M=D
@SP
M=M+1
//POP this 6
@6
D=A
@THIS
A=M+D
D=A
@SP
A=M
M=D
A=A-1
D=M
A=A+1
A=M
M=D
@SP
M=M-1
//PUSH constant 42
@42
D=A
@SP
A=M
M=D
@SP
M=M+1
//PUSH constant 45
@45
D=A
@SP
A=M
M=D
@SP
M=M+1
//POP that 5
@5
D=A
@THAT
A=M+D
D=A
@SP
A=M
M=D
A=A-1
D=M
A=A+1
A=M
M=D
@SP
M=M-1
//POP that 2
@2
D=A
@THAT
A=M+D
D=A
@SP
A=M
M=D
A=A-1
D=M
A=A+1
A=M
M=D
@SP
M=M-1
//PUSH constant 510
@510
D=A
@SP
A=M
M=D
@SP
M=M+1
//POP temp 6
@SP
M=M-1
A=M
D=M
@11
M=D
//PUSH local 0
@0
D=A
@LCL
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
//PUSH that 5
@5
D=A
@THAT
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
//add
@SP
M=M-1
A=M
D=M
A=A-1
M=D+M
//PUSH argument 1
@1
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
//sub
@SP
M=M-1
A=M
D=M
A=A-1
M=M-D
//PUSH this 6
@6
D=A
@THIS
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
//PUSH this 6
@6
D=A
@THIS
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
//add
@SP
M=M-1
A=M
D=M
A=A-1
M=D+M
//sub
@SP
M=M-1
A=M
D=M
A=A-1
M=M-D
//PUSH temp 6
@11
D=M
@SP
A=M
M=D
@SP
M=M+1
//add
@SP
M=M-1
A=M
D=M
A=A-1
M=D+M