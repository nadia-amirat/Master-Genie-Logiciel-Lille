package fil.univ.login.web;

import fil.univ.login.model.User;
import fil.univ.login.repository.UserRepository;
import fil.univ.login.service.PhoneverificationService;
import fil.univ.login.service.VerificationResult;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@CrossOrigin("*")
@RequestMapping("/api/v1/sms")

public class SmsController {
    //private final SmsService smsService;
    final
    PhoneverificationService phonesmsservice;
    private final String SMS_NOT_SEND = "It's been less that 5 minutes. No SMS code sent.";
    private final UserRepository userRepository;

    @Autowired
    public SmsController(PhoneverificationService phonesmsservice, UserRepository userRepository) {
        //this.smsService = smsService;
        this.phonesmsservice = phonesmsservice;
        this.userRepository = userRepository;
    }
    @PostMapping("/sendSms")
    public String sendSms(String email, Model model) {
        User user = userRepository.findByEmail(email);
        LocalDateTime userLastTry = user.getLastTry();
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        if (userLastTry != null && userLastTry.isAfter(fiveMinutesAgo)) {
            model.addAttribute("popup", SMS_NOT_SEND);
            model.addAttribute("email", email);
            return "checkSms";
        }
        System.out.println("heree");
        VerificationResult result = phonesmsservice.startVerification(user.getPhoneNumber());
        user.setLastTry(LocalDateTime.now());
        userRepository.save(user);
        model.addAttribute("email", email);
        return "checkSms";
    }
}
