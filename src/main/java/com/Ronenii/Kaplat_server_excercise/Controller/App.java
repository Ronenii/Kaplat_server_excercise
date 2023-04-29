package com.Ronenii.Kaplat_server_excercise.Controller;

import com.Ronenii.Kaplat_server_excercise.Model.Result;
import com.Ronenii.Kaplat_server_excercise.Model.TODO;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class App {

    private List<TODO> todos = new ArrayList<TODO>();
    Gson gson;
    public App(){
        gson = new Gson();
    }

    // 1st task
    @GetMapping({"/todo/health"})
    @ResponseStatus(HttpStatus.OK)
    public String health()
    {
        return "OK";
    }

    // 2nd task
    @PostMapping(
            value = {"/todo"},
            consumes = {"application/json"}
    )
    public ResponseEntity<String> createTODO(@RequestBody TODO todo)
    {
        Result<Integer> result = new Result<Integer>();
        String responseJson;
        HttpStatus status = null;
        if(TodoExists(todo))
        {
            result.setErrorMessage("Error: TODO with the title " + todo.getTitle() + " already exists in the system");
            status = HttpStatus.CONFLICT;
        }
        else if(!TodoHasCorrectTime(todo))
        {
            result.setErrorMessage("Error: Can’t create new TODO that its due date is in the past");
            status = HttpStatus.CONFLICT;
        }
        else
        {
            // add the to-do to the to-do array.
            todo.giveId();
            status = HttpStatus.OK;
            result.setResult(todo.getId());
            todos.add(todo);
        }

        // send the required response
        responseJson = gson.toJson(result);
        return ResponseEntity.status(status).body(responseJson);
    }

    // check if the do-do already exists in the array
    public boolean TodoExists(TODO todo)
    {
        for(TODO t: todos)
        {
            if(t.getTitle().equals(todo.getTitle()))
                return true;
        }
        return false;
    }

    // check if the to-do has a valid dueDate
    public boolean TodoHasCorrectTime(TODO todo)
    {
        return java.lang.System.currentTimeMillis() >= todo.getDueDate();
    }
}

