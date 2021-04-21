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
	addi	s0,sp,12
	li	t0,19260817
	lui	a7,%hi(.G0)
	sw	t0,%lo(.G0)(a7)
.B1:
	lui	t0,%hi(.G0)
	lw	t0,%lo(.G0)(t0)
	li	a7,100
	rem	t0,t0,a7
	li	a7,0
	sub	t0,t0,a7
	snez	t0,t0
	beq	t0,zero,.B2
	j	.B3
.B3:
	lui	t0,%hi(.G0)
	lw	t0,%lo(.G0)(t0)
	addi	t0,t0,-1
	lui	a7,%hi(.G0)
	sw	t0,%lo(.G0)(a7)
	j	.B1
.B2:
	li	t1,1073741823
	lui	t0,%hi(.G0)
	lw	t0,%lo(.G0)(t0)
	li	t2,13
	sll	t0,t0,t2
	xor	t0,t0,t0
	li	s2,17
	li	t2,-2147483648
	li	a7,0
	slt	s1,t0,a7
	xori	s1,s1,1
	beq	s1,zero,.B4
	j	.B5
.B5:
	sra	t2,t0,s2
	mv	a0,t2
	j	.B6
.B6:
	mv	t0,a0
	xor	t0,t0,t0
	li	t2,5
	sll	t0,t0,t2
	xor	t0,t0,t0
	lui	a7,%hi(.G0)
	sw	t0,%lo(.G0)(a7)
	and	t0,t0,t1
	mv	a0,t0
	li	t0,1073741823
	lui	t1,%hi(.G0)
	lw	t1,%lo(.G0)(t1)
	li	t2,13
	sll	t1,t1,t2
	xor	t1,t1,t1
	li	s2,17
	li	t2,-2147483648
	li	a7,0
	slt	s1,t1,a7
	xori	s1,s1,1
	beq	s1,zero,.B7
	j	.B8
.B8:
	sra	t2,t1,s2
	mv	a0,t2
	j	.B9
.B9:
	mv	t1,a0
	xor	t1,t1,t1
	li	t2,5
	sll	t1,t1,t2
	xor	t1,t1,t1
	lui	a7,%hi(.G0)
	sw	t1,%lo(.G0)(a7)
	and	t0,t1,t0
	li	a7,127
	and	t0,t0,a7
	li	a7,100000
	add	t0,t0,a7
	li	t1,0
	j	.B10
.B10:
	slt	t2,t1,t0
	beq	t2,zero,.B11
	j	.B12
.B12:
	addi	t1,t1,1
	j	.B10
.B11:
	li	a0,0
	j	.B13
.B7:
	li	a6,31
	sub	s1,a6,s2
	li	a6,1
	sll	s1,a6,s1
	xor	t2,t1,t2
	sra	t2,t2,s2
	or	t2,s1,t2
	mv	a0,t2
	j	.B9
.B4:
	li	a6,31
	sub	s1,a6,s2
	li	a6,1
	sll	s1,a6,s1
	xor	t2,t0,t2
	sra	t2,t2,s2
	or	t2,s1,t2
	mv	a0,t2
	j	.B6
.B13:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	main, .-main
