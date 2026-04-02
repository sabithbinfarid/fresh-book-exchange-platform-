package com.example.bookexchange.controller;

import com.example.bookexchange.dto.BookResponse;
import com.example.bookexchange.dto.OrderResponse;
import com.example.bookexchange.dto.UserResponse;
import com.example.bookexchange.service.BookService;
import com.example.bookexchange.service.OrderService;
import com.example.bookexchange.service.UserService;
import com.example.bookexchange.exception.ResourceNotFoundException;
import com.example.bookexchange.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping
public class ViewController {
    private final BookService bookService;
    private final UserService userService;
    private final OrderService orderService;
    private final UserRepository userRepository;

    public ViewController(BookService bookService, UserService userService, OrderService orderService, UserRepository userRepository) {
        this.bookService = bookService;
        this.userService = userService;
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        model.addAttribute("name", authentication.getName());
        
        // Add user role information
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("userRole", isAdmin ? "ADMIN" : "USER");

        if (isAdmin) {
            List<BookResponse> books = bookService.findAll();
            List<OrderResponse> borrows = orderService.findAll();

            model.addAttribute("totalBooks", books.size());
            model.addAttribute("availableBooks", books.stream().filter(b -> b.getStatus().name().equals("AVAILABLE")).count());
            model.addAttribute("reservedBooks", books.stream().filter(b -> b.getStatus().name().equals("RESERVED")).count());
            model.addAttribute("soldBooks", books.stream().filter(b -> b.getStatus().name().equals("SOLD")).count());

            model.addAttribute("pendingBorrows", borrows.stream().filter(o -> o.getStatus().name().equals("PENDING")).count());
            model.addAttribute("approvedBorrows", borrows.stream().filter(o -> o.getStatus().name().equals("APPROVED")).count());
            model.addAttribute("completedBorrows", borrows.stream().filter(o -> o.getStatus().name().equals("COMPLETED")).count());
        } else {
            Long userId = userRepository.findByEmail(authentication.getName())
                .map(user -> user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
            model.addAttribute("myBorrowsCount", orderService.findForUser(userId, false).size());
        }
        
        return "dashboard";
    }

    @GetMapping("/books-page")
    public String booksPage(Authentication authentication, Model model) {
        Long userId = userRepository.findByEmail(authentication.getName())
            .map(user -> user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        model.addAttribute("books", bookService.findAll());
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("borrowedBookIds", orderService.findActiveBorrowedBookIdsByUser(userId));
        return "books";
    }

    @GetMapping("/borrow-page")
    public String borrowPage(Authentication authentication, Model model) {
        Long userId = userRepository.findByEmail(authentication.getName())
            .map(user -> user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("borrows", orderService.findForUser(userId, isAdmin));
        return "orders";
    }

    @GetMapping("/orders-page")
    public String redirectOrdersPage() {
        return "redirect:/borrow-page";
    }

    @GetMapping("/admin/users-page")
    @PreAuthorize("hasRole('ADMIN')")
    public String usersPage(Model model) {
        List<UserResponse> nonAdminUsers = userService.findAll().stream()
            .filter(user -> user.getRoles().stream().noneMatch(role -> role.equals("ADMIN")))
            .toList();
        model.addAttribute("users", nonAdminUsers);
        return "admin-users";
    }
}
