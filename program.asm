.data
arr: .word 3 2
.text
la x3 arr
lw x4 0(x3)
lw x5 4(x3)
add x6 x4 x5
