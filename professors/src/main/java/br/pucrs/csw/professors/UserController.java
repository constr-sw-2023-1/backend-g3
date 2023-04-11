@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private KeycloakClient keycloakClient;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestParam String client_id,
                                                @RequestParam String client_secret,
                                                @RequestParam String username,
                                                @RequestParam String password,
                                                @RequestParam(defaultValue = "password") String grant_type) {
        try {
            AccessTokenResponse accessTokenResponse = keycloakClient.getAccessToken(client_id, client_secret, username, password, grant_type);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken_type(accessTokenResponse.getTokenType());
            loginResponse.setAccess_token(accessTokenResponse.getAccessToken());
            loginResponse.setExpires_in(accessTokenResponse.getExpiresIn());
            loginResponse.setRefresh_token(accessTokenResponse.getRefreshToken());
            loginResponse.setReferesh_expires_in(accessTokenResponse.getRefreshExpiresIn());
            return ResponseEntity.ok(loginResponse);
        } catch (KeycloakClientException e) {
            return ResponseEntity.status(e.getStatus()).build();
        }
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String accessToken = getTokenFromAuthorizationHeader(authorizationHeader);
            String userId = keycloakClient.createUser(accessToken, user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName());
            user.setId(userId);
            user.setEnabled(true);
            return ResponseEntity.created(URI.create("/users/" + userId)).body(user);
        } catch (KeycloakClientException e) {
            return ResponseEntity.status(e.getStatus()).build();
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(required = false) Boolean enabled) {
        try {
            String accessToken = getTokenFromAuthorizationHeader(authorizationHeader);
            List<User> users = keycloakClient.getUsers(accessToken, enabled);
            return ResponseEntity.ok(users);
        } catch (KeycloakClientException e) {
            return ResponseEntity.status(e.getStatus()).build();
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id) {
        try {
            String accessToken = getTokenFromAuthorizationHeader(authorizationHeader);
            User user = keycloakClient.getUserById(accessToken, id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(user);
            }
        } catch (KeycloakClientException e) {
            return ResponseEntity.status(e.getStatus()).build();
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Void> updateUser(@RequestBody User user, @RequestHeader("Authorization") String authorizationHeader, @PathVariable String id) {
        try {
            String accessToken = getTokenFromAuthorizationHeader(authorizationHeader);
            keycloakClient.updateUser(accessToken, id, user.getUsername(), user.getFirstName(), user.getLastName(), user.isEnabled());
            return ResponseEntity.ok().build();
        } catch (KeycloakClientException e) {
            return ResponseEntity.status(e.getStatus()).build();
        }
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<Void> updatePassword(@RequestBody PasswordUpdate passwordUpdate, @RequestHeader("Authorization") String authorizationHeader, @PathVariable String id) {
        try {
            String accessToken = getTokenFromAuthorizationHeader(authorizationHeader);
            keycloakClient.updatePassword(accessToken, id, passwordUpdate.getPassword());
            return ResponseEntity
        } catch (KeycloakClientException e) {
            return ResponseEntity.status(e.getStatus()).build();
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        // Verificar se o usuário com o ID especificado existe no sistema
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    
        // Enviar uma requisição DELETE para o Keycloak para desabilitar o usuário
        try {
            Keycloak.getInstance()
                    .realm(keycloakRealm)
                    .users()
                    .delete(user.getUsername());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete user", e);
        }
    
        // Retornar o código de status 200 se a requisição foi bem sucedida
        return ResponseEntity.ok().build();
    }
}