.data 

arr: .word 2 5 3 1
str1: .string "The elements of the array are: "
str2: .string "There are no three consecutive odd numbers in the array.\n"
str3: .string "There are three consecutive odd numbers in the array.\n"
newline: .string "\n"
space: .string " "

.text 

la x11 arr
addi x12 x0 0 # i=0
addi x13 x0 4 # the number of elements of the array 
addi x14 x0 0 # storing the value of count
addi x15 x0 3 # storing the required count
addi x16 x16 2 # storing the value to be divided with 
addi x21 x0 1 # storing the value of 1 for checking the divisiblity by 2

Loop:
    bge x12 x13 Exit
    lw x18 0(x11)  # loaidng the value of arr[i]
    rem x19 x18 x16 # storing the value of remainder after performing arr[i]%2
    beq x19 x21 incrementCounter  # if the remainder is 1 then increment the counter
    bne x19 x21 makeCounterZero  # if branch is not taken then make the counter 0
    j Loop
incrementCounter:
        addi x14 x14 1
        beq x15 x14 Exit
        addi x12 x12 1
        addi x11 x11 4
        j Loop
makeCounterZero:
    addi x14 x0 0
    addi x12 x12 1
    addi x11 x11 4
    j Loop
Exit:
    beq x14 x15 printSuccess
    la x10 str2
    li x17 4
    ecall
    j End
    printSuccess:
        la x10 str3
        li x17 4
        ecall
        j End
End:
    addi x0 x0 0
    
