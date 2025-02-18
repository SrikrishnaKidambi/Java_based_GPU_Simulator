.data 
.word 2 5 1 4 5 8 7 9 4 7
addi x10, x0, 0 # sum = 0
addi x11, x0, 0 # i = 0
addi x12, x0, 10 # 10
add x9, x8, x0 # x8 is the base register
Loop:
beq x11, x12, Exit
lw x13, 0(x9) # load arr[i] into x13
add x10, x10, x13 # sum += arr[i]
addi x11, x11, 1 # i++
addi x9, x9, 4 # x9 = x9 + 4
j Loop
Exit:
Nop