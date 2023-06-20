package com.globetouch.iotsmp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallBackController {
	
	@GetMapping("/osticket_fallback")
    public ResponseEntity<String> osTicketFallback() {
        return new ResponseEntity<>("We are facing some issue with ticket management system. Please contact support",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
