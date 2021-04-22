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
	.globl	.G1
	.align	2
	.type	.G1, @object
	.size	.G1, 4
.G1:
	.zero	4
	.text
	.align	2
	.globl	add4096
	.type	add4096, @function
add4096:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	addi	t0,t0,1
	li	a0,0
	j	.B1
.B1:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add4096, .-add4096
	.text
	.align	2
	.globl	add16
	.type	add16, @function
add16:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	li	a0,0
	j	.B3
.B3:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add16, .-add16
	.text
	.align	2
	.globl	add128
	.type	add128, @function
add128:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	li	a0,0
	j	.B5
.B5:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add128, .-add128
	.text
	.align	2
	.globl	add32
	.type	add32, @function
add32:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	li	a0,0
	j	.B7
.B7:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add32, .-add32
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
	lui	a7,%hi(.G0)
	sw	t0,%lo(.G0)(a7)
	li	t0,1
	lui	a7,%hi(.G1)
	sw	t0,%lo(.G1)(a7)
	call	sanity_check
	mv	t0,a0
	addi	t0,t0,-1
	li	a0,0
	j	.B9
.B9:
	lw	s0,8(sp)
	lw	ra,4(sp)
	addi	sp,sp,12
	jr	ra
	.size	main, .-main
	.text
	.align	2
	.globl	add8192
	.type	add8192, @function
add8192:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	li	a0,0
	j	.B11
.B11:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add8192, .-add8192
	.text
	.align	2
	.globl	add65536
	.type	add65536, @function
add65536:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	li	a0,0
	j	.B13
.B13:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add65536, .-add65536
	.text
	.align	2
	.globl	add262144
	.type	add262144, @function
add262144:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	li	a0,0
	j	.B15
.B15:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add262144, .-add262144
	.text
	.align	2
	.globl	add16384
	.type	add16384, @function
add16384:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	li	a0,0
	j	.B17
.B17:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add16384, .-add16384
	.text
	.align	2
	.globl	block
	.type	block, @function
block:
	addi	sp,sp,-12
	sw	s0,8(sp)
	sw	ra,4(sp)
	addi	s0,sp,12
	call	block
	mv	t0,a0
	li	a0,0
	j	.B19
.B19:
	lw	s0,8(sp)
	lw	ra,4(sp)
	addi	sp,sp,12
	jr	ra
	.size	block, .-block
	.text
	.align	2
	.globl	add8
	.type	add8, @function
add8:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	li	a0,0
	j	.B21
.B21:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add8, .-add8
	.text
	.align	2
	.globl	add64
	.type	add64, @function
add64:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	li	a0,0
	j	.B23
.B23:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add64, .-add64
	.text
	.align	2
	.globl	add2048
	.type	add2048, @function
add2048:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	li	a0,0
	j	.B25
.B25:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add2048, .-add2048
	.text
	.align	2
	.globl	add2
	.type	add2, @function
add2:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	li	a0,0
	j	.B27
.B27:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add2, .-add2
	.text
	.align	2
	.globl	add1
	.type	add1, @function
add1:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	addi	t0,t0,1
	li	a0,0
	j	.B29
.B29:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add1, .-add1
	.text
	.align	2
	.globl	add4
	.type	add4, @function
add4:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	li	a0,0
	j	.B31
.B31:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add4, .-add4
	.text
	.align	2
	.globl	add512
	.type	add512, @function
add512:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	li	a0,0
	j	.B33
.B33:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add512, .-add512
	.text
	.align	2
	.globl	add524288
	.type	add524288, @function
add524288:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	li	a0,0
	j	.B35
.B35:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add524288, .-add524288
	.text
	.align	2
	.globl	add256
	.type	add256, @function
add256:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	li	a0,0
	j	.B37
.B37:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add256, .-add256
	.text
	.align	2
	.globl	add131072
	.type	add131072, @function
add131072:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	li	a0,0
	j	.B39
.B39:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add131072, .-add131072
	.text
	.align	2
	.globl	wppp
	.type	wppp, @function
wppp:
	addi	sp,sp,-12
	sw	s0,8(sp)
	sw	ra,4(sp)
	addi	s0,sp,12
	call	wpp
	call	wpp
	call	wpp
	call	wpp
	call	wpp
	call	wpp
	call	wpp
	call	wpp
	mv	t0,a0
	li	a0,0
	j	.B41
.B41:
	lw	s0,8(sp)
	lw	ra,4(sp)
	addi	sp,sp,12
	jr	ra
	.size	wppp, .-wppp
	.text
	.align	2
	.globl	add32768
	.type	add32768, @function
add32768:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	li	a0,0
	j	.B43
.B43:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add32768, .-add32768
	.text
	.align	2
	.globl	wpp
	.type	wpp, @function
wpp:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	lui	t0,%hi(.G0)
	lw	t0,%lo(.G0)(t0)
	addi	t0,t0,1
	lui	a7,%hi(.G0)
	sw	t0,%lo(.G0)(a7)
	lui	t0,%hi(.G0)
	lw	t0,%lo(.G0)(t0)
	addi	t0,t0,1
	lui	a7,%hi(.G0)
	sw	t0,%lo(.G0)(a7)
	li	a0,0
	j	.B45
.B45:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	wpp, .-wpp
	.text
	.align	2
	.globl	wpppp
	.type	wpppp, @function
wpppp:
	addi	sp,sp,-12
	sw	s0,8(sp)
	sw	ra,4(sp)
	addi	s0,sp,12
	call	wppp
	call	wppp
	call	wppp
	call	wppp
	call	wppp
	call	wppp
	call	wppp
	mv	t0,a0
	li	a0,0
	j	.B47
.B47:
	lw	s0,8(sp)
	lw	ra,4(sp)
	addi	sp,sp,12
	jr	ra
	.size	wpppp, .-wpppp
	.text
	.align	2
	.globl	sanity_check
	.type	sanity_check, @function
sanity_check:
	addi	sp,sp,-12
	sw	s0,8(sp)
	sw	ra,4(sp)
	addi	s0,sp,12
	call	wP
	mv	t0,a0
	addi	t0,t0,1
	li	a0,0
	j	.B49
.B49:
	lw	s0,8(sp)
	lw	ra,4(sp)
	addi	sp,sp,12
	jr	ra
	.size	sanity_check, .-sanity_check
	.text
	.align	2
	.globl	bblock
	.type	bblock, @function
bblock:
	addi	sp,sp,-12
	sw	s0,8(sp)
	sw	ra,4(sp)
	addi	s0,sp,12
	call	wpppp
	call	wpppp
	call	bblock
	call	bblock
	mv	t0,a0
	li	a0,0
	j	.B51
.B51:
	lw	s0,8(sp)
	lw	ra,4(sp)
	addi	sp,sp,12
	jr	ra
	.size	bblock, .-bblock
	.text
	.align	2
	.globl	wP
	.type	wP, @function
wP:
	addi	sp,sp,-16
	sw	s0,12(sp)
	sw	ra,8(sp)
	addi	s0,sp,16
	li	t2,3
.B53:
	li	a7,1
	sra	s2,t2,a7
	li	t1,1
	li	t0,2
.B54:
	slt	s1,t0,s2
	beq	s1,zero,.B55
	j	.B56
.B56:
	rem	s1,t2,t0
	li	a7,0
	sub	s1,s1,a7
	seqz	s1,s1
	beq	s1,zero,.B57
	j	.B58
.B58:
	li	t1,0
	addi	t2,t2,1
	j	.B55
	j	.B57
.B55:
	li	a7,0
	sgt	s1,t2,a7
	beq	s1,zero,.B59
	add	t0,t2,t1
	li	a7,9
	rem	t0,t0,a7
	li	a7,0
	sub	t0,t0,a7
	seqz	t0,t0
	and	t0,s1,t0
	j	.B60
.B59:
	li	t0,0
	j	.B60
.B60:
	beq	t0,zero,.B61
	j	.B62
.B62:
	lui	t0,%hi(.G1)
	lw	t0,%lo(.G1)(t0)
	sw	t0,-12(s0)
	call	wpppp
	lw	t0,-12(s0)
	mv	t1,a0
	add	t0,t0,t1
	li	a7,2
	rem	t0,t0,a7
	li	a7,0
	sub	t0,t0,a7
	seqz	t0,t0
	beq	t0,zero,.B63
	j	.B64
.B64:
	lui	t0,%hi(.G1)
	lw	t0,%lo(.G1)(t0)
	mv	a0,t0
	call	add524288
	mv	t0,a0
	li	a7,524288
	sub	t0,t0,a7
	addi	t0,t0,-6
	mv	a0,t0
	j	.B65
.B63:
	call	block
	call	block
	call	block
	call	block
	call	bblock
	mv	t0,a0
	mv	a0,t0
	j	.B65
.B61:
	lui	t0,%hi(.G1)
	lw	t0,%lo(.G1)(t0)
	add	t0,t0,t1
	lui	a7,%hi(.G1)
	sw	t0,%lo(.G1)(a7)
	add	t2,t2,t1
	j	.B66
.B66:
	addi	t2,t2,1
	j	.B53
.B57:
	addi	t0,t0,1
	j	.B54
.B65:
	lw	s0,12(sp)
	lw	ra,8(sp)
	addi	sp,sp,16
	jr	ra
	.size	wP, .-wP
	.text
	.align	2
	.globl	add1024
	.type	add1024, @function
add1024:
	addi	sp,sp,-12
	sw	s0,8(sp)
	addi	s0,sp,12
	mv	t0,a0
	li	a0,0
	j	.B68
.B68:
	lw	s0,8(sp)
	addi	sp,sp,12
	jr	ra
	.size	add1024, .-add1024
