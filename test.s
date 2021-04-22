	.text
	.section	.rodata
	.align	2
.S0:
	.string	"str1"
	.align	2
.S1:
	.string	"str2"
	.align	2
.S2:
	.string	"str3"
	.align	2
.S3:
	.string	"str4"
	.align	2
.S4:
	.string	"str5"
	.align	2
.S5:
	.string	"str6"
	.align	2
.S6:
	.string	"str7"
	.align	2
.S7:
	.string	"str8"
	.align	2
.S8:
	.string	"str9"
	.align	2
.S9:
	.string	"str10"
	.text
	.section	.sbss,"aw",@nobits
	.globl	.G0
	.align	2
	.type	.G0, @object
	.size	.G0, 4
.G0:
	.zero	4
	.globl	.G1
	.align	2
	.type	.G1, @object
	.size	.G1, 4
.G1:
	.zero	4
	.text
	.align	2
	.globl	main
	.type	main, @function
main:
	addi	sp,sp,-60
	sw	s0,56(sp)
	sw	ra,52(sp)
	addi	s0,sp,60
	li	a0,30
	call	Mx_malloc
	mv	t0,a0
	li	t1,30
	li	a7,30
	beq	a7,zero,.B1
	li	a0,30
	sw	t0,-12(s0)
	call	Mx_malloc
	lw	t0,-12(s0)
	mv	t2,a0
	li	a6,30
	li	a7,2
	sll	t1,a6,a7
	li	a6,120
	add	t1,a6,t0
	sw	t2,0(t1)
	li	a6,30
	li	a7,1
	sub	t1,a6,a7
	li	t1,29
	lw	s0,56(sp)
	lw	ra,52(sp)
	addi	sp,sp,60
	jr	ra
	.size	main, .-main
.B2:
	lui	a7,%hi(.G0)
	sw	t0,%lo(.G0)(a7)
	li	a0,30
	call	Mx_malloc
	mv	t0,a0
	lui	a7,%hi(.G1)
	sw	t0,%lo(.G1)(a7)
	li	t2,0
.B3:
	li	a7,29
	sgt	t0,t2,a7
	xori	t0,t0,1
	beq	t0,zero,.B4
	j	.B5
.B5:
	lui	t0,%hi(.G0)
	lw	t0,%lo(.G0)(t0)
	addi	t1,t2,1
	li	a7,2
	sll	t1,t1,a7
	add	t0,t1,t0
	lw	t0,0(t0)
	li	a6,4
	add	t0,a6,t0
	lw	t0,0(t0)
	mv	a0,t0
	sw	t2,-16(s0)
	call	toString
	lw	t2,-16(s0)
	mv	s1,a0
	lui	t0,%hi(.G1)
	lw	t0,%lo(.G1)(t0)
	addi	t1,t2,1
	li	a7,2
	sll	t1,t1,a7
	add	t0,t1,t0
	sw	s1,0(t0)
	li	t1,0
.B6:
	slt	t0,t1,t2
	beq	t0,zero,.B7
	j	.B8
.B8:
	li	a7,1
	and	t0,t1,a7
	li	a7,0
	sub	t0,t0,a7
	seqz	t0,t0
	beq	t0,zero,.B9
	j	.B9
.B9:
	li	a7,1
	and	t0,t1,a7
	li	a7,1
	sub	t0,t0,a7
	seqz	t0,t0
	beq	t0,zero,.B10
	j	.B10
.B10:
	addi	t1,t1,1
	j	.B6
.B7:
	lui	t1,%hi(.S0)
	addi	t1,t1,%lo(.S0)
	lui	t0,%hi(.S1)
	addi	t0,t0,%lo(.S1)
	mv	a0,t1
	mv	a1,t0
	sw	t2,-56(s0)
	call	Mx_string_ADD
	lw	t2,-56(s0)
	mv	t0,a0
	lui	t1,%hi(.S2)
	addi	t1,t1,%lo(.S2)
	mv	a0,t0
	mv	a1,t1
	sw	t2,-52(s0)
	call	Mx_string_ADD
	lw	t2,-52(s0)
	mv	t0,a0
	lui	t1,%hi(.S3)
	addi	t1,t1,%lo(.S3)
	mv	a0,t0
	mv	a1,t1
	sw	t2,-48(s0)
	call	Mx_string_ADD
	lw	t2,-48(s0)
	mv	t0,a0
	lui	t1,%hi(.S4)
	addi	t1,t1,%lo(.S4)
	mv	a0,t0
	mv	a1,t1
	sw	t2,-44(s0)
	call	Mx_string_ADD
	lw	t2,-44(s0)
	mv	t0,a0
	lui	t1,%hi(.S5)
	addi	t1,t1,%lo(.S5)
	mv	a0,t0
	mv	a1,t1
	sw	t2,-40(s0)
	call	Mx_string_ADD
	lw	t2,-40(s0)
	mv	t1,a0
	lui	t0,%hi(.S6)
	addi	t0,t0,%lo(.S6)
	mv	a0,t1
	mv	a1,t0
	sw	t2,-36(s0)
	call	Mx_string_ADD
	lw	t2,-36(s0)
	mv	t1,a0
	lui	t0,%hi(.S7)
	addi	t0,t0,%lo(.S7)
	mv	a0,t1
	mv	a1,t0
	sw	t2,-32(s0)
	call	Mx_string_ADD
	lw	t2,-32(s0)
	mv	t0,a0
	lui	t1,%hi(.S8)
	addi	t1,t1,%lo(.S8)
	mv	a0,t0
	mv	a1,t1
	sw	t2,-28(s0)
	call	Mx_string_ADD
	lw	t2,-28(s0)
	mv	t0,a0
	lui	t1,%hi(.S9)
	addi	t1,t1,%lo(.S9)
	mv	a0,t0
	mv	a1,t1
	sw	t2,-24(s0)
	call	Mx_string_ADD
	lw	t2,-24(s0)
	mv	t0,a0
	mv	a0,t0
	sw	t2,-20(s0)
	call	println
	lw	t2,-20(s0)
	addi	t2,t2,1
	j	.B3
.B4:
	li	a0,0
	j	.B11
.B11:
	lw	s0,56(sp)
	lw	ra,52(sp)
	addi	sp,sp,60
	jr	ra
	.size	main, .-main
