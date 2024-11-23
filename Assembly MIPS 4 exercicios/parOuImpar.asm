    .data
prompt_numero:   .asciiz "\nDigite um numero: "
msg_par:         .asciiz "\nO numero é par.\n"
msg_impar:       .asciiz "\nO numero é impar.\n"

    .text
    .globl main

main:
    # Solicitar o número ao usuário
    li $v0, 4                 # Syscall para imprimir string
    la $a0, prompt_numero     # Carregar o prompt para o número
    syscall

    li $v0, 5                 # Syscall para ler um inteiro
    syscall
    move $t0, $v0             # Armazenar o número fornecido em $t0

    # Verificar se o número é par ou ímpar
    andi $t1, $t0, 1          # AND bit a bit com 1 (verifica se o bit menos significativo é 1)
    beqz $t1, numero_par      # Se o resultado for 0, o número é par

numero_impar:
    # Exibir mensagem de número ímpar
    li $v0, 4                 # Syscall para imprimir string
    la $a0, msg_impar         # Carregar a mensagem "O número é ímpar"
    syscall
    j fim                     # Pular para o fim do programa

numero_par:
    # Exibir mensagem de número par
    li $v0, 4                 # Syscall para imprimir string
    la $a0, msg_par           # Carregar a mensagem "O número é par"
    syscall

fim:
    # Encerrar o programa
    li $v0, 10                # Syscall para encerrar o programa
    syscall
