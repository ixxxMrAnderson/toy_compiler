    .file	"builtin.c"
	.text
	.section	.rodata
	.align	2
.LC0:
	.string	"%d"
	.text
	.align	2
	.globl	getInt
	.type	getInt, @function
getInt:
	addi	sp,sp,-32
	sw	ra,28(sp)
	sw	s0,24(sp)
	addi	s0,sp,32
	addi	a5,s0,-20
	mv	a1,a5
	lui	a5,%hi(.LC0)
	addi	a0,a5,%lo(.LC0)
	call	scanf
	lw	a5,-20(s0)
	mv	a0,a5
	lw	ra,28(sp)
	lw	s0,24(sp)
	addi	sp,sp,32
	jr	ra
	.size	getInt, .-getInt
	.section	.rodata
	.align	2
.LC1:
	.string	"%s"
	.text
	.align	2
	.globl	print
	.type	print, @function
print:
	addi	sp,sp,-32
	sw	ra,28(sp)
	sw	s0,24(sp)
	addi	s0,sp,32
	sw	a0,-20(s0)
	lw	a1,-20(s0)
	lui	a5,%hi(.LC1)
	addi	a0,a5,%lo(.LC1)
	call	printf
	nop
	lw	ra,28(sp)
	lw	s0,24(sp)
	addi	sp,sp,32
	jr	ra
	.size	print, .-print
	.align	2
	.globl	println
	.type	println, @function
println:
	addi	sp,sp,-32
	sw	ra,28(sp)
	sw	s0,24(sp)
	addi	s0,sp,32
	sw	a0,-20(s0)
	lw	a0,-20(s0)
	call	puts
	nop
	lw	ra,28(sp)
	lw	s0,24(sp)
	addi	sp,sp,32
	jr	ra
	.size	println, .-println
	.align	2
	.globl	printInt
	.type	printInt, @function
printInt:
	addi	sp,sp,-32
	sw	ra,28(sp)
	sw	s0,24(sp)
	addi	s0,sp,32
	sw	a0,-20(s0)
	lw	a1,-20(s0)
	lui	a5,%hi(.LC0)
	addi	a0,a5,%lo(.LC0)
	call	printf
	nop
	lw	ra,28(sp)
	lw	s0,24(sp)
	addi	sp,sp,32
	jr	ra
	.size	printInt, .-printInt
	.section	.rodata
	.align	2
.LC2:
	.string	"%d\n"
	.text
	.align	2
	.globl	printlnInt
	.type	printlnInt, @function
printlnInt:
	addi	sp,sp,-32
	sw	ra,28(sp)
	sw	s0,24(sp)
	addi	s0,sp,32
	sw	a0,-20(s0)
	lw	a1,-20(s0)
	lui	a5,%hi(.LC2)
	addi	a0,a5,%lo(.LC2)
	call	printf
	nop
	lw	ra,28(sp)
	lw	s0,24(sp)
	addi	sp,sp,32
	jr	ra
	.size	printlnInt, .-printlnInt
	.align	2
	.globl	getString
	.type	getString, @function
getString:
	addi	sp,sp,-32
	sw	ra,28(sp)
	sw	s0,24(sp)
	addi	s0,sp,32
	li	a0,1000
	call	malloc
	mv	a5,a0
	sw	a5,-20(s0)
	lw	a1,-20(s0)
	lui	a5,%hi(.LC1)
	addi	a0,a5,%lo(.LC1)
	call	scanf
	lw	a5,-20(s0)
	mv	a0,a5
	lw	ra,28(sp)
	lw	s0,24(sp)
	addi	sp,sp,32
	jr	ra
	.size	getString, .-getString
	.align	2
	.globl	toString
	.type	toString, @function
toString:
	addi	sp,sp,-64
	sw	ra,60(sp)
	sw	s0,56(sp)
	addi	s0,sp,64
	sw	a0,-52(s0)
	sw	zero,-20(s0)
	lw	a5,-52(s0)
	bge	a5,zero,.L10
	lw	a5,-52(s0)
	neg	a5,a5
	sw	a5,-52(s0)
	li	a5,1
	sw	a5,-20(s0)
.L10:
	lw	a5,-52(s0)
	sw	a5,-24(s0)
	sw	zero,-28(s0)
.L11:
	lw	a4,-24(s0)
	li	a5,10
	div	a5,a4,a5
	sw	a5,-24(s0)
	lw	a5,-28(s0)
	addi	a5,a5,1
	sw	a5,-28(s0)
	lw	a5,-24(s0)
	bne	a5,zero,.L11
	lw	a5,-28(s0)
	addi	a4,a5,1
	lw	a5,-20(s0)
	add	a5,a4,a5
	mv	a0,a5
	call	malloc
	mv	a5,a0
	sw	a5,-36(s0)
	lw	a4,-28(s0)
	lw	a5,-20(s0)
	add	a5,a4,a5
	lw	a4,-36(s0)
	add	a5,a4,a5
	sw	a5,-32(s0)
	lw	a5,-20(s0)
	beq	a5,zero,.L12
	lw	a5,-36(s0)
	li	a4,45
	sb	a4,0(a5)
.L12:
	lw	a5,-32(s0)
	addi	a4,a5,-1
	sw	a4,-32(s0)
	sb	zero,0(a5)
.L13:
	lw	a4,-52(s0)
	li	a5,10
	rem	a5,a4,a5
	andi	a4,a5,0xff
	lw	a5,-32(s0)
	addi	a3,a5,-1
	sw	a3,-32(s0)
	addi	a4,a4,48
	andi	a4,a4,0xff
	sb	a4,0(a5)
	lw	a4,-52(s0)
	li	a5,10
	div	a5,a4,a5
	sw	a5,-52(s0)
	lw	a5,-52(s0)
	bne	a5,zero,.L13
	lw	a5,-36(s0)
	mv	a0,a5
	lw	ra,60(sp)
	lw	s0,56(sp)
	addi	sp,sp,64
	jr	ra
	.size	toString, .-toString
	.align	2
	.globl	Mx_malloc
	.type	Mx_malloc, @function
Mx_malloc:
	addi	sp,sp,-48
	sw	ra,44(sp)
	sw	s0,40(sp)
	addi	s0,sp,48
	sw	a0,-36(s0)
	lw	a5,-36(s0)
	addi	a5,a5,1
	slli	a5,a5,2
	mv	a0,a5
	call	malloc
	mv	a5,a0
	sw	a5,-20(s0)
	lw	a5,-20(s0)
	lw	a4,-36(s0)
	sw	a4,0(a5)
	lw	a5,-20(s0)
	mv	a0,a5
	lw	ra,44(sp)
	lw	s0,40(sp)
	addi	sp,sp,48
	jr	ra
	.size	Mx_malloc, .-Mx_malloc
	.align	2
	.globl	Mx_array_size
	.type	Mx_array_size, @function
Mx_array_size:
	addi	sp,sp,-32
	sw	s0,28(sp)
	addi	s0,sp,32
	sw	a0,-20(s0)
	lw	a5,-20(s0)
	lw	a5,0(a5)
	mv	a0,a5
	lw	s0,28(sp)
	addi	sp,sp,32
	jr	ra
	.size	Mx_array_size, .-Mx_array_size
	.align	2
	.globl	Mx_string_substring
	.type	Mx_string_substring, @function
Mx_string_substring:
	addi	sp,sp,-48
	sw	ra,44(sp)
	sw	s0,40(sp)
	addi	s0,sp,48
	sw	a0,-36(s0)
	sw	a1,-40(s0)
	sw	a2,-44(s0)
	lw	a4,-44(s0)
	lw	a5,-40(s0)
	sub	a5,a4,a5
	addi	a5,a5,1
	mv	a0,a5
	call	malloc
	mv	a5,a0
	sw	a5,-20(s0)
	lw	a5,-40(s0)
	lw	a4,-36(s0)
	add	a3,a4,a5
	lw	a4,-44(s0)
	lw	a5,-40(s0)
	sub	a5,a4,a5
	mv	a2,a5
	mv	a1,a3
	lw	a0,-20(s0)
	call	memcpy
	lw	a4,-44(s0)
	lw	a5,-40(s0)
	sub	a5,a4,a5
	mv	a4,a5
	lw	a5,-20(s0)
	add	a5,a5,a4
	sb	zero,0(a5)
	lw	a5,-20(s0)
	mv	a0,a5
	lw	ra,44(sp)
	lw	s0,40(sp)
	addi	sp,sp,48
	jr	ra
	.size	Mx_string_substring, .-Mx_string_substring
	.align	2
	.globl	Mx_string_parseInt
	.type	Mx_string_parseInt, @function
Mx_string_parseInt:
	addi	sp,sp,-48
	sw	ra,44(sp)
	sw	s0,40(sp)
	addi	s0,sp,48
	sw	a0,-36(s0)
	addi	a5,s0,-20
	mv	a2,a5
	lui	a5,%hi(.LC0)
	addi	a1,a5,%lo(.LC0)
	lw	a0,-36(s0)
	call	sscanf
	lw	a5,-20(s0)
	mv	a0,a5
	lw	ra,44(sp)
	lw	s0,40(sp)
	addi	sp,sp,48
	jr	ra
	.size	Mx_string_parseInt, .-Mx_string_parseInt
	.align	2
	.globl	Mx_string_ord
	.type	Mx_string_ord, @function
Mx_string_ord:
	addi	sp,sp,-32
	sw	s0,28(sp)
	addi	s0,sp,32
	sw	a0,-20(s0)
	sw	a1,-24(s0)
	lw	a5,-24(s0)
	lw	a4,-20(s0)
	add	a5,a4,a5
	lbu	a5,0(a5)
	mv	a0,a5
	lw	s0,28(sp)
	addi	sp,sp,32
	jr	ra
	.size	Mx_string_ord, .-Mx_string_ord
	.align	2
	.globl	Mx_string_length
	.type	Mx_string_length, @function
Mx_string_length:
	addi	sp,sp,-32
	sw	ra,28(sp)
	sw	s0,24(sp)
	addi	s0,sp,32
	sw	a0,-20(s0)
	lw	a0,-20(s0)
	call	strlen
	mv	a5,a0
	mv	a0,a5
	lw	ra,28(sp)
	lw	s0,24(sp)
	addi	sp,sp,32
	jr	ra
	.size	Mx_string_length, .-Mx_string_length
	.align	2
	.globl	Mx_string_ADD
	.type	Mx_string_ADD, @function
Mx_string_ADD:
	addi	sp,sp,-48
	sw	ra,44(sp)
	sw	s0,40(sp)
	addi	s0,sp,48
	sw	a0,-36(s0)
	sw	a1,-40(s0)
	lw	a0,-36(s0)
	call	strlen
	mv	a5,a0
	sw	a5,-20(s0)
	lw	a0,-40(s0)
	call	strlen
	mv	a5,a0
	sw	a5,-24(s0)
	lw	a4,-20(s0)
	lw	a5,-24(s0)
	add	a5,a4,a5
	mv	a0,a5
	call	malloc
	mv	a5,a0
	sw	a5,-28(s0)
	lw	a5,-20(s0)
	mv	a2,a5
	lw	a1,-36(s0)
	lw	a0,-28(s0)
	call	memcpy
	lw	a5,-20(s0)
	lw	a4,-28(s0)
	add	a5,a4,a5
	lw	a4,-24(s0)
	mv	a2,a4
	lw	a1,-40(s0)
	mv	a0,a5
	call	memcpy
	lw	a4,-20(s0)
	lw	a5,-24(s0)
	add	a5,a4,a5
	mv	a4,a5
	lw	a5,-28(s0)
	add	a5,a5,a4
	sb	zero,0(a5)
	lw	a5,-28(s0)
	mv	a0,a5
	lw	ra,44(sp)
	lw	s0,40(sp)
	addi	sp,sp,48
	jr	ra
	.size	Mx_string_ADD, .-Mx_string_ADD
	.align	2
	.globl	Mx_string_EQ
	.type	Mx_string_EQ, @function
Mx_string_EQ:
	addi	sp,sp,-32
	sw	ra,28(sp)
	sw	s0,24(sp)
	addi	s0,sp,32
	sw	a0,-20(s0)
	sw	a1,-24(s0)
	lw	a1,-24(s0)
	lw	a0,-20(s0)
	call	strcmp
	mv	a5,a0
	seqz	a5,a5
	andi	a5,a5,0xff
	mv	a0,a5
	lw	ra,28(sp)
	lw	s0,24(sp)
	addi	sp,sp,32
	jr	ra
	.size	Mx_string_EQ, .-Mx_string_EQ
	.align	2
	.globl	Mx_string_NEQ
	.type	Mx_string_NEQ, @function
Mx_string_NEQ:
	addi	sp,sp,-32
	sw	ra,28(sp)
	sw	s0,24(sp)
	addi	s0,sp,32
	sw	a0,-20(s0)
	sw	a1,-24(s0)
	lw	a1,-24(s0)
	lw	a0,-20(s0)
	call	strcmp
	mv	a5,a0
	snez	a5,a5
	andi	a5,a5,0xff
	mv	a0,a5
	lw	ra,28(sp)
	lw	s0,24(sp)
	addi	sp,sp,32
	jr	ra
	.size	Mx_string_NEQ, .-Mx_string_NEQ
	.align	2
	.globl	Mx_string_LTH
	.type	Mx_string_LTH, @function
Mx_string_LTH:
	addi	sp,sp,-32
	sw	ra,28(sp)
	sw	s0,24(sp)
	addi	s0,sp,32
	sw	a0,-20(s0)
	sw	a1,-24(s0)
	lw	a1,-24(s0)
	lw	a0,-20(s0)
	call	strcmp
	mv	a5,a0
	srli	a5,a5,31
	andi	a5,a5,0xff
	mv	a0,a5
	lw	ra,28(sp)
	lw	s0,24(sp)
	addi	sp,sp,32
	jr	ra
	.size	Mx_string_LTH, .-Mx_string_LTH
	.align	2
	.globl	Mx_string_GTH
	.type	Mx_string_GTH, @function
Mx_string_GTH:
	addi	sp,sp,-32
	sw	ra,28(sp)
	sw	s0,24(sp)
	addi	s0,sp,32
	sw	a0,-20(s0)
	sw	a1,-24(s0)
	lw	a1,-24(s0)
	lw	a0,-20(s0)
	call	strcmp
	mv	a5,a0
	sgt	a5,a5,zero
	andi	a5,a5,0xff
	mv	a0,a5
	lw	ra,28(sp)
	lw	s0,24(sp)
	addi	sp,sp,32
	jr	ra
	.size	Mx_string_GTH, .-Mx_string_GTH
	.align	2
	.globl	Mx_string_LEQ
	.type	Mx_string_LEQ, @function
Mx_string_LEQ:
	addi	sp,sp,-32
	sw	ra,28(sp)
	sw	s0,24(sp)
	addi	s0,sp,32
	sw	a0,-20(s0)
	sw	a1,-24(s0)
	lw	a1,-24(s0)
	lw	a0,-20(s0)
	call	strcmp
	mv	a5,a0
	slti	a5,a5,1
	andi	a5,a5,0xff
	mv	a0,a5
	lw	ra,28(sp)
	lw	s0,24(sp)
	addi	sp,sp,32
	jr	ra
	.size	Mx_string_LEQ, .-Mx_string_LEQ
	.align	2
	.globl	Mx_string_GEQ
	.type	Mx_string_GEQ, @function
Mx_string_GEQ:
	addi	sp,sp,-32
	sw	ra,28(sp)
	sw	s0,24(sp)
	addi	s0,sp,32
	sw	a0,-20(s0)
	sw	a1,-24(s0)
	lw	a1,-24(s0)
	lw	a0,-20(s0)
	call	strcmp
	mv	a5,a0
	not	a5,a5
	srli	a5,a5,31
	andi	a5,a5,0xff
	mv	a0,a5
	lw	ra,28(sp)
	lw	s0,24(sp)
	addi	sp,sp,32
	jr	ra
	.size	Mx_string_GEQ, .-Mx_string_GEQ
	.ident	"GCC: (GNU) 10.2.0"