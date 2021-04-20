	.text
	.section	.rodata
	.text
	.section	.sbss,"aw",@nobits
	.text
	.align	2
	.globl	test
	.type	test, @function
test:
	addi	sp,sp,-24
	sw	s0,20(sp)
	sw	ra,16(sp)
	addi	s0,sp,24
	mv	s4,a0
	mv	s5,a1
	mv	s3,a2
	mv	s8,a3
	mv	s7,a4
	mv	s2,a5
	mv	s6,a6
	addi	t1,s0,0
	lw	t1,0(t1)
	mv	t0,t1
	addi	t1,s0,4
	lw	t1,0(t1)
	mv	t2,t1
	addi	t1,s0,8
	lw	t1,0(t1)
	sub	s1,s4,s5
	seqz	s1,s1
	beq	s1,zero,.B1
	j	.B2
.B2:
	sub	s1,s5,s3
	snez	s1,s1
	bne	s1,zero,.B3
	sub	s9,s3,s8
	snez	s9,s9
	or	s1,s1,s9
	j	.B4
.B3:
	li	s1,1
	j	.B4
.B4:
	beq	s1,zero,.B5
	j	.B6
.B6:
	mv	a0,s5
	mv	a1,s3
	mv	a2,s8
	mv	a3,s7
	mv	a4,s2
	mv	a5,s6
	mv	a6,t0
	addi	t0,sp,0
	sw	t2,0(t0)
	addi	t0,sp,4
	sw	t1,0(t0)
	addi	t0,sp,8
	sw	s4,0(t0)
	call	test
	mv	t0,a0
	addi	t0,t0,1
	mv	a0,t0
	j	.B7
.B5:
	addi	s2,s2,-1
	addi	s1,s6,-2
	mv	a0,s5
	mv	a1,s3
	mv	a2,s8
	mv	a3,s7
	mv	a4,s2
	mv	a5,s1
	mv	a6,t0
	addi	t0,sp,0
	sw	t2,0(t0)
	addi	t0,sp,4
	sw	t1,0(t0)
	addi	t0,sp,8
	sw	s4,0(t0)
	call	test
	mv	t0,a0
	addi	t0,t0,2
	mv	a0,t0
	j	.B7
.B1:
	add	t0,s4,s5
	add	t0,t0,s4
	mv	a0,t0
	j	.B7
.B7:
	lw	s0,20(sp)
	lw	ra,16(sp)
	addi	sp,sp,24
	jr	ra
	.size	test, .-test
	.text
	.align	2
	.globl	main
	.type	main, @function
main:
	addi	sp,sp,-40
	sw	s0,36(sp)
	sw	ra,32(sp)
	addi	s0,sp,40
	li	t1,19260817
	li	s3,0
	li	a7,1
	bne	a7,zero,.B9
.B9:
	li	t2,1073741823
	mv	t0,t1
	li	t1,13
	sll	t1,t0,t1
	xor	t0,t0,t1
	li	s2,17
	li	t1,-2147483648
	li	a7,0
	slt	s1,t0,a7
	xori	s1,s1,1
	beq	s1,zero,.B10
	j	.B11
.B11:
	sra	t1,t0,s2
	mv	a0,t1
	j	.B12
.B12:
	mv	t1,a0
	xor	t0,t0,t1
	li	t1,5
	sll	t1,t0,t1
	xor	t0,t0,t1
	and	t0,t0,t2
	mv	t1,t0
	li	s1,1073741823
	li	t2,13
	sll	t2,t1,t2
	xor	t1,t1,t2
	li	s4,17
	li	t2,-2147483648
	li	a7,0
	slt	s2,t1,a7
	xori	s2,s2,1
	beq	s2,zero,.B13
	j	.B14
.B14:
	sra	t2,t1,s4
	mv	a0,t2
	j	.B15
.B15:
	mv	t2,a0
	xor	t1,t1,t2
	li	t2,5
	sll	t2,t1,t2
	xor	t1,t1,t2
	and	t1,t1,s1
	li	a7,255
	and	t0,t0,a7
	li	a7,255
	and	t2,t1,a7
	sub	t0,t0,t2
	snez	t0,t0
	beq	t0,zero,.B16
	j	.B17
.B17:
	li	t2,1073741823
	li	t0,13
	sll	t0,t1,t0
	xor	t1,t1,t0
	li	s2,17
	li	t0,-2147483648
	li	a7,0
	slt	s1,t1,a7
	xori	s1,s1,1
	beq	s1,zero,.B18
	j	.B19
.B19:
	sra	t0,t1,s2
	mv	a0,t0
	j	.B20
.B20:
	mv	t0,a0
	xor	t1,t1,t0
	li	t0,5
	sll	t0,t1,t0
	xor	t1,t1,t0
	and	t1,t1,t2
	mv	t0,t1
	li	s1,1073741823
	mv	t2,t0
	li	t1,13
	sll	t1,t2,t1
	xor	t2,t2,t1
	li	s4,17
	li	t1,-2147483648
	li	a7,0
	slt	s2,t2,a7
	xori	s2,s2,1
	beq	s2,zero,.B21
	j	.B22
.B22:
	sra	t1,t2,s4
	mv	a0,t1
	j	.B23
.B23:
	mv	t1,a0
	xor	t2,t2,t1
	li	t1,5
	sll	t1,t2,t1
	xor	t2,t2,t1
	and	t2,t2,s1
	li	s2,1073741823
	mv	s1,t2
	li	t1,13
	sll	t1,s1,t1
	xor	s1,s1,t1
	li	s5,17
	li	t1,-2147483648
	li	a7,0
	slt	s4,s1,a7
	xori	s4,s4,1
	beq	s4,zero,.B24
	j	.B25
.B25:
	sra	t1,s1,s5
	mv	a0,t1
	j	.B26
.B26:
	mv	t1,a0
	xor	s1,s1,t1
	li	t1,5
	sll	t1,s1,t1
	xor	s1,s1,t1
	and	s1,s1,s2
	mv	s4,s1
	li	s2,1073741823
	li	t1,13
	sll	t1,s4,t1
	xor	s4,s4,t1
	li	s6,17
	li	t1,-2147483648
	li	a7,0
	slt	s5,s4,a7
	xori	s5,s5,1
	beq	s5,zero,.B27
	j	.B28
.B28:
	sra	t1,s4,s6
	mv	a0,t1
	j	.B29
.B29:
	mv	t1,a0
	xor	s4,s4,t1
	li	t1,5
	sll	t1,s4,t1
	xor	s4,s4,t1
	and	s4,s4,s2
	mv	t1,s4
	li	s5,1073741823
	li	s2,13
	sll	s2,t1,s2
	xor	t1,t1,s2
	li	s7,17
	li	s2,-2147483648
	li	a7,0
	slt	s6,t1,a7
	xori	s6,s6,1
	beq	s6,zero,.B30
	j	.B31
.B31:
	sra	s2,t1,s7
	mv	a0,s2
	j	.B32
.B32:
	mv	s2,a0
	xor	t1,t1,s2
	li	s2,5
	sll	s2,t1,s2
	xor	t1,t1,s2
	and	t1,t1,s5
	li	a7,3
	and	s10,t0,a7
	li	a7,28
	sra	s8,t0,a7
	li	a7,1
	and	s7,t2,a7
	li	a7,29
	sra	s9,t2,a7
	li	a7,25
	sra	s5,s1,a7
	li	a7,31
	and	s6,s1,a7
	li	a7,15
	sra	t0,s4,a7
	li	a7,32767
	and	s2,s4,a7
	li	a7,15
	sra	s1,t1,a7
	li	a7,32767
	and	t2,t1,a7
	mv	a0,s10
	mv	a1,s8
	mv	a2,s7
	mv	a3,s9
	mv	a4,s5
	mv	a5,s6
	mv	a6,t0
	addi	t0,sp,0
	sw	s2,0(t0)
	addi	t0,sp,4
	sw	s1,0(t0)
	addi	t0,sp,8
	sw	t2,0(t0)
	sw	s3,-24(s0)
	sw	t1,-20(s0)
	call	test
	lw	s3,-24(s0)
	lw	t1,-20(s0)
	mv	t0,a0
	xor	s3,s3,t0
	j	.B9
.B30:
	li	a6,31
	sub	s6,a6,s7
	li	a6,1
	sll	s6,a6,s6
	xor	s2,t1,s2
	sra	s2,s2,s7
	or	s2,s6,s2
	mv	a0,s2
	j	.B32
.B27:
	li	a6,31
	sub	s5,a6,s6
	li	a6,1
	sll	s5,a6,s5
	xor	t1,s4,t1
	sra	t1,t1,s6
	or	t1,s5,t1
	mv	a0,t1
	j	.B29
.B24:
	li	a6,31
	sub	s4,a6,s5
	li	a6,1
	sll	s4,a6,s4
	xor	t1,s1,t1
	sra	t1,t1,s5
	or	t1,s4,t1
	mv	a0,t1
	j	.B26
.B21:
	li	a6,31
	sub	s2,a6,s4
	li	a6,1
	sll	s2,a6,s2
	xor	t1,t2,t1
	sra	t1,t1,s4
	or	t1,s2,t1
	mv	a0,t1
	j	.B23
.B18:
	li	a6,31
	sub	s1,a6,s2
	li	a6,1
	sll	s1,a6,s1
	xor	t0,t1,t0
	sra	t0,t0,s2
	or	t0,s1,t0
	mv	a0,t0
	j	.B20
.B16:
	mv	a0,s3
	sw	s3,-16(s0)
	call	toString
	lw	s3,-16(s0)
	mv	t0,a0
	mv	a0,t0
	sw	s3,-12(s0)
	call	println
	lw	s3,-12(s0)
	mv	t0,a0
	addi	t0,s3,-19
	mv	a0,t0
	j	.B33
.B13:
	li	a6,31
	sub	s2,a6,s4
	li	a6,1
	sll	s2,a6,s2
	xor	t2,t1,t2
	sra	t2,t2,s4
	or	t2,s2,t2
	mv	a0,t2
	j	.B15
.B10:
	li	a6,31
	sub	s1,a6,s2
	li	a6,1
	sll	s1,a6,s1
	xor	t1,t0,t1
	sra	t1,t1,s2
	or	t1,s1,t1
	mv	a0,t1
	j	.B12
.B33:
	lw	s0,36(sp)
	lw	ra,32(sp)
	addi	sp,sp,40
	jr	ra
	.size	main, .-main
