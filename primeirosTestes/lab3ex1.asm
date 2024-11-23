.data
msg_qtd:         .asciiz "\nDigite a quantidade de números: "
msg_numero:      .asciiz "Digite o número "
msg_doispontos:  .asciiz ": "
msg_erro:        .asciiz "Quantidade inválida. Tente novamente.\n"
msg_trocas:      .asciiz "\nTotal de trocas realizadas: "
msg_ordenado:    .asciiz "\nNúmeros ordenados: "
virgula_espaco:  .asciiz ", "

.text
.globl main

# ========= MAIN =========

main:
    # Mensagem solicitando a quantidade de números
    li $v0, 4
    la $a0, msg_qtd
    syscall

    # Ler número inteiro
    li $v0, 5
    syscall
    
    # Armazena a quantidade de números do vetor em $t0
    move $t0, $v0

    # Verifica se a quantidade é válida (maior que 0)
    blez $t0, erro_quantidade     # Se $t0 <= 0, imprime mensagem de erro

    # Aloca memória para o vetor (4 bytes por número)
    mul $a0, $t0, 4               # Qtd * 4 = Tamanho total do vetor em bytes
    li $v0, 9                     # Syscall para alocar memória
    syscall
    move $s0, $v0                 # $s0 = endereço base do vetor (head do vetor)

    # Ler cada número e armazená-los no vetor
    li $t2, 0                     # Índice do vetor (inicializado em 0)

ler_numeros:
    # Mensagem "Digite o número "
    li $v0, 4
    la $a0, msg_numero
    syscall

    # Imprimir o índice (número 1, número 2, etc.)
    addi $a0, $t2, 1              # Incrementa para mostrar o número do índice
    li $v0, 1
    syscall
    
    # Finaliza mensagem "Digite o número "
    li $v0, 4
    la $a0, msg_doispontos
    syscall

    # Ler o número e armazenar no vetor
    li $v0, 5
    syscall
    sw $v0, 0($s0)                # Armazena o número no vetor
    addi $s0, $s0, 4              # Avança para o próximo índice (de 4 em 4 bytes)
    addi $t2, $t2, 1              # Incrementa o índice
    blt $t2, $t0, ler_numeros     # Se índice < quantidade, continua lendo

    # Redefine $s0 para o endereço base do vetor antes de ordenar
    subi $s0, $s0, 4              # Ajusta $s0 para o último elemento
    move $t3, $t0                 # $t3 = qtd (número de elementos)
    subi $t3, $t3, 1              # $t3 = (qtd - 1)

    j bubble_sort                 # Chama a função de ordenação

erro_quantidade:
    # Mensagem de erro
    li $v0, 4
    la $a0, msg_erro
    syscall
    
    # Loop de volta para main
    j main                     

# ========= BUBBLE SORT =========

bubble_sort:
    # Inicialização
    li $t4, 0                    # Inicializa a contagem de trocas em $t4

loop_externo:
    li $t5, 0                    # Inicializa a flag de trocas para cada passagem
    li $t6, 0                    # Índice de comparação começa do início
    move $s1, $s0                # Reinicia o ponteiro do vetor para o início

loop_interno:
    lw $t7, 0($s1)               # Carrega o valor do índice atual
    lw $t8, 4($s1)               # Carrega o próximo valor

    # Comparar e, se necessário, trocar
    ble $t7, $t8, sem_troca      # Se $t7 <= $t8, não troca

    # Realizar a troca
    sw $t8, 0($s1)               # Armazena $t8 na posição atual
    sw $t7, 4($s1)               # Armazena $t7 na próxima posição
    addi $t5, $t5, 1             # Sinaliza que houve uma troca
    addi $t4, $t4, 1             # Incrementa a contagem total de trocas

sem_troca:
    addi $s1, $s1, 4             # Avança para o próximo par
    addi $t6, $t6, 1             # Incrementa o índice interno
    blt $t6, $t3, loop_interno   # Continua até o penúltimo elemento

    beqz $t5, fim_ordenacao      # Se não houve trocas, termina a ordenação
    subi $t3, $t3, 1             # Reduz o alcance do loop interno
    j loop_externo               # Repete o loop externo

fim_ordenacao:
    j imprime_resultado          # Chama a função para exibir o resultado

# ========= IMPRIME RESULTADO =========

imprime_resultado:
    # Mensagem de números ordenados
    li $v0, 4
    la $a0, msg_ordenado
    syscall

    # Reinicializar o ponteiro do vetor para o endereço base (início do vetor)
    move $s0, $v0                 # Redefine o ponteiro para o endereço base
    li $t2, 0                     # Reinicializa o índice para o loop de impressão

    # Ajusta o ponteiro para o início do vetor novamente
    la $t9, 0($s0)                # Aqui corrigimos para garantir que $t0 aponta para a memória correta

imprime_vetor:
    lw $a0, 0($t9)                # Carrega o próximo número do vetor
    li $v0, 1                     # Syscall para imprimir um número
    syscall

    # Imprimir a vírgula e espaço, exceto no último número
    addi $t2, $t2, 1              # Incrementa o índice
    bge $t2, $t0, fim_impressao   # Se índice >= quantidade, termina impressão

    li $v0, 4
    la $a0, virgula_espaco
    syscall

    addi $t9, $t9, 4              # Avança para o próximo elemento do vetor
    j imprime_vetor               # Continua imprimindo o próximo número

fim_impressao:
    # Mensagem de total de trocas realizadas
    li $v0, 4
    la $a0, msg_trocas
    syscall

    # Imprime o total de trocas
    move $a0, $t4                 # Carrega o total de trocas em $a0
    li $v0, 1                     # Syscall para imprimir número
    syscall

    j main                        # Retorna ao menu principal
