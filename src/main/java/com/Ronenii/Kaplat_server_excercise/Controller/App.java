package com.Ronenii.Kaplat_server_excercise.Controller;

import com.Ronenii.Kaplat_server_excercise.Model.Result;
import com.Ronenii.Kaplat_server_excercise.Model.TODO;
import com.Ronenii.Kaplat_server_excercise.Model.eStatus;
import com.Ronenii.Kaplat_server_excercise.Model.eSortBy;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
public class App {

    private List<TODO> todos = new ArrayList<TODO>();
    Gson gson;
    private final int INVALID = -1;

    public App() {
        gson = new Gson();
    }

    /**
     * 1st task
     **/
    @GetMapping({"/todo/health"})
    @ResponseStatus(HttpStatus.OK)
    public String health() {
        return "OK";
    }

    /**
     * 2nd task
     **/
    @PostMapping(
            value = {"/todo"},
            consumes = {"application/json"}
    )
    public ResponseEntity<String> createTODO(@RequestBody TODO todo) {
        Result<Integer> result = new Result<Integer>();
        String responseJson;
        HttpStatus responseStatus = null;
        if (TodoExists(todo)) {
            result.setErrorMessage("Error: TODO with the title " + todo.getTitle() + " already exists in the system");
            System.out.println(result.getErrorMessage());
            responseStatus = HttpStatus.CONFLICT;
        } else if (!TodoHasCorrectTime(todo)) {
            result.setErrorMessage("Error: Can’t create new TODO that its due date is in the past");
            System.out.println(result.getErrorMessage());
            responseStatus = HttpStatus.CONFLICT;
        } else {
            // add the to-do to the to-do array.
            todo.giveId();
            responseStatus = HttpStatus.OK;
            result.setResult(todo.getId());
            System.out.println("Success in task 2: " + todo);
            todos.add(todo);
        }

        // send the required response
        responseJson = gson.toJson(result);
        return ResponseEntity.status(responseStatus).body(responseJson);
    }

    // check if the do-do already exists in the array
    public boolean TodoExists(TODO todo) {
        for (TODO t : todos) {
            if (t.getTitle().equals(todo.getTitle()))
                return true;
        }
        return false;
    }

    // check if the to-do has a valid dueDate
    public boolean TodoHasCorrectTime(TODO todo) {
        return java.lang.System.currentTimeMillis() >= todo.getDueDate();
    }

    /**
     * 3rd Task
     **/

    @GetMapping
    public ResponseEntity<String> getTodosCount(String status) {
        Result<Integer> result = new Result<Integer>();
        String responseJson;
        HttpStatus responseStatus = null;
        int instances;
        try {
            instances = countTodoInstances(status);
            responseStatus = HttpStatus.OK;
            result.setResult(instances);
            System.out.println("Success in task 3 (Filter=" + status + ", instances=" + instances + ")");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            responseStatus = HttpStatus.BAD_REQUEST;
        } finally {
            responseJson = gson.toJson(result);
        }
        return ResponseEntity.status(responseStatus).body(responseJson);
    }

    // Counts the number of instances of to-dos with the given filter in the to-do list.
    // If the filter given is wrong will return
    public int countTodoInstances(String filter) {
        if (!isValidStatusFilter(filter)) {
            throw new IllegalArgumentException(invalidParameterMessage(filter));
        } else if (filter.equals("ALL")) {
            return todos.size();
        } else {
            eStatus status = eStatus.valueOf(filter);
            int instances = 0;
            for (TODO t : todos) {
                if (t.getStatus() == status) {
                    instances++;
                }
            }
            return instances;
        }

    }

    // Checks if the given filter is a valid status filter
    public boolean isValidStatusFilter(String filter) {
        Set<String> filters = new HashSet<String>();
        filters.add("ALL");
        filters.add("PENDING");
        filters.add("DONE");
        filters.add("LATE");

        return filters.contains(filter);
    }

    /**
     * 4th task
     **/
    @GetMapping
    public ResponseEntity<String> getTodosCount(String status, String sortBy) {
        ArrayList<TODO> resultArray = new ArrayList<TODO>();
        Result<String> result = new Result<String>();
        String responseJson;
        HttpStatus responseStatus = null;
        try {
            sortTodos(resultArray,status,sortBy);
            responseStatus = HttpStatus.OK;
            result.setResult(gson.toJson(resultArray));
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
            responseStatus = HttpStatus.BAD_REQUEST;
        }
        finally {
            responseJson = gson.toJson(result);
        }

        return ResponseEntity.status(responseStatus).body(responseJson);
    }

    public boolean isValidSortingFilter(String filter) {
        Set<String> filters = new HashSet<String>();
        filters.add("");
        filters.add("ID");
        filters.add("DUE_DATE");
        filters.add("TITLE");

        return filters.contains(filter);
    }

    public void sortTodos(ArrayList<TODO> resultArray, String status, String sortBy) {
        StringBuilder errorMessage = new StringBuilder();
        if (!isValidSortingFilter(sortBy)) {
            errorMessage.append(invalidParameterMessage(sortBy));
        }
        if (!isValidStatusFilter(status)) {
            errorMessage.append(invalidParameterMessage(status));
        }
        if (!errorMessage.isEmpty()) {
            throw new IllegalArgumentException(errorMessage.toString());
        }


        if (status.equals("ALL")) {
            resultArray.addAll(todos);

        } else {
            addToArrayByStatus(resultArray, status);
        }
        SortTodosWithSortby(resultArray, sortBy);
    }

    public void addToArrayByStatus(ArrayList<TODO> resultArray, String status) {
        for (TODO t : todos) {
            if (eStatus.valueOf(status) == t.getStatus()) {
                resultArray.add(t);
            }
        }
    }

    public void SortTodosWithSortby(ArrayList<TODO> resultArray, String sortBy) {
        if (sortBy.equals("")) {
            sortBy = "ID";
        }
        eSortBy newSortBy = eSortBy.valueOf(sortBy);
        switch (newSortBy) {
            case ID:
                resultArray.sort(Comparator.comparing(TODO::getId));
                break;
            case DUE_DATE:
                resultArray.sort(Comparator.comparing(TODO::getDueDate));
                break;
            case TITLE:
                resultArray.sort(Comparator.comparing(TODO::getTitle));
                break;
        }
    }

    public String invalidParameterMessage(String param) {
        return "Invalid argument: " + param + "\n";
    }
}

