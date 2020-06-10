package lt.Renaldas.tasks.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotBlank(message = "Name is a must")
    private String name;

    @NotBlank(message = "Description is a must")
    @Column(columnDefinition="TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Trainee trainee;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    public Task() {
    }

    public Task(String name, String description, Trainee trainee, TaskStatus taskStatus) {
	this.name = name;
	this.description = description;
	if (trainee == null) {
	    this.trainee = Trainee.NOT_ASSIGNED;
	}
	this.trainee = trainee;
	this.taskStatus = taskStatus;
    }

    public void setId(long id) {
	this.id = id;
    }

    public long getId() {
	return id;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public TaskStatus getTaskStatus() {
	return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
	this.taskStatus = taskStatus;
    }

    public Trainee getTrainee() {
	return trainee;
    }

    public void setTrainee(Trainee trainee) {
	this.trainee = trainee;
    }

    public String getName() {
	return name;
    }

    @Override
    public String toString() {
	return "Task [id=" + id + ", name=" + name + ", description=" + description + ", taskStatus=" + taskStatus
		+ "]";
    }
}
