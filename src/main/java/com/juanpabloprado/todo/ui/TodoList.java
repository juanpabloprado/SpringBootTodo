package com.juanpabloprado.todo.ui;

import com.juanpabloprado.todo.Todo;
import com.juanpabloprado.todo.TodoRepository;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TodoList extends VerticalLayout implements TodoChangeListener {

  @Autowired TodoRepository repository;
  private List<Todo> todos;

  @PostConstruct
  void init() {
    setSpacing(true);

    update();
  }

  private void update() {
    setTodos(repository.findAll());
  }

  private void setTodos(List<Todo> todos) {
    this.todos = todos;
    removeAllComponents();

    todos.forEach(todo -> {
      addComponent(new TodoLayout(todo, this));
    });
  }

  public void save(Todo todo) {
    repository.save(todo);
    update();
  }

  @Override public void todoChanged(Todo todo) {
    save(todo);
  }

  public void deleteCompleted() {
    List<Todo> completedTodos = todos.stream().filter(Todo::isDone).collect(Collectors.toList());
    repository.deleteInBatch(completedTodos);
    update();
  }
}
