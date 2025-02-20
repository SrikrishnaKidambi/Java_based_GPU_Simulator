.data 
arr: .word 2 4 5 4 1 2 4 5 4 8 7 1 1 211 4 6 5 22 4 55
str1: .string "Array before sorting: "
str2: .string "Array after sorting: "
str3: .string "Swap called at arr[j] and arr[j+1] "
newline: .string "\n"
space: .string " "

.text 

main:
    subi x25 x0 19 # one subtracted from total number of elements in the array
    addi x11 x0 20 #number of elements in the array 
    addi x12 x0 0 #i=0
    addi x13 x0 0 #j=0
    la x15 arr # load the base address of the array
    addi x14 x0 0 #i=0 in for loop for printarray
    addi x30 x0 0 #i=0 in for loop for the array printing before sorting
    la x31 arr
    la x28 arr
    la x10 str1
    li x17 4
    ecall # printing the before array message 
    printBeforeSort:
        bge x30 x11 Loop1
        lw x29 0(x28)
        mv x10 x29
        li x17 1 
        ecall
        la x10 space
        li x17 4
        ecall
        addi x30 x30 1
        addi x28 x28 4
        j printBeforeSort
Loop1:
    bge x12 x25 callPrintArray
    la x15 arr # load the base address of the array
    addi x13 x0 0 #j=0
    j Loop2
Loop2:
    sub x21 x25 x12
    bge x13 x21 increment
    lw x18 0(x15) # arr[j]
    lw x19 4(x15) #arr[j+1]
    blt x19 x18 swap
    addi x15 x15 4
    addi x13 x13 1
    j Loop2
increment:
    addi x12 x12 1
    j Loop1
swap:
    sw x19 0(x15)
    sw x18 4(x15)
    addi x15 x15 4
    addi x13 x13 1
    j Loop2
callPrintArray:
    la x10 newline 
    li x17 4
    ecall
    la x10 str2 
    li x17 4
    ecall
    j printArray
printArray:
    bge x14 x11 Exit
    lw x16 0(x31)
    mv x10 x16
    li x17 1
    ecall 
    la x10 space 
    li x17 4
    ecalli
    addi x14 x14 1
    addi x31 x31 4
    j printArray
    
Exit:
    addi x0 x0 0
    
#Exit:
    #add x0 x0 x0
    