package lt.Renaldas.tasks.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lt.Renaldas.tasks.entities.Task;
import lt.Renaldas.tasks.entities.Trainee;
import lt.Renaldas.tasks.repositories.TaskRepository;

@Controller
public class TaskController {

    private final TaskRepository taskRepository;


    private List<Task> listByUser = new ArrayList<Task>();

    @Autowired
    public TaskController(TaskRepository taskRepository) {
	this.taskRepository = taskRepository;
    }

    @GetMapping("login")
    public String getLogin() {
	return "login";
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
    public String getEnter(Model model) {
	model.addAttribute("tasks", listByUser);
	return "redirect:/index";
    }

    @GetMapping("/index")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
    public String getEnter1(Model model) {

	Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
	Trainee loggedUser = detect(loggedInUser.getName());
	System.out.println("name: " + loggedInUser.getName());
	if (loggedUser != Trainee.NOT_ASSIGNED) {
	    listByUser = taskRepository.findAll().stream().filter(s -> s.getTrainee() == detect(loggedInUser.getName()))
		    .collect(Collectors.toList());
	} else {
	    listByUser = taskRepository.findAll();
	}

	model.addAttribute("tasks", listByUser);
	return "index";
    }

    @GetMapping("/signup")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
    public String showSignUpForm(Task task) {
	return "add-user";
    }

    @PostMapping("/adduser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
    public String addUser(@Valid Task task, BindingResult result, Model model) {
	if (result.hasErrors()) {
	    return "add-user";
	}
	System.out.println(task);
	taskRepository.save(task);

	model.addAttribute("tasks", listByUser);
	return "redirect:/index";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
	Task user = taskRepository.findById(id)
		.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

	model.addAttribute("task", user);
	return "update-user";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
    public String updateUser(@PathVariable("id") long id, @Valid Task task, BindingResult result, Model model) {
	if (result.hasErrors()) {
	    task.setId(id);
	    return "update-user";
	}
	System.out.println(task);
	taskRepository.save(task);
	model.addAttribute("tasks", listByUser);
	return "redirect:/index";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteUser(@PathVariable("id") long id, Model model) {
	Task user = taskRepository.findById(id)
		.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
	taskRepository.delete(user);
	model.addAttribute("tasks", listByUser);
	return "redirect:/index";
    }

    private Trainee detect(String string) {

	for (Trainee me : Trainee.values()) {
	    String check = me.name();
	    if (check.equalsIgnoreCase(string))
		return me;
	}
	return Trainee.NOT_ASSIGNED;
    }

}
