.data 


.text
<<<<<<< HEAD

addi X11 x0 3
addi x12 x0 4
j addFun
addi x14 x11 x11
=======
addi x8 x0 8
addi x11 x0 8
beq x11 x8 addFun
addi x14 x8 0
>>>>>>> a9fcff342ad96232990d625dd059fb874a1d938a
addFun:
	add x13 x11 x12
	mv x10 x11
	li x17 1
	addi x15 x13 8