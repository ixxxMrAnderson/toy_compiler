	.text
	.section	.rodata
	.text
	.section	.sbss,"aw",@nobits
	.text
	.align	2
	.globl	gcd
	.type	gcd, @function
gcd:
	addi	sp,sp,-12
	sw	s0,8(sp)
	sw	ra,4(sp)
	addi	s0,sp,12
	mv	t0,a0
	mv	t1,a1
	rem	t2,t0,t1
	li	a7,0
	sub	t2,t2,a7
	seqz	t2,t2
	beq	t2,zero,.B1
	j	.B2
.B2:
	mv	a0,t1
	j	.B3
.B1:
	rem	t0,t0,t1
	mv	a0,t1
	mv	a1,t0
	call	gcd
	mv	t0,a0
	mv	a0,t0
	j	.B3
.B3:
	lw	s0,8(sp)
	lw	ra,4(sp)
	addi	sp,sp,12
	jr	ra
	.size	gcd, .-gcd
	.text
	.align	2
	.globl	main
	.type	main, @function
main:
	addi	sp,sp,-12
	sw	s0,8(sp)
	sw	ra,4(sp)
	addi	s0,sp,12
	li	a0,10
	li	a1,1
	j	.B5
.B5:
	mv	t0,a0
	mv	t1,a1
	rem	t2,t0,t1
	li	a7,0
	sub	t2,t2,a7
	seqz	t2,t2
	beq	t2,zero,.B6
	j	.B7
.B7:
	mv	a0,t1
	j	.B8
.B8:
	mv	t0,a0
	mv	a0,t0
	call	toString
	mv	t0,a0
	mv	a0,t0
	call	println
	li	a0,34986
	li	a1,3087
	call	gcd
	mv	t0,a0
	mv	a0,t0
	call	toString
	mv	t0,a0
	mv	a0,t0
	call	println
	li	a0,2907
	li	a1,1539
	call	gcd
	mv	t0,a0
	mv	a0,t0
	call	toString
	mv	t0,a0
	mv	a0,t0
	call	println
	li	a0,0
	j	.B9
.B6:
	rem	t0,t0,t1
	mv	a0,t1
	mv	a1,t0
	call	gcd
	mv	t0,a0
	mv	a0,t0
	j	.B8
.B9:
	lw	s0,8(sp)
	lw	ra,4(sp)
	addi	sp,sp,12
	jr	ra
	.size	main, .-main
