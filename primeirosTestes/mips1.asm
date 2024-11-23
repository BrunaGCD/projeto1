.data
	N1: .asciiz "Digite o 1o numero: "
	N2: .asciiz "Digite o 2o numero: "
	Resultado: .asciiz "Resultado da soma: "

.text

# IMPRIMIR MENSAGEM N1
	li $v0,4
	la $a0,N1
	syscall
	
# INPUT DO USUÁRIO
	li $v0,5
	syscall

# FAZER COPIA DO DADO LIDO
	move $t0, $v0

# ----------------------

# IMPRIMIR MENSAGEM N2
	li $v0,4
	la $a0,N2
	syscall

# INPUT DO USUÁRIO
	li $v0,5
	syscall

# FAZER COPIA DO DADO LIDO
	move $t1, $v0

# ---------------------

# REALIZARA SOMA ENTRE NUMEROS
	add $t2, $t1, $t0

# IMPRIMIR MENSAGEM RESULTADO
	li $v0,4
	la $a0,Resultado
	syscall

# IMPRIMIR O RESULTADO DA SOMA
	li $v0,1
	move $a0, $t2
	syscall
