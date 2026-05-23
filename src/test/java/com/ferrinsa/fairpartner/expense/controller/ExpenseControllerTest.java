package com.ferrinsa.fairpartner.expense.controller;

import com.ferrinsa.fairpartner.exception.expense.expense.ExpenseNotFoundException;
import com.ferrinsa.fairpartner.expense.service.coordinator.ExpenseCoordinatorService;
import com.ferrinsa.fairpartner.expense.service.model.ExpenseWithSharesAndPayer;
import com.ferrinsa.fairpartner.expense.service.model.ExpensesWithBalances;
import com.ferrinsa.fairpartner.security.config.TestSecurityConfig;
import com.ferrinsa.fairpartner.security.config.TestUserDetailsServiceConfig;
import com.ferrinsa.fairpartner.security.jwt.JwtTokenFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({TestSecurityConfig.class, TestUserDetailsServiceConfig.class})
@WebMvcTest(
        controllers = ExpenseController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtTokenFilter.class
        )
)
@AutoConfigureMockMvc
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseCoordinatorService expenseCoordinatorService;

    private ExpenseWithSharesAndPayer buildMockExpense() {
        return new ExpenseWithSharesAndPayer(
                TestExpenseFactory.expense(),
                List.of(TestExpenseFactory.share()),
                TestExpenseFactory.user()
        );
    }

    @Nested
    @DisplayName("GET")
    class GetTests {

        @Test
        @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
        @DisplayName("/expenses/me/{id} - OK")
        void getExpense_returnOk200() throws Exception {

            when(expenseCoordinatorService.getExpenseDetails(1L, 1L))
                    .thenReturn(buildMockExpense());

            mockMvc.perform(get("/expenses/me/{id}", 1L)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.payer.id").value(1))
                    .andExpect(jsonPath("$.payer.name").value("Ivan"));

            verify(expenseCoordinatorService).getExpenseDetails(1L, 1L);
        }

        @Test
        @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
        @DisplayName("/expenses/me/{id} - NOT FOUND")
        void getExpense_return404_whenNotExists() throws Exception {

            when(expenseCoordinatorService.getExpenseDetails(1L, 99L))
                    .thenThrow(new ExpenseNotFoundException("99"));

            mockMvc.perform(get("/expenses/me/{id}", 99L))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
        @DisplayName("/expenses/me/expense-group/{id} - OK")
        void getListExpenses_returnOk200() throws Exception {

            when(expenseCoordinatorService.getListExpenses(1L, 1L))
                    .thenReturn(new ExpensesWithBalances(List.of(), List.of()));

            mockMvc.perform(get("/expenses/me/expense-group/{id}", 1L))
                    .andExpect(status().isOk());

            verify(expenseCoordinatorService).getListExpenses(1L, 1L);
        }
    }

    @Nested
    @DisplayName("POST")
    class PostTests {

        @Test
        @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
        @DisplayName("/expenses/me - CREATED")
        void createExpense_return201() throws Exception {

            when(expenseCoordinatorService.createExpense(any(), any()))
                    .thenReturn(buildMockExpense());

            mockMvc.perform(post("/expenses/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestExpenseFactory.CREATE_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"))
                    .andExpect(jsonPath("$.id").value(1));

            verify(expenseCoordinatorService).createExpense(any(), any());
        }

        @Test
        @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
        @DisplayName("/expenses/me - BAD REQUEST")
        void createExpense_return400_whenInvalid() throws Exception {

            String invalidJson = """
            {
              "expenseGroupId": 1
            }
            """;

            mockMvc.perform(post("/expenses/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("PATCH")
    class PatchTests {

        @Test
        @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
        @DisplayName("/expenses/me/{id} - OK")
        void updateExpense_returnOk200() throws Exception {

            when(expenseCoordinatorService.updateExpense(any(), any(), any()))
                    .thenReturn(buildMockExpense());

            mockMvc.perform(patch("/expenses/me/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestExpenseFactory.UPDATE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));

            verify(expenseCoordinatorService).updateExpense(any(), any(), any());
        }
    }

    @Nested
    @DisplayName("DELETE")
    class DeleteTests {

        @Test
        @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
        @DisplayName("/expenses/me/{id} - NO CONTENT")
        void deleteExpense_return204() throws Exception {

            mockMvc.perform(delete("/expenses/me/{id}", 1L))
                    .andExpect(status().isNoContent());

            verify(expenseCoordinatorService).deleteExpense(1L, 1L);
        }
    }
}