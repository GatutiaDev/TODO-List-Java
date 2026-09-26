package com.japinha.todolist.exception;

public class TarefaNaoEncontradaException extends RuntimeException {

    public TarefaNaoEncontradaException(String texto) {
        super(texto);
    }

}
