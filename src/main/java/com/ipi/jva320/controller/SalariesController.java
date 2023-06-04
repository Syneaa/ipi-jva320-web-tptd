package com.ipi.jva320.controller;

import com.ipi.jva320.exception.SalarieException;
import com.ipi.jva320.model.SalarieAideADomicile;
import com.ipi.jva320.service.SalarieAideADomicileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Controller
public class SalariesController {
    @Autowired
    private SalarieAideADomicileService salarieAideADomicileService;

    @GetMapping(value = "/salaries/{id}")
    public String getSalarie(@PathVariable("id") Long id, final ModelMap model) {
        SalarieAideADomicile salarie = salarieAideADomicileService.getSalarie(id);
        model.addAttribute("salarie", salarie);
        model.put("now", LocalDate.now());
        return "detail_Salarie";
    }
//TODO à revoir !!!
    @GetMapping(value = "/salaries/aide/new")
    public String create(final ModelMap model) {
        model.addAttribute("salarie", new SalarieAideADomicile());
        return "new_Salarie"; // à modifier en ajouter un nouveau formulaire (comme detail_salarie"
    }

    @PostMapping(value = "/salaries/save")
    public String createSalarie(SalarieAideADomicile salarie) throws SalarieException {
        salarieAideADomicileService.creerSalarieAideADomicile(salarie);
        return "redirect:/salaries/" + salarie.getId();
    }

    @PostMapping(value = "/salaries/{id}")
    public String update(SalarieAideADomicile salarie) throws SalarieException {
        salarieAideADomicileService.updateSalarieAideADomicile(salarie);
        return "redirect:/salaries/" + salarie.getId();
    }

    @GetMapping(value = "/salaries")
    public String getSalaries(final ModelMap model) {
        model.put("salaries", salarieAideADomicileService.getSalaries());
        return "list";
    }

    @GetMapping("/salaries/{id}/delete")
    public String supprimerSalarie(@PathVariable("id") Long id) throws SalarieException {
        salarieAideADomicileService.deleteSalarieAideADomicile(id);
        return "redirect:/salaries";
    }

    @RequestMapping(value= "/salaries/findAllByName")
    public ModelAndView findSalarieByName(@RequestParam String nom) {
        ModelAndView salaries = new ModelAndView("list");
        try {
        List<SalarieAideADomicile> salariesList = salarieAideADomicileService.getSalaries(nom);
        salaries.addObject("salaries", salariesList);
        return salaries;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun résultat");
        }
    }
}
