.data
base: .word 2 4 5 4 1 2 4 5 4 8 7 1 1 211 4 6 5 22 4 55 4 6 8 7 4 5 1 2 3 6 5 1 3
str1: .string "\nThe sorted array is:\n"
str: .string " "

.text
la x16 base # stores the base address of the array
li x31 4 # x31 has the value 4
jal x1 bubbleSort
j printArray


bubbleSort:
    addi x10 x0 0 # i = 0
    addi x12 x0 33 # n = 20
    LoopOuter:
        addi x11 x0 0 # j = 0
        addi x14 x12 -1
        bge x10 x14 Exit # Exit the outer loop if i>=n-1
        LoopInner:
            sub x13 x12 x10 # n - i
            addi x13 x13 -1 # n - i - 1
            bge x11 x13 Incrementer # Exit the inner loop if j>=n-i-1
            mul x19 x11 x31 # x19 has the value j*4
            add x20 x16 x19 # x20 has the address x16+j
            lw x25 0(x20) # x25 has the value of x16+j alias x20
            addi x21 x20 4 # x21 has the address x16+j+1 or x20+1
            lw x26 0(x21) # x26 has the value of x16+j+1 alias x26
            blt x26 x25 Swap
            addi x11 x11 1
            j LoopInner
            Swap:
                sw x25 0(x21) # we are swapping the contents in the memory
                sw x26 0(x20)
                addi x11 x11 1
                j LoopInner
        Incrementer:
            addi x10 x10 1
            j LoopOuter
    Exit:
        jr x1

printArray:
    la x10 str1
    li x17 4
    ecall
    addi x29 x0 0 #counter of the array
    Loop:
        beq x29 x12 Done
        li x17 1
        lw x30 0(x16)
        mv x10 x30
        ecall
        
        la x10 str
        li x17 4
        ecall
        
        addi x16 x16 4
        addi x29 x29 1
        j Loop
     Done:
            
