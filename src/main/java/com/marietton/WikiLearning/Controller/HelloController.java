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

    static public int labelID = 655362;

    @Autowired
    private PageRepository pageRepository;

    private boolean passeParEvaluation = false;

    @RequestMapping("/")
    public String index() {
        Data.fillIn(pageRepository);
        return "index";
    }

    @RequestMapping("/question")
    public String question(@RequestParam(value = "delai", required = false) String delai) {
        if (delai != null && passeParEvaluation == true) {
            long tempsAajouter = calculerTempsAajouter(delai);
            // Mise à jour du délai de répétition de la question en base
            pageRepository.updateQuestionTime(data, tempsAajouter);
            // Ajout de cet historique en base dans quizz_histo
            pageRepository.addHisto(data, delai);
            data.getPageList().remove(0);
            passeParEvaluation = false;
        }

        if (data.getPageList().size() == 0) {
            return "index";
        } else {
            //Calcul du chemin de la question suivante
            data.calculerChemin(pageRepository);
            return "question";
        }
    }

    @RequestMapping("/evaluation")
    public String evaluation() {
        pageRepository.recupererHisto(data);
        passeParEvaluation = true;
        return "evaluation";
    }

    @RequestMapping("/administration")
    public String administration(@RequestParam(value = "label", required = false) int label) {
        labelID = label;
        return index();
    }

    @RequestMapping("/choisiretiquette")
    public String choisiretiquette() {
        data.setListEtiquette(pageRepository.getListeEtiquettes());
        return "choisiretiquette";
    }
}