package com.tp3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tp3.service.AkkaService;



@Controller
public class AkkaController {

	@GetMapping("/")
	public String mapreduce() {
		return "index";
	}
	
	@PostMapping("/init")
	public String init() {
		 AkkaService.AKKASERVICE.init();
		 return "redirect:/";
	}
	
	@PostMapping("/analyser")
	public String analyserFile(@RequestParam ("fichier") String fichier,Model model) {
		AkkaService.AKKASERVICE.analyseFile(fichier);
		model.addAttribute("fichier",fichier);
		return "index";
		
	}
	
	@PostMapping("/resultat")
	public String resultat(@RequestParam ("mot") String mot,Model model) {
		int nombre = AkkaService.AKKASERVICE.getWordOccurrences(mot);
		model.addAttribute("mot",mot);
		model.addAttribute("nombre",nombre);
		return "index";
		
	}
	
}
