	.text
	.section	.rodata
	.align	2
.S0:
	.string	"("
	.align	2
.S1:
	.string	", "
	.align	2
.S2:
	.string	", "
	.align	2
.S3:
	.string	")"
	.text
	.section	.sbss,"aw",@nobits
	.text
	.align	2
	.globl	main
	.type	main, @function
main:
	addi	sp,sp,-996
	sw	s0,992(sp)
	sw	ra,988(sp)
	addi	s0,sp,996
	li	a0,12
	call	malloc
	mv	s1,a0
	mv	t0,a0
	li	t1,0
	addi	t2,t0,0
	sw	t1,0(t2)
	li	t1,0
	addi	t2,t0,4
	sw	t1,0(t2)
	li	t1,0
	addi	t0,t0,8
	sw	t1,0(t0)
	li	a0,12
	sw	s1,-992(s0)
	call	malloc
	lw	s1,-992(s0)
	mv	s2,a0
	mv	t0,a0
	li	t1,0
	addi	t2,t0,0
	sw	t1,0(t2)
	li	t1,0
	addi	t2,t0,4
	sw	t1,0(t2)
	li	t1,0
	addi	t0,t0,8
	sw	t1,0(t0)
	li	a0,12
	sw	s1,-988(s0)
	sw	s2,-984(s0)
	call	malloc
	lw	s1,-988(s0)
	lw	s2,-984(s0)
	mv	t2,a0
	mv	t0,a0
	li	t1,0
	addi	s3,t0,0
	sw	t1,0(s3)
	li	s3,0
	addi	t1,t0,4
	sw	s3,0(t1)
	li	t1,0
	addi	t0,t0,8
	sw	t1,0(t0)
	li	a0,12
	sw	t2,-980(s0)
	sw	s1,-976(s0)
	sw	s2,-972(s0)
	call	malloc
	lw	t2,-980(s0)
	lw	s1,-976(s0)
	lw	s2,-972(s0)
	mv	t0,a0
	mv	t1,a0
	li	s3,0
	addi	s4,t1,0
	sw	s3,0(s4)
	li	s4,0
	addi	s3,t1,4
	sw	s4,0(s3)
	li	s3,0
	addi	t1,t1,8
	sw	s3,0(t1)
	lui	t1,%hi(.S0)
	addi	t1,t1,%lo(.S0)
	addi	s3,s1,0
	lw	s3,0(s3)
	mv	a0,s3
	sw	t1,-968(s0)
	sw	s1,-964(s0)
	sw	t2,-960(s0)
	sw	t0,-956(s0)
	sw	s1,-952(s0)
	sw	s2,-948(s0)
	call	toString
	lw	t1,-968(s0)
	lw	s1,-964(s0)
	lw	t2,-960(s0)
	lw	t0,-956(s0)
	lw	s1,-952(s0)
	lw	s2,-948(s0)
	mv	s3,a0
	mv	a0,t1
	mv	a1,s3
	sw	s1,-944(s0)
	sw	t2,-940(s0)
	sw	t0,-936(s0)
	sw	s1,-932(s0)
	sw	s2,-928(s0)
	call	Mx_string_ADD
	lw	s1,-944(s0)
	lw	t2,-940(s0)
	lw	t0,-936(s0)
	lw	s1,-932(s0)
	lw	s2,-928(s0)
	mv	s3,a0
	lui	t1,%hi(.S1)
	addi	t1,t1,%lo(.S1)
	mv	a0,s3
	mv	a1,t1
	sw	s1,-924(s0)
	sw	t2,-920(s0)
	sw	t0,-916(s0)
	sw	s1,-912(s0)
	sw	s2,-908(s0)
	call	Mx_string_ADD
	lw	s1,-924(s0)
	lw	t2,-920(s0)
	lw	t0,-916(s0)
	lw	s1,-912(s0)
	lw	s2,-908(s0)
	mv	s3,a0
	addi	t1,s1,4
	lw	t1,0(t1)
	mv	a0,t1
	sw	s1,-904(s0)
	sw	t2,-900(s0)
	sw	t0,-896(s0)
	sw	s1,-892(s0)
	sw	s3,-888(s0)
	sw	s2,-884(s0)
	call	toString
	lw	s1,-904(s0)
	lw	t2,-900(s0)
	lw	t0,-896(s0)
	lw	s1,-892(s0)
	lw	s3,-888(s0)
	lw	s2,-884(s0)
	mv	t1,a0
	mv	a0,s3
	mv	a1,t1
	sw	s1,-880(s0)
	sw	t2,-876(s0)
	sw	t0,-872(s0)
	sw	s1,-868(s0)
	sw	s2,-864(s0)
	call	Mx_string_ADD
	lw	s1,-880(s0)
	lw	t2,-876(s0)
	lw	t0,-872(s0)
	lw	s1,-868(s0)
	lw	s2,-864(s0)
	mv	s3,a0
	lui	t1,%hi(.S2)
	addi	t1,t1,%lo(.S2)
	mv	a0,s3
	mv	a1,t1
	sw	s1,-860(s0)
	sw	t2,-856(s0)
	sw	t0,-852(s0)
	sw	s1,-848(s0)
	sw	s2,-844(s0)
	call	Mx_string_ADD
	lw	s1,-860(s0)
	lw	t2,-856(s0)
	lw	t0,-852(s0)
	lw	s1,-848(s0)
	lw	s2,-844(s0)
	mv	s3,a0
	addi	t1,s1,8
	lw	t1,0(t1)
	mv	a0,t1
	sw	s3,-840(s0)
	sw	t2,-836(s0)
	sw	t0,-832(s0)
	sw	s1,-828(s0)
	sw	s2,-824(s0)
	call	toString
	lw	s3,-840(s0)
	lw	t2,-836(s0)
	lw	t0,-832(s0)
	lw	s1,-828(s0)
	lw	s2,-824(s0)
	mv	t1,a0
	mv	a0,s3
	mv	a1,t1
	sw	t2,-820(s0)
	sw	t0,-816(s0)
	sw	s1,-812(s0)
	sw	s2,-808(s0)
	call	Mx_string_ADD
	lw	t2,-820(s0)
	lw	t0,-816(s0)
	lw	s1,-812(s0)
	lw	s2,-808(s0)
	mv	s3,a0
	lui	t1,%hi(.S3)
	addi	t1,t1,%lo(.S3)
	mv	a0,s3
	mv	a1,t1
	sw	t2,-804(s0)
	sw	t0,-800(s0)
	sw	s1,-796(s0)
	sw	s2,-792(s0)
	call	Mx_string_ADD
	lw	t2,-804(s0)
	lw	t0,-800(s0)
	lw	s1,-796(s0)
	lw	s2,-792(s0)
	mv	t1,a0
	mv	a0,t1
	sw	t2,-788(s0)
	sw	t0,-784(s0)
	sw	s1,-780(s0)
	sw	s2,-776(s0)
	call	println
	lw	t2,-788(s0)
	lw	t0,-784(s0)
	lw	s1,-780(s0)
	lw	s2,-776(s0)
	mv	t1,a0
	li	t1,849
	li	s4,-463
	li	s3,480
	addi	s5,s1,0
	sw	t1,0(s5)
	addi	t1,s1,4
	sw	s4,0(t1)
	addi	t1,s1,8
	sw	s3,0(t1)
	mv	t1,a0
	li	t1,-208
	li	s4,585
	li	s3,-150
	addi	s5,s2,0
	sw	t1,0(s5)
	addi	t1,s2,4
	sw	s4,0(t1)
	addi	t1,s2,8
	sw	s3,0(t1)
	mv	t1,a0
	li	t1,360
	li	s4,-670
	li	s3,-742
	addi	s5,t2,0
	sw	t1,0(s5)
	addi	t1,t2,4
	sw	s4,0(t1)
	addi	t1,t2,8
	sw	s3,0(t1)
	mv	t1,a0
	li	t1,-29
	li	s4,-591
	li	s3,-960
	addi	s5,t0,0
	sw	t1,0(s5)
	addi	t1,t0,4
	sw	s4,0(t1)
	addi	t1,t0,8
	sw	s3,0(t1)
	mv	t1,a0
	addi	t1,s1,0
	lw	t1,0(t1)
	addi	s3,s2,0
	lw	s3,0(s3)
	add	t1,t1,s3
	addi	s3,s1,0
	sw	t1,0(s3)
	addi	s3,s1,4
	lw	s3,0(s3)
	addi	t1,s2,4
	lw	t1,0(t1)
	add	s3,s3,t1
	addi	t1,s1,4
	sw	s3,0(t1)
	addi	s3,s1,8
	lw	s3,0(s3)
	addi	t1,s2,8
	lw	t1,0(t1)
	add	s3,s3,t1
	addi	t1,s1,8
	sw	s3,0(t1)
	mv	t1,s1
	addi	t1,s2,0
	lw	t1,0(t1)
	addi	s3,t2,0
	lw	s3,0(s3)
	add	t1,t1,s3
	addi	s3,s2,0
	sw	t1,0(s3)
	addi	s3,s2,4
	lw	s3,0(s3)
	addi	t1,t2,4
	lw	t1,0(t1)
	add	s3,s3,t1
	addi	t1,s2,4
	sw	s3,0(t1)
	addi	s3,s2,8
	lw	s3,0(s3)
	addi	t1,t2,8
	lw	t1,0(t1)
	add	s3,s3,t1
	addi	t1,s2,8
	sw	s3,0(t1)
	mv	t1,s2
	addi	t1,t0,0
	lw	t1,0(t1)
	addi	s3,t2,0
	lw	s3,0(s3)
	add	t1,t1,s3
	addi	s3,t0,0
	sw	t1,0(s3)
	addi	s3,t0,4
	lw	s3,0(s3)
	addi	t1,t2,4
	lw	t1,0(t1)
	add	s3,s3,t1
	addi	t1,t0,4
	sw	s3,0(t1)
	addi	s3,t0,8
	lw	s3,0(s3)
	addi	t1,t2,8
	lw	t1,0(t1)
	add	s3,s3,t1
	addi	t1,t0,8
	sw	s3,0(t1)
	mv	t1,t0
	addi	s3,t2,0
	lw	s3,0(s3)
	addi	t1,s1,0
	lw	t1,0(t1)
	sub	t1,s3,t1
	addi	s3,t2,0
	sw	t1,0(s3)
	addi	s3,t2,4
	lw	s3,0(s3)
	addi	t1,s1,4
	lw	t1,0(t1)
	sub	t1,s3,t1
	addi	s3,t2,4
	sw	t1,0(s3)
	addi	s3,t2,8
	lw	s3,0(s3)
	addi	t1,s1,8
	lw	t1,0(t1)
	sub	t1,s3,t1
	addi	s3,t2,8
	sw	t1,0(s3)
	mv	t1,t2
	addi	s3,s2,0
	lw	s3,0(s3)
	addi	t1,t0,0
	lw	t1,0(t1)
	sub	t1,s3,t1
	addi	s3,s2,0
	sw	t1,0(s3)
	addi	s3,s2,4
	lw	s3,0(s3)
	addi	t1,t0,4
	lw	t1,0(t1)
	sub	t1,s3,t1
	addi	s3,s2,4
	sw	t1,0(s3)
	addi	s3,s2,8
	lw	s3,0(s3)
	addi	t1,t0,8
	lw	t1,0(t1)
	sub	t1,s3,t1
	addi	s3,s2,8
	sw	t1,0(s3)
	mv	t1,s2
	addi	s3,t0,0
	lw	s3,0(s3)
	addi	t1,t2,0
	lw	t1,0(t1)
	sub	t1,s3,t1
	addi	s3,t0,0
	sw	t1,0(s3)
	addi	s3,t0,4
	lw	s3,0(s3)
	addi	t1,t2,4
	lw	t1,0(t1)
	sub	t1,s3,t1
	addi	s3,t0,4
	sw	t1,0(s3)
	addi	s3,t0,8
	lw	s3,0(s3)
	addi	t1,t2,8
	lw	t1,0(t1)
	sub	t1,s3,t1
	addi	s3,t0,8
	sw	t1,0(s3)
	mv	t1,t0
	addi	t1,t2,0
	lw	t1,0(t1)
	addi	s3,s2,0
	lw	s3,0(s3)
	add	t1,t1,s3
	addi	s3,t2,0
	sw	t1,0(s3)
	addi	s3,t2,4
	lw	s3,0(s3)
	addi	t1,s2,4
	lw	t1,0(t1)
	add	s3,s3,t1
	addi	t1,t2,4
	sw	s3,0(t1)
	addi	s3,t2,8
	lw	s3,0(s3)
	addi	t1,s2,8
	lw	t1,0(t1)
	add	s3,s3,t1
	addi	t1,t2,8
	sw	s3,0(t1)
	mv	t1,t2
	addi	t1,s1,0
	lw	t1,0(t1)
	addi	s3,s2,0
	lw	s3,0(s3)
	add	t1,t1,s3
	addi	s3,s1,0
	sw	t1,0(s3)
	addi	s3,s1,4
	lw	s3,0(s3)
	addi	t1,s2,4
	lw	t1,0(t1)
	add	s3,s3,t1
	addi	t1,s1,4
	sw	s3,0(t1)
	addi	s3,s1,8
	lw	s3,0(s3)
	addi	t1,s2,8
	lw	t1,0(t1)
	add	s3,s3,t1
	addi	t1,s1,8
	sw	s3,0(t1)
	mv	t1,s1
	mv	t1,s2
	addi	s3,s2,0
	lw	s3,0(s3)
	addi	s4,t1,0
	lw	s4,0(s4)
	add	s3,s3,s4
	addi	s4,s2,0
	sw	s3,0(s4)
	addi	s4,s2,4
	lw	s4,0(s4)
	addi	s3,t1,4
	lw	s3,0(s3)
	add	s4,s4,s3
	addi	s3,s2,4
	sw	s4,0(s3)
	addi	s3,s2,8
	lw	s3,0(s3)
	addi	t1,t1,8
	lw	t1,0(t1)
	add	s3,s3,t1
	addi	t1,s2,8
	sw	s3,0(t1)
	mv	t1,s2
	mv	t1,t2
	addi	s3,t2,0
	lw	s3,0(s3)
	addi	s4,t1,0
	lw	s4,0(s4)
	add	s3,s3,s4
	addi	s4,t2,0
	sw	s3,0(s4)
	addi	s4,t2,4
	lw	s4,0(s4)
	addi	s3,t1,4
	lw	s3,0(s3)
	add	s4,s4,s3
	addi	s3,t2,4
	sw	s4,0(s3)
	addi	s3,t2,8
	lw	s3,0(s3)
	addi	t1,t1,8
	lw	t1,0(t1)
	add	s3,s3,t1
	addi	t1,t2,8
	sw	s3,0(t1)
	mv	t1,t2
	addi	s3,s1,0
	lw	s3,0(s3)
	addi	t1,t0,0
	lw	t1,0(t1)
	sub	t1,s3,t1
	addi	s3,s1,0
	sw	t1,0(s3)
	addi	s3,s1,4
	lw	s3,0(s3)
	addi	t1,t0,4
	lw	t1,0(t1)
	sub	t1,s3,t1
	addi	s3,s1,4
	sw	t1,0(s3)
	addi	s3,s1,8
	lw	s3,0(s3)
	addi	t1,t0,8
	lw	t1,0(t1)
	sub	s3,s3,t1
	addi	t1,s1,8
	sw	s3,0(t1)
	mv	t1,s1
	addi	t1,s1,0
	lw	t1,0(t1)
	addi	s3,s2,0
	lw	s3,0(s3)
	add	t1,t1,s3
	addi	s3,s1,0
	sw	t1,0(s3)
	addi	s3,s1,4
	lw	s3,0(s3)
	addi	t1,s2,4
	lw	t1,0(t1)
	add	s3,s3,t1
	addi	t1,s1,4
	sw	s3,0(t1)
	addi	s3,s1,8
	lw	s3,0(s3)
	addi	t1,s2,8
	lw	t1,0(t1)
	add	s3,s3,t1
	addi	t1,s1,8
	sw	s3,0(t1)
	mv	t1,s1
	addi	s3,s2,0
	lw	s3,0(s3)
	addi	t1,t2,0
	lw	t1,0(t1)
	sub	t1,s3,t1
	addi	s3,s2,0
	sw	t1,0(s3)
	addi	s3,s2,4
	lw	s3,0(s3)
	addi	t1,t2,4
	lw	t1,0(t1)
	sub	t1,s3,t1
	addi	s3,s2,4
	sw	t1,0(s3)
	addi	s3,s2,8
	lw	s3,0(s3)
	addi	t1,t2,8
	lw	t1,0(t1)
	sub	s3,s3,t1
	addi	t1,s2,8
	sw	s3,0(t1)
	addi	s3,s1,0
	lw	s3,0(s3)
	addi	t1,s1,0
	lw	t1,0(t1)
	mul	s3,s3,t1
	addi	s4,s1,4
	lw	s4,0(s4)
	addi	t1,s1,4
	lw	t1,0(t1)
	mul	t1,s4,t1
	add	s4,s3,t1
	addi	t1,s1,8
	lw	t1,0(t1)
	addi	s3,s1,8
	lw	s3,0(s3)
	mul	t1,t1,s3
	add	t1,s4,t1
	mv	a0,t1
	sw	t2,-772(s0)
	sw	t0,-768(s0)
	sw	s1,-764(s0)
	sw	s2,-760(s0)
	call	toString
	lw	t2,-772(s0)
	lw	t0,-768(s0)
	lw	s1,-764(s0)
	lw	s2,-760(s0)
	mv	t1,a0
	mv	a0,t1
	sw	t2,-756(s0)
	sw	t0,-752(s0)
	sw	s1,-748(s0)
	sw	s2,-744(s0)
	call	println
	lw	t2,-756(s0)
	lw	t0,-752(s0)
	lw	s1,-748(s0)
	lw	s2,-744(s0)
	mv	t1,a0
	addi	s3,s2,0
	lw	s3,0(s3)
	addi	t1,s2,0
	lw	t1,0(t1)
	mul	s3,s3,t1
	addi	s4,s2,4
	lw	s4,0(s4)
	addi	t1,s2,4
	lw	t1,0(t1)
	mul	t1,s4,t1
	add	s4,s3,t1
	addi	t1,s2,8
	lw	t1,0(t1)
	addi	s3,s2,8
	lw	s3,0(s3)
	mul	t1,t1,s3
	add	t1,s4,t1
	mv	a0,t1
	sw	t2,-740(s0)
	sw	t0,-736(s0)
	sw	s1,-732(s0)
	sw	s2,-728(s0)
	call	toString
	lw	t2,-740(s0)
	lw	t0,-736(s0)
	lw	s1,-732(s0)
	lw	s2,-728(s0)
	mv	t1,a0
	mv	a0,t1
	sw	t2,-724(s0)
	sw	t0,-720(s0)
	sw	s1,-716(s0)
	sw	s2,-712(s0)
	call	println
	lw	t2,-724(s0)
	lw	t0,-720(s0)
	lw	s1,-716(s0)
	lw	s2,-712(s0)
	mv	t1,a0
	addi	s3,s2,0
	lw	s3,0(s3)
	addi	t1,t2,0
	lw	t1,0(t1)
	sub	s3,s3,t1
	addi	s4,s2,0
	lw	s4,0(s4)
	addi	t1,t2,0
	lw	t1,0(t1)
	sub	t1,s4,t1
	mul	s5,s3,t1
	addi	t1,s2,4
	lw	t1,0(t1)
	addi	s3,t2,4
	lw	s3,0(s3)
	sub	s3,t1,s3
	addi	t1,s2,4
	lw	t1,0(t1)
	addi	s4,t2,4
	lw	s4,0(s4)
	sub	t1,t1,s4
	mul	t1,s3,t1
	add	s4,s5,t1
	addi	s3,s2,8
	lw	s3,0(s3)
	addi	t1,t2,8
	lw	t1,0(t1)
	sub	s3,s3,t1
	addi	t1,s2,8
	lw	t1,0(t1)
	addi	s5,t2,8
	lw	s5,0(s5)
	sub	t1,t1,s5
	mul	t1,s3,t1
	add	t1,s4,t1
	mv	a0,t1
	sw	t2,-708(s0)
	sw	t0,-704(s0)
	sw	s1,-700(s0)
	sw	s2,-696(s0)
	call	toString
	lw	t2,-708(s0)
	lw	t0,-704(s0)
	lw	s1,-700(s0)
	lw	s2,-696(s0)
	mv	t1,a0
	mv	a0,t1
	sw	t2,-692(s0)
	sw	t0,-688(s0)
	sw	s1,-684(s0)
	sw	s2,-680(s0)
	call	println
	lw	t2,-692(s0)
	lw	t0,-688(s0)
	lw	s1,-684(s0)
	lw	s2,-680(s0)
	mv	t1,a0
	addi	s3,t0,0
	lw	s3,0(s3)
	addi	t1,s1,0
	lw	t1,0(t1)
	sub	s3,s3,t1
	addi	s4,t0,0
	lw	s4,0(s4)
	addi	t1,s1,0
	lw	t1,0(t1)
	sub	t1,s4,t1
	mul	t1,s3,t1
	addi	s3,t0,4
	lw	s3,0(s3)
	addi	s4,s1,4
	lw	s4,0(s4)
	sub	s4,s3,s4
	addi	s3,t0,4
	lw	s3,0(s3)
	addi	s5,s1,4
	lw	s5,0(s5)
	sub	s3,s3,s5
	mul	s3,s4,s3
	add	s4,t1,s3
	addi	s3,t0,8
	lw	s3,0(s3)
	addi	t1,s1,8
	lw	t1,0(t1)
	sub	s3,s3,t1
	addi	t1,t0,8
	lw	t1,0(t1)
	addi	s5,s1,8
	lw	s5,0(s5)
	sub	t1,t1,s5
	mul	t1,s3,t1
	add	t1,s4,t1
	mv	a0,t1
	sw	t2,-676(s0)
	sw	t0,-672(s0)
	sw	s1,-668(s0)
	sw	s2,-664(s0)
	call	toString
	lw	t2,-676(s0)
	lw	t0,-672(s0)
	lw	s1,-668(s0)
	lw	s2,-664(s0)
	mv	t1,a0
	mv	a0,t1
	sw	t2,-660(s0)
	sw	t0,-656(s0)
	sw	s1,-652(s0)
	sw	s2,-648(s0)
	call	println
	lw	t2,-660(s0)
	lw	t0,-656(s0)
	lw	s1,-652(s0)
	lw	s2,-648(s0)
	mv	t1,a0
	addi	s3,t2,0
	lw	s3,0(s3)
	addi	t1,s1,0
	lw	t1,0(t1)
	mul	s4,s3,t1
	addi	t1,t2,4
	lw	t1,0(t1)
	addi	s3,s1,4
	lw	s3,0(s3)
	mul	t1,t1,s3
	add	s3,s4,t1
	addi	t1,t2,8
	lw	t1,0(t1)
	addi	s4,s1,8
	lw	s4,0(s4)
	mul	t1,t1,s4
	add	t1,s3,t1
	mv	a0,t1
	sw	t2,-644(s0)
	sw	t0,-640(s0)
	sw	s1,-636(s0)
	sw	s2,-632(s0)
	call	toString
	lw	t2,-644(s0)
	lw	t0,-640(s0)
	lw	s1,-636(s0)
	lw	s2,-632(s0)
	mv	t1,a0
	mv	a0,t1
	sw	t2,-628(s0)
	sw	t0,-624(s0)
	sw	s1,-620(s0)
	sw	s2,-616(s0)
	call	println
	lw	t2,-628(s0)
	lw	t0,-624(s0)
	lw	s1,-620(s0)
	lw	s2,-616(s0)
	mv	t1,a0
	li	a0,12
	sw	s2,-612(s0)
	sw	t0,-608(s0)
	sw	t2,-604(s0)
	sw	t0,-600(s0)
	sw	s1,-596(s0)
	sw	s2,-592(s0)
	call	malloc
	lw	s2,-612(s0)
	lw	t0,-608(s0)
	lw	t2,-604(s0)
	lw	t0,-600(s0)
	lw	s1,-596(s0)
	lw	s2,-592(s0)
	mv	t1,a0
	mv	s4,a0
	li	s3,0
	addi	s5,s4,0
	sw	s3,0(s5)
	li	s5,0
	addi	s3,s4,4
	sw	s5,0(s3)
	li	s5,0
	addi	s3,s4,8
	sw	s5,0(s3)
	addi	s4,s2,4
	lw	s4,0(s4)
	addi	s3,t0,8
	lw	s3,0(s3)
	mul	s3,s4,s3
	addi	s4,s2,8
	lw	s4,0(s4)
	addi	s5,t0,4
	lw	s5,0(s5)
	mul	s4,s4,s5
	sub	s3,s3,s4
	addi	s4,s2,8
	lw	s4,0(s4)
	addi	s5,t0,0
	lw	s5,0(s5)
	mul	s6,s4,s5
	addi	s5,s2,0
	lw	s5,0(s5)
	addi	s4,t0,8
	lw	s4,0(s4)
	mul	s4,s5,s4
	sub	s4,s6,s4
	addi	s6,s2,0
	lw	s6,0(s6)
	addi	s5,t0,4
	lw	s5,0(s5)
	mul	s6,s6,s5
	addi	s5,s2,4
	lw	s5,0(s5)
	addi	s7,t0,0
	lw	s7,0(s7)
	mul	s5,s5,s7
	sub	s5,s6,s5
	addi	s6,t1,0
	sw	s3,0(s6)
	addi	s3,t1,4
	sw	s4,0(s3)
	addi	s3,t1,8
	sw	s5,0(s3)
	mv	s3,a0
	lui	s4,%hi(.S0)
	addi	s4,s4,%lo(.S0)
	addi	s3,t1,0
	lw	s3,0(s3)
	mv	a0,s3
	sw	s4,-588(s0)
	sw	t1,-584(s0)
	sw	t2,-580(s0)
	sw	t0,-576(s0)
	sw	s1,-572(s0)
	sw	s2,-568(s0)
	call	toString
	lw	s4,-588(s0)
	lw	t1,-584(s0)
	lw	t2,-580(s0)
	lw	t0,-576(s0)
	lw	s1,-572(s0)
	lw	s2,-568(s0)
	mv	s3,a0
	mv	a0,s4
	mv	a1,s3
	sw	t1,-564(s0)
	sw	t2,-560(s0)
	sw	t0,-556(s0)
	sw	s1,-552(s0)
	sw	s2,-548(s0)
	call	Mx_string_ADD
	lw	t1,-564(s0)
	lw	t2,-560(s0)
	lw	t0,-556(s0)
	lw	s1,-552(s0)
	lw	s2,-548(s0)
	mv	s3,a0
	lui	s4,%hi(.S1)
	addi	s4,s4,%lo(.S1)
	mv	a0,s3
	mv	a1,s4
	sw	t1,-544(s0)
	sw	t2,-540(s0)
	sw	t0,-536(s0)
	sw	s1,-532(s0)
	sw	s2,-528(s0)
	call	Mx_string_ADD
	lw	t1,-544(s0)
	lw	t2,-540(s0)
	lw	t0,-536(s0)
	lw	s1,-532(s0)
	lw	s2,-528(s0)
	mv	s3,a0
	addi	s4,t1,4
	lw	s4,0(s4)
	mv	a0,s4
	sw	s3,-524(s0)
	sw	t1,-520(s0)
	sw	t2,-516(s0)
	sw	t0,-512(s0)
	sw	s1,-508(s0)
	sw	s2,-504(s0)
	call	toString
	lw	s3,-524(s0)
	lw	t1,-520(s0)
	lw	t2,-516(s0)
	lw	t0,-512(s0)
	lw	s1,-508(s0)
	lw	s2,-504(s0)
	mv	s4,a0
	mv	a0,s3
	mv	a1,s4
	sw	t1,-500(s0)
	sw	t2,-496(s0)
	sw	t0,-492(s0)
	sw	s1,-488(s0)
	sw	s2,-484(s0)
	call	Mx_string_ADD
	lw	t1,-500(s0)
	lw	t2,-496(s0)
	lw	t0,-492(s0)
	lw	s1,-488(s0)
	lw	s2,-484(s0)
	mv	s3,a0
	lui	s4,%hi(.S2)
	addi	s4,s4,%lo(.S2)
	mv	a0,s3
	mv	a1,s4
	sw	t1,-480(s0)
	sw	t2,-476(s0)
	sw	t0,-472(s0)
	sw	s1,-468(s0)
	sw	s2,-464(s0)
	call	Mx_string_ADD
	lw	t1,-480(s0)
	lw	t2,-476(s0)
	lw	t0,-472(s0)
	lw	s1,-468(s0)
	lw	s2,-464(s0)
	mv	s3,a0
	addi	t1,t1,8
	lw	t1,0(t1)
	mv	a0,t1
	sw	s3,-460(s0)
	sw	t2,-456(s0)
	sw	t0,-452(s0)
	sw	s1,-448(s0)
	sw	s2,-444(s0)
	call	toString
	lw	s3,-460(s0)
	lw	t2,-456(s0)
	lw	t0,-452(s0)
	lw	s1,-448(s0)
	lw	s2,-444(s0)
	mv	t1,a0
	mv	a0,s3
	mv	a1,t1
	sw	t2,-440(s0)
	sw	t0,-436(s0)
	sw	s1,-432(s0)
	sw	s2,-428(s0)
	call	Mx_string_ADD
	lw	t2,-440(s0)
	lw	t0,-436(s0)
	lw	s1,-432(s0)
	lw	s2,-428(s0)
	mv	t1,a0
	lui	s3,%hi(.S3)
	addi	s3,s3,%lo(.S3)
	mv	a0,t1
	mv	a1,s3
	sw	t2,-424(s0)
	sw	t0,-420(s0)
	sw	s1,-416(s0)
	sw	s2,-412(s0)
	call	Mx_string_ADD
	lw	t2,-424(s0)
	lw	t0,-420(s0)
	lw	s1,-416(s0)
	lw	s2,-412(s0)
	mv	t1,a0
	mv	a0,t1
	sw	t2,-408(s0)
	sw	t0,-404(s0)
	sw	s1,-400(s0)
	sw	s2,-396(s0)
	call	println
	lw	t2,-408(s0)
	lw	t0,-404(s0)
	lw	s1,-400(s0)
	lw	s2,-396(s0)
	mv	t1,a0
	lui	s3,%hi(.S0)
	addi	s3,s3,%lo(.S0)
	addi	t1,s1,0
	lw	t1,0(t1)
	mv	a0,t1
	sw	s3,-392(s0)
	sw	s1,-388(s0)
	sw	t2,-384(s0)
	sw	t0,-380(s0)
	sw	s2,-376(s0)
	call	toString
	lw	s3,-392(s0)
	lw	s1,-388(s0)
	lw	t2,-384(s0)
	lw	t0,-380(s0)
	lw	s2,-376(s0)
	mv	t1,a0
	mv	a0,s3
	mv	a1,t1
	sw	s1,-372(s0)
	sw	t2,-368(s0)
	sw	t0,-364(s0)
	sw	s2,-360(s0)
	call	Mx_string_ADD
	lw	s1,-372(s0)
	lw	t2,-368(s0)
	lw	t0,-364(s0)
	lw	s2,-360(s0)
	mv	t1,a0
	lui	s3,%hi(.S1)
	addi	s3,s3,%lo(.S1)
	mv	a0,t1
	mv	a1,s3
	sw	s1,-356(s0)
	sw	t2,-352(s0)
	sw	t0,-348(s0)
	sw	s2,-344(s0)
	call	Mx_string_ADD
	lw	s1,-356(s0)
	lw	t2,-352(s0)
	lw	t0,-348(s0)
	lw	s2,-344(s0)
	mv	t1,a0
	addi	s3,s1,4
	lw	s3,0(s3)
	mv	a0,s3
	sw	s1,-340(s0)
	sw	t1,-336(s0)
	sw	t2,-332(s0)
	sw	t0,-328(s0)
	sw	s2,-324(s0)
	call	toString
	lw	s1,-340(s0)
	lw	t1,-336(s0)
	lw	t2,-332(s0)
	lw	t0,-328(s0)
	lw	s2,-324(s0)
	mv	s3,a0
	mv	a0,t1
	mv	a1,s3
	sw	s1,-320(s0)
	sw	t2,-316(s0)
	sw	t0,-312(s0)
	sw	s2,-308(s0)
	call	Mx_string_ADD
	lw	s1,-320(s0)
	lw	t2,-316(s0)
	lw	t0,-312(s0)
	lw	s2,-308(s0)
	mv	t1,a0
	lui	s3,%hi(.S2)
	addi	s3,s3,%lo(.S2)
	mv	a0,t1
	mv	a1,s3
	sw	s1,-304(s0)
	sw	t2,-300(s0)
	sw	t0,-296(s0)
	sw	s2,-292(s0)
	call	Mx_string_ADD
	lw	s1,-304(s0)
	lw	t2,-300(s0)
	lw	t0,-296(s0)
	lw	s2,-292(s0)
	mv	t1,a0
	addi	s1,s1,8
	lw	s1,0(s1)
	mv	a0,s1
	sw	t1,-288(s0)
	sw	t2,-284(s0)
	sw	t0,-280(s0)
	sw	s2,-276(s0)
	call	toString
	lw	t1,-288(s0)
	lw	t2,-284(s0)
	lw	t0,-280(s0)
	lw	s2,-276(s0)
	mv	s1,a0
	mv	a0,t1
	mv	a1,s1
	sw	t2,-272(s0)
	sw	t0,-268(s0)
	sw	s2,-264(s0)
	call	Mx_string_ADD
	lw	t2,-272(s0)
	lw	t0,-268(s0)
	lw	s2,-264(s0)
	mv	t1,a0
	lui	s1,%hi(.S3)
	addi	s1,s1,%lo(.S3)
	mv	a0,t1
	mv	a1,s1
	sw	t2,-260(s0)
	sw	t0,-256(s0)
	sw	s2,-252(s0)
	call	Mx_string_ADD
	lw	t2,-260(s0)
	lw	t0,-256(s0)
	lw	s2,-252(s0)
	mv	t1,a0
	mv	a0,t1
	sw	t2,-248(s0)
	sw	t0,-244(s0)
	sw	s2,-240(s0)
	call	println
	lw	t2,-248(s0)
	lw	t0,-244(s0)
	lw	s2,-240(s0)
	mv	t1,a0
	lui	s1,%hi(.S0)
	addi	s1,s1,%lo(.S0)
	addi	t1,s2,0
	lw	t1,0(t1)
	mv	a0,t1
	sw	s1,-236(s0)
	sw	t2,-232(s0)
	sw	s2,-228(s0)
	sw	t0,-224(s0)
	call	toString
	lw	s1,-236(s0)
	lw	t2,-232(s0)
	lw	s2,-228(s0)
	lw	t0,-224(s0)
	mv	t1,a0
	mv	a0,s1
	mv	a1,t1
	sw	t2,-220(s0)
	sw	s2,-216(s0)
	sw	t0,-212(s0)
	call	Mx_string_ADD
	lw	t2,-220(s0)
	lw	s2,-216(s0)
	lw	t0,-212(s0)
	mv	t1,a0
	lui	s1,%hi(.S1)
	addi	s1,s1,%lo(.S1)
	mv	a0,t1
	mv	a1,s1
	sw	t2,-208(s0)
	sw	s2,-204(s0)
	sw	t0,-200(s0)
	call	Mx_string_ADD
	lw	t2,-208(s0)
	lw	s2,-204(s0)
	lw	t0,-200(s0)
	mv	t1,a0
	addi	s1,s2,4
	lw	s1,0(s1)
	mv	a0,s1
	sw	t1,-196(s0)
	sw	t2,-192(s0)
	sw	s2,-188(s0)
	sw	t0,-184(s0)
	call	toString
	lw	t1,-196(s0)
	lw	t2,-192(s0)
	lw	s2,-188(s0)
	lw	t0,-184(s0)
	mv	s1,a0
	mv	a0,t1
	mv	a1,s1
	sw	t2,-180(s0)
	sw	s2,-176(s0)
	sw	t0,-172(s0)
	call	Mx_string_ADD
	lw	t2,-180(s0)
	lw	s2,-176(s0)
	lw	t0,-172(s0)
	mv	t1,a0
	lui	s1,%hi(.S2)
	addi	s1,s1,%lo(.S2)
	mv	a0,t1
	mv	a1,s1
	sw	t2,-168(s0)
	sw	s2,-164(s0)
	sw	t0,-160(s0)
	call	Mx_string_ADD
	lw	t2,-168(s0)
	lw	s2,-164(s0)
	lw	t0,-160(s0)
	mv	t1,a0
	addi	s1,s2,8
	lw	s1,0(s1)
	mv	a0,s1
	sw	t1,-156(s0)
	sw	t2,-152(s0)
	sw	t0,-148(s0)
	call	toString
	lw	t1,-156(s0)
	lw	t2,-152(s0)
	lw	t0,-148(s0)
	mv	s1,a0
	mv	a0,t1
	mv	a1,s1
	sw	t2,-144(s0)
	sw	t0,-140(s0)
	call	Mx_string_ADD
	lw	t2,-144(s0)
	lw	t0,-140(s0)
	mv	t1,a0
	lui	s1,%hi(.S3)
	addi	s1,s1,%lo(.S3)
	mv	a0,t1
	mv	a1,s1
	sw	t2,-136(s0)
	sw	t0,-132(s0)
	call	Mx_string_ADD
	lw	t2,-136(s0)
	lw	t0,-132(s0)
	mv	t1,a0
	mv	a0,t1
	sw	t2,-128(s0)
	sw	t0,-124(s0)
	call	println
	lw	t2,-128(s0)
	lw	t0,-124(s0)
	mv	t1,a0
	lui	s1,%hi(.S0)
	addi	s1,s1,%lo(.S0)
	addi	t1,t2,0
	lw	t1,0(t1)
	mv	a0,t1
	sw	s1,-120(s0)
	sw	t2,-116(s0)
	sw	t0,-112(s0)
	call	toString
	lw	s1,-120(s0)
	lw	t2,-116(s0)
	lw	t0,-112(s0)
	mv	t1,a0
	mv	a0,s1
	mv	a1,t1
	sw	t2,-108(s0)
	sw	t0,-104(s0)
	call	Mx_string_ADD
	lw	t2,-108(s0)
	lw	t0,-104(s0)
	mv	t1,a0
	lui	s1,%hi(.S1)
	addi	s1,s1,%lo(.S1)
	mv	a0,t1
	mv	a1,s1
	sw	t2,-100(s0)
	sw	t0,-96(s0)
	call	Mx_string_ADD
	lw	t2,-100(s0)
	lw	t0,-96(s0)
	mv	t1,a0
	addi	s1,t2,4
	lw	s1,0(s1)
	mv	a0,s1
	sw	t2,-92(s0)
	sw	t1,-88(s0)
	sw	t0,-84(s0)
	call	toString
	lw	t2,-92(s0)
	lw	t1,-88(s0)
	lw	t0,-84(s0)
	mv	s1,a0
	mv	a0,t1
	mv	a1,s1
	sw	t2,-80(s0)
	sw	t0,-76(s0)
	call	Mx_string_ADD
	lw	t2,-80(s0)
	lw	t0,-76(s0)
	mv	t1,a0
	lui	s1,%hi(.S2)
	addi	s1,s1,%lo(.S2)
	mv	a0,t1
	mv	a1,s1
	sw	t2,-72(s0)
	sw	t0,-68(s0)
	call	Mx_string_ADD
	lw	t2,-72(s0)
	lw	t0,-68(s0)
	mv	t1,a0
	addi	t2,t2,8
	lw	t2,0(t2)
	mv	a0,t2
	sw	t1,-64(s0)
	sw	t0,-60(s0)
	call	toString
	lw	t1,-64(s0)
	lw	t0,-60(s0)
	mv	t2,a0
	mv	a0,t1
	mv	a1,t2
	sw	t0,-56(s0)
	call	Mx_string_ADD
	lw	t0,-56(s0)
	mv	t1,a0
	lui	t2,%hi(.S3)
	addi	t2,t2,%lo(.S3)
	mv	a0,t1
	mv	a1,t2
	sw	t0,-52(s0)
	call	Mx_string_ADD
	lw	t0,-52(s0)
	mv	t1,a0
	mv	a0,t1
	sw	t0,-48(s0)
	call	println
	lw	t0,-48(s0)
	mv	t1,a0
	lui	t2,%hi(.S0)
	addi	t2,t2,%lo(.S0)
	addi	t1,t0,0
	lw	t1,0(t1)
	mv	a0,t1
	sw	t0,-44(s0)
	sw	t2,-40(s0)
	call	toString
	lw	t0,-44(s0)
	lw	t2,-40(s0)
	mv	t1,a0
	mv	a0,t2
	mv	a1,t1
	sw	t0,-36(s0)
	call	Mx_string_ADD
	lw	t0,-36(s0)
	mv	t1,a0
	lui	t2,%hi(.S1)
	addi	t2,t2,%lo(.S1)
	mv	a0,t1
	mv	a1,t2
	sw	t0,-32(s0)
	call	Mx_string_ADD
	lw	t0,-32(s0)
	mv	t1,a0
	addi	t2,t0,4
	lw	t2,0(t2)
	mv	a0,t2
	sw	t0,-28(s0)
	sw	t1,-24(s0)
	call	toString
	lw	t0,-28(s0)
	lw	t1,-24(s0)
	mv	t2,a0
	mv	a0,t1
	mv	a1,t2
	sw	t0,-20(s0)
	call	Mx_string_ADD
	lw	t0,-20(s0)
	mv	t1,a0
	lui	t2,%hi(.S2)
	addi	t2,t2,%lo(.S2)
	mv	a0,t1
	mv	a1,t2
	sw	t0,-16(s0)
	call	Mx_string_ADD
	lw	t0,-16(s0)
	mv	t1,a0
	addi	t0,t0,8
	lw	t0,0(t0)
	mv	a0,t0
	sw	t1,-12(s0)
	call	toString
	lw	t1,-12(s0)
	mv	t0,a0
	mv	a0,t1
	mv	a1,t0
	call	Mx_string_ADD
	mv	t0,a0
	lui	t1,%hi(.S3)
	addi	t1,t1,%lo(.S3)
	mv	a0,t0
	mv	a1,t1
	call	Mx_string_ADD
	mv	t0,a0
	mv	a0,t0
	call	println
	mv	t0,a0
	li	a0,0
	j	.B1
.B1:
	lw	s0,992(sp)
	lw	ra,988(sp)
	addi	sp,sp,996
	jr	ra
	.size	main, .-main
