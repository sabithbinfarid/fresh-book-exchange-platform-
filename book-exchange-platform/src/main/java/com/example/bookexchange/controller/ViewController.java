package com.example.bookexchange.controller;

import com.example.bookexchange.service.BookService;
import com.example.bookexchange.service.OrderService;
import com.example.bookexchange.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class ViewController {
    private final BookService bookService;
    private final UserService userService;
    private final OrderService orderService;

    public ViewController(BookService bookService, UserService userService, OrderService orderService) {
        this.bookService = bookService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        model.addAttribute("name", authentication.getName());
        return "dashboard";
    }

    @GetMapping("/books-page")
    public String booksPage(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "books";
    }

    @GetMapping("/orders-page")
    public String ordersPage(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "orders";
    }

    @GetMapping("/admin/users-page")
    public String usersPage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin-users";
    }
}
