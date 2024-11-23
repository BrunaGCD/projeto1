    .data
prompt1:    .asciiz "\nDigite o 1º numero: "
prompt2:    .asciiz "\nDigite o 2º numero: "
prompt3:    .asciiz "\nDigite o 3º numero: "
msg_maior:  .asciiz "\nMaior valor: "
msg_medio:  .asciiz "\nValor intermediario: "
msg_menor:  .asciiz "\nMenor valor: "

    .text
    .globl main

main:
    # Solicita o primeiro número
    li $v0, 4               # Syscall para imprimir string
    la $a0, prompt1         # Carrega o prompt para o 1º número
    syscall

    li $v0, 5               # Syscall para ler um inteiro
    syscall
    move $t0, $v0           # Armazena o 1º número em $t0

    # Solicita o segundo número
    li $v0, 4               # Syscall para imprimir string
    la $a0, prompt2         # Carrega o prompt para o 2º número
    syscall

    li $v0, 5               # Syscall para ler um inteiro
    syscall
    move $t1, $v0           # Armazena o 2º número em $t1

    # Solicita o terceiro número
    li $v0, 4               # Syscall para imprimir string
    la $a0, prompt3         # Carrega o prompt para o 3º número
    syscall

    li $v0, 5               # Syscall para ler um inteiro
    syscall
    move $t2, $v0           # Armazena o 3º número em $t2

    # Encontrar o maior (max)
    move $t3, $t0           # Inicializa t3 com o primeiro número (t0)
    bgt $t1, $t3, maior_t1  # Se t1 > t3, t1 é o maior
    move $t3, $t1
maior_t1:
    bgt $t2, $t3, maior_t2  # Se t2 > t3, t2 é o maior
    move $t3, $t2
maior_t2:

    # Encontrar o menor (min)
    move $t4, $t0           # Inicializa t4 com o primeiro número (t0)
    blt $t1, $t4, menor_t1  # Se t1 < t4, t1 é o menor
    move $t4, $t1
menor_t1:
    blt $t2, $t4, menor_t2  # Se t2 < t4, t2 é o menor
    move $t4, $t2
menor_t2:

    # Calcular o intermediário: intermediario = a + b + c - maior - menor
    add $t5, $t0, $t1       # t5 = a + b
    add $t5, $t5, $t2       # t5 = a + b + c
    sub $t5, $t5, $t3       # t5 = (a + b + c) - maior
    sub $t5, $t5, $t4       # t5 = (a + b + c - maior) - menor = intermediário

    # Exibir o maior valor
    li $v0, 4               # Syscall para imprimir string
    la $a0, msg_maior       # Carrega a mensagem "Maior valor"
    syscall

    move $a0, $t3           # Carrega o maior valor
    li $v0, 1               # Syscall para imprimir inteiro
    syscall

    # Exibir o valor intermediario
    li $v0, 4               # Syscall para imprimir string
    la $a0, msg_medio       # Carrega a mensagem "Valor intermediario"
    syscall

    move $a0, $t5           # Carrega o valor intermediário
    li $v0, 1               # Syscall para imprimir inteiro
    syscall

    # Exibir o menor valor
    li $v0, 4               # Syscall para imprimir string
    la $a0, msg_menor       # Carrega a mensagem "Menor valor"
    syscall

    move $a0, $t4           # Carrega o menor valor
    li $v0, 1               # Syscall para imprimir inteiro
    syscall

    # Encerrar o programa
    li $v0, 10              # Syscall para encerrar o programa
    syscall
