/*

[ INTEGRANTES DO GRUPO ]
 
Bruna Gonçalves Corte David     | RA: 10425696
Caio Guilherme dos Santos Silva | RA: 10420097
Daniela Pereira da Silva        | RA: 10410906

*/

// Importando Bibliotecas
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class REPL {
    private LinkedList codeList;
    private boolean unsavedChanges;
    private String currentFilePath;
    private int[] registradores;
    private boolean[] registradoresDeclarados;

    public REPL() {
        this.codeList = new LinkedList();
        this.unsavedChanges = false;
        this.currentFilePath = null;
        this.registradores = new int[26];
        this.registradoresDeclarados = new boolean[26];

    }


    // =-=-=-=-=-=-=-=-=-=-=-=-=-= LOOP PRINCIPAL (REPL) =-=-=-=-=-=-=-=-=-=-=-=-=-=

    public void loopREPL() {
        Scanner scanner = new Scanner(System.in);
        String input;
    
        System.out.println("=-=-= Interpretador de Assembly Simplificado =-=-=");
    
        while (true) {
            System.out.print("> ");
            input = scanner.nextLine().trim();
    
            // Comando EXIT
            if (input.equalsIgnoreCase("EXIT")) {
                exit();
                break;
            }
    
            // Comando INS
            if (input.toUpperCase().startsWith("INS")) {
                try {
                    String[] parts = input.split(" ", 3);
                    int lineNumber = Integer.parseInt(parts[1]);
                    String instruction = parts[2];
                    insertLine(lineNumber, instruction);
                } catch (Exception e) {
                    System.out.println("Erro: formato inválido. Use 'INS <número da linha> <instrução>'.");
                }
                continue;
            }
    
            // Comando DEL
            if (input.toUpperCase().startsWith("DEL")) {
                try {
                    String[] parts = input.split(" ");
                    if (parts.length == 2) {        // Deleta linha específica
                        int lineNumber = Integer.parseInt(parts[1]);
                        deleteLine(lineNumber);
                    } else if (parts.length == 3) { // Deleta intervalo
                        int startLine = Integer.parseInt(parts[1]);
                        int endLine = Integer.parseInt(parts[2]);
                        deleteRange(startLine, endLine);
                    } else {
                        System.out.println("Erro: formato inválido. Use 'DEL <número da linha>' ou 'DEL <linha inicial> <linha final>'.");
                    }
                } catch (Exception e) {
                    System.out.println("Erro: formato inválido para o comando DEL.");
                }
                continue;
            }
    
            // Comando SAVE
            if (input.toUpperCase().startsWith("SAVE")) {
                String[] parts = input.split(" ", 2);
                if (parts.length == 1) { // Salva no arquivo atual
                    if (currentFilePath != null) {
                        save(currentFilePath);
                    } else {
                        System.out.println("Erro: nenhum arquivo carregado. Use 'LOAD <caminho do arquivo>' ou 'SAVE <caminho do arquivo>' para especificar um arquivo.");
                    }
                } else {                 // Salva no caminho especificado
                    save(parts[1].trim());
                }
                continue;
            }

    
            // Comando LOAD
            if (input.toUpperCase().startsWith("LOAD")) {
                try {
                    String[] parts = input.split(" ", 2);
                    if (parts.length == 2) {
                        loadFile(parts[1].trim());
                    } else {
                        System.out.println("Erro: formato inválido. Use 'LOAD <caminho do arquivo>'.");
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao carregar o arquivo: " + e.getMessage());
                }
                continue;
            }
    
            // Comando LIST
            if (input.equalsIgnoreCase("LIST")) {
                list();
                continue;
            }
    
            // Comando RUN
            if (input.equalsIgnoreCase("RUN")) {
                run();
                continue;
            }
    
            System.out.println("Erro: Comando inválido.");
        }
    }


    // =-=-=-=-=-=-=-=-=-=-=-=-=-= MÉTODOS BÁSICOS =-=-=-=-=-=-=-=-=-=-=-=-=-=


    // Carrega um arquivo .ed1
    public void loadFile(String filePath) {
        // Verifica se há alterações não salvas antes de carregar novo arquivo
        if (unsavedChanges) {
            System.out.println("Arquivo atual contém alterações não salvas. Deseja salvar? (S/N)");
            Scanner inputScanner = new Scanner(System.in);
            String response = inputScanner.nextLine().trim().toUpperCase();
            if (response.equals("S")) {
                save(currentFilePath);
            }
        }

        // Carrega novo arquivo
        try (Scanner fileScanner = new Scanner(new File(filePath))) {
            codeList = new LinkedList(); // Reinicia a lista para carregar um novo arquivo
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (!line.isEmpty()) {
                    // Divide a linha em número da linha e instrução
                    String[] parts = line.split(" ", 2);
                    if (parts.length < 2) {
                        System.out.println("Erro: formato de linha inválido. Linha ignorada: " + line);
                        continue;
                    }
                    try {
                        int lineNumber = Integer.parseInt(parts[0]);
                        String instruction = parts[1];
                        codeList.insertOrUpdate(lineNumber, instruction);
                    } catch (NumberFormatException e) {
                        System.out.println("Erro: número da linha inválido. Linha ignorada: " + line);
                    }
                }
            }
            currentFilePath = filePath;
            unsavedChanges = false;
            System.out.println("Arquivo '" + filePath + "' carregado com sucesso.");
        } catch (FileNotFoundException e) {
            System.out.println("Erro: arquivo não encontrado - '" + filePath + "'.");
        } catch (Exception e) {
            System.out.println("Erro ao carregar o arquivo '" + filePath + "': " + e.getMessage());
        }
    }

    // Lista o conteúdo da lista encadeada de 20 em 20 linhas
    public void list() {
        if (codeList.isEmpty()) {
            System.out.println("A memória está vazia.");
            return;
        }

        Node current = codeList.getHead(); // Obtém o nó inicial da lista
        int lineCounter = 0;

        while (current != null) {
            System.out.println(current);
            current = current.getNext();
            lineCounter++;

            // Pausa a cada 20 linhas
            if (lineCounter % 20 == 0 && current != null) {
                System.out.println("Pressione Enter para continuar...");
                new java.util.Scanner(System.in).nextLine(); // Input antes de prosseguir
            }
        }
    }

    // Insere ou atualiza uma linha na lista encadeada
    public void insertLine(int lineNumber, String instruction) {
        if (lineNumber < 0) {
            System.out.println("Erro: o número da linha não deve ser negativo.");
            return;
        }

        String result = codeList.insertOrUpdate(lineNumber, instruction);
        unsavedChanges = true;
        System.out.println("Linha " + result + ":");
        System.out.println(lineNumber + ": " + instruction);
    }

    // Remove uma linha específica
    public void deleteLine(int lineNumber) {
        if (lineNumber < 0) {
            System.out.println("Erro: o número da linha não deve ser negativo.");
            return;
        }

        codeList.remove(lineNumber);
        unsavedChanges = true;
    }

    // Remove um intervalo de linhas
    public void deleteRange(int startLine, int endLine) {
        if (startLine < 0 || endLine < 0 || startLine > endLine) {
            System.out.println("Erro: intervalo de linhas inválido.");
            return;
        }

        codeList.removeRange(startLine, endLine);
        unsavedChanges = true;
    }

    // Salva o conteúdo da lista encadeada em um arquivo
    public void save(String filePath) {
        File file = new File(filePath);

        // Se o arquivo existir, pergunta ao usuário se deseja sobrescrever
        if (file.exists()) {
            System.out.println("Arquivo '" + filePath + "' já existe. Deseja sobrescrevê-lo? (S/N)");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine().trim().toUpperCase();
            if (!response.equals("S")) {
                System.out.println("Operação de salvamento cancelada.");
                return;
            }
        }

        // Salva o conteúdo da lista no arquivo especificado
        try (PrintWriter writer = new PrintWriter(file)) {
            Node current = codeList.getHead();
            while (current != null) {
                writer.println(current.getLineNumber() + " " + current.getInstruction());
                current = current.getNext();
            }
            unsavedChanges = false;
            currentFilePath = filePath;  // Atualiza o caminho atual
            System.out.println("Arquivo '" + filePath + "' salvo com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo '" + filePath + "': " + e.getMessage());
        }
    }


    // Encerrar o REPL
    public void exit() {
        // Confirma se usuário deseja salvar alterações antes de sair
        if (unsavedChanges) {
            System.out.println("Existem alterações não salvas. Deseja salvar antes de sair? (S/N)");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine().trim().toUpperCase();
            if (response.equals("S")) {
                if (currentFilePath != null) {
                    save(currentFilePath);
                } else {
                    System.out.println("Digite o caminho do arquivo para salvar:");
                    String filePath = scanner.nextLine().trim();
                    save(filePath);
                }
            }
        }
        System.out.println("Programa encerrado.");
    }



    // =-=-=-=-=-=-=-=-=-=-=-=-=-= MÉTODOS ATRELADOS COM MÉTODO RUN =-=-=-=-=-=-=-=-=-=-=-=-=-=

    // Executa o código Assembly armazenado na lista encadeada
    public void run() {
        // Valida se memória está vazia
        if (codeList.isEmpty()) {
            System.out.println("Erro: nenhuma instrução carregada na memória.");
            return;
        }

        // Reinicializa todos os registradores para 0
        for (int i = 0; i < registradores.length; i++) {
            registradores[i] = 0;
            registradoresDeclarados[i] = false;
        }

        // Inicializa posição na primeira linha
        Node current = codeList.getHead();

        // Loop de execução
        while (current != null) {
            // Divide o conteúdo da linha atual em partes
            String instruction = current.getInstruction();
            String[] parts = instruction.split(" ");

            // Identifica comando na primeira parte da linha atual
            switch (parts[0].toLowerCase()) {
                case "mov":
                    executeMov(parts);
                    break;
                case "jnz":
                    current = executeJnz(parts, current);
                    continue; // Evita de ir para a linha seguinte, pois `jnz` modifica a posição atual
                case "out":
                    executeOut(parts);
                    break;
                case "inc":
                    executeInc(parts);
                    break;
                case "dec":
                    executeDec(parts);
                    break;
                case "add":
                    executeAdd(parts);
                    break;
                case "sub":
                    executeSub(parts);
                    break;
                case "mul":
                    executeMul(parts);
                    break;
                case "div":
                    executeDiv(parts);
                    break;
                default:
                    System.out.println("Erro: instrução inválida - " + instruction);
                    return;
            }

            // Vai para a próxima linha
            current = current.getNext();
        }
    }


    // =-=-= INSTRUÇÕES =-=-=

    // Instrução MOV
    private void executeMov(String[] parts) {
        if (parts.length != 3) {
            System.out.println("Erro: sintaxe incorreta em 'mov'.");
            return;
        }

        // Obtém e valida primeiro registrador
        int regIndexX = getRegisterIndex(parts[1]);
        if (regIndexX == -1) {
            System.out.println("Erro: registrador inválido em 'mov'.");
            return;
        }

        // Verifica se o segundo argumento é um número ou outro registrador
        int value; // Variável auxiliar
        int regIndexY = getRegisterIndex(parts[2]);
        if (regIndexY != -1) { // Caso 'y' seja um registrador, copia o valor do registrador y para x
            // Valida se o registrador foi declarado
            if (!registradoresDeclarados[regIndexY]) {
                System.out.println("Erro: registrador '" + parts[2] + "' não declarado.");
                return;
            }
            value = registradores[regIndexY];
        } else { // Caso 'y' seja um número inteiro, armazena o valor diretamente em x
            try {
                value = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                System.out.println("Erro: valor inválido em 'mov'.");
                return;
            }
        }

        // Realiza atualização
        registradores[regIndexX] = value;
        registradoresDeclarados[regIndexX] = true;
    }

    // Instrução JNZ
    private Node executeJnz(String[] parts, Node current) {
        if (parts.length != 3) {
            System.out.println("Erro: sintaxe incorreta em 'jnz'.");
            return current.getNext();
        }

        // Obtém e valida o primeiro registrador
        int regIndex = getRegisterIndex(parts[1]);
        if (regIndex == -1) {
            System.out.println("Erro: registrador inválido em 'jnz'.");
            return current.getNext();
        }
        // Valida se o registrador foi declarado
        if (!registradoresDeclarados[regIndex]) {
            System.out.println("Erro: registrador '" + parts[1] + "' não declarado.");
            return null;
        }

        // Verifica se o valor armazenado no registrador é diferente de zero
        int value = registradores[regIndex];
        if (value != 0) {
            // Verifica o segundo argumento (pode ser um registrador ou um número)
            int targetLineNumber;
            int regIndexY = getRegisterIndex(parts[2]);

            if (regIndexY != -1) {
                // Valida se o registrador foi declarado
                if (!registradoresDeclarados[regIndexY]) {
                    System.out.println("Erro: registrador '" + parts[2] + "' não declarado.");
                    return null;
                }
                // Se for um registrador, usa o valor nele armazenado como linha alvo
                targetLineNumber = registradores[regIndexY];
            } else {
                // Se for um número inteiro, tenta convertê-lo diretamente
                try {
                    targetLineNumber = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    System.out.println("Erro: número de linha inválido em 'jnz'.");
                    return current.getNext();
                }
            }

            // Tenta encontrar o nó correspondente ao número da linha alvo
            Node target = getNodeAtLineNumber(targetLineNumber);
            if (target != null) {
                return target;
            } else {
                System.out.println("Erro: linha alvo inválida em 'jnz'.");
            }
        }

        // Se o valor do registrador for zero, continua para a próxima linha
        return current.getNext();
    }


    // Instrução OUT
    private void executeOut(String[] parts) {
        if (parts.length != 2) {
            System.out.println("Erro: sintaxe incorreta em 'out'.");
            return;
        }

        // Obtém e valida registrador
        int regIndex = getRegisterIndex(parts[1]);
        if (regIndex == -1) {
            System.out.println("Erro: registrador inválido em 'out'.");
            return;
        }
        // Valida se o registrador foi declarado
        if (!registradoresDeclarados[regIndex]) {
            System.out.println("Erro: registrador '" + parts[1] + "' não declarado.");
            return;
        }
        
        // Imprime inteiro armazenado no registrador
        System.out.println(registradores[regIndex]);
    }

    // Instrução INC
    private void executeInc(String[] parts) {
        if (parts.length != 2) {
            System.out.println("Erro: sintaxe incorreta em 'inc'.");
            return;
        }

        // Obtém e valida registrador
        int regIndex = getRegisterIndex(parts[1]);
        if (regIndex == -1) {
            System.out.println("Erro: registrador inválido em 'inc'.");
            return;
        }
        // Valida se o registrador foi declarado
        if (!registradoresDeclarados[regIndex]) {
            System.out.println("Erro: registrador '" + parts[1] + "' não declarado.");
            return;
        }

        // Incrementa inteiro armazenado no registrador
        registradores[regIndex]++;
    }

    // Instrução DEC
    private void executeDec(String[] parts) {
        if (parts.length != 2) {
            System.out.println("Erro: sintaxe incorreta em 'dec'.");
            return;
        }

        // Obtém e valida registrador
        int regIndex = getRegisterIndex(parts[1]);
        if (regIndex == -1) {
            System.out.println("Erro: registrador inválido em 'dec'.");
            return;
        }
        // Valida se o registrador foi declarado
        if (!registradoresDeclarados[regIndex]) {
            System.out.println("Erro: registrador '" + parts[1] + "' não declarado.");
            return;
        }

        // Decrementa inteiro armazenado no registrador
        registradores[regIndex]--;
    }

    // Instrução ADD
    private void executeAdd(String[] parts) {
        if (parts.length != 3) {
            System.out.println("Erro: sintaxe incorreta em 'add'.");
            return;
        }

        // Obtém e valida o primeiro registrador
        int regIndexX = getRegisterIndex(parts[1]);
        if (regIndexX == -1) {
            System.out.println("Erro: registrador inválido em 'add'.");
            return;
        }
        // Valida se o registrador foi declarado
        if (!registradoresDeclarados[regIndexX]) {
            System.out.println("Erro: registrador '" + parts[1] + "' não declarado.");
            return;
        }

        // Verifica se o segundo argumento é um número ou outro registrador
        int value; // Variável auxiliar
        int regIndexY = getRegisterIndex(parts[2]);
        if (regIndexY != -1) { 
            // Valida se o registrador foi declarado
            if (!registradoresDeclarados[regIndexY]) {
                System.out.println("Erro: registrador '" + parts[2] + "' não declarado.");
                return;
            }
            // Caso 'y' seja um registrador, obtém o valor dele
            value = registradores[regIndexY];
        } else {
            // Verifica se 'y' é um número inteiro
            try {
                value = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                System.out.println("Erro: valor inválido em 'add'.");
                return;
            }
        }

        // Realiza a soma no primeiro registrador
        registradores[regIndexX] += value;
    }

    // Instrução SUB
    private void executeSub(String[] parts) {
        if (parts.length != 3) {
            System.out.println("Erro: sintaxe incorreta em 'sub'.");
            return;
        }

        // Obtém e valida o primeiro registrador
        int regIndexX = getRegisterIndex(parts[1]);
        if (regIndexX == -1) {
            System.out.println("Erro: registrador inválido em 'sub'.");
            return;
        }
        // Valida se o registrador foi declarado
        if (!registradoresDeclarados[regIndexX]) {
            System.out.println("Erro: registrador '" + parts[1] + "' não declarado.");
            return;
        }

        // Verifica se o segundo argumento é um número ou outro registrador
        int value; // Variável auxiliar
        int regIndexY = getRegisterIndex(parts[2]);
        if (regIndexY != -1) { 
            // Valida se o registrador foi declarado
            if (!registradoresDeclarados[regIndexY]) {
                System.out.println("Erro: registrador '" + parts[2] + "' não declarado.");
                return;
            }
            // Caso 'y' seja um registrador, obtém o valor dele
            value = registradores[regIndexY];
        } else {
            // Verifica se 'y' é um número inteiro
            try {
                value = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                System.out.println("Erro: valor inválido em 'sub'.");
                return;
            }
        }

        // Realiza a subtração no primeiro registrador
        registradores[regIndexX] -= value;
    }

    // Instrução MUL
    private void executeMul(String[] parts) {
        if (parts.length != 3) {
            System.out.println("Erro: sintaxe incorreta em 'mul'.");
            return;
        }

        // Obtém e valida o primeiro registrador
        int regIndexX = getRegisterIndex(parts[1]);
        if (regIndexX == -1) {
            System.out.println("Erro: registrador inválido em 'mul'.");
            return;
        }
        // Valida se o registrador foi declarado
        if (!registradoresDeclarados[regIndexX]) {
            System.out.println("Erro: registrador '" + parts[1] + "' não declarado.");
            return;
        }

        // Verifica se o segundo argumento é um número ou outro registrador
        int value; // Variável auxiliar
        int regIndexY = getRegisterIndex(parts[2]);
        if (regIndexY != -1) { 
            // Valida se o registrador foi declarado
            if (!registradoresDeclarados[regIndexY]) {
                System.out.println("Erro: registrador '" + parts[2] + "' não declarado.");
                return;
            }
            // Caso 'y' seja um registrador, obtém o valor dele
            value = registradores[regIndexY];
        } else {
            // Verifica se 'y' é um número inteiro
            try {
                value = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                System.out.println("Erro: valor inválido em 'mul'.");
                return;
            }
        }

        // Realiza a multiplicação no primeiro registrador
        registradores[regIndexX] *= value;
    }
    
    // Instrução DIV
    private void executeDiv(String[] parts) {
        if (parts.length != 3) {
            System.out.println("Erro: sintaxe incorreta em 'div'.");
            return;
        }

        // Obtém e valida o primeiro registrador
        int regIndexX = getRegisterIndex(parts[1]);
        if (regIndexX == -1) {
            System.out.println("Erro: registrador inválido em 'div'.");
            return;
        }
        // Valida se o registrador foi declarado
        if (!registradoresDeclarados[regIndexX]) {
            System.out.println("Erro: registrador '" + parts[1] + "' não declarado.");
            return;
        }

        // Verifica se o segundo argumento é um número ou outro registrador
        int value; // Variável auxiliar
        int regIndexY = getRegisterIndex(parts[2]);
        if (regIndexY != -1) {
            // Valida se o registrador foi declarado
            if (!registradoresDeclarados[regIndexY]) {
                System.out.println("Erro: registrador '" + parts[2] + "' não declarado.");
                return;
            } 
            // Caso 'y' seja um registrador, obtém o valor dele
            value = registradores[regIndexY];
        } else {
            // Verifica se 'y' é um número inteiro
            try {
                value = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                System.out.println("Erro: valor inválido em 'div'.");
                return;
            }
        }

        // Valida divisão por zero
        if (value == 0) {
            System.out.println("Erro: divisão por zero não permitida.");
            return;
        }

        // Realiza a divisão no primeiro registrador
        registradores[regIndexX] /= value;
    }


    // =-=-= GETTERS =-=-=

    // Obtém índice de um registrador
    private int getRegisterIndex(String reg) {
        if (reg.length() == 1) {
            char ch = Character.toLowerCase(reg.charAt(0));  // Trata como case insensitive
            if (ch >= 'a' && ch <= 'z') {
                return ch - 'a';  // 'a' é mapeado para 0, 'b' para 1, ..., 'z' para 25
            }
        }
        return -1;  // Retorna -1 se o registrador for inválido
    }

    // Obtém o nó com base no número da linha
    private Node getNodeAtLineNumber(int lineNumber) {
        Node current = codeList.getHead();
        while (current != null) {
            if (current.getLineNumber() == lineNumber) {
                return current;
            }
            current = current.getNext();
        }
        return null;  // Retorna null se o número da linha não for encontrado
    }

}


