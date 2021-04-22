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
	mv	s3,a0
	mv	s8,a1
	mv	s7,a2
	mv	s4,a3
	mv	s6,a4
	mv	s2,a5
	mv	s9,a6
	addi	t1,s0,0
	lw	t1,0(t1)
	mv	s1,t1
	addi	t1,s0,4
	lw	t1,0(t1)
	mv	t2,t1
	addi	t1,s0,8
	lw	t1,0(t1)
	sub	t0,s3,s8
	seqz	t0,t0
	beq	t0,zero,.B1
	j	.B2
.B2:
	sub	t0,s8,s7
	snez	t0,t0
	bne	t0,zero,.B3
	sub	s5,s7,s4
	snez	s5,s5
	or	t0,t0,s5
	j	.B4
.B3:
	li	t0,1
	j	.B4
.B4:
	beq	t0,zero,.B5
	j	.B6
.B6:
	mv	a0,s8
	mv	a1,s7
	mv	a2,s4
	mv	a3,s6
	mv	a4,s2
	mv	a5,s9
	mv	a6,s1
	addi	t0,sp,0
	sw	t2,0(t0)
	addi	t0,sp,4
	sw	t1,0(t0)
	addi	t0,sp,8
	sw	s3,0(t0)
	j	.B7
.B7:
	mv	s4,a0
	mv	s2,a1
	mv	s7,a2
	mv	s8,a3
	mv	s6,a4
	mv	s3,a5
	mv	s5,a6
	addi	t0,s0,0
	lw	t0,0(t0)
	mv	t1,t0
	addi	t0,s0,4
	lw	t0,0(t0)
	mv	t2,t0
	addi	t0,s0,8
	lw	t0,0(t0)
	sub	s1,s4,s2
	seqz	s1,s1
	beq	s1,zero,.B8
	j	.B9
.B9:
	sub	s1,s2,s7
	snez	s1,s1
	bne	s1,zero,.B10
	sub	s9,s7,s8
	snez	s9,s9
	or	s1,s1,s9
	j	.B11
.B10:
	li	s1,1
	j	.B11
.B11:
	beq	s1,zero,.B12
	j	.B13
.B13:
	mv	a0,s2
	mv	a1,s7
	mv	a2,s8
	mv	a3,s6
	mv	a4,s3
	mv	a5,s5
	mv	a6,t1
	addi	t1,sp,0
	sw	t2,0(t1)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s4,0(t0)
.B12:
	addi	s1,s3,-1
	addi	s3,s5,-2
	mv	a0,s2
	mv	a1,s7
	mv	a2,s8
	mv	a3,s6
	mv	a4,s1
	mv	a5,s3
	mv	a6,t1
	addi	t1,sp,0
	sw	t2,0(t1)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s4,0(t0)
	j	.B14
.B14:
	mv	s4,a0
	mv	s2,a1
	mv	s7,a2
	mv	s8,a3
	mv	s6,a4
	mv	s3,a5
	mv	s5,a6
	addi	t0,s0,0
	lw	t0,0(t0)
	mv	t1,t0
	addi	t0,s0,4
	lw	t0,0(t0)
	mv	s1,t0
	addi	t0,s0,8
	lw	t0,0(t0)
	sub	t2,s4,s2
	seqz	t2,t2
	beq	t2,zero,.B15
	j	.B16
.B16:
	sub	t2,s2,s7
	snez	t2,t2
	bne	t2,zero,.B17
	sub	s9,s7,s8
	snez	s9,s9
	or	t2,t2,s9
	j	.B18
.B17:
	li	t2,1
	j	.B18
.B18:
	beq	t2,zero,.B19
	j	.B20
.B20:
	mv	a0,s2
	mv	a1,s7
	mv	a2,s8
	mv	a3,s6
	mv	a4,s3
	mv	a5,s5
	mv	a6,t1
	addi	t1,sp,0
	sw	s1,0(t1)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s4,0(t0)
	j	.B21
.B21:
	mv	s2,a0
	mv	s7,a1
	mv	s8,a2
	mv	s4,a3
	mv	s5,a4
	mv	s3,a5
	mv	s6,a6
	addi	t1,s0,0
	lw	t1,0(t1)
	mv	s1,t1
	addi	t1,s0,4
	lw	t1,0(t1)
	mv	t2,t1
	addi	t1,s0,8
	lw	t1,0(t1)
	sub	t0,s2,s7
	seqz	t0,t0
	beq	t0,zero,.B22
	j	.B23
.B23:
	sub	t0,s7,s8
	snez	t0,t0
	bne	t0,zero,.B24
	sub	s9,s8,s4
	snez	s9,s9
	or	t0,t0,s9
	j	.B25
.B24:
	li	t0,1
	j	.B25
.B25:
	beq	t0,zero,.B26
	j	.B27
.B27:
	mv	a0,s7
	mv	a1,s8
	mv	a2,s4
	mv	a3,s5
	mv	a4,s3
	mv	a5,s6
	mv	a6,s1
	addi	t0,sp,0
	sw	t2,0(t0)
	addi	t0,sp,4
	sw	t1,0(t0)
	addi	t0,sp,8
	sw	s2,0(t0)
.B26:
	addi	s3,s3,-1
	addi	t0,s6,-2
	mv	a0,s7
	mv	a1,s8
	mv	a2,s4
	mv	a3,s5
	mv	a4,s3
	mv	a5,t0
	mv	a6,s1
	addi	t0,sp,0
	sw	t2,0(t0)
	addi	t0,sp,4
	sw	t1,0(t0)
	addi	t0,sp,8
	sw	s2,0(t0)
.B22:
	add	t0,s2,s7
	add	t0,t0,s2
	mv	a0,t0
	j	.B28
.B28:
	mv	t0,a0
	addi	t0,t0,1
	mv	a0,t0
	j	.B29
.B29:
	mv	t0,a0
	addi	t0,t0,2
	mv	a0,t0
	j	.B30
.B30:
	mv	t0,a0
	addi	t0,t0,1
	mv	a0,t0
	j	.B31
.B19:
	addi	t2,s3,-1
	addi	s3,s5,-2
	mv	a0,s2
	mv	a1,s7
	mv	a2,s8
	mv	a3,s6
	mv	a4,t2
	mv	a5,s3
	mv	a6,t1
	addi	t1,sp,0
	sw	s1,0(t1)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s4,0(t0)
	j	.B32
.B32:
	mv	s4,a0
	mv	s2,a1
	mv	s7,a2
	mv	s8,a3
	mv	s6,a4
	mv	s3,a5
	mv	s5,a6
	addi	t0,s0,0
	lw	t0,0(t0)
	mv	t1,t0
	addi	t0,s0,4
	lw	t0,0(t0)
	mv	s1,t0
	addi	t0,s0,8
	lw	t0,0(t0)
	sub	t2,s4,s2
	seqz	t2,t2
	beq	t2,zero,.B33
	j	.B34
.B34:
	sub	t2,s2,s7
	snez	t2,t2
	bne	t2,zero,.B35
	sub	s9,s7,s8
	snez	s9,s9
	or	t2,t2,s9
	j	.B36
.B35:
	li	t2,1
	j	.B36
.B36:
	beq	t2,zero,.B37
	j	.B38
.B38:
	mv	a0,s2
	mv	a1,s7
	mv	a2,s8
	mv	a3,s6
	mv	a4,s3
	mv	a5,s5
	mv	a6,t1
	addi	t1,sp,0
	sw	s1,0(t1)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s4,0(t0)
	j	.B39
.B39:
	mv	s2,a0
	mv	s7,a1
	mv	s8,a2
	mv	s4,a3
	mv	s5,a4
	mv	s3,a5
	mv	s6,a6
	addi	t1,s0,0
	lw	t1,0(t1)
	mv	s1,t1
	addi	t1,s0,4
	lw	t1,0(t1)
	mv	t2,t1
	addi	t1,s0,8
	lw	t1,0(t1)
	sub	t0,s2,s7
	seqz	t0,t0
	beq	t0,zero,.B40
	j	.B41
.B41:
	sub	t0,s7,s8
	snez	t0,t0
	bne	t0,zero,.B42
	sub	s9,s8,s4
	snez	s9,s9
	or	t0,t0,s9
	j	.B43
.B42:
	li	t0,1
	j	.B43
.B43:
	beq	t0,zero,.B44
	j	.B45
.B45:
	mv	a0,s7
	mv	a1,s8
	mv	a2,s4
	mv	a3,s5
	mv	a4,s3
	mv	a5,s6
	mv	a6,s1
	addi	t0,sp,0
	sw	t2,0(t0)
	addi	t0,sp,4
	sw	t1,0(t0)
	addi	t0,sp,8
	sw	s2,0(t0)
.B44:
	addi	s3,s3,-1
	addi	t0,s6,-2
	mv	a0,s7
	mv	a1,s8
	mv	a2,s4
	mv	a3,s5
	mv	a4,s3
	mv	a5,t0
	mv	a6,s1
	addi	t0,sp,0
	sw	t2,0(t0)
	addi	t0,sp,4
	sw	t1,0(t0)
	addi	t0,sp,8
	sw	s2,0(t0)
	j	.B46
.B46:
	mv	s4,a0
	mv	s2,a1
	mv	s8,a2
	mv	s6,a3
	mv	s3,a4
	mv	s9,a5
	mv	s7,a6
	addi	t0,s0,0
	lw	t0,0(t0)
	mv	t2,t0
	addi	t0,s0,4
	lw	t0,0(t0)
	mv	s1,t0
	addi	t0,s0,8
	lw	t0,0(t0)
	sub	t1,s4,s2
	seqz	t1,t1
	beq	t1,zero,.B47
	j	.B48
.B48:
	sub	s5,s2,s8
	snez	s5,s5
	bne	s5,zero,.B49
	sub	t1,s8,s6
	snez	t1,t1
	or	t1,s5,t1
	j	.B50
.B49:
	li	t1,1
	j	.B50
.B50:
	beq	t1,zero,.B51
	j	.B52
.B52:
	mv	a0,s2
	mv	a1,s8
	mv	a2,s6
	mv	a3,s3
	mv	a4,s9
	mv	a5,s7
	mv	a6,t2
	addi	t1,sp,0
	sw	s1,0(t1)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s4,0(t0)
	j	.B53
.B53:
	mv	s5,a0
	mv	s2,a1
	mv	s3,a2
	mv	s8,a3
	mv	s6,a4
	mv	s9,a5
	mv	s4,a6
	addi	t1,s0,0
	lw	t1,0(t1)
	mv	t2,t1
	addi	t1,s0,4
	lw	t1,0(t1)
	mv	s1,t1
	addi	t1,s0,8
	lw	t1,0(t1)
	sub	t0,s5,s2
	seqz	t0,t0
	beq	t0,zero,.B54
	j	.B55
.B55:
	sub	s7,s2,s3
	snez	s7,s7
	bne	s7,zero,.B56
	sub	t0,s3,s8
	snez	t0,t0
	or	t0,s7,t0
	j	.B57
.B56:
	li	t0,1
	j	.B57
.B57:
	beq	t0,zero,.B58
	j	.B59
.B59:
	mv	a0,s2
	mv	a1,s3
	mv	a2,s8
	mv	a3,s6
	mv	a4,s9
	mv	a5,s4
	mv	a6,t2
	addi	t0,sp,0
	sw	s1,0(t0)
	addi	t0,sp,4
	sw	t1,0(t0)
	addi	t0,sp,8
	sw	s5,0(t0)
.B58:
	addi	t0,s9,-1
	addi	s4,s4,-2
	mv	a0,s2
	mv	a1,s3
	mv	a2,s8
	mv	a3,s6
	mv	a4,t0
	mv	a5,s4
	mv	a6,t2
	addi	t0,sp,0
	sw	s1,0(t0)
	addi	t0,sp,4
	sw	t1,0(t0)
	addi	t0,sp,8
	sw	s5,0(t0)
.B54:
	add	t0,s5,s2
	add	t0,t0,s5
	mv	a0,t0
	j	.B60
.B60:
	mv	t0,a0
	addi	t0,t0,1
	mv	a0,t0
	j	.B61
.B61:
	mv	t0,a0
	addi	t0,t0,2
	mv	a0,t0
	j	.B62
.B62:
	mv	t0,a0
	addi	t0,t0,1
	mv	a0,t0
	j	.B63
.B63:
	mv	t0,a0
	addi	t0,t0,2
	mv	a0,t0
	j	.B29
.B51:
	addi	s5,s9,-1
	addi	t1,s7,-2
	mv	a0,s2
	mv	a1,s8
	mv	a2,s6
	mv	a3,s3
	mv	a4,s5
	mv	a5,t1
	mv	a6,t2
	addi	t1,sp,0
	sw	s1,0(t1)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s4,0(t0)
.B47:
	add	t0,s4,s2
	add	t0,t0,s4
	mv	a0,t0
	j	.B61
.B40:
	add	t0,s2,s7
	add	t0,t0,s2
	mv	a0,t0
	j	.B62
.B37:
	addi	t2,s3,-1
	addi	s3,s5,-2
	mv	a0,s2
	mv	a1,s7
	mv	a2,s8
	mv	a3,s6
	mv	a4,t2
	mv	a5,s3
	mv	a6,t1
	addi	t1,sp,0
	sw	s1,0(t1)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s4,0(t0)
	j	.B64
.B64:
	mv	s5,a0
	mv	s2,a1
	mv	s7,a2
	mv	s8,a3
	mv	s6,a4
	mv	s3,a5
	mv	s4,a6
	addi	t1,s0,0
	lw	t1,0(t1)
	mv	s1,t1
	addi	t1,s0,4
	lw	t1,0(t1)
	mv	t2,t1
	addi	t1,s0,8
	lw	t1,0(t1)
	sub	t0,s5,s2
	seqz	t0,t0
	beq	t0,zero,.B65
	j	.B66
.B66:
	sub	t0,s2,s7
	snez	t0,t0
	bne	t0,zero,.B67
	sub	s9,s7,s8
	snez	s9,s9
	or	t0,t0,s9
	j	.B68
.B67:
	li	t0,1
	j	.B68
.B68:
	beq	t0,zero,.B69
	j	.B70
.B70:
	mv	a0,s2
	mv	a1,s7
	mv	a2,s8
	mv	a3,s6
	mv	a4,s3
	mv	a5,s4
	mv	a6,s1
	addi	t0,sp,0
	sw	t2,0(t0)
	addi	t0,sp,4
	sw	t1,0(t0)
	addi	t0,sp,8
	sw	s5,0(t0)
	j	.B71
.B71:
	mv	s2,a0
	mv	s7,a1
	mv	s8,a2
	mv	s3,a3
	mv	s5,a4
	mv	s4,a5
	mv	s6,a6
	addi	t0,s0,0
	lw	t0,0(t0)
	mv	t1,t0
	addi	t0,s0,4
	lw	t0,0(t0)
	mv	s1,t0
	addi	t0,s0,8
	lw	t0,0(t0)
	sub	t2,s2,s7
	seqz	t2,t2
	beq	t2,zero,.B72
	j	.B73
.B73:
	sub	t2,s7,s8
	snez	t2,t2
	bne	t2,zero,.B74
	sub	s9,s8,s3
	snez	s9,s9
	or	t2,t2,s9
	j	.B75
.B74:
	li	t2,1
	j	.B75
.B75:
	beq	t2,zero,.B76
	j	.B77
.B77:
	mv	a0,s7
	mv	a1,s8
	mv	a2,s3
	mv	a3,s5
	mv	a4,s4
	mv	a5,s6
	mv	a6,t1
	addi	t1,sp,0
	sw	s1,0(t1)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s2,0(t0)
.B76:
	addi	s4,s4,-1
	addi	t2,s6,-2
	mv	a0,s7
	mv	a1,s8
	mv	a2,s3
	mv	a3,s5
	mv	a4,s4
	mv	a5,t2
	mv	a6,t1
	addi	t1,sp,0
	sw	s1,0(t1)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s2,0(t0)
	j	.B78
.B78:
	mv	s4,a0
	mv	s2,a1
	mv	s8,a2
	mv	s6,a3
	mv	s3,a4
	mv	s9,a5
	mv	s7,a6
	addi	t0,s0,0
	lw	t0,0(t0)
	mv	t1,t0
	addi	t0,s0,4
	lw	t0,0(t0)
	mv	s1,t0
	addi	t0,s0,8
	lw	t0,0(t0)
	sub	t2,s4,s2
	seqz	t2,t2
	beq	t2,zero,.B79
	j	.B80
.B80:
	sub	s5,s2,s8
	snez	s5,s5
	bne	s5,zero,.B81
	sub	t2,s8,s6
	snez	t2,t2
	or	t2,s5,t2
	j	.B82
.B81:
	li	t2,1
	j	.B82
.B82:
	beq	t2,zero,.B83
	j	.B84
.B84:
	mv	a0,s2
	mv	a1,s8
	mv	a2,s6
	mv	a3,s3
	mv	a4,s9
	mv	a5,s7
	mv	a6,t1
	addi	t1,sp,0
	sw	s1,0(t1)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s4,0(t0)
	j	.B85
.B85:
	mv	s5,a0
	mv	s3,a1
	mv	s2,a2
	mv	s8,a3
	mv	s6,a4
	mv	s9,a5
	mv	s4,a6
	addi	t1,s0,0
	lw	t1,0(t1)
	mv	t0,t1
	addi	t1,s0,4
	lw	t1,0(t1)
	mv	s1,t1
	addi	t1,s0,8
	lw	t1,0(t1)
	sub	t2,s5,s3
	seqz	t2,t2
	beq	t2,zero,.B86
	j	.B87
.B87:
	sub	s7,s3,s2
	snez	s7,s7
	bne	s7,zero,.B88
	sub	t2,s2,s8
	snez	t2,t2
	or	t2,s7,t2
	j	.B89
.B88:
	li	t2,1
	j	.B89
.B89:
	beq	t2,zero,.B90
	j	.B91
.B91:
	mv	a0,s3
	mv	a1,s2
	mv	a2,s8
	mv	a3,s6
	mv	a4,s9
	mv	a5,s4
	mv	a6,t0
	addi	t0,sp,0
	sw	s1,0(t0)
	addi	t0,sp,4
	sw	t1,0(t0)
	addi	t0,sp,8
	sw	s5,0(t0)
.B90:
	addi	t2,s9,-1
	addi	s4,s4,-2
	mv	a0,s3
	mv	a1,s2
	mv	a2,s8
	mv	a3,s6
	mv	a4,t2
	mv	a5,s4
	mv	a6,t0
	addi	t0,sp,0
	sw	s1,0(t0)
	addi	t0,sp,4
	sw	t1,0(t0)
	addi	t0,sp,8
	sw	s5,0(t0)
.B86:
	add	t0,s5,s3
	add	t0,t0,s5
	mv	a0,t0
	j	.B92
.B92:
	mv	t0,a0
	addi	t0,t0,1
	mv	a0,t0
	j	.B93
.B93:
	mv	t0,a0
	addi	t0,t0,2
	mv	a0,t0
	j	.B94
.B94:
	mv	t0,a0
	addi	t0,t0,1
	mv	a0,t0
	j	.B95
.B95:
	mv	t0,a0
	addi	t0,t0,2
	mv	a0,t0
	j	.B63
.B83:
	addi	s5,s9,-1
	addi	t2,s7,-2
	mv	a0,s2
	mv	a1,s8
	mv	a2,s6
	mv	a3,s3
	mv	a4,s5
	mv	a5,t2
	mv	a6,t1
	addi	t1,sp,0
	sw	s1,0(t1)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s4,0(t0)
	j	.B96
.B96:
	mv	s4,a0
	mv	s7,a1
	mv	s3,a2
	mv	s8,a3
	mv	s6,a4
	mv	s2,a5
	mv	s9,a6
	addi	t0,s0,0
	lw	t0,0(t0)
	mv	s1,t0
	addi	t0,s0,4
	lw	t0,0(t0)
	mv	t2,t0
	addi	t0,s0,8
	lw	t0,0(t0)
	sub	t1,s4,s7
	seqz	t1,t1
	beq	t1,zero,.B97
	j	.B98
.B98:
	sub	s5,s7,s3
	snez	s5,s5
	bne	s5,zero,.B99
	sub	t1,s3,s8
	snez	t1,t1
	or	t1,s5,t1
	j	.B100
.B99:
	li	t1,1
	j	.B100
.B100:
	beq	t1,zero,.B101
	j	.B102
.B102:
	mv	a0,s7
	mv	a1,s3
	mv	a2,s8
	mv	a3,s6
	mv	a4,s2
	mv	a5,s9
	mv	a6,s1
	addi	t1,sp,0
	sw	t2,0(t1)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s4,0(t0)
	j	.B103
.B103:
	mv	s2,a0
	mv	s8,a1
	mv	s7,a2
	mv	s4,a3
	mv	s3,a4
	mv	s5,a5
	mv	s6,a6
	addi	t0,s0,0
	lw	t0,0(t0)
	mv	t2,t0
	addi	t0,s0,4
	lw	t0,0(t0)
	mv	s1,t0
	addi	t0,s0,8
	lw	t0,0(t0)
	sub	t1,s2,s8
	seqz	t1,t1
	beq	t1,zero,.B104
	j	.B105
.B105:
	sub	s9,s8,s7
	snez	s9,s9
	bne	s9,zero,.B106
	sub	t1,s7,s4
	snez	t1,t1
	or	t1,s9,t1
	j	.B107
.B106:
	li	t1,1
	j	.B107
.B107:
	beq	t1,zero,.B108
	j	.B109
.B109:
	mv	a0,s8
	mv	a1,s7
	mv	a2,s4
	mv	a3,s3
	mv	a4,s5
	mv	a5,s6
	mv	a6,t2
	addi	t1,sp,0
	sw	s1,0(t1)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s2,0(t0)
.B108:
	addi	t1,s5,-1
	addi	s5,s6,-2
	mv	a0,s8
	mv	a1,s7
	mv	a2,s4
	mv	a3,s3
	mv	a4,t1
	mv	a5,s5
	mv	a6,t2
	addi	t1,sp,0
	sw	s1,0(t1)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s2,0(t0)
	j	.B110
.B110:
	mv	s6,a0
	mv	s4,a1
	mv	s3,a2
	mv	s8,a3
	mv	s7,a4
	mv	s9,a5
	mv	s5,a6
	addi	t0,s0,0
	lw	t0,0(t0)
	mv	t2,t0
	addi	t0,s0,4
	lw	t0,0(t0)
	mv	t1,t0
	addi	t0,s0,8
	lw	t0,0(t0)
	sub	s1,s6,s4
	seqz	s1,s1
	beq	s1,zero,.B111
	j	.B112
.B112:
	sub	s2,s4,s3
	snez	s2,s2
	bne	s2,zero,.B113
	sub	s1,s3,s8
	snez	s1,s1
	or	s1,s2,s1
	j	.B114
.B113:
	li	s1,1
	j	.B114
.B114:
	beq	s1,zero,.B115
	j	.B116
.B116:
	mv	a0,s4
	mv	a1,s3
	mv	a2,s8
	mv	a3,s7
	mv	a4,s9
	mv	a5,s5
	mv	a6,t2
	addi	t2,sp,0
	sw	t1,0(t2)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s6,0(t0)
	j	.B117
.B117:
	mv	s6,a0
	mv	s7,a1
	mv	s3,a2
	mv	s4,a3
	mv	s5,a4
	mv	s2,a5
	mv	s9,a6
	addi	t0,s0,0
	lw	t0,0(t0)
	mv	t1,t0
	addi	t0,s0,4
	lw	t0,0(t0)
	mv	s1,t0
	addi	t0,s0,8
	lw	t0,0(t0)
	sub	t2,s6,s7
	seqz	t2,t2
	beq	t2,zero,.B118
	j	.B119
.B119:
	sub	t2,s7,s3
	snez	t2,t2
	bne	t2,zero,.B120
	sub	s8,s3,s4
	snez	s8,s8
	or	t2,t2,s8
	j	.B121
.B120:
	li	t2,1
	j	.B121
.B121:
	beq	t2,zero,.B122
	j	.B123
.B123:
	mv	a0,s7
	mv	a1,s3
	mv	a2,s4
	mv	a3,s5
	mv	a4,s2
	mv	a5,s9
	mv	a6,t1
	addi	t1,sp,0
	sw	s1,0(t1)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s6,0(t0)
.B122:
	addi	s2,s2,-1
	addi	t2,s9,-2
	mv	a0,s7
	mv	a1,s3
	mv	a2,s4
	mv	a3,s5
	mv	a4,s2
	mv	a5,t2
	mv	a6,t1
	addi	t1,sp,0
	sw	s1,0(t1)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s6,0(t0)
.B118:
	add	t0,s6,s7
	add	t0,t0,s6
	mv	a0,t0
	j	.B124
.B124:
	mv	t0,a0
	addi	t0,t0,1
	mv	a0,t0
	j	.B125
.B125:
	mv	t0,a0
	addi	t0,t0,2
	mv	a0,t0
	j	.B126
.B126:
	mv	t0,a0
	addi	t0,t0,1
	mv	a0,t0
	j	.B127
.B127:
	mv	t0,a0
	addi	t0,t0,2
	mv	a0,t0
	j	.B93
.B115:
	addi	s2,s9,-1
	addi	s1,s5,-2
	mv	a0,s4
	mv	a1,s3
	mv	a2,s8
	mv	a3,s7
	mv	a4,s2
	mv	a5,s1
	mv	a6,t2
	addi	t2,sp,0
	sw	t1,0(t2)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s6,0(t0)
.B111:
	add	t0,s6,s4
	add	t0,t0,s6
	mv	a0,t0
	j	.B125
.B104:
	add	t0,s2,s8
	add	t0,t0,s2
	mv	a0,t0
	j	.B126
.B101:
	addi	s2,s2,-1
	addi	t1,s9,-2
	mv	a0,s7
	mv	a1,s3
	mv	a2,s8
	mv	a3,s6
	mv	a4,s2
	mv	a5,t1
	mv	a6,s1
	addi	t1,sp,0
	sw	t2,0(t1)
	addi	t1,sp,4
	sw	t0,0(t1)
	addi	t0,sp,8
	sw	s4,0(t0)
.B97:
	add	t0,s4,s7
	add	t0,t0,s4
	mv	a0,t0
	j	.B127
.B79:
	add	t0,s4,s2
	add	t0,t0,s4
	mv	a0,t0
	j	.B93
.B72:
	add	t0,s2,s7
	add	t0,t0,s2
	mv	a0,t0
	j	.B94
.B69:
	addi	t0,s3,-1
	addi	s3,s4,-2
	mv	a0,s2
	mv	a1,s7
	mv	a2,s8
	mv	a3,s6
	mv	a4,t0
	mv	a5,s3
	mv	a6,s1
	addi	t0,sp,0
	sw	t2,0(t0)
	addi	t0,sp,4
	sw	t1,0(t0)
	addi	t0,sp,8
	sw	s5,0(t0)
	call	test
	mv	t0,a0
	addi	t0,t0,2
	mv	a0,t0
	j	.B95
.B65:
	add	t0,s5,s2
	add	t0,t0,s5
	mv	a0,t0
	j	.B95
.B33:
	add	t0,s4,s2
	add	t0,t0,s4
	mv	a0,t0
	j	.B63
.B15:
	add	t0,s4,s2
	add	t0,t0,s4
	mv	a0,t0
	j	.B29
.B8:
	add	t0,s4,s2
	add	t0,t0,s4
	mv	a0,t0
	j	.B30
.B5:
	addi	s2,s2,-1
	addi	t0,s9,-2
	mv	a0,s8
	mv	a1,s7
	mv	a2,s4
	mv	a3,s6
	mv	a4,s2
	mv	a5,t0
	mv	a6,s1
	addi	t0,sp,0
	sw	t2,0(t0)
	addi	t0,sp,4
	sw	t1,0(t0)
	addi	t0,sp,8
	sw	s3,0(t0)
	call	test
	mv	t0,a0
	addi	t0,t0,2
	mv	a0,t0
	j	.B31
.B1:
	add	t0,s3,s8
	add	t0,t0,s3
	mv	a0,t0
	j	.B31
.B31:
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
	li	t2,0
.B129:
	mv	a0,t1
	j	.B130
.B130:
	mv	t1,a0
	li	s1,1073741823
	li	t0,13
	sll	t0,t1,t0
	xor	t1,t1,t0
	mv	a0,t1
	li	a1,17
	j	.B131
.B131:
	mv	s4,a0
	mv	s3,a1
	li	t0,-2147483648
	li	a7,0
	slt	s2,s4,a7
	xori	s2,s2,1
	beq	s2,zero,.B132
	j	.B133
.B133:
	sra	t0,s4,s3
	mv	a0,t0
	j	.B134
.B134:
	mv	t0,a0
	xor	t1,t1,t0
	li	t0,5
	sll	t0,t1,t0
	xor	t1,t1,t0
	and	t1,t1,s1
	mv	a0,t1
	j	.B135
.B135:
	mv	s2,a0
	mv	a0,s2
	j	.B136
.B136:
	mv	t0,a0
	li	t1,1073741823
	li	s1,13
	sll	s1,t0,s1
	xor	t0,t0,s1
	mv	a0,t0
	li	a1,17
	j	.B137
.B137:
	mv	s5,a0
	mv	s4,a1
	li	s1,-2147483648
	li	a7,0
	slt	s3,s5,a7
	xori	s3,s3,1
	beq	s3,zero,.B138
	j	.B139
.B139:
	sra	s1,s5,s4
	mv	a0,s1
	j	.B140
.B140:
	mv	s1,a0
	xor	t0,t0,s1
	li	s1,5
	sll	s1,t0,s1
	xor	t0,t0,s1
	and	t0,t0,t1
	mv	a0,t0
	j	.B141
.B141:
	mv	t1,a0
	li	a7,255
	and	t0,s2,a7
	li	a7,255
	and	s1,t1,a7
	sub	t0,t0,s1
	snez	t0,t0
	beq	t0,zero,.B142
	j	.B143
.B143:
	mv	a0,t1
	j	.B144
.B144:
	mv	s1,a0
	li	t1,1073741823
	li	t0,13
	sll	t0,s1,t0
	xor	s1,s1,t0
	mv	a0,s1
	li	a1,17
	j	.B145
.B145:
	mv	s4,a0
	mv	s3,a1
	li	t0,-2147483648
	li	a7,0
	slt	s2,s4,a7
	xori	s2,s2,1
	beq	s2,zero,.B146
	j	.B147
.B147:
	sra	t0,s4,s3
	mv	a0,t0
	j	.B148
.B148:
	mv	t0,a0
	xor	s1,s1,t0
	li	t0,5
	sll	t0,s1,t0
	xor	s1,s1,t0
	and	s1,s1,t1
	mv	a0,s1
	j	.B149
.B149:
	mv	s3,a0
	mv	a0,s3
	j	.B150
.B150:
	mv	t0,a0
	li	s1,1073741823
	li	t1,13
	sll	t1,t0,t1
	xor	t0,t0,t1
	mv	a0,t0
	li	a1,17
	j	.B151
.B151:
	mv	s5,a0
	mv	s4,a1
	li	t1,-2147483648
	li	a7,0
	slt	s2,s5,a7
	xori	s2,s2,1
	beq	s2,zero,.B152
	j	.B153
.B153:
	sra	t1,s5,s4
	mv	a0,t1
	j	.B154
.B154:
	mv	t1,a0
	xor	t0,t0,t1
	li	t1,5
	sll	t1,t0,t1
	xor	t0,t0,t1
	and	t0,t0,s1
	mv	a0,t0
	j	.B155
.B155:
	mv	s2,a0
	mv	a0,s2
	j	.B156
.B156:
	mv	t0,a0
	li	t1,1073741823
	li	s1,13
	sll	s1,t0,s1
	xor	t0,t0,s1
	mv	a0,t0
	li	a1,17
	j	.B157
.B157:
	mv	s6,a0
	mv	s5,a1
	li	s1,-2147483648
	li	a7,0
	slt	s4,s6,a7
	xori	s4,s4,1
	beq	s4,zero,.B158
	j	.B159
.B159:
	sra	s1,s6,s5
	mv	a0,s1
	j	.B160
.B160:
	mv	s1,a0
	xor	t0,t0,s1
	li	s1,5
	sll	s1,t0,s1
	xor	t0,t0,s1
	and	t0,t0,t1
	mv	a0,t0
	j	.B161
.B161:
	mv	s4,a0
	mv	a0,s4
	j	.B162
.B162:
	mv	t0,a0
	li	t1,1073741823
	li	s1,13
	sll	s1,t0,s1
	xor	t0,t0,s1
	mv	a0,t0
	li	a1,17
	j	.B163
.B163:
	mv	s7,a0
	mv	s6,a1
	li	s1,-2147483648
	li	a7,0
	slt	s5,s7,a7
	xori	s5,s5,1
	beq	s5,zero,.B164
	j	.B165
.B165:
	sra	s1,s7,s6
	mv	a0,s1
	j	.B166
.B166:
	mv	s1,a0
	xor	t0,t0,s1
	li	s1,5
	sll	s1,t0,s1
	xor	t0,t0,s1
	and	t0,t0,t1
	mv	a0,t0
	j	.B167
.B167:
	mv	t0,a0
	mv	a0,t0
	j	.B168
.B168:
	mv	s1,a0
	li	s5,1073741823
	li	t1,13
	sll	t1,s1,t1
	xor	s1,s1,t1
	mv	a0,s1
	li	a1,17
	j	.B169
.B169:
	mv	s7,a0
	mv	s8,a1
	li	t1,-2147483648
	li	a7,0
	slt	s6,s7,a7
	xori	s6,s6,1
	beq	s6,zero,.B170
	j	.B171
.B171:
	sra	t1,s7,s8
	mv	a0,t1
	j	.B172
.B172:
	mv	t1,a0
	xor	s1,s1,t1
	li	t1,5
	sll	t1,s1,t1
	xor	s1,s1,t1
	and	s1,s1,s5
	mv	a0,s1
	j	.B173
.B173:
	mv	t1,a0
	li	a7,3
	and	s10,s3,a7
	li	a7,28
	sra	s8,s3,a7
	li	a7,1
	and	s7,s2,a7
	li	a7,29
	sra	s9,s2,a7
	li	a7,25
	sra	s5,s4,a7
	li	a7,31
	and	s6,s4,a7
	li	a7,15
	sra	s4,t0,a7
	li	a7,32767
	and	s3,t0,a7
	li	a7,15
	sra	s2,t1,a7
	li	a7,32767
	and	s1,t1,a7
	mv	a0,s10
	mv	a1,s8
	mv	a2,s7
	mv	a3,s9
	mv	a4,s5
	mv	a5,s6
	mv	a6,s4
	addi	t0,sp,0
	sw	s3,0(t0)
	addi	t0,sp,4
	sw	s2,0(t0)
	addi	t0,sp,8
	sw	s1,0(t0)
	sw	t2,-24(s0)
	sw	t1,-20(s0)
	call	test
	lw	t2,-24(s0)
	lw	t1,-20(s0)
	mv	t0,a0
	xor	t2,t2,t0
	j	.B129
.B170:
	li	a6,31
	sub	s6,a6,s8
	li	a6,1
	sll	s6,a6,s6
	xor	t1,s7,t1
	sra	t1,t1,s8
	or	t1,s6,t1
	mv	a0,t1
	j	.B172
.B164:
	li	a6,31
	sub	s5,a6,s6
	li	a6,1
	sll	s5,a6,s5
	xor	s1,s7,s1
	sra	s1,s1,s6
	or	s1,s5,s1
	mv	a0,s1
	j	.B166
.B158:
	li	a6,31
	sub	s4,a6,s5
	li	a6,1
	sll	s4,a6,s4
	xor	s1,s6,s1
	sra	s1,s1,s5
	or	s1,s4,s1
	mv	a0,s1
	j	.B160
.B152:
	li	a6,31
	sub	s2,a6,s4
	li	a6,1
	sll	s2,a6,s2
	xor	t1,s5,t1
	sra	t1,t1,s4
	or	t1,s2,t1
	mv	a0,t1
	j	.B154
.B146:
	li	a6,31
	sub	s2,a6,s3
	li	a6,1
	sll	s2,a6,s2
	xor	t0,s4,t0
	sra	t0,t0,s3
	or	t0,s2,t0
	mv	a0,t0
	j	.B148
.B142:
	mv	a0,t2
	sw	t2,-16(s0)
	call	toString
	lw	t2,-16(s0)
	mv	t0,a0
	mv	a0,t0
	sw	t2,-12(s0)
	call	println
	lw	t2,-12(s0)
	addi	t0,t2,-19
	mv	a0,t0
	j	.B174
.B138:
	li	a6,31
	sub	s3,a6,s4
	li	a6,1
	sll	s3,a6,s3
	xor	s1,s5,s1
	sra	s1,s1,s4
	or	s1,s3,s1
	mv	a0,s1
	j	.B140
.B132:
	li	a6,31
	sub	s2,a6,s3
	li	a6,1
	sll	s2,a6,s2
	xor	t0,s4,t0
	sra	t0,t0,s3
	or	t0,s2,t0
	mv	a0,t0
	j	.B134
.B174:
	lw	s0,36(sp)
	lw	ra,32(sp)
	addi	sp,sp,40
	jr	ra
	.size	main, .-main
