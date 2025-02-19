.data 
str: .string "The number of ones in the given number are: "
str1: .string "The number of zeroes in the given number are: "
newline: .string "\n"
str2: .string "The given number: "

.text
addi x10 x0 15 # given value
li x11 1431655765
li x12 858993459
li x13 252645135
li x14 63
add x8 x10 x0  #store the number for printing purpose

add x15 x10 x0  # storing the given value in x15
srai x16 x15 1 #i>>1
and x17 x16 x11 #i>>1 & 0x55555555
sub x15 x15 x17 # i- ((i>>1)&0x55555555)

and x16 x15 x12  # i & 0x33333333
srai x17 x15 2   # i>>2
and x18 x17 x12  # ((i >> 2) & 0x33333333)
add x15 x16 x18  #((i & 0x33333333)+((i >> 2) & 0x33333333))

srai x16 x15 4 # i>>4
add x17 x16 x15 # i+i>>4
and x15 x17 x13 # i=i+i>>4 & 0x0f0f0f0f

srai x16 x15 8
add x15 x15 x16

srai x16 x15 16 
add x15 x15 x16

addi x20 x0 32
sub x21 x20 x15 # x21 stores the number of zeroes in the number(subtract the number of ones from 32)

la x10 str2
li x17 4
ecall
mv x10 x8
li x17 1
ecall
la x10 newline
li x17 4
ecall
# print the number of zeroes
la x10 str1
li x17 4
ecall 
mv x10 x21
li x17 1
ecall
la x10 newline
li x17 4
ecall
#print number of ones
la x10 str
li x17 4
ecall
mv x10 x15
li x17 1
ecall




