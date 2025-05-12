.text

li x8 400 #has the size of the L1 cache
li x9 4 #has the size of the word
li x8 100 #it maintains the value of X=(size of the cache)/(size of word)

addi x21 x0 0
addi x22 x0 10

Loop:
    beq x22 x21 done
    jal x1 Add
    addi x21 x21 1
    j Loop

Add:
    li x18 0
    bne CID 0 afterSum0
    addi x23 x0 0 #i=0
    addi x24 x0 25 #first 25
    Loop0:
        beq x23 x24 done0
        lw x30 0(x18)
        add x25 x25 x30
        addi x18 x18 400
        addi x23 x23 1
        j Loop0

    done0:
        j afterSum0
    
    afterSum0:
    SYNC
    bne CID 1 afterSum1
    addi x23 x0 0 #i=0
    addi x24 x0 25 #second 25
    
    addi x18 x0 10000
    Loop1:
        beq x23 x24 done1
        lw x30 0(x18)
        add x25 x25 x30
        addi x18 x18 400
        addi x23 x23 1
        j Loop1
    
    done1:
        j afterSum1
    
    afterSum1:
    SYNC
    bne CID 2 afterSum2
    addi x23 x0 0 #i=0
    addi x24 x0 25 #second 25
    
    addi x18 x0 20000
    Loop2:
        beq x23 x24 done2
        lw x30 0(x18)
        add x25 x25 x30
        addi x18 x18 400
        addi x23 x23 1
        j Loop2
    
    done2:
        j afterSum2

    afterSum2:
    SYNC
    bne CID 3 afterSum3
    addi x23 x0 0 #i=0
    addi x24 x0 25 #second 25
    
    addi x18 x0 30000
    Loop3:
        beq x23 x24 done3
        lw x30 0(x18)
        add x25 x25 x30
        addi x18 x18 400
        addi x23 x23 1
        j Loop3
    
    done3:
        j afterSum3

    afterSum3:
        SYNC
        jr x1

done:
    SYNC
    bne CID 0 mark1
    sw x25 90000(x0)

    mark1:
    SYNC
    bne CID 1 mark2
    sw x25 90004(x0)

    mark2:
    SYNC
    bne CID 2 mark3
    sw x25 90008(x0)

    mark3:
    SYNC
    bne CID 3 preproces
    sw x25 90012(x0)
preproces:
SYNC
j storingDone

storingDone:
SYNC
bne CID 0 Finish
lw x22 90000(x0)
lw x23 90004(x0)
lw x24 90008(x0)
lw x25 90012(x0)
add x23 x22 x23
add x24 x24 x23
add x25 x25 x24
sw x25 90000(x0)
li x17 1
lw x10 90000(x0)
ecall

Finish: