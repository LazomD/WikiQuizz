package com.marietton.WikiLearning;

import com.marietton.WikiLearning.Controller.HelloController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyon on 14/07/2017.
 */
@Repository
public class PageRepository {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Find all customers, thanks Java 8, you can create a custom RowMapper like this :
    public List<Page> findAll() {
        Timestamp timestampActuelTS = new Timestamp(System.currentTimeMillis());
        long timestampActuel = timestampActuelTS.getTime()/1000;

        String requete = "select * from CONTENT " +
                "LEFT OUTER JOIN CONTENT_LABEL on CONTENT.CONTENTID = CONTENT_LABEL.CONTENTID " +
                "left outer join quizz_time on quizz_time.CONTENTID = CONTENT.CONTENTID " +
                "where CONTENT_LABEL.LABELID = " + HelloController.labelID + " " +
                "and (quizz_time.TIMESTAMP < " + timestampActuel + " or quizz_time.CONTENTID is null) " +
                "and CONTENT.TITLE IS NOT NULL " +
                "order by quizz_time.TIMESTAMP ASC";

        List<Page> result = jdbcTemplate.query(
                requete,
            (rs, rowNum) -> {
                return new Page(rs.getInt("CONTENT.CONTENTID"), rs.getString("CONTENT.TITLE"), rs.getString("CONTENT.TITLE"), rs.getString("CONTENT.TITLE"));
            }
        );
        return result;
    }

    public void updateQuestionTime(Data data, long tempsAajouter) {
        Page page = data.getPageList().get(0);

        // Calcul du temps à mettre à jour
        Timestamp timestampActuelTS = new Timestamp(System.currentTimeMillis());
        long timestampActuel = timestampActuelTS.getTime()/1000;
        long timestampMaj = timestampActuel + tempsAajouter;

        // Est ce que l'enregistrement dans quizz-time existe ?
        Integer cnt = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM quizz_time WHERE CONTENTID = ?", Integer.class, page.getContent_id());

        if ( cnt != null && cnt > 0) {
            String requete = "update quizz_time set TIMESTAMP = ? where CONTENTID = ?";
            jdbcTemplate.update(
                    requete,
                    timestampMaj, page.getContent_id());
        }else{
            jdbcTemplate.update(
                    "INSERT INTO quizz_time (CONTENTID, TIMESTAMP) VALUES (?, ?)",
                    new Object[]{page.getContent_id(), timestampMaj}
            );
        }
    }

    public List<Page> computeChemin(List<Page> pageList) {

        List<Page> retour = pageList;

        String requete = "SELECT CONTENT.TITLE FROM CONFANCESTORS " +
                "left outer join CONTENT on CONTENT.CONTENTID = CONFANCESTORS.ANCESTORID " +
                "WHERE CONFANCESTORS.DESCENDENTID = " + pageList.get(0).getContent_id() + " " +
                "order by CONFANCESTORS.ANCESTORPOSITION";

        List<String> resultList = jdbcTemplate.query(
                requete,
                (rs, rowNum) -> {
                    return new String(rs.getString("CONTENT.TITLE"));
                }
        );

        String cheminCalcule = "";
        for (int i = 0; i < resultList.size(); i++) {
            cheminCalcule = cheminCalcule + "/" + resultList.get(i);
        }

        retour.get(0).setChemin(cheminCalcule);
        return retour;
    }

    public void addHisto(Data data, String delai) {
        Page page = data.getPageList().get(0);

        Timestamp timestampActuelTS = new Timestamp(System.currentTimeMillis());
        long timestampActuel = timestampActuelTS.getTime()/1000;

        jdbcTemplate.update(
                "INSERT INTO quizz_histo (CONTENTID, DUREE, TIMESTAMP) VALUES (?, ?, ?)",
                new Object[]{page.getContent_id(), Tool.calculerIdentifiantDelai(delai), timestampActuel}
        );
    }

    public void recupererHisto(Data data) {
        Page page = data.getPageList().get(0);

        String requete = "SELECT DUREE FROM quizz_histo " +
                "WHERE CONTENTID = " + page.getContent_id() + " " +
                "order by TIMESTAMP desc";

        List<Integer> resultList = jdbcTemplate.query(
                requete,
                (rs, rowNum) -> {
                    return new Integer(rs.getString("quizz_histo.DUREE"));
                }
        );

        data.setHistoList(resultList);
    }

    public List<Etiquette> getListeEtiquettes() {

        String requete = "SELECT LABEL.LABELID, LABEL.NAME  FROM LABEL ";

        List<Etiquette> resultList = jdbcTemplate.query(
                requete,
                (rs, rowNum) -> {
                    return new Etiquette(
                            Integer.parseInt(rs.getString("LABEL.LABELID")),
                            rs.getString("LABEL.NAME"));
                }
        );

        return resultList;
    }
}
