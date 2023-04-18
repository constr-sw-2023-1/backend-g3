package br.pucrs.csw.professors.web;

import br.pucrs.csw.professors.pojo.PasswordUpdate;
import br.pucrs.csw.professors.pojo.User;
import br.pucrs.csw.professors.service.UserService;
import br.pucrs.csw.professors.web.error.ErrorDetails;
import br.pucrs.csw.professors.web.input.UserToCreate;
import br.pucrs.csw.professors.web.input.UserUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(operationId = "createUser", responses = {
            @ApiResponse(responseCode = "201", description = "User Created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "409", description = "Username already exists",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "401", description = "Invalid Token",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDetails.class)))
    })
    public ResponseEntity<User> create(@RequestBody UserToCreate professorToCreate) {
        professorToCreate.isValid();
        User createdUser = userService.create(professorToCreate.toUser());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(operationId = "getAllUsers", responses = {
            @ApiResponse(responseCode = "200", description = "User Created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = User.class)))),
            @ApiResponse(responseCode = "401", description = "Invalid Token",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDetails.class)))
    })
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "deleteUser", description = "Delete an user by id", responses = {
            @ApiResponse(responseCode = "204", description = "User Deleted"),
            @ApiResponse(responseCode = "401", description = "Invalid Token",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "404", description = "User Not found with Id",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDetails.class)))
    })
    @Parameters({@Parameter(in = ParameterIn.PATH, name = "id", description = "userId",
            schema = @Schema(type = "string"))
    })
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(operationId = "getbyId", description = "Get User By Id", responses = {
            @ApiResponse(responseCode = "200", description = "Returned User",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "Invalid Token",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "404", description = "User Not found with Id",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDetails.class)))
    })
    @Parameters({@Parameter(in = ParameterIn.PATH, name = "id", description = "userId",
            schema = @Schema(type = "string"))
    })
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/{id}")
    @Operation(operationId = "updateByd", description = "Update user by its id", responses = {
            @ApiResponse(responseCode = "200", description = "Updated User",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "Invalid Token",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "404", description = "User Not found with Id",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDetails.class)))
    })
    @Parameters({@Parameter(in = ParameterIn.PATH, name = "id", description = "userId",
            schema = @Schema(type = "string"))
    })
    public ResponseEntity<User> updateUser(@RequestBody @Valid UserUpdate professorData, @PathVariable String id) {
        User updatedUser = userService.updateUserById(id, professorData);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{id}")
    @Operation(operationId = "updatePassword", description = "Update password of id", responses = {
            @ApiResponse(responseCode = "200", description = "Updated User",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "Invalid Token",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "404", description = "User Not found with Id",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDetails.class)))
    })
    @Parameters({@Parameter(in = ParameterIn.PATH, name = "id", description = "userId",
            schema = @Schema(type = "string"))
    })
    public ResponseEntity<User> updatePassword(@RequestBody PasswordUpdate passwordUpdate, @PathVariable String id) {
        User updatedUser = userService.updatePassword(id, passwordUpdate);
        return ResponseEntity.ok(updatedUser);
    }

}

/*

673daf1e982340daad109723741cc4569b50395ec70758283518b8671865484457205be87bec6bc480174735fcfc826f4a73316158e017bf74793a02f0020248f18c5f1a6bbd07dff0e33442404e463a780ea86cac956fe544ee16352535bbf5f909b04695bf50c63fa66ee814fd28c595343e2e421185828cf967a41c47827a68bfc8b7aa86fe43eaf77572c3c5ed2f
 */
