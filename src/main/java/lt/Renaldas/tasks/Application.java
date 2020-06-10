package lt.Renaldas.tasks;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lt.Renaldas.tasks.entities.Task;
import lt.Renaldas.tasks.entities.TaskStatus;
import lt.Renaldas.tasks.entities.Trainee;
import lt.Renaldas.tasks.repositories.TaskRepository;

@SpringBootApplication 
@EnableAutoConfiguration
@ComponentScan(basePackages={"lt.Renaldas.tasks"})
@EnableTransactionManagement
public class Application {
    
    
	@Bean
	public CommandLineRunner projectTestData(TaskRepository taskService) {
		return (args) -> {
		    taskService.save(new Task("LINDA Task", "Task description, Task description, Task description, Task descriptionTask description",Trainee.LINDA ,TaskStatus.NOT_COMPLETED));
		    taskService.save(new Task("LINDA Task", "Dummy Task description ",Trainee.LINDA , TaskStatus.NOT_COMPLETED));
		    taskService.save(new Task("LINDA Dummy Task", "a Task description",Trainee.LINDA , TaskStatus.NOT_COMPLETED));
		    taskService.save(new Task("KAROLIS Task", "Task description",Trainee.KAROLIS,TaskStatus.NOT_COMPLETED));
		    taskService.save(new Task("KAROLIS Task", "Dummy Task description ",Trainee.KAROLIS , TaskStatus.NOT_COMPLETED));
		    taskService.save(new Task("KAROLIS Dummy Task", "a Task description",Trainee.KAROLIS , TaskStatus.NOT_COMPLETED));
		    taskService.save(new Task("Dummy Task", "Task description",Trainee.TOM ,TaskStatus.NOT_COMPLETED));
		    taskService.save(new Task("TOMAS Task", "Dummy Task description ",Trainee.TOM , TaskStatus.NOT_COMPLETED));
		    taskService.save(new Task("TOMAS Dummy Task", "a Task description",Trainee.TOM , TaskStatus.NOT_COMPLETED));
		    
		};
	}

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
}
