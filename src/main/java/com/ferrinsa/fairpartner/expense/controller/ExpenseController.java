package com.ferrinsa.fairpartner.expense.controller;

import com.ferrinsa.fairpartner.expense.service.domain.ExpenseService;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Validated
@RestController
@RequestMapping("/expenses")
public class ExpenseController {

}
