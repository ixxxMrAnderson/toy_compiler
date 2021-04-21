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
	addi	sp,sp,-24
	sw	s0,20(sp)
	sw	ra,16(sp)
	addi	s0,sp,24
	li	a0,4
	call	Mx_malloc
	mv	t0,a0
	lui	a7,%hi(.G0)
	sw	t0,%lo(.G0)(a7)
	li	t0,0
.B1:
	lui	t1,%hi(.G0)
	lw	t1,%lo(.G0)(t1)
	mv	a0,t1
	sw	t0,-12(s0)
	call	Mx_array_size
	lw	t0,-12(s0)
	mv	t1,a0
	slt	t1,t0,t1
	beq	t1,zero,.B2
	j	.B3
.B3:
	li	s1,0
	lui	t2,%hi(.G0)
	lw	t2,%lo(.G0)(t2)
	addi	t1,t0,1
	li	a7,2
	sll	t1,t1,a7
	add	t1,t1,t2
	sw	s1,0(t1)
	lui	t1,%hi(.G0)
	lw	t1,%lo(.G0)(t1)
	addi	t2,t0,1
	li	a7,2
	sll	t2,t2,a7
	add	t1,t2,t1
	lw	t1,0(t1)
	mv	a0,t1
	sw	t0,-20(s0)
	call	toString
	lw	t0,-20(s0)
	mv	t1,a0
	mv	a0,t1
	sw	t0,-16(s0)
	call	print
	lw	t0,-16(s0)
	addi	t0,t0,1
	j	.B1
.B2:
	li	a0,0
	j	.B4
.B4:
	lw	s0,20(sp)
	lw	ra,16(sp)
	addi	sp,sp,24
	jr	ra
	.size	main, .-main
