/*

[ INTEGRANTES DO GRUPO ]
 
Bruna Gonçalves Corte David     | RA: 10425696
Caio Guilherme dos Santos Silva | RA: 10420097
Daniela Pereira da Silva        | RA: 10410906

*/

public class LinkedList {

    // =-=-= ATRIBUTOS =-=-=
    private Node head;


    // =-=-= CONSTRUTOR =-=-=
    public LinkedList() {
        head = null;
    }
    
    
    // =-=-=-= MÉTODOS =-=-=-=
    
    // GET nó inicial da lista
    public Node getHead() {
        return head;
    }

    // Verifica se a lista está vazia
    public boolean isEmpty() {
        return head == null;
    }

    // Insere ou atualiza uma linha de código Assembly
    public String insertOrUpdate(int lineNumber, String instruction) {
        // Inicializa a posição como a primeira linha da lista
        Node current = head;
        Node previous = null;

        // Procura pela posição correta na lista ou se já existe a linha
        while (current != null && current.getLineNumber() < lineNumber) {
            previous = current;
            current = current.getNext();
        }

        // Atualiza a linha se ela já existir e finaliza operação
        if (current != null && current.getLineNumber() == lineNumber) {
            current.setInstruction(instruction);
            return "atualizada";
        }

        // Insere uma nova linha
        Node newNode = new Node(lineNumber, instruction);
        if (previous == null) { // Se não houver linha anterior, então é a primeira
            newNode.setNext(head);
            head = newNode;
        } else {                // Insere entre "previous" e "current"
            previous.setNext(newNode);
            newNode.setNext(current);
        }
        return "inserida";
    }

    // Remove uma linha específica da lista
    public void remove(int lineNumber) {
        // Inicializa a posição como a primeira linha da lista
        Node current = head;
        Node previous = null;

        // Procura a linha a ser removida
        while (current != null && current.getLineNumber() != lineNumber) {
            previous = current;
            current = current.getNext();
        }

        // A linha não existe se não foi encontrada
        if (current == null) {
            System.out.println("Erro: linha " + lineNumber + " inexistente.");
            return;
        }

        // Remove a linha encontrada
        if (previous == null) { // Se não houver linha anterior, então é a primeira
            head = current.getNext();
        } else {                // Reestrutura encadeamento para remover o nó
            previous.setNext(current.getNext());
        }
        System.out.println("Linha removida:");
        System.out.println(lineNumber + ": " + current.getInstruction());
    }

    // Remove um intervalo de linhas
    public void removeRange(int startLine, int endLine) {
        // Valida o intervalo
        if (startLine > endLine) {
            System.out.println("Erro: intervalo inválido de linhas.");
            return;
        }

        // Inicializa a posição como a primeira linha da lista
        Node current = head;
        Node previous = null;

        // Percorre a lista até o início do intervalo a ser removido
        while (current != null && current.getLineNumber() < startLine) {
            previous = current;
            current = current.getNext();
        }

        // Marca o início do intervalo de remoção
        Node startNode = current;

        // Cria uma StringBuilder para armazenar as linhas e instruções removidas
        StringBuilder removedLines = new StringBuilder();
        while (current != null && current.getLineNumber() <= endLine) {
            removedLines.append(current.getLineNumber()).append(": ").append(current.getInstruction()).append("\n");
            current = current.getNext();
        }

        // Valida se o intervalo existe
        if (startNode == null || removedLines.length() == 0) {
            System.out.println("Erro: intervalo de linhas inexistente.");
            return;
        }

        // Remoção do intervalo
        if (previous == null) { // Se não houver linha anterior, então o intervalo é desde a primeira linha
            head = current;
        } else {                // Remove o intervalo
            previous.setNext(current);
        }

        // Imprime as linhas removidas
        System.out.println("Linhas removidas:");
        System.out.print(removedLines.toString());
    }
}
