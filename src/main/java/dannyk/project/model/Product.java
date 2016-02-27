package dannyk.project.model;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;

public class Product implements Serializable {
    private Integer id;
    private String name;
    private Integer age;

    public Product() {
    }

    public Product(Integer id, String name, Integer age) {
      this.id = id;
      this.name = name;
      this.age = age;
    }

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Integer getAge() {
      return age;
    }

    public void setAge(Integer age) {
      this.age = age;
    }

    @Override
    public String toString() {
      return MessageFormat.format("Product'{'id={0}, name=''{1}'', age={2}'}'", id, name, age);
    }
  }
