/*

[ INTEGRANTES DO GRUPO ]
 
Bruna Gonçalves Corte David     | RA: 10425696
Caio Guilherme dos Santos Silva | RA: 10420097
Daniela Pereira da Silva        | RA: 10410906

*/

public class Node {
    
    // =-=-= ATRIBUTOS =-=-=
    private String instruction; // Armazena a INSTRUÇÃO da linha
    private int lineNumber;     // Armazena o NÚMERO da linha
    private Node next;          // Armazena o(a) PRÓXIMO nó/linha


    // =-=-= CONSTRUTORES =-=-=
    public Node(int lineNumber, String instruction) {
        this.lineNumber = lineNumber;
        this.instruction = instruction;
        this.next = null;
    }

    public Node(int lineNumber, String instruction, Node next) {
        this.lineNumber = lineNumber;
        this.instruction = instruction;
        this.next = next;
    }


    // =-=-= GETTERS =-=-=
    public String getInstruction() {
        return instruction;
    }
    
    public int getLineNumber() {
        return lineNumber;
    }
    
    public Node getNext() {
        return next;
    }
    
    
    // =-=-= SETTERS =-=-=
    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setNext(Node next) {
        this.next = next;
    }


    // =-=-= IMPRESSÃO =-=-=
    @Override
    public String toString() {
        return lineNumber + ": " + instruction;
    }
}
