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
//POP pointer 1
@SP
M=M-1
A=M
D=M
@4
M=D
//PUSH constant 0
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
//POP that 0
@0
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
//PUSH constant 1
@1
D=A
@SP
A=M
M=D
@SP
M=M+1
//POP that 1
@1
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
//PUSH argument 0
@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
//PUSH constant 2
@2
D=A
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
//POP argument 0
@0
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
//LABEL MAIN_LOOP_START
(MAIN_LOOP_START)
//PUSH argument 0
@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
//IF-GOTOCOMPUTE_ELEMENT
@SP
M=M-1
A=M
D=M
@COMPUTE_ELEMENT
D;JGT
//GOTO END_PROGRAM
@END_PROGRAM
0;JMP
//LABEL COMPUTE_ELEMENT
(COMPUTE_ELEMENT)
//PUSH that 0
@0
D=A
@THAT
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
//PUSH that 1
@1
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
//PUSH pointer 1
@4
D=M
@SP
A=M
M=D
@SP
M=M+1
//PUSH constant 1
@1
D=A
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
//POP pointer 1
@SP
M=M-1
A=M
D=M
@4
M=D
//PUSH argument 0
@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
//PUSH constant 1
@1
D=A
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
//POP argument 0
@0
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
//GOTO MAIN_LOOP_START
@MAIN_LOOP_START
0;JMP
//LABEL END_PROGRAM
(END_PROGRAM)
