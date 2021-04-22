	.text
	.section	.rodata
	.text
	.section	.sbss,"aw",@nobits
	.globl	.G0
	.align	2
	.type	.G0, @object
	.size	.G0, 4
.G0:
	.zero	4
	.text
	.align	2
	.globl	main
	.type	main, @function
main:
	addi	sp,sp,-12
	sw	s0,8(sp)
	sw	ra,4(sp)
	addi	s0,sp,12
	li	t0,0
	li	s1,0
.B1:
	li	a7,200
	slt	t1,s1,a7
	beq	t1,zero,.B2
	j	.B3
.B3:
	addi	s1,s1,1
	addi	t0,t0,1
	j	.B4
.B4:
	addi	s1,s1,1
	j	.B1
.B2:
	j	.B5
.B5:
	li	t1,114514
	li	a7,0
	beq	a7,zero,.B6
.B6:
	li	a6,343542
	li	a7,114514
	add	t1,a6,a7
	li	t1,0
	li	s2,0
	li	s1,1
	li	s3,1
.B7:
	li	a7,100
	sgt	t1,s3,a7
	xori	t1,t1,1
	beq	t1,zero,.B8
	j	.B9
.B9:
	add	s2,s2,s3
	addi	s3,s3,1
	j	.B7
.B8:
.B10:
	li	a7,1
	slt	t1,s3,a7
	xori	t1,t1,1
	beq	t1,zero,.B11
	j	.B12
.B12:
	add	s2,s2,s3
	addi	s3,s3,-1
	j	.B10
.B11:
	addi	s3,s3,1
.B13:
	li	a7,10
	sgt	t1,s3,a7
	xori	t1,t1,1
	beq	t1,zero,.B14
	j	.B15
.B15:
	mul	s1,s1,s3
	addi	s3,s3,1
	j	.B13
.B14:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B17
.B17:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B18
.B18:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B19
.B19:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B20
.B20:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B21
.B21:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B22
.B22:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B23
.B23:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B24
.B24:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B25
.B25:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B26
.B26:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B27
.B27:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B28
.B28:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B29
.B29:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B30
.B30:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B31
.B31:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B32
.B32:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B33
.B33:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B34
.B34:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B35
.B35:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B36
.B36:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B37
.B37:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B38
.B38:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B39
.B39:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B40
.B40:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B41
.B41:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B42
.B42:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B43
.B43:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B44
.B44:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B45
.B45:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B46
.B46:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B47
.B47:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B48
.B48:
	li	a7,3628800
	sub	t1,s1,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B49
.B49:
	li	a7,10100
	sub	t1,s2,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B50
.B50:
	li	a7,3628800
	sub	t1,s1,a7
	seqz	t1,t1
	beq	t1,zero,.B16
	j	.B16
.B16:
	li	t1,1919
	add	t1,t0,t1
	li	s1,0
.B51:
	li	t0,80
	slt	t0,s1,t0
	beq	t0,zero,.B52
	j	.B53
.B53:
	li	t0,0
.B54:
	li	s2,80
	slt	s2,t0,s2
	beq	s2,zero,.B55
	j	.B56
.B56:
	addi	t0,t0,1
	j	.B54
.B55:
	addi	s1,s1,1
	j	.B51
.B52:
	li	s1,0
.B57:
	li	t0,80
	slt	t0,s1,t0
	beq	t0,zero,.B58
	j	.B59
.B59:
	li	a7,234
	mul	t0,t2,a7
	li	a7,111
	add	t0,t0,a7
	addi	t0,t0,0
	addi	t0,t0,7
	addi	t0,t0,0
	addi	t0,t0,0
	addi	t0,t0,7
	addi	t0,t0,0
	addi	t0,t0,0
	addi	t0,t0,7
	addi	t0,t0,0
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12719578
	xor	s2,t0,a7
	li	t0,80
	li	a7,508104
	add	t0,t0,a7
	li	a7,111
	add	t0,t0,a7
	addi	t0,t0,0
	addi	t0,t0,7
	addi	t0,t0,0
	addi	t0,t0,0
	addi	t0,t0,7
	addi	t0,t0,0
	li	a7,5
	div	s3,t2,a7
	li	a7,6
	mul	s3,s3,a7
	add	t0,t0,s3
	addi	t0,t0,7
	addi	t0,t0,0
	xor	t0,s2,t0
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12719578
	xor	t0,t0,a7
	li	a7,12
	mul	t2,t2,a7
	li	a6,12211342
	add	t2,a6,t2
	xor	t2,t0,t2
	addi	t2,t2,11
	li	t2,0
	addi	s1,s1,1
	j	.B57
.B58:
	li	t0,114514
	add	t0,t1,t0
	li	a7,50997
	sub	t0,t0,a7
	mv	a0,t0
	call	toString
	mv	t0,a0
	mv	a0,t0
	call	println
	li	a0,0
	j	.B60
.B60:
	lw	s0,8(sp)
	lw	ra,4(sp)
	addi	sp,sp,12
	jr	ra
	.size	main, .-main
