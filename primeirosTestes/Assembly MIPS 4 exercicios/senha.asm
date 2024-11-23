    .data
prompt_senha:    .asciiz "\nDigite a senha: "
msg_sucesso:     .asciiz "\nSenha correta! Acesso permitido.\n"
msg_falha:       .asciiz "\nSenha incorreta! Tente novamente.\n"

    .text
    .globl main

main:
    # Definir a senha correta no registrador $t0
    li $t0, 1234              # A senha correta é 1234, armazenada no registrador $t0

autenticacao:
    # Solicitar a senha ao usuário
    li $v0, 4                 # Syscall para imprimir string
    la $a0, prompt_senha      # Carregar o prompt para a senha
    syscall

    li $v0, 5                 # Syscall para ler um inteiro
    syscall
    move $t1, $v0             # Armazena a senha fornecida pelo usuário em $t1

    # Comparar a senha fornecida com a senha correta
    xor $t2, $t0, $t1         # XOR bit a bit entre a senha correta ($t0) e a fornecida ($t1)
    beqz $t2, senha_correta   # Se $t2 == 0, a senha é correta (não houve diferença nos bits)

senha_incorreta:
    # Exibir a mensagem de falha
    li $v0, 4                 # Syscall para imprimir string
    la $a0, msg_falha         # Carrega a mensagem de senha incorreta
    syscall

    # Voltar a solicitar a senha
    j autenticacao

senha_correta:
    # Exibir a mensagem de sucesso
    li $v0, 4                 # Syscall para imprimir string
    la $a0, msg_sucesso       # Carrega a mensagem de senha correta
    syscall

    # Encerrar o programa
    li $v0, 10                # Syscall para encerrar o programa
    syscall
