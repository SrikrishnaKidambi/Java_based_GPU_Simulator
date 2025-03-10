
.data
arr: .word 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100
.text
la x14 arr
bne CID 0 after1
addi x13 x0 0
addi x11 x0 0
addi x12 x0 25
loop: beq x11 x12 after1
lw x15 0(x14)
add x13 x13 x15
sw x13 512(x0)
addi x14 x14 4
addi x11 x11 1
j loop
after1: bne CID 1 after2
addi x11 x0 0
addi x13 x0 0
addi x12 x0 25
loop_1: beq x11 x12 after2
lw x15 100(x14)
add x13 x13 x15
sw x13 516(x0)
addi x14 x14 4
addi x11 x11 1
j loop_1
after2: bne CID 2 after3
addi x11 x0 0
addi x13 x0 0
addi x12 x0 25
loop_2: beq x11 x12 after3
lw x15 200(x14)
add x13 x13 x15
sw x13 520(x0)
addi x14 x14 4
addi x11 x11 1
j loop_2
after3: bne CID 3 after4
addi x11 x0 0
addi x13 x0 0
addi x12 x0 25
loop_3: beq x11 x12 after4
lw x15 300(x14)
add x13 x13 x15
sw x13 524(x0)
addi x14 x14 4
addi x11 x11 1
j loop_3
after4: bne CID 0 nop
lw x17 512(x0)
lw x18 516(x0)
lw x19 520(x0)
lw x20 524(x0)
add x21 x17 x18
add x22 x21 x19
add x23 x22 x20
sw x23 528(x0)
nop: addi x0 x0 0
