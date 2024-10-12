package com.group6.assignment2.controllers.teacher;

import com.group6.assignment2.entity.InviteLink;
import com.group6.assignment2.entity.User;
import com.group6.assignment2.repository.InviteLinkRepository;
import com.group6.assignment2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InviteController {

    @Autowired
    private InviteLinkRepository inviteLinkRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Step 1: Display the page to accept the invite
    @GetMapping("/auth/invite/{inviteCode}")
    public String acceptInvitePage(@PathVariable("inviteCode") String inviteCode, Model model) {
        // Fetch the invite link by the invite code
        InviteLink inviteLink = inviteLinkRepository.findByInviteCode(inviteCode);

        if (inviteLink == null || inviteLink.isRedeemed()) {
            // If invite code is invalid or already redeemed, show an error message
            model.addAttribute("errorMessage", "Invalid or expired invite link.");
            return "teacher/acceptInvite";
        }

        // Pass the invite code to the form for later submission
        model.addAttribute("inviteCode", inviteCode);
        return "teacher/acceptInvite";
    }

    // Step 2: Handle the form submission to accept the invite
    @PostMapping("/auth/invite/accept")
    public String acceptInvite(@RequestParam("inviteCode") String inviteCode,
                               @RequestParam("password") String password, Model model) {

        // Fetch the invite link by invite code
        InviteLink inviteLink = inviteLinkRepository.findByInviteCode(inviteCode);

        if (inviteLink == null || inviteLink.isRedeemed()) {
            // If invite code is invalid or already redeemed, show an error message
            model.addAttribute("errorMessage", "Invalid or expired invite link.");
            return "teacher/acceptInvite";
        }

        // Fetch the associated user (teacher)
        User user = inviteLink.getTeacher();

        // Set the new password and mark the invite as redeemed
        user.setPassword(passwordEncoder.encode(password));
        inviteLink.setRedeemed(true);

        // Save the updated user and invite link
        userRepository.save(user);
        inviteLinkRepository.save(inviteLink);

        // Success message
        model.addAttribute("successMessage", "Your account has been activated. You can now log in.");
        return "teacher/acceptInvite";
    }
}
