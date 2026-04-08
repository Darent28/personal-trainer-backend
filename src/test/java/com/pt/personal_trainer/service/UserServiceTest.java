package com.pt.personal_trainer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pt.personal_trainer.domain.dto.UserResponseDto;
import com.pt.personal_trainer.domain.input.UserInput;
import com.pt.personal_trainer.entity.User;
import com.pt.personal_trainer.exception.CustomExceptions.NotFoundException;
import com.pt.personal_trainer.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void postUser_shouldCreateAndReturnUser() {
        UserInput input = new UserInput("john", "john@test.com", "password123", 1);
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        UserResponseDto result = userService.postUser(input);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.username()).isEqualTo("john");
        assertThat(result.email()).isEqualTo("john@test.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUsers_shouldReturnAllUsers() {
        User user1 = new User("john", "john@test.com", "enc", 1);
        user1.setId(1L);
        User user2 = new User("jane", "jane@test.com", "enc", 2);
        user2.setId(2L);
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserResponseDto> result = userService.getUsers();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).username()).isEqualTo("john");
        assertThat(result.get(1).username()).isEqualTo("jane");
    }

    @Test
    void getUserById_shouldReturnUser_whenExists() {
        User user = new User("john", "john@test.com", "enc", 1);
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDto result = userService.getUserById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.username()).isEqualTo("john");
    }

    @Test
    void getUserById_shouldThrow_whenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("99");
    }

    @Test
    void updateUsername_shouldUpdateAndReturn() {
        User user = new User("john", "john@test.com", "enc", 1);
        user.setId(1L);
        User updated = new User("newname", "john@test.com", "enc", 1);
        updated.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user), Optional.of(updated));

        UserInput input = new UserInput("newname", null, null, null);
        UserResponseDto result = userService.updateUsername(1L, input);

        assertThat(result.username()).isEqualTo("newname");
        verify(userRepository).updateUsernameById(1L, "newname");
    }

    @Test
    void deleteUser_shouldSoftDelete() {
        User user = new User("john", "john@test.com", "enc", 1);
        user.setId(1L);
        user.setStatus(1);
        User deleted = new User("john", "john@test.com", "enc", 1);
        deleted.setId(1L);
        deleted.setStatus(0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user), Optional.of(deleted));

        UserResponseDto result = userService.deleteUser(1L);

        verify(userRepository).updateStatusById(1L);
    }

    @Test
    void deleteUser_shouldThrow_whenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(99L))
            .isInstanceOf(NotFoundException.class);
    }
}
