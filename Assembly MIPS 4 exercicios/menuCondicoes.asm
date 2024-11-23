    .data
menu:       .asciiz "\nMenu:\n1. Soma\n2. Subtracao\n3. Multiplicacao\n4. Divisao\n5. Sair\nEscolha: "
num1_prompt:.asciiz "\nDigite o primeiro numero: "
num2_prompt:.asciiz "\nDigite o segundo numero: "
res_soma:   .asciiz "\nResultado da soma: "
res_sub:    .asciiz "\nResultado da subtracao: "
res_mul:    .asciiz "\nResultado da multiplicacao: "
res_div:    .asciiz "\nResultado da divisao: Quociente: "
res_rest:   .asciiz " Resto: "
err_div_zero: .asciiz "\nErro: Divisao por zero!\n"
newline:    .asciiz "\n"
    
    .text
    .globl main

main:
    # Exibir o menu e ler a opcao do usuario
menu_loop:
    li $v0, 4                # Syscall para imprimir string
    la $a0, menu             # Carrega o endereco da string do menu
    syscall                  # Chama o syscall para exibir o menu

    li $v0, 5                # Syscall para ler um inteiro
    syscall                  # Le a opcao escolhida
    move $t0, $v0            # Salva a opcao escolhida em $t0

    # Verifica se o usuario escolheu a opcao de sair (5)
    li $t1, 5                # Carrega 5 em $t1
    beq $t0, $t1, exit       # Se a opcao for 5, sai do programa

    # Se a opcao for de 1 a 4, continua para solicitar os numeros
    li $t1, 1
    blt $t0, $t1, menu_loop  # Se for menor que 1, volta ao menu
    li $t1, 4
    bgt $t0, $t1, menu_loop  # Se for maior que 4, volta ao menu

    # Solicita os dois numeros
    li $v0, 4                # Syscall para imprimir string
    la $a0, num1_prompt      # Carrega o endereco da string
    syscall                  # Exibe o prompt para o primeiro numero

    li $v0, 5                # Syscall para ler um inteiro
    syscall                  # Le o primeiro numero
    move $t1, $v0            # Armazena o primeiro numero em $t1

    li $v0, 4                # Syscall para imprimir string
    la $a0, num2_prompt      # Carrega o endereco da string
    syscall                  # Exibe o prompt para o segundo numero

    li $v0, 5                # Syscall para ler um inteiro
    syscall                  # Le o segundo numero
    move $t2, $v0            # Armazena o segundo numero em $t2

    # Realiza a operacao escolhida
    li $t3, 1                # Para comparar com as opcoes
    beq $t0, $t3, soma       # Se opcao = 1, vai para soma
    li $t3, 2
    beq $t0, $t3, subtracao  # Se opcao = 2, vai para subtracao
    li $t3, 3
    beq $t0, $t3, multiplicacao  # Se opcao = 3, vai para multiplicacao
    li $t3, 4
    beq $t0, $t3, divisao    # Se opcao = 4, vai para divisao

    # Volta ao menu se nenhuma opcao valida for escolhida
    b menu_loop

soma:
    add $t3, $t1, $t2        # Soma os dois numeros
    li $v0, 4                # Exibir string
    la $a0, res_soma         # Mensagem de resultado da soma
    syscall

    move $a0, $t3            # Carrega o resultado no $a0 para exibir
    li $v0, 1                # Exibir inteiro
    syscall
    b menu_loop              # Volta ao menu

subtracao:
    sub $t3, $t1, $t2        # Subtrai os dois numeros
    li $v0, 4                # Exibir string
    la $a0, res_sub          # Mensagem de resultado da subtracao
    syscall

    move $a0, $t3            # Carrega o resultado no $a0 para exibir
    li $v0, 1                # Exibir inteiro
    syscall
    b menu_loop              # Volta ao menu

multiplicacao:
    mul $t3, $t1, $t2        # Multiplica os dois numeros
    li $v0, 4                # Exibir string
    la $a0, res_mul          # Mensagem de resultado da multiplicacao
    syscall

    move $a0, $t3            # Carrega o resultado no $a0 para exibir
    li $v0, 1                # Exibir inteiro
    syscall
    b menu_loop              # Volta ao menu

divisao:
    # Verifica se o segundo numero e zero (divisao por zero)
    beqz $t2, div_erro       # Se $t2 == 0, vai para o erro

    div $t1, $t2             # Divisao
    mflo $t3                 # Quociente no LO
    mfhi $t4                 # Resto no HI

    # Exibe o quociente
    li $v0, 4                # Exibir string
    la $a0, res_div          # Mensagem de resultado da divisao
    syscall

    move $a0, $t3            # Carrega o quociente no $a0
    li $v0, 1                # Exibir inteiro
    syscall

    # Exibe o resto
    li $v0, 4                # Exibir string
    la $a0, res_rest         # Mensagem de resto
    syscall

    move $a0, $t4            # Carrega o resto no $a0
    li $v0, 1                # Exibir inteiro
    syscall

    b menu_loop              # Volta ao menu

div_erro:
    li $v0, 4                # Exibir mensagem de erro
    la $a0, err_div_zero     # Mensagem de erro divisao por zero
    syscall
    b menu_loop              # Volta ao menu

exit:
    li $v0, 10               # Syscall para encerrar o programa
    syscall
