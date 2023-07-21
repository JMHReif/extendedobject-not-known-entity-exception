package com.jmhreif.extendedobjectnotknownentityexception;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
public class ExtendedobjectNotKnownEntityExceptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExtendedobjectNotKnownEntityExceptionApplication.class, args);
	}

}

@RestController
@RequestMapping("/persons")
class PersonController {
	private final ChildRepository repo;


	PersonController(ChildRepository repo) {
		this.repo = repo;
	}

	@GetMapping("/{name}")
	List<Child> getChildren(@PathVariable String name) {
		return repo.getChildren(name);
	}
}

interface ChildRepository extends Neo4jRepository<Child, Long> {
	@Query("MATCH (p:Person {name: $name})-[r:PARENT_OF]->(c:Child) RETURN c;")
	List<Child> getChildren(String name);
}

@Node
class Person {
	@Id
	@GeneratedValue
	private Long id;

	private String name;

	public Person(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

@Node
class Child extends Person {
	private Integer age;

	@ReadOnlyProperty
	private String readOnly;

	public Child(Long id, String name) {
		super(id, name);
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getReadOnly() {
		return readOnly;
	}
}