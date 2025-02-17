.data
.word 1, 5, 4 ,3
.word 7, 11, 6, 10, 8

    la x16, 0 #This is a comment
    li x31, 1
    jal x1, bubbleSort
    j Over

Swap:mv x22, x26
     mv x26, x25
     mv x25, x22
     sw x25, 0(x20)
     sw x26, 0(x21)
    

bubbleSort:
    addi x10, x0, 0
    addi x11, x0, 0
    addi x12, x0, 20

LoopOuter:
    addi x14, x12, -1
    bge x10, x14, Exit

LoopInner:
    sub x13, x12, x10
    addi x13, x13, -1
    bge x11, x13, Incrementer
    
    mul x19, x11, x31
    add x20, x16, x19
    lw x25, 0(x20)
    addi x21, x20, 1
    lw x26, 0(x21)
    
    blt x26, x25, Swap
    addi x11, x11, 1
    j LoopInner

Incrementer:
    addi x10, x10, 1
    j LoopOuter

Exit:
    jr x1
    addi x29, x0, 0

Over: