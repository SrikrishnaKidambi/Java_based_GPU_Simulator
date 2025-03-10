.data
arr: .word 42 98 77 22 64 70 18 87 55 35 92 91 53 36 31 19 14 21 67 28 34 94 2 72 78 51 59 47 50 46 9 5 84 24 57 90 79 65 97 60 30 41 89 48 40 95 17 38 16 4 29 49 45 6 44 100 83 71 85 32 15 73 86 63 13 3 52 68 61 69 88 7 39 75 80 76 81 99 23 8 82 96 10 43 20 27 66 12 11 56 33 54 1 74 62 93 25 58 37 26
str1: .string "\nThe sum of all the hundred elements is: "

.text
la x18 arr
addi x9 x0 0
addi x31 x0 100
addi x16 x0 0  #sum var
addi x8 x0 0
Loop:
beq x8 x31 Done
lw x25 0(x18)
add x16 x16 x25
sw x16 1020(x0)
addi x18 x18 4
addi x8 x8 1
j Loop

Done:
la x10 str1
li x17 4
ecall
lw x25 1020(x0)

mv x10 x25
li x17 1
ecall