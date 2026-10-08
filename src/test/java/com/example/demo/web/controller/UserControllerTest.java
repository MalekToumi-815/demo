package com.example.demo.web.controller;

import com.example.demo.domain.Service.UserService;
import com.example.demo.domain.exception.ResourceNotFoundException;
import com.example.demo.domain.model.User;
import com.example.demo.web.dto.CreateUserRequestdto;
import com.example.demo.web.dto.UserResponsedto;
import com.example.demo.web.exception.GlobalExceptionHandler;
import com.example.demo.web.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @Test
    void getAllUsersReturnsUsers() throws Exception {
        User user = new User("Ada", "Lovelace", "ada@example.com");
        user.setId(1L);
        when(userService.getAllUsers()).thenReturn(List.of(user));
        when(userMapper.toDto(user)).thenReturn(new UserResponsedto(1L, "Ada Lovelace", "ada@example.com"));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fullname").value("Ada Lovelace"))
                .andExpect(jsonPath("$[0].email").value("ada@example.com"));
    }

    @Test
    void getUserByIdReturnsUser() throws Exception {
        User user = new User("Ada", "Lovelace", "ada@example.com");
        user.setId(1L);
        when(userService.getUserById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(new UserResponsedto(1L, "Ada Lovelace", "ada@example.com"));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullname").value("Ada Lovelace"))
                .andExpect(jsonPath("$.email").value("ada@example.com"));
    }

    @Test
    void getMissingUserReturnsProblemDetail() throws Exception {
        when(userService.getUserById(42L))
                .thenThrow(new ResourceNotFoundException("User not found with id: 42"));

        mockMvc.perform(get("/api/users/42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("User not found with id: 42"));
    }

    @Test
    void createUserReturnsCreatedUser() throws Exception {
        User user = new User("Ada", "Lovelace", "ada@example.com");
        User savedUser = new User("Ada", "Lovelace", "ada@example.com");
        savedUser.setId(1L);
        when(userMapper.toEntity(any(CreateUserRequestdto.class))).thenReturn(user);
        when(userService.createUser(user)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser))
                .thenReturn(new UserResponsedto(1L, "Ada Lovelace", "ada@example.com"));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Ada","surname":"Lovelace","email":"ada@example.com"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullname").value("Ada Lovelace"))
                .andExpect(jsonPath("$.email").value("ada@example.com"));
    }
}
