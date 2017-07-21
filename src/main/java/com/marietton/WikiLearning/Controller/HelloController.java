package com.marietton.WikiLearning.Controller;

import com.marietton.WikiLearning.Data;
import com.marietton.WikiLearning.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;

import static com.marietton.WikiLearning.Tool.calculerTempsAajouter;

@Controller
public class HelloController {

    @Autowired
    private Data data;

    @Autowired
    DataSource dataSource;

    @Autowired
    private PageRepository pageRepository;

    @RequestMapping("/")
    public String index(Model model) {
        Data.fillIn(pageRepository);
        return "index";
    }

    @RequestMapping("/question")
    public String question(@RequestParam(value = "delai", required=false) String delai) {
        if (delai != null) {
            long tempsAajouter = calculerTempsAajouter(delai);
            // Mise à jour du délai de répétition de la question en base
            pageRepository.updateQuestionTime(data, tempsAajouter);
            // Ajout de cet historique en base dans quizz_histo
            pageRepository.addHisto(data, delai);
            data.getPageList().remove(0);
        }

        if(data.getPageList().size() == 0){
            return "index";
        }else{
            //Calcul du chemin de la question
            data.calculerChemin(pageRepository);
            return "question";
        }
    }

    @RequestMapping("/evaluation")
    public String evaluation(Model model) {
        pageRepository.recupererHisto(data);
        return "evaluation";
    }

}