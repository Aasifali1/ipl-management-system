package com.knoldus.kup.ipl.controllers;

import com.knoldus.kup.ipl.models.Match;
import com.knoldus.kup.ipl.repository.MatchRepository;
import com.knoldus.kup.ipl.services.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/matches")
public class MatchController {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MatchService matchService;

    @PostMapping("/save")
    public String saveMatch(final @Valid Match match, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            return "add-match";
        }
        matchService.getAlertOnSave(redirectAttributes, match);
        return "redirect:/ipl/admin";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") long id, Model model) {
        matchService.getMatchDetails(model, id);
        return "update-match";
    }

    @PostMapping("/update/{id}")
    public String matchUpdate(@PathVariable long id, Match match, Model model,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            return "add-match";
        }
        if(matchService.isSlotBooked(match)){
            matchService.getAlertIfSlotBooked(redirectAttributes);
            return "redirect:/matches/edit/"+match.getId();
        }
        if(matchService.isTeamSame(match)){
            matchService.getAlertIfTeamSame(redirectAttributes);
            return "redirect:/matches/edit/"+match.getId();
        }
        matchService.getAlertOnUpdate(redirectAttributes, match);
        return "redirect:/ipl/admin";
    }

    @GetMapping("/list")
    public String getMatches(Model model){
        matchService.getMatchesWithModel(model);
        return "match-details";
    }

    @GetMapping("/delete/{id}")
    public String deleteMatch(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        matchService.getAlertOnDelete(redirectAttributes,id);
        return "redirect:/ipl/admin";
    }

}
