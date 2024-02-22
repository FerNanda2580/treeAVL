package tree;

import java.util.ArrayList;
import java.util.List;

import estrut.Tree;

public class BinarySearchTree implements Tree {

    private class AVLNode {
        int data;
        int height;
        AVLNode left;
        AVLNode right;

        AVLNode(int data) {
            this.data = data;
            this.height = 1;
            this.left = null;
            this.right = null;
        }
    }

    private AVLNode root;

    @Override
    public boolean buscaElemento(int valor) {
        return buscaElemento(valor, root);
    }

    private boolean buscaElemento(int valor, AVLNode node) {
        if (node == null) {
            return false;
        }

        if (valor < node.data) {
            return buscaElemento(valor, node.left);
        } else if (valor > node.data) {
            return buscaElemento(valor, node.right);
        } else {
            return true;
        }
    }

    @Override
    public int minimo() {
        if (root == null) {
            return Integer.MIN_VALUE;
        }

        AVLNode currentNode = root;
        while (currentNode.left != null) {
            currentNode = currentNode.left;
        }

        return currentNode.data;
    }

    @Override
    public int maximo() {
        if (root == null) {
            return Integer.MAX_VALUE;
        }

        AVLNode currentNode = root;
        while (currentNode.right != null) {
            currentNode = currentNode.right;
        }

        return currentNode.data;
    }

    @Override
    public void insereElemento(int valor) {
        root = insereElemento(root, valor);
    }

    private AVLNode insereElemento(AVLNode node, int valor) {
        if (node == null) {
            return new AVLNode(valor);
        }

        if (valor < node.data) {
            node.left = insereElemento(node.left, valor);
        } else if (valor > node.data) {
            node.right = insereElemento(node.right, valor);
        } else {
            return node;  // Duplicatas não são permitidas em uma árvore AVL
        }

        // Atualiza a altura do nó atual
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        // Verifica o fator de balanceamento e realiza rotações, se necessário
        int balance = getBalance(node);

        // Rotações para equilíbrio
        if (balance > 1 && valor < node.left.data) {
            return rotateRight(node);
        }
        if (balance < -1 && valor > node.right.data) {
            return rotateLeft(node);
        }
        if (balance > 1 && valor > node.left.data) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && valor < node.right.data) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    @Override
    public void remove(int valor) {
        root = remove(root, valor);
    }

    private AVLNode remove(AVLNode node, int valor) {
        if (node == null) {
            return null;
        }
    
        if (valor < node.data) {
            node.left = remove(node.left, valor);
        } else if (valor > node.data) {
            node.right = remove(node.right, valor);
        } else {
            if ((node.left == null) || (node.right == null)) {
                AVLNode temp = (node.left == null) ? node.right : node.left;
    
                if (temp == null) {
                    temp = node;
                    node = null;
                } else {
                    node = temp;
                }
            } else {
                AVLNode temp = minValueNode(node.right);
                node.data = temp.data;
                node.right = remove(node.right, temp.data);
            }
        }
    
        // Se o nó foi removido, retornamos null
        if (node == null) {
            return null;
        }
    
        // Atualiza a altura do nó atual
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
    
        // Verifica o fator de balanceamento e realiza rotações, se necessário
        int balance = getBalance(node);
    
        // Rotações para equilíbrio
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }
        if (balance < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
    
        return node;
    }

    private AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    @Override
    public int[] preOrdem() {
        List<Integer> result = new ArrayList<>();
        preOrdem(root, result);
        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    private void preOrdem(AVLNode node, List<Integer> result) {
        if (node != null) {
            result.add(node.data);
            preOrdem(node.left, result);
            preOrdem(node.right, result);
        }
    }

    @Override
    public int[] emOrdem() {
        List<Integer> result = new ArrayList<>();
        emOrdem(root, result);
        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    private void emOrdem(AVLNode node, List<Integer> result) {
        if (node != null) {
            emOrdem(node.left, result);
            result.add(node.data);
            emOrdem(node.right, result);
        }
    }

    @Override
    public int[] posOrdem() {
        List<Integer> result = new ArrayList<>();
        posOrdem(root, result);
        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    private void posOrdem(AVLNode node, List<Integer> result) {
        if (node != null) {
            posOrdem(node.left, result);
            posOrdem(node.right, result);
            result.add(node.data);
        }
    }

    private int getHeight(AVLNode node) {
        return (node == null) ? 0 : node.height;
    }

    private int getBalance(AVLNode node) {
        return (node == null) ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }

    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;

        return y;
    }
}