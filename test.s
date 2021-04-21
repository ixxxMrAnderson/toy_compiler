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
	lui	a7,%hi(.G0)
	sw	a6,%lo(.G0)(a7)
	li	s1,0
	li	t1,0
.B1:
	li	a7,200
	slt	t0,t1,a7
	beq	t0,zero,.B2
	j	.B3
.B3:
	addi	t1,t1,1
	addi	s1,s1,1
	j	.B4
.B4:
	addi	t1,t1,1
	j	.B1
.B2:
	j	.B5
.B5:
	li	t0,114514
	li	a7,0
	beq	a7,zero,.B6
.B6:
	add	t1,t0,t0
	add	t1,t1,t0
	add	t0,t1,t0
	li	t0,0
	addi	t1,t0,1
	addi	t0,t1,1
	li	a7,2
	mul	t0,t0,a7
	li	a7,100000
	sub	t2,t0,a7
	li	s5,0
	li	s4,1
	li	s6,1
.B7:
	li	a7,100
	sgt	s3,s6,a7
	xori	s3,s3,1
	beq	s3,zero,.B8
	j	.B9
.B9:
	add	s5,s5,s6
	addi	s6,s6,1
	j	.B7
.B8:
.B10:
	li	a7,1
	slt	s3,s6,a7
	xori	s3,s3,1
	beq	s3,zero,.B11
	j	.B12
.B12:
	add	s5,s5,s6
	addi	s6,s6,-1
	j	.B10
.B11:
	addi	s6,s6,1
.B13:
	li	a7,10
	sgt	s3,s6,a7
	xori	s3,s3,1
	beq	s3,zero,.B14
	j	.B15
.B15:
	mul	s4,s4,s6
	addi	s6,s6,1
	j	.B13
.B14:
	sub	s3,t1,t0
	snez	s3,s3
	beq	s3,zero,.B16
	j	.B17
.B17:
	sub	s3,t1,t2
	snez	s3,s3
	beq	s3,zero,.B16
	j	.B18
.B18:
	add	s3,t1,t0
	add	s6,t0,t1
	sub	s3,s3,s6
	seqz	s3,s3
	beq	s3,zero,.B16
	j	.B19
.B19:
	sub	s3,t1,t0
	snez	s3,s3
	beq	s3,zero,.B16
	j	.B20
.B20:
	sub	s3,t1,t2
	snez	s3,s3
	beq	s3,zero,.B16
	j	.B21
.B21:
	add	s6,t1,t0
	add	s3,t0,t1
	sub	s3,s6,s3
	seqz	s3,s3
	beq	s3,zero,.B16
	j	.B22
.B22:
	sub	s3,t1,t0
	snez	s3,s3
	beq	s3,zero,.B16
	j	.B23
.B23:
	sub	s3,t1,t2
	snez	s3,s3
	beq	s3,zero,.B16
	j	.B24
.B24:
	add	s6,t1,t0
	add	s3,t0,t1
	sub	s3,s6,s3
	seqz	s3,s3
	beq	s3,zero,.B16
	j	.B25
.B25:
	sub	s3,t1,t0
	snez	s3,s3
	beq	s3,zero,.B16
	j	.B26
.B26:
	sub	s3,t1,t2
	snez	s3,s3
	beq	s3,zero,.B16
	j	.B27
.B27:
	add	s3,t1,t0
	add	s6,t0,t1
	sub	s3,s3,s6
	seqz	s3,s3
	beq	s3,zero,.B16
	j	.B28
.B28:
	li	a7,10100
	sub	s3,s5,a7
	seqz	s3,s3
	beq	s3,zero,.B16
	j	.B29
.B29:
	li	a7,10100
	sub	s3,s5,a7
	seqz	s3,s3
	beq	s3,zero,.B16
	j	.B30
.B30:
	li	a7,10100
	sub	s3,s5,a7
	seqz	s3,s3
	beq	s3,zero,.B16
	j	.B31
.B31:
	li	a7,10100
	sub	s3,s5,a7
	seqz	s3,s3
	beq	s3,zero,.B16
	j	.B32
.B32:
	li	a7,10100
	sub	s3,s5,a7
	seqz	s3,s3
	beq	s3,zero,.B16
	j	.B33
.B33:
	li	a7,10100
	sub	s3,s5,a7
	seqz	s3,s3
	beq	s3,zero,.B16
	j	.B34
.B34:
	li	a7,10100
	sub	s3,s5,a7
	seqz	s3,s3
	beq	s3,zero,.B16
	j	.B35
.B35:
	li	a7,10100
	sub	s3,s5,a7
	seqz	s3,s3
	beq	s3,zero,.B16
	j	.B36
.B36:
	sub	s3,t1,t0
	snez	s3,s3
	beq	s3,zero,.B16
	j	.B37
.B37:
	sub	s3,t1,t2
	snez	s3,s3
	beq	s3,zero,.B16
	j	.B38
.B38:
	add	s3,t1,t0
	add	s6,t0,t1
	sub	s3,s3,s6
	seqz	s3,s3
	beq	s3,zero,.B16
	j	.B39
.B39:
	sub	s3,t1,t0
	snez	s3,s3
	beq	s3,zero,.B16
	j	.B40
.B40:
	sub	t2,t1,t2
	snez	t2,t2
	beq	t2,zero,.B16
	j	.B41
.B41:
	add	t2,t1,t0
	add	t0,t0,t1
	sub	t0,t2,t0
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B42
.B42:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B43
.B43:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B44
.B44:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B45
.B45:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B46
.B46:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B47
.B47:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B48
.B48:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B49
.B49:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B50
.B50:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B51
.B51:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B52
.B52:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B53
.B53:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B54
.B54:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B55
.B55:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B56
.B56:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B57
.B57:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B58
.B58:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B59
.B59:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B60
.B60:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B61
.B61:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B62
.B62:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B63
.B63:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B64
.B64:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B65
.B65:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B66
.B66:
	li	a7,3628800
	sub	t0,s4,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B67
.B67:
	li	a7,10100
	sub	t0,s5,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B68
.B68:
	li	a7,3628800
	sub	t0,s4,a7
	seqz	t0,t0
	beq	t0,zero,.B16
	j	.B16
.B16:
	li	t0,1919
	add	t1,s1,t0
	li	t2,0
.B69:
	lui	t0,%hi(.G0)
	lw	t0,%lo(.G0)(t0)
	slt	t0,t2,t0
	beq	t0,zero,.B70
	j	.B71
.B71:
	li	t0,0
.B72:
	lui	s1,%hi(.G0)
	lw	s1,%lo(.G0)(s1)
	slt	s1,t0,s1
	beq	s1,zero,.B73
	j	.B74
.B74:
	addi	t0,t0,1
	j	.B72
.B73:
	addi	t2,t2,1
	j	.B69
.B70:
	li	t2,0
.B75:
	lui	t0,%hi(.G0)
	lw	t0,%lo(.G0)(t0)
	slt	t0,t2,t0
	beq	t0,zero,.B76
	j	.B77
.B77:
	li	a7,234
	mul	t0,s2,a7
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
	xor	s1,t0,a7
	lui	t0,%hi(.G0)
	lw	t0,%lo(.G0)(t0)
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
	div	s3,s2,a7
	li	a7,6
	mul	s3,s3,a7
	add	t0,t0,s3
	addi	t0,t0,7
	addi	t0,t0,0
	xor	t0,s1,t0
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
	mul	s1,s2,a7
	li	a6,12211342
	add	s1,a6,s1
	xor	s2,t0,s1
	addi	s2,s2,11
	li	s2,0
	addi	t2,t2,1
	j	.B75
.B76:
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
	j	.B78
.B78:
	lw	s0,8(sp)
	lw	ra,4(sp)
	addi	sp,sp,12
	jr	ra
	.size	main, .-main
